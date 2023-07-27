package io.rotlabs.flakerandroidretrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.rotlabs.flakerandroidapp.ui.listitem.NetworkRequestItem
import io.rotlabs.flakerandroidapp.ui.listitem.SectionDateItem
import io.rotlabs.flakerandroidapp.ui.theme.FlakerAndroidTheme
import io.rotlabs.flakerandroidapp.R as CompanionAppResource

@OptIn(ExperimentalMaterial3Api::class)
class FlakerActivity : ComponentActivity() {

    private val viewModel: FlakerViewModel by viewModels { FlakerViewModel.Factory }

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
                            state = state,
                            scrollBehavior = scrollBehavior,
                            onToggleChange = viewModel::toggleFlaker,
                            onPrefsClick = viewModel::openPrefs
                        )
                    }
                ) { scaffoldPadding ->
                    NetworkRequestList(modifier = Modifier.padding(scaffoldPadding), state = state)
                }

                if (state.toShowPrefs) {
                    FlakerPrefsDialog(
                        onDismissRequest = viewModel::closePrefs,
                        onConfirmAction = viewModel::updatePrefs,
                        currentValues = viewModel.getCurrentPrefs(),
                    )
                }
            }
        }
    }

    @Composable
    private fun NetworkRequestList(modifier: Modifier = Modifier, state: FlakerViewModel.ViewState) {
        if (state.showNoRequests) {
            LazyColumn(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = stringResource(R.string.no_requests_yet),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp)

                    )
                }
            }
        } else {
            LazyColumn(modifier = modifier) {
                items(state.networkRequests) { item ->
                    when (item) {
                        is NetworkRequestUi.NetworkRequestItem -> {
                            NetworkRequestItem(
                                networkRequest = item.networkRequest,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                        }

                        is NetworkRequestUi.DateItem -> {
                            Spacer(modifier = Modifier.size(8.dp))
                            SectionDateItem(
                                formattedDate = item.formattedString,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun FlakerBar(
        modifier: Modifier = Modifier,
        state: FlakerViewModel.ViewState,
        onToggleChange: (Boolean) -> Unit,
        onPrefsClick: () -> Unit,
        scrollBehavior: TopAppBarScrollBehavior
    ) {
        LargeTopAppBar(
            modifier = modifier,
            scrollBehavior = scrollBehavior,
            title = {
                Text(
                    text = stringResource(id = CompanionAppResource.string.companion_app_name),
                    color = MaterialTheme.colorScheme.primary
                )
            },
            actions = {
                Switch(
                    checked = state.isFlakerOn,
                    onCheckedChange = onToggleChange,
                    modifier = Modifier.wrapContentWidth()
                )

                Spacer(modifier = Modifier.size(8.dp))

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search network requests")
                }

                Spacer(modifier = Modifier.size(4.dp))

                IconButton(onClick = onPrefsClick) {
                    Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Search network requests")
                }

                Spacer(modifier = Modifier.size(8.dp))
            }
        )
    }
}
