package name.kishinevsky.michael.moonriseassistant.storage.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import name.kishinevsky.michael.moonriseassistant.storage.entity.SettingsEntity

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings WHERE id = 1")
    fun getSettings(): Flow<SettingsEntity?>

    @Upsert
    suspend fun insertOrUpdate(settings: SettingsEntity)
}
