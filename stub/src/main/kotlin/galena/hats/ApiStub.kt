package galena.hats

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.gson
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

var default: SupporterData? = null
val CACHE = hashMapOf<String, SupporterData>()

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        gson()
    }

    routing {
        route("/api") {
            get("/{uuid}") {
                val uuid = call.pathParameters["uuid"]!!
                log.info("Data requested for UUID $uuid")
                val data = CACHE[uuid] ?: default
                data?.let {
                    call.respond(it)
                } ?: run {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            put("/default") {
                val data = call.receive<SupporterData>()

                log.info("Overwriting default data")

                default = data

                call.respond(data)
            }

            put("/{uuid}") {
                val uuid = call.pathParameters["uuid"]!!
                val data = call.receive<SupporterData>()

                log.info("Overwriting data for UUID $uuid")

                CACHE[uuid] = data

                call.respond(data)
            }

            delete("/{uuid}") {
                val uuid = call.pathParameters["uuid"]!!

                log.info("Deleting data for UUID $uuid")

                CACHE.remove(uuid)

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
