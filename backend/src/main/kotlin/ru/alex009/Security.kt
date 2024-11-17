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
    val config = environment.config
    val keycloakUrl = config.property("jwt.keycloak.url").getString()
    val keycloakRealm = config.property("jwt.keycloak.realm").getString()
    val keycloakIssuer = config.property("jwt.keycloak.issuer").getString()

    authentication {
        jwt("auth-jwt") {
            verifier(
                // получаем ключи для верификации с keycloak
                jwkProvider = UrlJwkProvider(
                    URL("$keycloakUrl/realms/$keycloakRealm/protocol/openid-connect/certs")
                ),
                // проверяем что выпущен jwt нашим keycloak и в нужном realm
                issuer = keycloakIssuer
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
