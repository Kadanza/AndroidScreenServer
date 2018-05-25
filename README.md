# AndroidScreenServer


A simple channel for transferring screenshoots from your anroid device to your computer. 
Just run ScreenServer.jar in your pc, create the class in your android project and use method screen() to get image and
send to your pc


object NetClientTool {

    fun screen(context : Activity)  {
        val image: Bitmap
        try {
            val client = Socket("10.0.2.2", 9999)
            val capture = Screenshot.capture()
            image =  capture.bitmap
            val holder = ImageHolder(image)
            val request = Request( "screens/android.png" ,  holder)

            val json = gson.toJson(request)
            val output = client.outputStream
            output.write(json.toByteArray())
            client.close()


        }catch (e : Exception){
            w("", e)
        }
    }

    internal class RenderedImageTypeAdapter : TypeAdapter<Bitmap>() {
        @Throws(IOException::class)
        override fun write(out: JsonWriter, image: Bitmap) {
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            val imageBase64 = android.util.Base64.encodeToString(b, android.util.Base64.NO_WRAP)
            out.value(imageBase64)
        }

        @Throws(IOException::class)
        override fun read(`in`: JsonReader): Bitmap {
            val imageBase64 = `in`.nextString()
            val encodeByte = android.util.Base64.decode(imageBase64, android.util.Base64.NO_WRAP)
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        }
    }
    
    
 

}

                       
    val gson = GsonBuilder()
            .registerTypeHierarchyAdapter(Bitmap::class.java, RenderedImageTypeAdapter())
            .create()
            
     class ImageHolder(val image: Bitmap)
    data class Request(val name: String,
                       val holder : ImageHolder){}
