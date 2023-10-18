package io.github.bolt.flakersampleapp.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.bolt.flakersampleapp.data.remote.RemoteResult
import io.github.bolt.flakersampleapp.home.data.remote.UsersApiClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Suppress("UnusedPrivateProperty")
class HomeViewModel(
    private val usersApiClient: UsersApiClient,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    data class UiState(
        val isRetrofitRequestLoading: Boolean = false,
        val isKtorRequestLoading: Boolean = false
    )

    sealed class SideEffect {
        data class ShowSnackBar(val message: String) : SideEffect()
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun fireRetrofitRequest() {
        viewModelScope.launch {
            _uiState.emit(_uiState.value.copy(isRetrofitRequestLoading = true))
            when (usersApiClient.getUsers()) {
                is RemoteResult.Success.Data -> {
                    _sideEffect.emit(SideEffect.ShowSnackBar("Retrofit Request: Success"))
                }
                is RemoteResult.Success.Empty -> {
                    _sideEffect.emit(SideEffect.ShowSnackBar("Retrofit Request: Empty"))
                }
                is RemoteResult.Error -> {
                    _sideEffect.emit(SideEffect.ShowSnackBar("Retrofit Request: Error"))
                }
            }
            _uiState.emit(_uiState.value.copy(isRetrofitRequestLoading = false))
        }
    }
}
