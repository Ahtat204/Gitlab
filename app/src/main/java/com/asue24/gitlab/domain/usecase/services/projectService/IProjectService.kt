package com.asue24.gitlab.domain.usecase.services.projectService

import com.asue24.gitlab.GetMyProjectsQuery

interface IProjectService {
    fun GetAllProjects(): List<GetMyProjectsQuery.Node?>?
}