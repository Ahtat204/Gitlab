package com.asue24.gitlab.presentation.viewmodels

import com.asue24.gitlab.data.repositories.profile.ProfileRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) {}