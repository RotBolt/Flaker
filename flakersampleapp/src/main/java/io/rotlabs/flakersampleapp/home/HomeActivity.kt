package io.rotlabs.flakersampleapp.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.rotlabs.flakerandroidokhttp.FLAKER_ACTION_OPEN
import io.rotlabs.flakerandroidokhttp.ui.FlakerActivity
import io.rotlabs.flakersampleapp.MainApplication
import io.rotlabs.flakersampleapp.home.di.HomeContainer
import io.rotlabs.flakersampleapp.theme.FlakerTheme
import kotlinx.coroutines.flow.collectLatest

class HomeActivity : ComponentActivity() {

    private var homeContainer: HomeContainer? = null

    private val viewModel: HomeViewModel by viewModels { homeContainer!!.homeViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MainApplication).appContainer.initHomeContainer()
        homeContainer = (application as MainApplication).appContainer.homeContainer()

        setContent {
            val state by viewModel.uiState.collectAsState()
            val snackBarHostState = remember { SnackbarHostState() }

            LaunchedEffect(Unit) {
                viewModel.sideEffect.collectLatest { sideEffect ->
                    when (sideEffect) {
                        is HomeViewModel.SideEffect.ShowSnackBar -> {
                            val result = snackBarHostState.showSnackbar(
                                message = sideEffect.message,
                                duration = SnackbarDuration.Long,
                                withDismissAction = true,
                                actionLabel = "Go to flaker"
                            )
                            when (result) {
                                SnackbarResult.ActionPerformed -> openFlaker()
                                SnackbarResult.Dismissed -> { /* Do Nothing */ }
                            }
                        }
                    }
                }
            }

            FlakerTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackBarHostState) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (state.isRetrofitRequestLoading.not()) {
                            Button(onClick = { viewModel.fireRetrofitRequest() }) {
                                Text(text = "Sample Retrofit GET Request")
                            }
                        } else {
                            CircularProgressIndicator()
                        }

                        Spacer(modifier = Modifier.size(24.dp))

                        if (state.isKtorRequestLoading.not()) {
                            Button(
                                onClick = {
                                    Toast.makeText(this@HomeActivity, "To be added", Toast.LENGTH_LONG).show()
                                }
                            ) {
                                Text(text = "Sample Ktor GET Request")
                            }
                        } else {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }

    private fun openFlaker() {
        val intent = Intent(this, FlakerActivity::class.java).setAction(FLAKER_ACTION_OPEN)
        startActivity(intent)
    }
    override fun onDestroy() {
        (application as MainApplication).appContainer.releaseHomeContainer()
        homeContainer = null
        super.onDestroy()
    }
}
