package com.ahtat204.gitlab.reponses.objects

import com.ahtat204.gitlab.data.queries.GetProjectIssuesQuery.Assignees
import com.ahtat204.gitlab.data.queries.GetProjectIssuesQuery.Data
import com.ahtat204.gitlab.data.queries.GetProjectIssuesQuery.Issues
import com.ahtat204.gitlab.data.queries.GetProjectIssuesQuery.Node
import com.ahtat204.gitlab.data.queries.GetProjectIssuesQuery.Node1
import com.ahtat204.gitlab.data.queries.GetProjectIssuesQuery.PageInfo
import com.ahtat204.gitlab.data.queries.GetProjectIssuesQuery.Project
import com.ahtat204.gitlab.data.queries.type.IssueState

val mockedIssues = Data(
    project = Project(
        __typename = "Project",
        issues = Issues(
            __typename = "IssuesConnection",
            nodes = listOf(
                Node(
                    __typename = "Issue",
                    id = "issue_001",
                    name = "bug-fix-auth",
                    title = "Fix authentication token expiration bypass",
                    state = IssueState.opened, // Assumed enum value
                    createdAt = "2026-09-26T16:26:00Z", // Passed as String since type is Any
                    assignees = Assignees(
                        __typename = "AssigneeConnection",
                        nodes = listOf(
                            Node1(__typename = "User", name = "Alex Smith"),
                            Node1(__typename = "User", name = "Jordan Doe")
                        )
                    )
                ),
                Node(
                    __typename = "Issue",
                    id = "issue_002",
                    name = "feature-dark-mode",
                    title = "Implement dark mode theme across dashboard",
                    state = IssueState.closed,
                    createdAt = "2026-09-25T10:00:00Z",
                    assignees = Assignees(
                        __typename = "AssigneeConnection",
                        nodes = listOf(
                            Node1(__typename = "User", name = "Taylor Swift")
                        )
                    )
                )
            ),
            pageInfo = PageInfo(
                __typename = "PageInfo",
                hasNextPage = true,
                endCursor = "eyJjdXJzb3IiOiJpc3N1ZV8wMDIifQ==",
                startCursor = "eyJjdXJzb3IiOiJpc3N1ZV8wMDEifQ=="
            )
        )
    )
)