package com.ahtat204.gitlab.reponses.objects

import com.ahtat204.gitlab.data.queries.GetRepositoryBranchesQuery.Data
import com.ahtat204.gitlab.data.queries.GetRepositoryBranchesQuery.Project
import com.ahtat204.gitlab.data.queries.GetRepositoryBranchesQuery.Repository

val mockBranchData = Data(
    project = Project(
        __typename = "Project",
        id = "project-id-789",
        repository = Repository(
            __typename = "Repository",
            branchNames = listOf("main", "develop", "release/v1.0")
        )
    )
)
