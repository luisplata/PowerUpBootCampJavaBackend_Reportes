package com.peryloth.dynamodb;

import com.peryloth.dynamodb.helper.TemplateAdapterOperations;
import com.peryloth.model.reporte.Reporte;
import com.peryloth.model.reporte.gateways.ReporteRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DynamoDBTemplateAdapter
        extends TemplateAdapterOperations<Reporte, String, ModelEntity>
        implements ReporteRepository {

    private static final String COUNTER_ID = "approvedLoans";
    private final DynamoDbEnhancedAsyncClient enhancedClient;
    private final DynamoDbAsyncClient lowLevelClient;
    private final String tableName = "LoanCounters";

    public DynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory,
                                   ObjectMapper mapper,
                                   DynamoDbAsyncClient lowLevelClient) {
        super(connectionFactory, mapper, d -> mapper.map(d, Reporte.class), "LoanCounters");
        this.enhancedClient = connectionFactory;
        this.lowLevelClient = lowLevelClient;
    }

    @Override
    public Mono<Reporte> incrementCounter(String counterId, Long amountToAdd, Double totalAmountToAdd) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("counterId", AttributeValue.builder().s(counterId).build());

        Map<String, String> expressionNames = new HashMap<>();
        expressionNames.put("#count", "count");
        expressionNames.put("#totalAmount", "totalAmount");

        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":incrCount", AttributeValue.builder().n(amountToAdd.toString()).build());
        expressionValues.put(":incrTotal", AttributeValue.builder().n(totalAmountToAdd.toString()).build());
        expressionValues.put(":zero", AttributeValue.builder().n("0").build());

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .updateExpression("ADD #count :incrCount SET #totalAmount = if_not_exists(#totalAmount, :zero) + :incrTotal")
                .expressionAttributeNames(expressionNames)
                .expressionAttributeValues(expressionValues)
                .returnValues(ReturnValue.ALL_NEW)
                .build();

        return Mono.fromFuture(() -> lowLevelClient.updateItem(request))
                .map(UpdateItemResponse::attributes)
                .map(attrs -> Reporte.builder()
                        .counterId(counterId)
                        .count(Long.parseLong(attrs.get("count").n()))
                        .totalAmount(Double.parseDouble(attrs.get("totalAmount").n()))
                        .build());
    }

    @Override
    public Mono<Reporte> getApprovedTotal() {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("counterId", AttributeValue.builder().s(COUNTER_ID).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();

        return Mono.fromFuture(() -> lowLevelClient.getItem(request))
                .filter(GetItemResponse::hasItem)
                .map(GetItemResponse::item)
                .map(attrs -> Reporte.builder()
                        .counterId(COUNTER_ID)
                        .count(Long.parseLong(attrs.getOrDefault("count", AttributeValue.builder().n("0").build()).n()))
                        .totalAmount(Double.parseDouble(attrs.getOrDefault("totalAmount", AttributeValue.builder().n("0").build()).n()))
                        .build());
    }
}
