package personal.rowan.petfinder.application

import android.app.Application
import android.content.Context

import io.realm.Realm
import io.realm.RealmConfiguration
import personal.rowan.petfinder.application.dagger.component.AppComponent
import personal.rowan.petfinder.application.dagger.component.DaggerAppComponent
import personal.rowan.petfinder.application.dagger.module.AppModule

/**
 * Created by Rowan Hall
 */

class App : Application() {

    private lateinit var mAppComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        val realmConfiguration = RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)

        mAppComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    companion object {
        fun applicationComponent(context: Context): AppComponent {
            return (context.applicationContext as App).mAppComponent
        }
    }

}
