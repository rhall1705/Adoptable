package personal.rowan.petfinder.application

import android.app.Application
import android.content.Context

import io.realm.Realm
import io.realm.RealmConfiguration
import personal.rowan.petfinder.application.dagger.component.ApplicationComponent
import personal.rowan.petfinder.application.dagger.component.DaggerApplicationComponent
import personal.rowan.petfinder.application.dagger.module.ApplicationModule

/**
 * Created by Rowan Hall
 */

class App : Application() {

    private lateinit var mAppComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        val realmConfiguration = RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)

        mAppComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    companion object {
        fun applicationComponent(context: Context): ApplicationComponent {
            return (context.applicationContext as App).mAppComponent
        }
    }

}
