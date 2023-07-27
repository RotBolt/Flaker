package io.rotlabs.flakersampleapp.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.rotlabs.flakersampleapp.data.remote.RemoteResult
import io.rotlabs.flakersampleapp.home.data.remote.UsersApiClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@Suppress("UnusedPrivateProperty")
class HomeViewModel constructor(
    private val usersApiClient: UsersApiClient,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    sealed class SideEffect {
        data class ShowToast(val message: String) : SideEffect()
    }

    private val _sideEffect = MutableSharedFlow<SideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun getUsers() {
        viewModelScope.launch {
            when (usersApiClient.getUsers()) {
                is RemoteResult.Success.Data -> {
                    _sideEffect.emit(SideEffect.ShowToast("Success"))
                }
                is RemoteResult.Success.Empty -> {
                    _sideEffect.emit(SideEffect.ShowToast("Empty"))
                }
                is RemoteResult.Error -> {
                    _sideEffect.emit(SideEffect.ShowToast("Error"))
                }
            }
        }
    }
}
