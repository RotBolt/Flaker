package io.rotlabs.flakerandroidui.elements.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rotlabs.flakedomain.networkrequest.NetworkRequest
import io.rotlabs.flakerandroidui.theme.FlakerAndroidTheme
import io.rotlabs.flakerandroidui.theme.statusCodeError
import io.rotlabs.flakerandroidui.theme.statusCodeOther
import io.rotlabs.flakerandroidui.theme.statusCodeSuccess
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun NetworkRequestItem(
    modifier: Modifier = Modifier,
    networkRequest: NetworkRequest
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Text(
                text = networkRequest.responseCode.toString(),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = getStatusCodeColor(networkRequest.responseCode)
                ),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp, end = 32.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                val methodAndPathText = "${networkRequest.method} /${networkRequest.path}"
                Text(
                    text = methodAndPathText,
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                )

                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.Start)
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Host",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(16.dp)
                    )
                    Text(
                        text = networkRequest.host,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 4.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .align(Alignment.Start)
                ) {
                    val localTime = Instant.fromEpochMilliseconds(networkRequest.requestTime)
                        .toLocalDateTime(TimeZone.currentSystemDefault())

                    Text(
                        text = localTime.time.toString().substringBefore("."),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Text(
                        text = "${networkRequest.responseTimeTaken} ms",
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 20.dp)
                    )

                    if (networkRequest.isFailedByFlaker) {
                        Text(
                            text = "flaker",
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Suppress("MagicNumber")
fun getStatusCodeColor(statusCode: Long): Color {
    return when (statusCode) {
        in 200..299 -> statusCodeSuccess
        in 400..599 -> statusCodeError
        else -> statusCodeOther
    }
}

@Suppress("UnusedPrivateMember")
@Composable
@Preview
fun NetworkRequestItemPreview() {
    FlakerAndroidTheme {
        Column {
            repeat(4) {
                val networkRequest = NetworkRequest(
                    host = "localhost:8080",
                    path = "v1/sample/path",
                    method = "GET",
                    requestTime = 1687673435000,
                    responseTimeTaken = 145L,
                    responseCode = if (it == 0) 200 else if (it == 1) 300 else 404,
                    isFailedByFlaker = it >= 2,
                    createdAt = 1692270425000
                )
                NetworkRequestItem(networkRequest = networkRequest, modifier = Modifier.padding(16.dp))
            }
        }
    }
}
