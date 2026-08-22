package com.ahtat204.gitlab.reponses.objects

import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery.CurrentUser
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery.Data
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery.Language
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery.Namespace
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery.Node
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery.Node1
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery.PageInfo
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery.Pipelines
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery.Projects
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum

val mockDataList: Data =
    Data(
        currentUser = CurrentUser(
            __typename = "User",
            id = "user-101",
            avatarUrl = "https://example.com",
            namespace = Namespace(
                __typename = "Namespace",
                projects = Projects(
                    __typename = "ProjectConnection",
                    pageInfo = PageInfo(
                        __typename = "PageInfo",
                        startCursor = "eyJjdXJzb3IiOiIxIn0=",
                        hasNextPage = true,
                        hasPreviousPage = false
                    ),
                    nodes = listOf(
                        Node(
                            __typename = "Project",
                            id = "project-abc",
                            name = "Mobile App Backend",
                            fullPath = "engineering/mobile-backend",
                            description = "Kotlin and Ktor microservice for the core banking application.",
                            visibility = "private",
                            topics = listOf("kotlin", "ktor", "microservices", "backend"),
                            lastActivityAt = "2026-08-22T14:30:00Z",
                            languages = listOf(
                                Language(
                                    __typename = "Language",
                                    name = "Kotlin",
                                    color = "#A97BFF"
                                ),
                                Language(
                                    __typename = "Language",
                                    name = "Docker",
                                    color = "#384d54"
                                )
                            ),
                            pipelines = Pipelines(
                                __typename = "PipelineConnection",
                                nodes = listOf(
                                    Node1(
                                        __typename = "Pipeline",
                                        id = "pipe-990",
                                        status = PipelineStatusEnum.SUCCESS
                                    ),
                                    Node1(
                                        __typename = "Pipeline",
                                        id = "pipe-991",
                                        status = PipelineStatusEnum.RUNNING
                                    )
                                )
                            )
                        ),
                        Node(
                            __typename = "Project",
                            id = "project-xyz",
                            name = "Documentation Portal",
                            fullPath = "engineering/docs-portal",
                            description = "Public developer onboarding guides and API specification pages.",
                            visibility = "public",
                            topics = listOf("documentation", "markdown", "hugo"),
                            lastActivityAt = "2026-08-21T09:15:00Z",
                            languages = listOf(
                                Language(
                                    __typename = "Language",
                                    name = "Markdown",
                                    color = "#083fae"
                                ),
                                Language(__typename = "Language", name = "Go", color = "#00ADD8")
                            ),
                            pipelines = Pipelines(
                                __typename = "PipelineConnection",
                                nodes = listOf(
                                    Node1(
                                        __typename = "Pipeline",
                                        id = "pipe-880",
                                        status = PipelineStatusEnum.SUCCESS
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    )

val nullProjects = Data(
    currentUser = CurrentUser(
        __typename = "User",
        id = "user-102",
        avatarUrl = null, // Testing nullable state
        namespace = Namespace(
            __typename = "Namespace",
            projects = Projects(
                __typename = "ProjectConnection",
                pageInfo = PageInfo(
                    __typename = "PageInfo",
                    startCursor = null,
                    hasNextPage = false,
                    hasPreviousPage = false
                ),
                nodes = emptyList() // Testing empty repository list state
            )
        )
    )
)
