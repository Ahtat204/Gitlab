package com.ahtat204.gitlab.reponses.objects

import com.ahtat204.gitlab.data.queries.GetProjectMembersQuery.Data
import com.ahtat204.gitlab.data.queries.GetProjectMembersQuery.Node
import com.ahtat204.gitlab.data.queries.GetProjectMembersQuery.PageInfo
import com.ahtat204.gitlab.data.queries.GetProjectMembersQuery.Project
import com.ahtat204.gitlab.data.queries.GetProjectMembersQuery.ProjectMembers
import com.ahtat204.gitlab.data.queries.GetProjectMembersQuery.User

val mockedMembers = Data(
    project = Project(
        __typename = "Project",
        id = "gid://gitlab/Project/101",
        projectMembers = ProjectMembers(
            __typename = "ProjectMemberConnection",
            nodes = listOf(
                Node(
                    __typename = "ProjectMember",
                    id = "gid://gitlab/ProjectMember/1",
                    createdAt = "2023-10-27T10:00:00Z",
                    user = User(
                        __typename = "User",
                        name = "Lahcen AHTAT",
                        username = "Ahtat204",
                        bio = "Android Engineer & Systems Architecture enthusiast."
                    )
                ),
                Node(
                    __typename = "ProjectMember",
                    id = "gid://gitlab/ProjectMember/2",
                    createdAt = "2023-11-01T08:15:22Z",
                    user = User(
                        __typename = "User",
                        name = "GitLab Bot",
                        username = "project_101_bot",
                        bio = "ML Engineer & C++ enthusiast."
                    )
                )
            ),
            pageInfo = PageInfo(
                __typename = "PageInfo",
                endCursor = "eyJpZCI6IjIifQ",
                startCursor = "eyJpZCI6IjEifQ",
                hasPreviousPage = false,
                hasNextPage = true
            )
        )
    )
)