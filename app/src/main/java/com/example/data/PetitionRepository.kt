package com.example.data

import android.content.Context
import androidx.room.Room
import com.example.model.PetitionData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PetitionRepository(context: Context) {
    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "absent_petitions.db"
    ).build()

    private val prefs = context.getSharedPreferences("advocate_profile", Context.MODE_PRIVATE)

    fun getAllSavedPetitions(): Flow<List<PetitionData>> {
        return db.petitionDao().getAllPetitions().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun savePetition(petition: PetitionData) {
        db.petitionDao().insertPetition(PetitionEntity.fromDomain(petition))
    }

    suspend fun deletePetition(id: String) {
        db.petitionDao().deleteById(id)
    }

    fun saveAdvocateProfile(nameAndAddress: String, defaultPlace: String) {
        prefs.edit()
            .putString("advocate_address", nameAndAddress)
            .putString("default_place", defaultPlace)
            .apply()
    }

    fun getSavedAdvocateAddress(): String {
        return prefs.getString(
            "advocate_address",
            "MAHENDAR B, ADVOCATE\nH.No. 9-46/5, Lane No.7, Teachers Colony, Adilabad\nCell No: 8501002211"
        ) ?: "MAHENDAR B, ADVOCATE\nH.No. 9-46/5, Lane No.7, Teachers Colony, Adilabad\nCell No: 8501002211"
    }

    fun getSavedDefaultPlace(): String {
        return prefs.getString("default_place", "Adilabad") ?: "Adilabad"
    }
}
