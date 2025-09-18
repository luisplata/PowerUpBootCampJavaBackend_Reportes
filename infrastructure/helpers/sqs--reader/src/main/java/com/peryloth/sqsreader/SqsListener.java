package com.peryloth.sqsreader;

import com.peryloth.sqsreader.mapper.LoanEventMapper;
import com.peryloth.usecase.saveamount.ISaveAmount;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsListener {

    private final SqsAsyncClient sqsAsyncClient;
    private final ISaveAmount saveAmount;
    private final LoanEventMapper loanEventMapper;

    @Value("${aws.sqs.queueUrl}")
    private String queueUrl;

    @PostConstruct
    public void startListener() {
        pollMessages();
    }

    private void pollMessages() {
        sqsAsyncClient.receiveMessage(r -> r.queueUrl(queueUrl)
                        .maxNumberOfMessages(10)
                        .waitTimeSeconds(20)) // long polling
                .thenAccept(response -> {
                    response.messages().forEach(this::processMessage);
                    pollMessages(); // seguir escuchando
                })
                .exceptionally(e -> {
                    log.error("❌ Error recibiendo mensajes de SQS", e);
                    pollMessages();
                    return null;
                });
    }

    private void processMessage(Message message) {
        log.info("📥 Mensaje recibido: id={} body={}", message.messageId(), message.body());
        saveAmount.saveLoan(loanEventMapper.toDomain(message.body()))
                .doOnSuccess(v -> deleteMessage(message))
                .doOnError(e -> log.error("❌ Error procesando mensaje {}", message.messageId(), e))
                .subscribe();
    }

    private void deleteMessage(Message message) {
        sqsAsyncClient.deleteMessage(r -> r.queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle()));
        log.info("✅ Mensaje {} eliminado de la cola", message.messageId());
    }
}
