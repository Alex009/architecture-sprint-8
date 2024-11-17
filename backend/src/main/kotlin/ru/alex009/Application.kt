package ru.alex009

import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureCORS()
    configureSecurity()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}
