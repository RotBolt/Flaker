package io.rotlabs.flakersampleapp.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.rotlabs.flakersampleapp.MainApplication
import io.rotlabs.flakersampleapp.home.di.HomeContainer
import io.rotlabs.flakersampleapp.theme.FlakerTheme
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {

    private var homeContainer: HomeContainer? = null

    private val viewModel: HomeViewModel by viewModels { homeContainer!!.homeViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MainApplication).appContainer.initHomeContainer()
        homeContainer = (application as MainApplication).appContainer.homeContainer()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.sideEffect.collect { sideEffect ->
                        when (sideEffect) {
                            is HomeViewModel.SideEffect.ShowToast -> {
                                Toast.makeText(
                                    this@HomeActivity,
                                    sideEffect.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        setContent {
            FlakerTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = { viewModel.getUsers() }) {
                        Text(text = "Sample GET User")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        (application as MainApplication).appContainer.releaseHomeContainer()
        homeContainer = null
        super.onDestroy()
    }
}
