package com.peryloth.webclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peryloth.usecase.saveamount.IGetUserRepository;
import com.peryloth.usecase.saveamount.dto.UsuarioResponseDTO;
import com.peryloth.webclient.dto.GetUserByEmailRequestDTO;
import com.peryloth.webclient.dto.TokenValidationResponse;
import com.peryloth.webclient.dto.UserValidationRequest;
import com.peryloth.webclient.helper.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class EstadosWebClientAdapter implements IGetUserRepository {

    private static final Logger logger = LoggerFactory.getLogger(EstadosWebClientAdapter.class);

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";


    private final JwtTokenProvider jwtTokenProvider;
    private final WebClient webClient;

    public EstadosWebClientAdapter(JwtTokenProvider jwtTokenProvider, @Qualifier("userService") WebClient webClient) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.webClient = webClient;
    }

    @Override
    public Mono<Boolean> isUserValid(String id, String email) {
        return jwtTokenProvider.generateToken()
                .doOnNext(key -> logger.info("Generated JWT Token: [{}]", key))
                .flatMap(jwtToken ->
                        webClient.post()
                                .uri("/api/v1/users/validate")
                                .header(HEADER_AUTHORIZATION, BEARER_PREFIX + jwtToken)
                                .bodyValue(new UserValidationRequest(id, email))
                                .exchangeToMono(response ->
                                        response.bodyToMono(Boolean.class)
                                                .defaultIfEmpty(false)
                                                .doOnNext(valid ->
                                                        logger.info("User valid response ({}): [{}]",
                                                                response.statusCode(), valid))
                                ))
                .onErrorResume(ex -> {
                    logger.warn("Error en WebClient: [{}]", ex.getMessage());
                    return Mono.just(false);
                });
    }

    public Mono<Boolean> isTokenValid(String token) {
        return webClient.get()
                .uri("/api/v1/token/validate")
                .header(HEADER_AUTHORIZATION, BEARER_PREFIX + token)
                .exchangeToMono(response ->
                        response.bodyToMono(TokenValidationResponse.class)
                                .map(dto -> "OK".equalsIgnoreCase(dto.status()))
                                .defaultIfEmpty(false)
                )
                .doOnNext(valid -> logger.info("Token valid: [{}]", valid))
                .doOnError(ex -> logger.warn("Error validando token: [{}]", ex.getMessage()))
                .onErrorReturn(false);
    }

    public Mono<UsuarioResponseDTO> getUserByEmail(String email, String token) {
        return webClient.post()
                .uri("/api/v1/users/getUser")
                .header(HEADER_AUTHORIZATION, BEARER_PREFIX + token)
                .bodyValue(new GetUserByEmailRequestDTO(email))
                .exchangeToMono(response -> {
                    System.out.println("email: " + email);
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(String.class)
                                .switchIfEmpty(Mono.error(new RuntimeException("Respuesta vacía del servicio getUser")))
                                .doOnNext(body -> System.out.println("RAW BODY: " + body))
                                .flatMap(body -> {
                                    try {
                                        UsuarioResponseDTO dto = new ObjectMapper().readValue(body, UsuarioResponseDTO.class);
                                        System.out.println("DTO: " + dto);
                                        return Mono.just(dto);
                                    } catch (Exception e) {
                                        return Mono.error(new RuntimeException("Error parseando response a UsuarioResponseDTO", e));
                                    }
                                });
                    }
                    return Mono.error(new IllegalArgumentException("Usuario no encontrado o token inválido"));
                })
                .doOnError(ex -> logger.warn("Error obteniendo usuario: [{}]", ex.getMessage()));
    }
}
