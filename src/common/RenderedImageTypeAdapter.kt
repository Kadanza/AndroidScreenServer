package common

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

import javax.imageio.ImageIO
import java.awt.image.RenderedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Base64

internal class RenderedImageTypeAdapter constructor() : TypeAdapter<RenderedImage>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, image: RenderedImage) {
        // Intermediate buffer
        val output = ByteArrayOutputStream()
        ImageIO.write(image, "PNG", output)
        val encoder = Base64.getEncoder()
        val imageBase64 = encoder.encodeToString(output.toByteArray())
        out.value(imageBase64)
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): RenderedImage {
        // The same in reverse order
        val imageBase64 = `in`.nextString()
        val decoder = Base64.getDecoder()
        val input = decoder.decode(imageBase64)
        return ImageIO.read(ByteArrayInputStream(input))
    }



}