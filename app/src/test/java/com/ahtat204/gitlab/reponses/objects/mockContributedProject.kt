package com.ahtat204.gitlab.reponses.objects

import com.ahtat204.gitlab.data.queries.GetMyContributedProjectsQuery.ContributedProjects
import com.ahtat204.gitlab.data.queries.GetMyContributedProjectsQuery.CurrentUser
import com.ahtat204.gitlab.data.queries.GetMyContributedProjectsQuery.Data
import com.ahtat204.gitlab.data.queries.GetMyContributedProjectsQuery.Language
import com.ahtat204.gitlab.data.queries.GetMyContributedProjectsQuery.Node
import com.ahtat204.gitlab.data.queries.GetMyContributedProjectsQuery.Node1
import com.ahtat204.gitlab.data.queries.GetMyContributedProjectsQuery.PageInfo
import com.ahtat204.gitlab.data.queries.GetMyContributedProjectsQuery.Pipelines
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum

val mockContributedProject = Data(
    currentUser = CurrentUser(
        __typename = "CurrentUser",
        contributedProjects = ContributedProjects(
            __typename = "ContributedProjects",
            pageInfo = PageInfo(
                __typename = "PageInfo",
                startCursor = "eyJjdXJzb3IiOiIxIn0=",
                hasNextPage = true,
                hasPreviousPage = false,
                endCursor = "eyJjdXJzb3IiOiIxMCJ9"
            ),
            nodes = listOf(
                Node(
                    __typename = "Project",
                    avatarUrl = "https://example.com",
                    id = "gid://gitlab/Project/1",
                    topics = listOf("kotlin", "graphql", "android"),
                    lastActivityAt = "2026-10-24T20:05:00Z",
                    languages = listOf(
                        Language(__typename = "Language", color = "#A97BFF", name = "Kotlin"),
                        Language(__typename = "Language", color = "#E34C26", name = "HTML")
                    ),
                    name = "mock-api-generator",
                    fullPath = "developer/mock-api-generator",
                    description = "A tool to generate mock data structures.",
                    visibility = "public",
                    pipelines = Pipelines(
                        __typename = "PipelineConnection",
                        nodes = listOf(
                            Node1(
                                __typename = "Pipeline",
                                id = "gid://gitlab/Pipeline/42",
                                status = PipelineStatusEnum.SUCCESS
                            )
                        )
                    )
                )
            )
        )
    )
)