package client

import com.google.gson.GsonBuilder
import common.RenderedImageTypeAdapter
import java.awt.image.BufferedImage
import java.io.File
import java.net.Socket
import javax.imageio.ImageIO
import java.awt.image.RenderedImage


class ImageHolder(val image: RenderedImage)

data class Request(val name: String,
                   val holder : ImageHolder) {
}


val gson = GsonBuilder()
        .registerTypeHierarchyAdapter(RenderedImage::class.java, RenderedImageTypeAdapter())
        .create()

fun main(args: Array<String>) {

    val client = Socket("127.0.0.1", 9999)

    val image: BufferedImage = ImageIO.read(File("qwer.png"))
    val holder = ImageHolder(image)

    val request = Request( "scrs/qwerty56.png" ,  holder)

//    val serializer = CustomSerializer()
//    val requestBytes = serializer.Serialize(request)


    val json = gson.toJson(request)
    println(json)

    val output = client.outputStream
    output.write(json.toByteArray())


    client.close()

}


