package com.udacity.asteroidradar

import android.app.Application
import android.opengl.Visibility
import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import timber.log.Timber

class AsteroidViewModel(application: Application) : AndroidViewModel(application){

    private val database = getDatabase(application)

    private val asteroidsRepository = AsteroidsRepository(database)
    val asteroids = asteroidsRepository.asteroids

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
       refresh()
    }

    fun refresh(){
        viewModelScope.launch {
            _gettingAsteroids = true
            showProgressBar.value = _gettingAsteroids && _gettingImageURL
            Timber.e("show progress bar")
            asteroidsRepository.refreshAsteroids()
            _gettingAsteroids = false
            _gettingAsteroids = true
            showProgressBar.value = _gettingAsteroids && _gettingImageURL
            Timber.e("hide progress bar")

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