package personal.rowan.petfinder.network

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.model.pet.Pets
import java.io.IOException

/**
 * Created by Rowan Hall
 */
class PetsTypeAdapter : TypeAdapter<Pets>() {

    private val gson = Gson()

    @Throws(IOException::class)
    override fun write(jsonWriter: JsonWriter, Pets: Pets) {
        gson.toJson(Pets, Pets.javaClass, jsonWriter)
    }

    @Throws(IOException::class)
    override fun read(jsonReader: JsonReader): Pets {
        val Pets: Pets

        jsonReader.beginObject()
        jsonReader.nextName()

        if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
            @Suppress("UNCHECKED_CAST")
            Pets = Pets(gson.fromJson<Any>(jsonReader, Array<Pet>::class.java) as Array<Pet>)
        } else if (jsonReader.peek() == JsonToken.BEGIN_OBJECT) {
            Pets = Pets(gson.fromJson<Any>(jsonReader, Pet::class.java) as Pet)
        } else {
            throw JsonParseException("Unexpected token " + jsonReader.peek())
        }

        jsonReader.endObject()
        return Pets
    }
}