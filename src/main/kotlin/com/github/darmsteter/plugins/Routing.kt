package com.github.darmsteter.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.thymeleaf.*
import kotlinx.serialization.Serializable

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond(ThymeleafContent("index", mapOf()))
        }
        post("/") {
            val formParameters = call.receiveParameters()
            val text = formParameters["text"].toString()
            val ok = "200 ОК"
            call.respond(ThymeleafContent("data", mapOf("text" to ok)))
        }
    }
}

@Serializable
data class CoolRequest(val text: String)