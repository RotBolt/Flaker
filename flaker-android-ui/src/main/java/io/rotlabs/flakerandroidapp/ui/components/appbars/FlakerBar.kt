package io.rotlabs.flakerandroidapp.ui.components.appbars

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.rotlabs.flakerandroidui.R

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UnusedParameter")
@Composable
fun FlakerBar(
    modifier: Modifier = Modifier,
    switchStateProvider: () -> Boolean,
    onToggleChange: (Boolean) -> Unit,
    onPrefsClick: () -> Unit,
    onSearchClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(id = R.string.companion_app_name),
                color = MaterialTheme.colorScheme.primary
            )
        },
        actions = {
            Switch(
                checked = switchStateProvider(),
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
