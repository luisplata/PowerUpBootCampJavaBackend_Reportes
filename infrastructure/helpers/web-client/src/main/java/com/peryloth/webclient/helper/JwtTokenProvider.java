package com.peryloth.webclient.helper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private static final long EXPIRATION_TIME = 3600_000; // 1h en ms

    public JwtTokenProvider(UserServiceProperties props) {
        this.key = Keys.hmacShaKeyFor(props.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    public Mono<String> generateToken() {
        return Mono.fromSupplier(() ->
                Jwts.builder()
                        .setIssuer("hu3-reportes") // identifica quién generó el token
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact()
        );
    }
}
