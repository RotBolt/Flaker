package io.rotlabs.flakerandroidretrofit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.rotlabs.flakedomain.networkrequest.NetworkRequest
import io.rotlabs.flakerretrofit.data.FlakerRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FlakerViewModel(
    private val flakerRepo: FlakerRepo,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _viewStateFlow = MutableStateFlow<ViewState>(ViewState())
    val viewStateFlow = _viewStateFlow.asStateFlow()

    data class ViewState(
        val isFlakerOn: Boolean = false,
        val networkRequests: List<NetworkRequest> = emptyList()
    )

    suspend fun loadAllRequests() {
        val list = flakerRepo.allRequests()
        _viewStateFlow.emit(_viewStateFlow.value.copy(networkRequests = list))
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
