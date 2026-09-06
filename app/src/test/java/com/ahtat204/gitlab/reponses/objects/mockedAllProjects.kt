package com.ahtat204.gitlab.reponses.objects


import com.ahtat204.gitlab.data.queries.GetAllProjectsQuery.CurrentUser
import com.ahtat204.gitlab.data.queries.GetAllProjectsQuery.Data
import com.ahtat204.gitlab.data.queries.GetAllProjectsQuery.Language
import com.ahtat204.gitlab.data.queries.GetAllProjectsQuery.Node
import com.ahtat204.gitlab.data.queries.GetAllProjectsQuery.Node1
import com.ahtat204.gitlab.data.queries.GetAllProjectsQuery.PageInfo
import com.ahtat204.gitlab.data.queries.GetAllProjectsQuery.Pipelines
import com.ahtat204.gitlab.data.queries.GetAllProjectsQuery.Project
import com.ahtat204.gitlab.data.queries.GetAllProjectsQuery.ProjectMemberships
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum


val mockedAllProjects = Data(
    currentUser = CurrentUser(
        __typename = "CurrentUser",
        projectMemberships = ProjectMemberships(
            __typename = "ProjectMembershipConnection",
            pageInfo = PageInfo(
                __typename = "PageInfo",
                hasPreviousPage = false,
                hasNextPage = true,
                endCursor = "eyJpZCI6IjEwMSJ9"
            ),
            nodes = listOf(
                Node(
                    __typename = "ProjectMembership",
                    id = "gid://gitlab/ProjectMember/1",
                    createdAt = "2023-10-27T10:00:00Z",
                    project = Project(
                        __typename = "Project",
                        id = "gid://gitlab/Project/101",
                        fullPath = "android-dev/gitlab-client",
                        name = "GitLab Client",
                        description = "A native Android client for GitLab built with Compose.",
                        avatarUrl = "https://gitlab.com/uploads/project/avatar/101/logo.png",
                        visibility = "public",
                        topics = listOf("android", "kotlin", "graphql", "apollo"),
                        languages = listOf(
                            Language(
                                __typename = "Language",
                                name = "Kotlin",
                                color = "#F18E33"
                            ),
                            Language(
                                __typename = "Language",
                                name = "Java",
                                color = "#b07219"
                            )
                        ),
                        pipelines = Pipelines(
                            __typename = "PipelineConnection",
                            nodes = listOf(
                                Node1(
                                    __typename = "Pipeline",
                                    id = "gid://gitlab/Ci::Pipeline/5001",
                                    status = PipelineStatusEnum.SUCCESS
                                ),
                                Node1(
                                    __typename = "Pipeline",
                                    id = "gid://gitlab/Ci::Pipeline/5002",
                                    status = PipelineStatusEnum.RUNNING
                                )
                            )
                        )
                    )
                )
            )
        )
    )
)