package com.peryloth.webclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class UserServiceWebClientConfig {

    @Bean
    @Qualifier("userService")
    public WebClient userServiceWebClient(
            WebClient.Builder builder,
            @Value("${user-service.base-url}") String baseUrl) {
        System.out.println(">>> USER-SERVICE BASE URL user-service.base-url = " + baseUrl);
        return builder.baseUrl(baseUrl)
                .filter((request, next) -> next.exchange(request)
                        .flatMap(response -> response.bodyToMono(String.class)
                                .doOnNext(body -> System.out.println("RAW BODY: " + body))
                                // reconstruyo el response
                                .flatMap(b -> Mono.just(response.mutate().body(b).build()))
                        )
                )
                .build();
    }
}