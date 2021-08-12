package kz.bfgroup.formmap

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class FormMapApp: Application() {

    override fun onCreate() {
        MapKitFactory.setApiKey(BuildConfig.YMP_KEY)
        super.onCreate()
    }
}