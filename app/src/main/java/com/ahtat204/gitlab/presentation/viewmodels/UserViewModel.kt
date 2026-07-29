package com.ahtat204.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.ahtat204.gitlab.data.remote.repositories.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {}