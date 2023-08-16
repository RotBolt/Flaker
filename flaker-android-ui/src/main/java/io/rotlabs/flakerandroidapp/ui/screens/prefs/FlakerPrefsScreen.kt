package io.rotlabs.flakerandroidapp.ui.screens.prefs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.rotlabs.flakerandroidui.R
import kotlin.math.round

private val PrefUiDtoSaver = Saver<FlakerPrefsUiDto, String>(
    save = { "${it.delay}, ${it.failPercent}, ${it.variancePercent}, ${it.retentionPolicyDays}" },
    restore = { valuesString ->
        valuesString.split(",").let {
            FlakerPrefsUiDto(it[0].trim().toInt(), it[1].trim().toInt(), it[2].trim().toInt(), it[3].trim())
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
                PrefDropDownItem(
                    title = "${stringResource(id = R.string.retention_policy_label)}:",
                    values = stringArrayResource(id = R.array.retention_policy_days),
                    currentValue = newValues.retentionPolicyDays,
                    onItemSelect = { value -> newValues = newValues.copy(retentionPolicyDays = value) },
                    modifier = Modifier.padding(24.dp)
                )
            }

            item {
                val delayLabel = stringResource(id = R.string.delay_label)
                val delayText = remember(newValues.delay) { "$delayLabel: ${newValues.delay}ms" }
                val delayCurrentValue = remember(newValues.delay) { newValues.delay.toFloat() / HUNDRED_PERCENT }
                PrefSliderItem(
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
                PrefSliderItem(
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
                PrefSliderItem(
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
private fun PrefSliderItem(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrefDropDownItem(
    modifier: Modifier = Modifier,
    title: String,
    values: Array<String>,
    currentValue: String,
    onItemSelect: (String) -> Unit
) {
    var toExpand by remember { mutableStateOf(false) }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)

        Spacer(modifier = Modifier.size(8.dp))

        ExposedDropdownMenuBox(
            modifier = Modifier.clickable { toExpand = toExpand.not() },
            expanded = toExpand,
            onExpandedChange = {}
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp).menuAnchor()
            ) {
                Text(text = currentValue, modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.size(8.dp))

                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
            }
            ExposedDropdownMenu(
                expanded = toExpand,
                onDismissRequest = {
                    toExpand = toExpand.not()
                }
            ) {
                values.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it) },
                        onClick = {
                            onItemSelect(it)
                            toExpand = toExpand.not()
                        }
                    )
                }
            }
        }
    }
}
