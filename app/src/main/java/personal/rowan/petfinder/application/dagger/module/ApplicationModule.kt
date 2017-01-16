package personal.rowan.petfinder.application.dagger.module

import android.content.Context

import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val mContext: Context) {

    @Provides
    fun providesContext(): Context {
        return mContext
    }

}
