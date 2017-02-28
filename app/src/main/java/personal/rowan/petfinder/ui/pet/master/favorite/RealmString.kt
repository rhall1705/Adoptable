package personal.rowan.petfinder.ui.pet.master.favorite

import io.realm.RealmList
import io.realm.RealmObject
import java.util.*

/**
 * Created by Rowan Hall
 */
open class RealmString (private var mString: String): RealmObject() {

    constructor(): this("")

    fun value(): String {
        return mString
    }

    companion object {

        fun toRealmStringList(strings: List<String>): RealmList<RealmString> {
            val realmStrings: RealmList<RealmString> = RealmList()
            for(string in strings) {
                realmStrings.add(RealmString(string))
            }
            return realmStrings
        }

        fun toStringList(realmStrings: RealmList<RealmString>): List<String> {
            val strings: MutableList<String> = ArrayList()
            for(realmString in realmStrings) {
                strings.add(realmString.value())
            }
            return strings
        }

    }

}