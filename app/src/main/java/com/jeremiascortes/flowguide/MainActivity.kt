package com.jeremiascortes.flowguide

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jeremiascortes.flowguide.features.auth.di.SupabaseClient
import com.jeremiascortes.flowguide.navigation.Navigation
import com.jeremiascortes.flowguide.ui.theme.FlowGuideTheme
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
            val navController = rememberNavController()
            val currentBackStack by navController.currentBackStackEntryAsState()
            val currentRoute = currentBackStack?.destination?.route
            val showTopBar = currentRoute in listOf("home", "profile")

            FlowGuideTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (showTopBar) {

                        }
                    },
                    bottomBar = { },
                    content = { Navigation() }
                )
            }
        }
    }
}
