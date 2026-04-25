package com.asue24.gitlab.data.repositories.project

import com.asue24.gitlab.GetMyProjectsQuery

interface ProjectRepository {
    fun getAllProjects():List<GetMyProjectsQuery.Project>
    fun getProjectById(id:String):GetMyProjectsQuery.Project
}