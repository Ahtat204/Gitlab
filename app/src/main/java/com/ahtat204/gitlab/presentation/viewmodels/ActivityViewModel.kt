package com.ahtat204.gitlab.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.remote.retrofit.Events
import com.ahtat204.gitlab.data.repositories.stats.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Callback
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(private val gitlab: RetrofitClient): ViewModel() {
    /** Backing state for [Events]. */
    private val _events = MutableStateFlow<Events?>(null)

    /** Public immutable flow of [Events] */
    val events: StateFlow<Events?> get() = _events.asStateFlow()

    fun loadEvents(){
        viewModelScope.launch {
            try {
                val result=gitlab.getEvents()
                if(result.isSuccessful){
                    result.body()?.let { body->
                        _events.value=body
                        Log.d("reponseEvents",body.toString())
                    }

                }
                else{
                    Log.e("RetrofitError",result.errorBody().toString())
                }
            }
            catch (e: Throwable){
                Log.e("ActivityViewModelException","the error is :${e.message} caused By ${e.cause}.See Full Stack Trace:\n ${e.stackTrace}")
                throw e
            }

        }
    }

}