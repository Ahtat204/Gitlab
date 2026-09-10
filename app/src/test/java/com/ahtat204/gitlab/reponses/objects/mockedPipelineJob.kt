package com.ahtat204.gitlab.reponses.objects

import com.ahtat204.gitlab.data.queries.GetPipelineJobQuery.Data
import com.ahtat204.gitlab.data.queries.GetPipelineJobQuery.Job
import com.ahtat204.gitlab.data.queries.GetPipelineJobQuery.Project


val mockedPipelineJob = Data(
    project = Project(
        __typename = "Project",
        id = "gid://gitlab/Project/12345",
        job = Job(
            __typename = "CiJob",
            id = "gid://gitlab/Ci::Build/987654",
            duration = 345,
            createdAt = "2023-10-27T10:00:00Z", // Represented as Any, usually a String or Custom Scalar
            name = "build-android-debug"
        )
    )
)