package com.voitto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitto.data.entity.ResourceEntity
import com.voitto.data.repository.ResourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResourcesViewModel @Inject constructor(
    private val resourceRepository: ResourceRepository
) : ViewModel() {
    
    private val _selectedType = MutableStateFlow("kela")
    val selectedType: StateFlow<String> = _selectedType.asStateFlow()
    
    val resources: StateFlow<List<ResourceEntity>> = _selectedType.flatMapLatest { type ->
        resourceRepository.getResourcesByType(type)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()
    
    init {
        initializeResources()
    }
    
    private fun initializeResources() {
        viewModelScope.launch {
            if (!_isInitialized.value) {
                resourceRepository.seedInitialResources()
                _isInitialized.value = true
            }
        }
    }
    
    fun selectResourceType(type: String) {
        _selectedType.value = type
    }
    
    fun getResourceTypes(): List<Pair<String, String>> {
        return listOf(
            "kela" to "Kela",
            "ruoka_apu" to "Ruoka-apu",
            "velkaneuvonta" to "Velkaneuvonta",
            "tyo_koulutus" to "Työ & koulutus",
            "asuminen" to "Asuminen"
        )
    }
}
