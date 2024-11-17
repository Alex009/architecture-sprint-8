package ru.alex009

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        authenticate("auth-jwt") {
            get("/reports") {
                val jwt: JWTPrincipal = call.principal<JWTPrincipal>()!!

                // проверяем группу
                val isAccessGranted: Boolean = jwt.payload.getClaim("realm_access")
                    .asMap()
                    .get("roles")
                    .let { it as List<String> }
                    .any { it == "prothetic_user" }

                if (!isAccessGranted) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }

                val userId: String = jwt.payload.subject
                call.respond("hello $userId")
            }
        }
    }
}
