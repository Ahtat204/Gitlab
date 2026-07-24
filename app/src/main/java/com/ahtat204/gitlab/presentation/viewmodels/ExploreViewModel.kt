package com.ahtat204.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(private val userRepository: ProjectRepository) :
    ViewModel() {
        

    }