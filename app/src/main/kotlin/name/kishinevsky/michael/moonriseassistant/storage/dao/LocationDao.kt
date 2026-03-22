@file:Suppress("unused", "RedundantSuppression")

package name.kishinevsky.michael.moonriseassistant.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import name.kishinevsky.michael.moonriseassistant.storage.entity.LocationEntity

@Dao
interface LocationDao {

    @Insert
    suspend fun insert(location: LocationEntity): Long

    @Query("SELECT * FROM locations ORDER BY id")
    fun getAll(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE isActive = 1 LIMIT 1")
    fun getActive(): Flow<LocationEntity?>

    @Query("UPDATE locations SET isActive = 0")
    suspend fun clearActive()

    @Query("UPDATE locations SET isActive = 1 WHERE id = :locationId")
    suspend fun setActive(locationId: Long)

    @Query("DELETE FROM locations WHERE id = :locationId")
    suspend fun delete(locationId: Long)

    @Query("SELECT COUNT(*) FROM locations")
    suspend fun count(): Int

    @Update
    suspend fun update(location: LocationEntity)

    @Query("SELECT * FROM locations WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): LocationEntity?
}
