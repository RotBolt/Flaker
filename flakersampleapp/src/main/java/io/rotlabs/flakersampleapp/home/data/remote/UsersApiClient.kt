package io.rotlabs.flakersampleapp.home.data.remote

import io.rotlabs.flakersampleapp.data.remote.RemoteResult
import io.rotlabs.flakersampleapp.data.remote.handleRemoteResponse
import io.rotlabs.flakersampleapp.home.data.remote.dto.UserData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UsersApiClient(
    private val usersApiService: UsersApiService,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getUsers(): RemoteResult<UserData> {
        return withContext(ioDispatcher) {
            return@withContext handleRemoteResponse {
                usersApiService.getUsers()
            }
        }
    }
}
