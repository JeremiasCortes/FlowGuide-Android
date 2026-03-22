package com.jeremiascortes.flowguide

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jeremiascortes.flowguide.features.auth.di.SupabaseClient
import com.jeremiascortes.flowguide.navigation.AppNavHost
import com.jeremiascortes.flowguide.presentation.theme.FlowGuideTheme
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.auth.handleDeeplinks
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var supabaseClientWrapper: SupabaseClient

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // IMPORTANTE: Esto maneja automáticamente los deep links de OAuth
        supabaseClientWrapper.supabaseClient.handleDeeplinks(intent)

        enableEdgeToEdge()
        setContent {
            FlowGuideTheme {
                AppNavHost()
            }
        }
    }
}
