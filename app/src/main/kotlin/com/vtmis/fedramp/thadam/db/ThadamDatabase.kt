package com.vtmis.fedramp.thadam.db

import android.content.Context
import androidx.room.*

/** Room entity for trust history records */
@Entity(tableName = "trust_history")
data class TrustHistoryEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val globalScore: Double,
    val trustLevel: String,
    val confidentiality: Double,
    val integrity: Double,
    val availability: Double,
    val chainFloor: String,
    val gate1Score: Double,
    val gate2Score: Double,
    val gate3Score: Double,
    val gate4Score: Double,
    val gate5Score: Double,
    val gate6Score: Double,
    val deviceName: String,
    val manufacturer: String,
    val androidVersion: String,
    val securityPatch: String
)

@Dao
interface TrustHistoryDao {
    @Insert
    suspend fun insert(entry: TrustHistoryEntry)

    @Query("SELECT * FROM trust_history ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecent(limit: Int = 100): List<TrustHistoryEntry>

    @Query("SELECT * FROM trust_history WHERE timestamp >= :since ORDER BY timestamp ASC")
    suspend fun getSince(since: Long): List<TrustHistoryEntry>

    @Query("SELECT COUNT(*) FROM trust_history")
    suspend fun count(): Int

    @Query("DELETE FROM trust_history WHERE id IN (SELECT id FROM trust_history ORDER BY timestamp ASC LIMIT :count)")
    suspend fun deleteOldest(count: Int)

    @Query("SELECT AVG(globalScore) FROM trust_history WHERE timestamp >= :since")
    suspend fun avgScoreSince(since: Long): Double?

    @Query("SELECT MIN(globalScore) FROM trust_history WHERE timestamp >= :since")
    suspend fun minScoreSince(since: Long): Double?

    @Query("SELECT MAX(globalScore) FROM trust_history WHERE timestamp >= :since")
    suspend fun maxScoreSince(since: Long): Double?
}

@Database(entities = [TrustHistoryEntry::class], version = 1, exportSchema = false)
abstract class ThadamDatabase : RoomDatabase() {
    abstract fun trustHistoryDao(): TrustHistoryDao

    companion object {
        @Volatile private var INSTANCE: ThadamDatabase? = null

        fun get(context: Context): ThadamDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ThadamDatabase::class.java,
                    "thadam_trust.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
