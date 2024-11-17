package ru.alex009

import com.auth0.jwk.UrlJwkProvider
import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.plugins.cors.routing.CORS
import java.net.URL
import java.util.Date

fun Application.configureSecurity() {
    val jwtRealm = "reports-realm"

    val keycloakUrl = "http://localhost.proxyman.io:8080"
    val keycloakRealm = "reports-realm"

    authentication {
        jwt("auth-jwt") {
            realm = jwtRealm
            verifier(
                // получаем ключи для верификации с keycloak
                jwkProvider = UrlJwkProvider(
                    URL("$keycloakUrl/realms/$keycloakRealm/protocol/openid-connect/certs")
                ),
                // проверяем что выпущен jwt нашим keycloak и в нужном realm
                issuer = "$keycloakUrl/realms/$keycloakRealm"
            )
            validate { credential ->
                // проверяем не истек ли срок жизни токена
                if (credential.expiresAt?.after(Date()) == true) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

fun Application.configureCORS() {
    install(CORS) {
        allowHost("localhost.proxyman.io:3000")
        allowHeader(HttpHeaders.Authorization)
    }
}
