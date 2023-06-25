package io.rotlabs.flakerandroidapp.ui.listitem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.rotlabs.flakerandroidapp.ui.theme.FlakerAndroidTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SectionDateItem(timeInMillis: Long) {
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
    val formattedDate = dateFormatter.format(Date(timeInMillis))

    Text(
        text = formattedDate.toString(),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
@Preview
fun SectionDateItemPreview() {
    FlakerAndroidTheme {
        val timeInMillis = 1687673435000
        SectionDateItem(timeInMillis = timeInMillis)
    }
}
