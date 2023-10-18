package io.github.rotbolt.flakerandroidui.components.lists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.rotbolt.flakerandroidui.R
import io.github.rotbolt.flakerandroidui.elements.listitem.NetworkRequestItem
import io.github.rotbolt.flakerandroidui.elements.listitem.SectionDateItem

typealias NetworkRequestListContent = Map<NetworkRequestUi.DateItem, List<NetworkRequestUi.NetworkRequestItem>>

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NetworkRequestList(modifier: Modifier = Modifier, contentProvider: () -> NetworkRequestListContent) {
    val content = contentProvider()
    if (content.isEmpty()) {
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
            content.forEach { (date, networkRequestList) ->

                stickyHeader {
                    SectionDateItem(
                        formattedDate = date.formattedString,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    )
                }

                items(networkRequestList) { item ->
                    NetworkRequestItem(
                        networkRequestInfo = item.networkRequestInfo,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
        }
    }
}
