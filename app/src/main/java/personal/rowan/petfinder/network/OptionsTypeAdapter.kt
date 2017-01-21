package personal.rowan.petfinder.network

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import personal.rowan.petfinder.model.pet.Option
import personal.rowan.petfinder.model.pet.Options
import java.io.IOException

/**
 * Created by Rowan Hall
 */

class OptionsTypeAdapter : TypeAdapter<Options>() {

    private val gson = Gson()

    @Throws(IOException::class)
    override fun write(jsonWriter: JsonWriter, Options: Options) {
        gson.toJson(Options, Options.javaClass, jsonWriter)
    }

    @Throws(IOException::class)
    override fun read(jsonReader: JsonReader): Options {
        val options: Options

        jsonReader.beginObject()
        if (jsonReader.peek() == JsonToken.END_OBJECT) {
            jsonReader.endObject()
            return Options(Option())
        }
        if (jsonReader.peek() == JsonToken.END_ARRAY) {
            jsonReader.endArray()
            return Options(Option())
        }
        jsonReader.nextName()

        if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
            @Suppress("UNCHECKED_CAST")
            options = Options(gson.fromJson<Any>(jsonReader, Array<Option>::class.java) as Array<Option>)
        } else if (jsonReader.peek() == JsonToken.BEGIN_OBJECT) {
            options = Options(gson.fromJson<Any>(jsonReader, Option::class.java) as Option)
        } else {
            throw JsonParseException("Unexpected token " + jsonReader.peek())
        }

        jsonReader.endObject()
        return options
    }
}
