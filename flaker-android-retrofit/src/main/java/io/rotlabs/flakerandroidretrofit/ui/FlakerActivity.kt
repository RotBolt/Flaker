package io.rotlabs.flakerandroidretrofit.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import io.rotlabs.flakerandroidretrofit.di.FlakerAndroidRetrofitContainer
import io.rotlabs.flakerandroidui.components.appbars.FlakerBar
import io.rotlabs.flakerandroidui.components.lists.NetworkRequestList
import io.rotlabs.flakerandroidui.screens.prefs.FlakerPrefsDialog
import io.rotlabs.flakerandroidui.theme.FlakerAndroidTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
class FlakerActivity : ComponentActivity() {

    private val viewModel: FlakerViewModel by viewModels { FlakerAndroidRetrofitContainer.flakerViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

            val state by viewModel.viewStateFlow.collectAsState()

            FlakerAndroidTheme {
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        FlakerBar(
                            switchStateProvider = { state.isFlakerOn },
                            scrollBehavior = scrollBehavior,
                            onToggleChange = viewModel::toggleFlaker,
                            onPrefsClick = viewModel::openPrefs,
                            onSearchClick = {}
                        )
                    }
                ) { scaffoldPadding ->
                    NetworkRequestList(modifier = Modifier.padding(scaffoldPadding)) {
                        state.networkRequests
                    }
                }

                AnimatedVisibility(
                    visible = state.toShowPrefs,
                    enter = fadeIn() + expandVertically { 0 },
                    exit = fadeOut() + shrinkVertically { 0 }
                ) {
                    FlakerPrefsDialog(
                        onDismissRequest = viewModel::closePrefs,
                        onConfirmAction = viewModel::updatePrefs,
                        currentValuesProvider = { state.currentPrefs },
                    )
                }
            }
        }
    }
}
