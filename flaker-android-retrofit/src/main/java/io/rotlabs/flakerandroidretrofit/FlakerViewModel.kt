package io.rotlabs.flakerandroidretrofit

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.rotlabs.flakerretrofit.data.FlakerRepo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FlakerViewModel(
    private val flakerRepo: FlakerRepo,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _viewStateFlow = MutableStateFlow(ViewState())
    val viewStateFlow = _viewStateFlow.asStateFlow()

    data class ViewState(
        val isFlakerOn: Boolean = false,
        val networkRequests: List<NetworkRequestUi> = emptyList(),
        val toShowPrefs: Boolean = false,
    ) {
        val showNoRequests: Boolean
            get() = networkRequests.isEmpty()
    }

    init {
        observeIsFlakerOn()
        observeAllRequests()
    }

    private fun observeIsFlakerOn() {
        flakerRepo.observeFlakerOn().onEach {
            _viewStateFlow.emit(_viewStateFlow.value.copy(isFlakerOn = it))
        }.launchIn(viewModelScope)
    }

    private fun observeAllRequests() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("FlakerViewModel", "Error loading all requests", throwable)
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            flakerRepo.observeAllRequests()
                .collectLatest {
                    val uiList: List<NetworkRequestUi> = it
                        .map { NetworkRequestUi.NetworkRequestItem(it) }
                        .groupBy { it.networkRequest.requestTime }
                        .entries
                        .flatMap { (dateLong, items) ->
                            listOf(NetworkRequestUi.DateItem(dateLong.toString())) + items
                        }
                    _viewStateFlow.emit(_viewStateFlow.value.copy(networkRequests = uiList))
                }
        }
    }

    fun toggleFlaker(value: Boolean) {
        flakerRepo.saveShouldIntercept(value)
    }

    fun openPrefs() {
        viewModelScope.launch { _viewStateFlow.emit(_viewStateFlow.value.copy(toShowPrefs = true)) }
    }

    fun getCurrentPrefs(): FlakerPrefsUiDto {
        return FlakerPrefsUiDto(
            delay = flakerRepo.getDelayValue(),
            failPercent = flakerRepo.getFailPercent(),
            variancePercent = flakerRepo.getVariancePercent()
        )
    }

    fun closePrefs() {
        viewModelScope.launch { _viewStateFlow.emit(_viewStateFlow.value.copy(toShowPrefs = false)) }
    }

    fun updatePrefs(flakerPrefsUiDto: FlakerPrefsUiDto) {
        flakerRepo.saveDelayValue(flakerPrefsUiDto.delay)
        flakerRepo.saveFailPercent(flakerPrefsUiDto.failPercent)
        flakerRepo.saveVariancePercent(flakerPrefsUiDto.variancePercent)
        closePrefs()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val flakerRepo = FlakerRepo(checkNotNull(this[APPLICATION_KEY]))
                FlakerViewModel(flakerRepo, savedStateHandle)
            }
        }
    }
}
