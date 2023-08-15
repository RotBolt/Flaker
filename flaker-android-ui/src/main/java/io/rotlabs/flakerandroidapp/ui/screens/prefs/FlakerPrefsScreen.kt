package io.rotlabs.flakerandroidapp.ui.screens.prefs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.rotlabs.flakerandroidui.R
import kotlin.math.round

private val PrefUiDtoSaver = Saver<FlakerPrefsUiDto, String>(
    save = { "${it.delay}, ${it.failPercent}, ${it.variancePercent}" },
    restore = { valuesString ->
        valuesString.split(",").let {
            FlakerPrefsUiDto(it[0].trim().toInt(), it[1].trim().toInt(), it[2].trim().toInt())
        }
    }
)

private const val HUNDRED_PERCENT = 100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlakerPrefsDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirmAction: (FlakerPrefsUiDto) -> Unit,
    currentValuesProvider: () -> FlakerPrefsUiDto
) {
    BackHandler {
        onDismissRequest()
    }

    var newValues by rememberSaveable(stateSaver = PrefUiDtoSaver) { mutableStateOf(value = currentValuesProvider()) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(text = "Preferences", modifier = Modifier.padding(horizontal = 8.dp)) },
                navigationIcon = {
                    IconButton(onClick = onDismissRequest) {
                        Icon(imageVector = Icons.Outlined.Close, contentDescription = "Dismiss")
                    }
                },
                actions = {
                    Button(
                        onClick = { onConfirmAction(newValues) },
                    ) {
                        Text(text = "Save")
                    }

                    Spacer(modifier = Modifier.size(16.dp))
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                val delayLabel = stringResource(id = R.string.delay_label)
                val delayText = remember(newValues.delay) { "$delayLabel: ${newValues.delay}ms" }
                val delayCurrentValue = remember(newValues.delay) { newValues.delay.toFloat() / HUNDRED_PERCENT }
                PrefsItem(
                    title = delayText,
                    currentValue = delayCurrentValue,
                    onValueChange = { value ->
                        if (value >= 0.5f) {
                            newValues = newValues.copy(delay = (round(value).toInt() * HUNDRED_PERCENT))
                        }
                    },
                    discreteSteps = 19,
                    modifier = Modifier.padding(24.dp)
                )

                Spacer(modifier = Modifier.size(16.dp))
            }

            item {
                val failPercentLabel = stringResource(id = R.string.fail_percentage_label)
                val failPercentText = remember(newValues.failPercent) { "$failPercentLabel: ${newValues.failPercent}%" }
                val failPercentCurrentValue = remember(newValues.failPercent) { newValues.failPercent.toFloat() }
                PrefsItem(
                    title = failPercentText,
                    currentValue = failPercentCurrentValue,
                    onValueChange = { value -> newValues = newValues.copy(failPercent = round(value).toInt()) },
                    discreteSteps = 19,
                    modifier = Modifier.padding(24.dp)
                )

                Spacer(modifier = Modifier.size(16.dp))
            }

            item {
                val variancePercentLabel = stringResource(id = R.string.variance_percentage_label)
                val variancePercentText =
                    remember(newValues.variancePercent) { "$variancePercentLabel: ${newValues.variancePercent}%" }
                val variancePercentCurrentValue =
                    remember(newValues.variancePercent) { newValues.variancePercent.toFloat() }
                PrefsItem(
                    title = variancePercentText,
                    currentValue = variancePercentCurrentValue,
                    onValueChange = { value -> newValues = newValues.copy(variancePercent = round(value).toInt()) },
                    discreteSteps = 19,
                    modifier = Modifier.padding(24.dp)
                )
            }
        }
    }
}

@Composable
private fun PrefsItem(
    modifier: Modifier = Modifier,
    title: String,
    minValue: Float = 0f,
    maxValue: Float = 100f,
    currentValue: Float,
    onValueChange: (Float) -> Unit,
    discreteSteps: Int = 0,
) {
    Column(modifier = modifier) {
        Text(text = title)

        Spacer(modifier = Modifier.size(8.dp))

        Slider(
            value = currentValue,
            onValueChange = onValueChange,
            valueRange = minValue..maxValue,
            steps = discreteSteps
        )
    }
}
