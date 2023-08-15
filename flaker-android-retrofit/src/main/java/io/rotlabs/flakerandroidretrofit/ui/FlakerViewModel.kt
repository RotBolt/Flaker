package io.rotlabs.flakerandroidretrofit.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.rotlabs.flakerandroidapp.ui.components.lists.NetworkRequestUi
import io.rotlabs.flakerandroidapp.ui.screens.prefs.FlakerPrefsUiDto
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepo
import io.rotlabs.flakerprefs.PrefDataStore
import io.rotlabs.flakerprefs.dto.FlakerPrefs
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("UnusedPrivateProperty")
class FlakerViewModel(
    private val networkRequestRepo: NetworkRequestRepo,
    private val prefDataStore: PrefDataStore,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _viewStateFlow = MutableStateFlow(ViewState())
    val viewStateFlow = _viewStateFlow.asStateFlow()

    data class ViewState(
        val isFlakerOn: Boolean = false,
        val networkRequests: Map<NetworkRequestUi.DateItem, List<NetworkRequestUi.NetworkRequestItem>> = emptyMap(),
        val currentPrefs: FlakerPrefsUiDto = FlakerPrefsUiDto.IMMATERIAL,
    ) {
        val toShowPrefs: Boolean
            get() = currentPrefs != FlakerPrefsUiDto.IMMATERIAL
    }

    init {
        observeIsFlakerOn()
        observeAllRequests()
    }

    private fun observeIsFlakerOn() {
        prefDataStore.getPrefs()
            .map { it.shouldIntercept }
            .onEach { _viewStateFlow.emit(_viewStateFlow.value.copy(isFlakerOn = it)) }
            .launchIn(viewModelScope)
    }

    private fun observeAllRequests() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("FlakerViewModel", "Error loading all requests", throwable)
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            networkRequestRepo.observeAll()
                .collectLatest { list ->
                    val uiMapList = list
                        .map { NetworkRequestUi.NetworkRequestItem(it) }
                        .sortedByDescending { it.networkRequest.requestTime }
                        .groupBy {
                            val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
                            val formattedString = dateFormatter.format(Date(it.networkRequest.requestTime))
                            NetworkRequestUi.DateItem(formattedString)
                        }
                    _viewStateFlow.emit(_viewStateFlow.value.copy(networkRequests = uiMapList))
                }
        }
    }

    fun toggleFlaker(value: Boolean) {
        viewModelScope.launch {
            prefDataStore.savePrefs(prefDataStore.getPrefs().first().copy(shouldIntercept = value))
        }
    }

    fun openPrefs() {
        viewModelScope.launch {
            val flakerPrefs = prefDataStore.getPrefs().first()
            val flakerPrefsDto = FlakerPrefsUiDto(
                delay = flakerPrefs.delay,
                failPercent = flakerPrefs.failPercent,
                variancePercent = flakerPrefs.variancePercent
            )
            _viewStateFlow.emit(_viewStateFlow.value.copy(currentPrefs = flakerPrefsDto))
        }
    }

    fun closePrefs() {
        viewModelScope.launch {
            _viewStateFlow.emit(
                _viewStateFlow.value.copy(currentPrefs = FlakerPrefsUiDto.IMMATERIAL)
            )
        }
    }

    fun updatePrefs(flakerPrefsUiDto: FlakerPrefsUiDto) {
        viewModelScope.launch {
            prefDataStore.savePrefs(
                FlakerPrefs(
                    shouldIntercept = true,
                    delay = flakerPrefsUiDto.delay,
                    failPercent = flakerPrefsUiDto.failPercent,
                    variancePercent = flakerPrefsUiDto.variancePercent
                )
            )
        }
        closePrefs()
    }
}
