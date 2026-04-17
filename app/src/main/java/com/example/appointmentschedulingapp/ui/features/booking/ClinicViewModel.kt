package com.example.appointmentschedulingapp.ui.features.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.domain.model.Clinic
import com.example.appointmentschedulingapp.domain.usecase.clinicUsecase.GetClinicsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import java.text.Normalizer

@HiltViewModel
class ClinicViewModel @Inject constructor(
    private val getClinicsUseCase: GetClinicsUseCase,
) : ViewModel() {

    private val _clinics = MutableStateFlow<List<Clinic>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCity = MutableStateFlow("Tất cả")
    val selectedCity = _selectedCity.asStateFlow()


    private val _selectedDistrict = MutableStateFlow<String?>(null)
    val selectedDistrict = _selectedDistrict.asStateFlow()

    init {
        loadClinics()
    }

    val availableCities: StateFlow<List<String>> = _clinics
        .map { list -> listOf("Tất cả") + list.map { it.city }.filter { it.isNotBlank() }.distinct().sorted() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("Tất cả"))

    // Filtered clinics dựa theo search + city
    val filteredClinics: StateFlow<List<Clinic>> = combine(_clinics, _searchQuery, _selectedCity) { list, query, city ->
        list.filter { clinic ->
            val normalizedQuery = query.removeVietnameseAccents().lowercase()
            val normalizedName = clinic.name.removeVietnameseAccents().lowercase()

            val matchesName =
                normalizedQuery.isBlank() || normalizedName.contains(normalizedQuery)
            val matchesCity = city == "Tất cả" || clinic.city == city
            matchesName && matchesCity
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    private fun loadClinics() {
        viewModelScope.launch {
            _clinics.value = getClinicsUseCase()
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCitySelected(city: String) {
        _selectedCity.value = city
    }

    fun String.removeVietnameseAccents(): String {
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        return temp.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    }

}