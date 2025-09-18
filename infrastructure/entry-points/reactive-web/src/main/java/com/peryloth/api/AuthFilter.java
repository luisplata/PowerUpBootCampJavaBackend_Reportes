package com.peryloth.api;

import com.peryloth.usecase.saveamount.IGetUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    private final IGetUserRepository getUserRepository;

    public AuthFilter(IGetUserRepository getUserRepository) {
        this.getUserRepository = getUserRepository;
    }

    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        String authHeader = request.headers().firstHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);

        try {
            return getUserRepository.isTokenValid(token).flatMap(isValid -> {
                if (Boolean.TRUE.equals(isValid)) {
                    return next.handle(request); // 👈 SOLO pasa si usuario+rol existen
                } else {
                    return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
                }
            });
        } catch (Exception e) {
            return ServerResponse.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
