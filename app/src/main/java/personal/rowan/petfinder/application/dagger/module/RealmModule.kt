package personal.rowan.petfinder.application.dagger.module

import dagger.Module
import dagger.Provides
import io.realm.Realm

@Module
class RealmModule {

    @Provides
    internal fun provideRealm(): Realm {
        return Realm.getDefaultInstance()
    }

}
