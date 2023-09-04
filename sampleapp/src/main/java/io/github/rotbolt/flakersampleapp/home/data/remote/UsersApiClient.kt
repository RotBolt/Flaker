package io.github.rotbolt.flakersampleapp.home.data.remote

import io.github.rotbolt.flakersampleapp.data.remote.RemoteResult
import io.github.rotbolt.flakersampleapp.data.remote.handleRemoteResponse
import io.github.rotbolt.flakersampleapp.home.data.remote.dto.UserData
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
