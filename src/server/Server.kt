package server

import client.Request
import client.gson
import common.Response
import java.io.File
import java.net.ServerSocket
import java.nio.charset.Charset
import java.util.*
import javax.imageio.ImageIO

fun main(args: Array<String>) {

    val th = Thread({

        val server = ServerSocket(9999)
        println("Server running on port ${server.localPort}")
        while (true) {
            try {

                val client = server.accept()
                println("Client conected : ${client.inetAddress.hostAddress}")

//                val serializer = CustomSerializer()

                val scanner = Scanner(client.inputStream)
                while (scanner.hasNextLine()) {
                    val text = scanner.nextLine()
                    val requestBytes = text.toByteArray(Charset.defaultCharset())
                    val request = gson.fromJson<Request>(text, Request::class.java)
                    val file = File ( request.name)

                    file.mkdirs()

                    ImageIO.write( request.holder.image, "png", file )

                    print(request)
                }

                scanner.close()
                client.close()

            } catch (e: Exception) {
                print(e.toString())
            }
        }

        server.close()

    }).start()

}



