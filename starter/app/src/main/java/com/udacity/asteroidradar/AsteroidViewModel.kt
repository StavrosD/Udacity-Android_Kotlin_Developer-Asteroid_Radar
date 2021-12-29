package com.udacity.asteroidradar

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class AsteroidViewModel(application: Application) : AndroidViewModel(application){

    private val database = getDatabase(application)

    private val asteroidsRepository = AsteroidsRepository(database)
    val asteroids = asteroidsRepository.asteroids
    val imageOfTheDay = asteroidsRepository.imageOfTheDay

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetails
        get() = _navigateToAsteroidDetails

    private var _gettingImageURL : Boolean = false
    private var _gettingAsteroids : Boolean = false
    val showProgressBar = MutableLiveData<Boolean>().apply { postValue(false) }


    fun onAsteroidClicked(id:Asteroid){
        _navigateToAsteroidDetails.value = id
    }

    fun onAsteroidDetailsNavigated(){
        _navigateToAsteroidDetails.value = null
    }

    init {
        refreshAsteroids()
        refreshImageOfTheDay()
    }

    fun refreshAsteroids(){
        viewModelScope.launch {
            _gettingAsteroids = true
            showProgressBar.value = _gettingAsteroids || _gettingImageURL
            asteroidsRepository.refreshAsteroids()
            _gettingAsteroids = false
            showProgressBar.value = _gettingAsteroids || _gettingImageURL
        }
    }
    fun refreshImageOfTheDay(){
        viewModelScope.launch {
            _gettingImageURL = true
            showProgressBar.value = _gettingAsteroids || _gettingImageURL
            asteroidsRepository.refreshImageOfTheDay()
            _gettingImageURL = false
            showProgressBar.value = _gettingAsteroids || _gettingImageURL
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AsteroidViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AsteroidViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")        }
    }
}