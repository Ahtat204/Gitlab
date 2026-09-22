package com.ahtat204.gitlab.reponses.objects

import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery.Commit
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery.Data
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery.MergeRequest
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery.Node
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery.PageInfo
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery.Pipelines
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery.Project
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery.User
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum

val mockProjectPipelinesData = Data(
    project = Project(
        __typename = "Project",
        id = "project-id-123",
        pipelines = Pipelines(
            __typename = "PipelineConnection",
            nodes = listOf(
                Node(
                    __typename = "Pipeline",
                    id = "pipeline-id-001",
                    status = PipelineStatusEnum.SUCCESS,
                    type = "CI_BRANCH",
                    duration = 320,
                    finishedAt = "2026-08-25T20:00:00Z", // Can be a String or custom DateTime scalar type
                    ref = "main",
                    mergeRequest = MergeRequest(
                        __typename = "MergeRequest",
                        name = "implementing Kafka Consumer"
                    ),
                    commit = Commit(
                        __typename = "Commit",
                        name = "feat: implement user authentication"
                    ),
                    user = User(
                        __typename = "User",
                        name = "Alex Developer"
                    )
                ),
                Node(
                    __typename = "Pipeline",
                    id = "pipeline-id-002",
                    status = PipelineStatusEnum.FAILED,
                    type = "CI_MERGE_REQUEST",
                    duration = 45,
                    finishedAt = "2026-08-25T21:15:00Z",
                    ref = "feature/api-fix",
                    mergeRequest = MergeRequest(
                        __typename = "MergeRequest",
                        name = "Resolve API payload serialization bug"
                    ),
                    commit = Commit(
                        __typename = "Commit",
                        name = "fix: update payload constraints"
                    ),
                    user = User(
                        __typename = "User",
                        name = "****" // Example of masked bot user name
                    )
                )
            ),
            pageInfo = PageInfo(
                __typename = "PageInfo",
                hasNextPage = true,
                startCursor = "cursor-start-abc",
                endCursor = "cursor-end-xyz",
                hasPreviousPage = false
            )
        )
    )
)
