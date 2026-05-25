package com.ahtat204.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.ahtat204.gitlab.data.repositories.profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) :
    ViewModel() {
    public fun loadUserProfile() {

    }
}