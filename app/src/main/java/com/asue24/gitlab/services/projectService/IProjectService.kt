package com.asue24.gitlab.services.projectService

import com.asue24.gitlab.GetMyProjectsQuery

interface IProjectService {
    public  fun GetAllProjects(): List<GetMyProjectsQuery.Node?>?
}