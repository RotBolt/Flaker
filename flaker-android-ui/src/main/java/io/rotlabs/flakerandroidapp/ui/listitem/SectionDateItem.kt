package io.rotlabs.flakerandroidapp.ui.listitem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SectionDateItem(formattedDate: String, modifier: Modifier = Modifier) {
    Text(
        text = formattedDate,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier.fillMaxWidth()
    )
}
