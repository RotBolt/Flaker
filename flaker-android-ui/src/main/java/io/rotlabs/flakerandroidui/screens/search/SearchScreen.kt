package io.rotlabs.flakerandroidui.screens.search

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.rotlabs.flakerandroidui.R
import io.rotlabs.flakerandroidui.components.lists.NetworkRequestList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

private const val DEBOUNCE_PERIOD = 300L

@OptIn(FlowPreview::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchUiDto: SearchUiDto,
    onBack: () -> Unit,
    onSearch: (String) -> Unit
) {
    BackHandler {
        onBack()
    }

    var searchTerm by remember { mutableStateOf("") }

    val searchTermFlow = remember { MutableStateFlow("") }

    val coroutineScope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current

    fun clearSearch() {
        searchTerm = ""
        coroutineScope.launch { searchTermFlow.emit("") }
    }

    LaunchedEffect(Unit) {
        searchTermFlow
            .debounce(DEBOUNCE_PERIOD)
            .collectLatest {
                onSearch(it.trim())
            }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchTerm,
                onValueChange = {
                    searchTerm = it
                    coroutineScope.launch { searchTermFlow.emit(it) }
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Navigate back",
                        modifier = Modifier.clickable(onClick = onBack)
                    )
                },
                trailingIcon = {
                    AnimatedVisibility(visible = searchTerm.isNotEmpty()) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Clear search",
                            modifier = Modifier.clickable(onClick = ::clearSearch)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch(searchTerm)
                        keyboardController?.hide()
                    }
                )
            )
        }
    ) {
        if (searchUiDto.filteredContent != null) {
            NetworkRequestList(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(it)
            ) {
                searchUiDto.filteredContent
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.search_screen_default_label),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}
