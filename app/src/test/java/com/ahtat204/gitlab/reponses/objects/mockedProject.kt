package com.ahtat204.gitlab.reponses.objects

import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery

val mockProjectDetails = GetProjectDetailsQuery.Data(
    project = GetProjectDetailsQuery.Project(
        __typename = "Project",
        id = "gid://gitlab/Project/123",
        name = "gitlab-android-app",
        fullPath = "gitlab-org/gitlab-android-app",
        description = "A native GitLab client for Android.",
        starCount = 1024,
        forksCount = 256,
        openIssuesCount = 42,
        openMergeRequestsCount = 12,
        namespace = GetProjectDetailsQuery.Namespace(
            __typename = "Namespace", path = "gitlab-org"
        ),
        pipelineCounts = GetProjectDetailsQuery.PipelineCounts(
            __typename = "PipelineCounts", pending = 1, running = 3
        )
    )
)