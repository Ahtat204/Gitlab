package com.ahtat204.gitlab.reponses.objects


import com.ahtat204.gitlab.data.queries.GetProjectPipelineQuery.Data
import com.ahtat204.gitlab.data.queries.GetProjectPipelineQuery.Jobs
import com.ahtat204.gitlab.data.queries.GetProjectPipelineQuery.Node
import com.ahtat204.gitlab.data.queries.GetProjectPipelineQuery.PageInfo
import com.ahtat204.gitlab.data.queries.GetProjectPipelineQuery.Pipeline
import com.ahtat204.gitlab.data.queries.GetProjectPipelineQuery.Project
import com.ahtat204.gitlab.data.queries.type.CiJobStatus

val mockedPipeline = Data(
    project = Project(
        __typename = "Project",
        id = "gid://gitlab/Project/1",
        pipeline = Pipeline(
            __typename = "Pipeline",
            id = "gid://gitlab/Ci::Pipeline/8812345",
            name = "Production Deploy",
            type = "CI_PIPE",
            computeMinutes = 12.5,
            jobs = Jobs(
                __typename = "JobConnection",
                nodes = listOf(
                    Node(
                        __typename = "CiJob",
                        id = "gid://gitlab/Ci::Build/123456",
                        createdAt = "2023-11-01T14:30:00Z",
                        status = CiJobStatus.SUCCESS,
                        duration = 180
                    ),
                    Node(
                        __typename = "CiJob",
                        id = "gid://gitlab/Ci::Build/123457",
                        createdAt = "2023-11-01T14:35:00Z",
                        status = CiJobStatus.FAILED,
                        duration = 45
                    )
                ),
                pageInfo = PageInfo(
                    __typename = "PageInfo",
                    endCursor = "eyJpZCI6IjEyMzQ1NyJ9",
                    startCursor = "eyJpZCI6IjEyMzQ1NiJ9",
                    hasNextPage = false
                )
            )
        )
    )
)