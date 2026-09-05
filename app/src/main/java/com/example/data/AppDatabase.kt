package com.example.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.model.AccusedPerson
import com.example.model.PetitionData
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

@Entity(tableName = "saved_petitions")
data class PetitionEntity(
    @PrimaryKey
    val id: String,
    val timestamp: Long,
    val mainCaseNo: String,
    val district: String,
    val courtHeading: String,
    val isCustomCourt: Boolean,
    val filingYear: Int,
    val legalProvision: String,
    val hearingPurpose: String,
    val respondentName: String,
    val accusedJson: String,
    val placeName: String,
    val filingDate: String,
    val advocateAddress: String
) {
    fun toDomain(): PetitionData {
        val list = mutableListOf<AccusedPerson>()
        try {
            val arr = JSONArray(accusedJson)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    AccusedPerson(
                        id = obj.optString("id"),
                        rank = obj.optString("rank"),
                        name = obj.optString("name"),
                        reason = obj.optString("reason")
                    )
                )
            }
        } catch (_: Exception) {
            // fallback if json parsing fails
        }
        return PetitionData(
            id = id,
            timestamp = timestamp,
            district = district,
            courtHeading = courtHeading,
            isCustomCourt = isCustomCourt,
            mainCaseNo = mainCaseNo,
            filingYear = filingYear,
            legalProvision = legalProvision,
            hearingPurpose = hearingPurpose,
            respondentName = respondentName,
            accusedList = if (list.isEmpty()) listOf(AccusedPerson()) else list,
            placeName = placeName,
            filingDate = filingDate,
            advocateAddress = advocateAddress
        )
    }

    companion object {
        fun fromDomain(data: PetitionData): PetitionEntity {
            val arr = JSONArray()
            data.accusedList.forEach { a ->
                val obj = JSONObject()
                obj.put("id", a.id)
                obj.put("rank", a.rank)
                obj.put("name", a.name)
                obj.put("reason", a.reason)
                arr.put(obj)
            }
            return PetitionEntity(
                id = data.id,
                timestamp = data.timestamp,
                mainCaseNo = data.mainCaseNo,
                district = data.district,
                courtHeading = data.courtHeading,
                isCustomCourt = data.isCustomCourt,
                filingYear = data.filingYear,
                legalProvision = data.legalProvision,
                hearingPurpose = data.hearingPurpose,
                respondentName = data.respondentName,
                accusedJson = arr.toString(),
                placeName = data.placeName,
                filingDate = data.filingDate,
                advocateAddress = data.advocateAddress
            )
        }
    }
}

@Dao
interface PetitionDao {
    @Query("SELECT * FROM saved_petitions ORDER BY timestamp DESC")
    fun getAllPetitions(): Flow<List<PetitionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPetition(petition: PetitionEntity)

    @Delete
    suspend fun deletePetition(petition: PetitionEntity)

    @Query("DELETE FROM saved_petitions WHERE id = :id")
    suspend fun deleteById(id: String)
}

@Database(entities = [PetitionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petitionDao(): PetitionDao
}
