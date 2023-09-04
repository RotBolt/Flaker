package io.github.rotbolt.flakerandroidokhttp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import io.github.rotbolt.flakerandroidokhttp.di.FlakerAndroidOkhttpContainer
import io.github.rotbolt.flakerandroidui.components.appbars.FlakerBar
import io.github.rotbolt.flakerandroidui.components.lists.NetworkRequestList
import io.github.rotbolt.flakerandroidui.screens.prefs.FlakerPrefsDialog
import io.github.rotbolt.flakerandroidui.screens.search.SearchScreen
import io.github.rotbolt.flakerandroidui.theme.FlakerAndroidTheme

@OptIn(ExperimentalMaterial3Api::class)
class FlakerActivity : ComponentActivity() {

    private val viewModel: FlakerViewModel by viewModels { FlakerAndroidOkhttpContainer.flakerViewModelFactory() }

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
                            onSearchClick = viewModel::openSearch
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

                AnimatedVisibility(
                    visible = state.toShowSearch,
                    enter = fadeIn() + slideInHorizontally { it },
                    exit = fadeOut() + slideOutHorizontally { it }
                ) {
                    SearchScreen(
                        searchUiDto = state.searchData,
                        onBack = viewModel::closeSearch,
                        onSearch = viewModel::searchNetworkRequests
                    )
                }
            }
        }
    }
}
