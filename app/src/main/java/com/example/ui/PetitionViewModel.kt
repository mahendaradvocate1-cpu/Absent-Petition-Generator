package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.PetitionRepository
import com.example.model.AccusedPerson
import com.example.model.CourtData
import com.example.model.PetitionData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class AppScreen {
    FORM,
    PREVIEW,
    HISTORY
}

class PetitionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PetitionRepository(application)

    private val _currentScreen = MutableStateFlow(AppScreen.FORM)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Form state
    private val _selectedState = MutableStateFlow("Telangana")
    val selectedState: StateFlow<String> = _selectedState.asStateFlow()

    private val _selectedDistrict = MutableStateFlow("Adilabad")
    val selectedDistrict: StateFlow<String> = _selectedDistrict.asStateFlow()

    private val _availableCourts = MutableStateFlow(CourtData.telanganaCourts["Adilabad"] ?: emptyList())
    val availableCourts: StateFlow<List<String>> = _availableCourts.asStateFlow()

    private val _selectedCourt = MutableStateFlow(
        CourtData.telanganaCourts["Adilabad"]?.firstOrNull { it.contains("POCSO") }
            ?: CourtData.telanganaCourts["Adilabad"]?.firstOrNull()
            ?: "IN THE COURT OF THE HON’BLE FASTRACK SPECIAL JUDGE FOR TRIAL OF POCSO CASES, AT: ADILABAD"
    )
    val selectedCourt: StateFlow<String> = _selectedCourt.asStateFlow()

    private val _isCustomCourt = MutableStateFlow(false)
    val isCustomCourt: StateFlow<Boolean> = _isCustomCourt.asStateFlow()

    private val _customCourtText = MutableStateFlow("")
    val customCourtText: StateFlow<String> = _customCourtText.asStateFlow()

    private val _mainCaseNo = MutableStateFlow("S.C. POCSO No. 13/2025")
    val mainCaseNo: StateFlow<String> = _mainCaseNo.asStateFlow()

    private val _legalProvision = MutableStateFlow("SECTION 355 OF B.N.S.S")
    val legalProvision: StateFlow<String> = _legalProvision.asStateFlow()

    private val _hearingPurpose = MutableStateFlow("his appearance and for Examination")
    val hearingPurpose: StateFlow<String> = _hearingPurpose.asStateFlow()

    private val _respondentName = MutableStateFlow("State of T.S., through SHO PS Jainath")
    val respondentName: StateFlow<String> = _respondentName.asStateFlow()

    private val _accusedList = MutableStateFlow(
        listOf(
            AccusedPerson(rank = "A1", name = "Ravi Rakade", reason = "suffering from ill health"),
            AccusedPerson(rank = "A2", name = "Vinod Borkar", reason = "suffering from ill health")
        )
    )
    val accusedList: StateFlow<List<AccusedPerson>> = _accusedList.asStateFlow()

    private val _placeName = MutableStateFlow("Adilabad")
    val placeName: StateFlow<String> = _placeName.asStateFlow()

    private val _filingDate = MutableStateFlow(getTodayDateString())
    val filingDate: StateFlow<String> = _filingDate.asStateFlow()

    private val _filingYear = MutableStateFlow(Calendar.getInstance().get(Calendar.YEAR))
    val filingYear: StateFlow<Int> = _filingYear.asStateFlow()

    private val _advocateAddress = MutableStateFlow(repository.getSavedAdvocateAddress())
    val advocateAddress: StateFlow<String> = _advocateAddress.asStateFlow()

    // Generated petition
    private val _generatedPetition = MutableStateFlow<PetitionData?>(null)
    val generatedPetition: StateFlow<PetitionData?> = _generatedPetition.asStateFlow()

    // Saved petitions history
    val savedPetitions: StateFlow<List<PetitionData>> = repository.getAllSavedPetitions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        val savedPlace = repository.getSavedDefaultPlace()
        if (savedPlace.isNotBlank()) {
            _placeName.value = savedPlace
        }
    }

    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    fun setDistrict(district: String) {
        _selectedDistrict.value = district
        val courts = CourtData.telanganaCourts[district] ?: emptyList()
        _availableCourts.value = courts

        val defaultCourt = courts.firstOrNull { it.contains("POCSO") } ?: courts.firstOrNull() ?: ""
        _selectedCourt.value = defaultCourt
        _isCustomCourt.value = false

        val cleanPlace = district.split(" ")[0].replace("/", "")
        _placeName.value = cleanPlace
    }

    fun setCourt(court: String) {
        if (court == "CUSTOM") {
            _isCustomCourt.value = true
        } else {
            _isCustomCourt.value = false
            _selectedCourt.value = court
        }
    }

    fun setCustomCourtText(text: String) {
        _customCourtText.value = text
    }

    fun setMainCaseNo(caseNo: String) {
        _mainCaseNo.value = caseNo
    }

    fun setLegalProvision(provision: String) {
        _legalProvision.value = provision
    }

    fun setHearingPurpose(purpose: String) {
        _hearingPurpose.value = purpose
    }

    fun setRespondentName(name: String) {
        _respondentName.value = name
    }

    fun setPlaceName(place: String) {
        _placeName.value = place
    }

    fun setFilingDate(date: String) {
        _filingDate.value = date
        // Try to parse year
        try {
            val parts = date.split("/", "-")
            if (parts.size == 3) {
                val yr = parts[2].trim().toIntOrNull()
                if (yr != null && yr in 2000..2099) {
                    _filingYear.value = yr
                }
            }
        } catch (_: Exception) {}
    }

    fun setAdvocateAddress(address: String) {
        _advocateAddress.value = address
    }

    fun saveAdvocateProfileAsDefault() {
        repository.saveAdvocateProfile(_advocateAddress.value, _placeName.value)
        _snackbarMessage.value = "Advocate profile saved as default"
    }

    fun addAccused(rank: String = "", name: String = "", reason: String = "suffering from ill health") {
        _accusedList.update { current ->
            val nextCount = current.size + 1
            val assignedRank = rank.ifBlank { "A$nextCount" }
            current + AccusedPerson(rank = assignedRank, name = name, reason = reason)
        }
    }

    fun removeAccused(id: String) {
        _accusedList.update { current ->
            if (current.size > 1) {
                current.filter { it.id != id }
            } else {
                current
            }
        }
    }

    fun updateAccusedRank(id: String, rank: String) {
        _accusedList.update { current ->
            current.map { if (it.id == id) it.copy(rank = rank) else it }
        }
    }

    fun updateAccusedName(id: String, name: String) {
        _accusedList.update { current ->
            current.map { if (it.id == id) it.copy(name = name) else it }
        }
    }

    fun updateAccusedReason(id: String, reason: String) {
        _accusedList.update { current ->
            current.map { if (it.id == id) it.copy(reason = reason) else it }
        }
    }

    fun generatePetition() {
        val courtHeading = if (_isCustomCourt.value) {
            _customCourtText.value.trim().ifBlank { "IN THE COURT OF THE HON’BLE SPECIAL JUDGE" }
        } else {
            _selectedCourt.value
        }

        val petition = PetitionData(
            state = _selectedState.value,
            district = _selectedDistrict.value,
            courtHeading = courtHeading,
            isCustomCourt = _isCustomCourt.value,
            mainCaseNo = _mainCaseNo.value.trim().ifBlank { "Case No. /2026" },
            filingYear = _filingYear.value,
            legalProvision = _legalProvision.value,
            hearingPurpose = _hearingPurpose.value.trim().ifBlank { "hearing" },
            respondentName = _respondentName.value.trim().ifBlank { "State" },
            accusedList = _accusedList.value,
            placeName = _placeName.value.trim().ifBlank { "Court Place" },
            filingDate = _filingDate.value.trim().ifBlank { getTodayDateString() },
            advocateAddress = _advocateAddress.value.trim()
        )

        _generatedPetition.value = petition
        _currentScreen.value = AppScreen.PREVIEW

        // Automatically save to local database
        viewModelScope.launch {
            repository.savePetition(petition)
        }
    }

    fun loadPetition(petition: PetitionData) {
        _selectedState.value = petition.state
        _selectedDistrict.value = petition.district
        _availableCourts.value = CourtData.telanganaCourts[petition.district] ?: emptyList()
        _selectedCourt.value = petition.courtHeading
        _isCustomCourt.value = petition.isCustomCourt
        if (petition.isCustomCourt) {
            _customCourtText.value = petition.courtHeading
        }
        _mainCaseNo.value = petition.mainCaseNo
        _filingYear.value = petition.filingYear
        _legalProvision.value = petition.legalProvision
        _hearingPurpose.value = petition.hearingPurpose
        _respondentName.value = petition.respondentName
        _accusedList.value = petition.accusedList
        _placeName.value = petition.placeName
        _filingDate.value = petition.filingDate
        _advocateAddress.value = petition.advocateAddress

        _generatedPetition.value = petition
        _currentScreen.value = AppScreen.PREVIEW
    }

    fun deleteSavedPetition(id: String) {
        viewModelScope.launch {
            repository.deletePetition(id)
            _snackbarMessage.value = "Petition removed from history"
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}
