package com.tecsup.gowaykiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecsup.gowaykiapp.ui.navigation.GowaykiNav
import com.tecsup.gowaykiapp.ui.theme.GowaykiAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GowaykiAppTheme {
                GowaykiNav()
            }
        }
    }
}
