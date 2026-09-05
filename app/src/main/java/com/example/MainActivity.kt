package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.AppScreen
import com.example.ui.PetitionViewModel
import com.example.ui.components.AppTopBar
import com.example.ui.screens.PetitionFormScreen
import com.example.ui.screens.PetitionPreviewScreen
import com.example.ui.screens.SavedPetitionsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PolishBackground

class MainActivity : ComponentActivity() {
    private val viewModel: PetitionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                LegalScribeApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun LegalScribeApp(viewModel: PetitionViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val generatedPetition by viewModel.generatedPetition.collectAsState()
    val savedPetitions by viewModel.savedPetitions.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    // Handle back button
    BackHandler(enabled = currentScreen != AppScreen.FORM) {
        viewModel.navigateTo(AppScreen.FORM)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                currentScreen = currentScreen,
                savedCount = savedPetitions.size,
                onNavigate = { viewModel.navigateTo(it) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = PolishBackground,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(PolishBackground)
        ) {
            when (currentScreen) {
                AppScreen.FORM -> {
                    PetitionFormScreen(
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                AppScreen.PREVIEW -> {
                    generatedPetition?.let { petition ->
                        PetitionPreviewScreen(
                            petition = petition,
                            viewModel = viewModel,
                            modifier = Modifier.fillMaxSize()
                        )
                    } ?: run {
                        // Fallback if no petition generated yet
                        PetitionFormScreen(
                            viewModel = viewModel,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                AppScreen.HISTORY -> {
                    SavedPetitionsScreen(
                        petitions = savedPetitions,
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    androidx.compose.material3.Text(text = "Hello $name!", modifier = modifier)
}
