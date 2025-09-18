package com.peryloth.usecase.saveamount;

import com.peryloth.usecase.saveamount.dto.UsuarioResponseDTO;
import reactor.core.publisher.Mono;

public interface IGetUserRepository {
    Mono<Boolean> isUserValid(String id, String email);

    Mono<Boolean> isTokenValid(String token);

    Mono<UsuarioResponseDTO> getUserByEmail(String email, String token);
}
