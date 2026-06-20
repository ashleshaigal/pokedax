package com.aigal.pokedax

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltApplication : Application() {
    // This class serves as the application-level Hilt container.
    // Hilt will automatically generate the necessary code for you.
}