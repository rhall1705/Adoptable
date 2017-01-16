package personal.rowan.petfinder.network

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

import java.io.IOException

import personal.rowan.petfinder.model.pet.Breed
import personal.rowan.petfinder.model.pet.Breeds

/**
 * Created by Rowan Hall
 */

class BreedsTypeAdapter : TypeAdapter<Breeds>() {

    private val gson = Gson()

    @Throws(IOException::class)
    override fun write(jsonWriter: JsonWriter, Breeds: Breeds) {
        gson.toJson(Breeds, Breeds.javaClass, jsonWriter)
    }

    @Throws(IOException::class)
    override fun read(jsonReader: JsonReader): Breeds {
        val Breeds: Breeds

        jsonReader.beginObject()
        jsonReader.nextName()

        if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
            @Suppress("UNCHECKED_CAST")
            Breeds = Breeds(gson.fromJson<Any>(jsonReader, Array<Breed>::class.java) as Array<Breed>)
        } else if (jsonReader.peek() == JsonToken.BEGIN_OBJECT) {
            Breeds = Breeds(gson.fromJson<Any>(jsonReader, Breed::class.java) as Breed)
        } else {
            throw JsonParseException("Unexpected token " + jsonReader.peek())
        }

        jsonReader.endObject()
        return Breeds
    }
}
