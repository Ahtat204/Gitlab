package com.ahtat204.gitlab.reponses.objects

import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery.Author
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery.Data
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery.Edge
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery.Labels
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery.MergeRequests
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery.Node
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery.Node1
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery.Node2
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery.PageInfo
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery.Pipelines
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery.Project
import com.ahtat204.gitlab.data.queries.type.MergeRequestState
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum


val mockedMRs = Data(
    project = Project(
        id = "gid://gitlab/Project/123",
        __typename = "Project",
        mergeRequests = MergeRequests(
            __typename = "MergeRequestConnection",
            nodes = listOf(
                Node(
                    __typename = "MergeRequest",
                    id = "gid://gitlab/MergeRequest/12345",
                    name = "Features/implement-auth-flow",
                    author = Author(
                        __typename = "User",
                        name = "Jane Doe"
                    ),
                    iid = "42",
                    conflicts = false,
                    pipelines = Pipelines(
                        __typename = "PipelineConnection",
                        edges = listOf(
                            Edge(
                                __typename = "PipelineEdge",
                                node = Node1(
                                    __typename = "Pipeline",
                                    status = PipelineStatusEnum.SUCCESS,
                                    id = "gid://gitlab/Ci::Pipeline/98765"
                                )
                            )
                        )
                    ),
                    createdAt = "2026-10-04T21:40:00Z", state = MergeRequestState.opened,
                    sourceBranch = "features/implement-auth-flow",
                    updatedAt = "2026-10-04T21:45:00Z",
                    targetBranch = "main",
                    labels = Labels(
                        __typename = "LabelConnection",
                        nodes = listOf(
                            Node2(
                                __typename = "Label",
                                id = "gid://gitlab/Label/789",
                                title = "bugfix",
                                color = "#FF0000"
                            )
                        )
                    )
                )
            ),
            pageInfo = PageInfo(
                __typename = "PageInfo",
                startCursor = "eyJpZCI6IjEyMzQ1In0",
                hasNextPage = false,
                endCursor = "eyJpZCI6IjEyMzQ1In0",
                hasPreviousPage = false
            )
        )
    )
)