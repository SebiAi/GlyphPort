package com.sebiai.glyphport.data

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

val Context.dataStore: DataStore<UserPreferences> by dataStore(
    fileName = "user_preferences.json",
    serializer = UserPreferences.dataStoreSerializer()
)

@Serializable
data class UserPreferences(
    // Increment when migration is needed - migration should be handled in readFrom!
    val version: Long,
    val checkForUpdates: Boolean
) {
    companion object {
        fun dataStoreSerializer(): Serializer<UserPreferences> {
            return object: Serializer<UserPreferences> {
                override val defaultValue: UserPreferences
                    get() = UserPreferences(
                        version = 1,
                        checkForUpdates = true
                    )

                private val json = Json { ignoreUnknownKeys = true }

                override suspend fun readFrom(input: InputStream): UserPreferences {
                    val userPreferences = try {
                        json.decodeFromString<UserPreferences>(
                            input.readBytes().decodeToString()
                        )
                    } catch (serialization: SerializationException) {
                        throw CorruptionException("Unable to read Settings", serialization)
                    }

                    if (userPreferences.version < defaultValue.version) {
                        // TODO: Migrate
                    }

                    return userPreferences
                }

                override suspend fun writeTo(
                    t: UserPreferences,
                    output: OutputStream
                ) {
                    output.write(
                        json.encodeToString(t).encodeToByteArray()
                    )
                }

            }
        }
    }
}

class UserPreferencesRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    val checkForUpdates: Flow<Boolean> = context.dataStore.data.map {
        it.checkForUpdates
    }

    suspend fun setCheckForUpdates(enabled: Boolean) {
        context.dataStore.updateData {
            it.copy(checkForUpdates = enabled)
        }
    }
}
