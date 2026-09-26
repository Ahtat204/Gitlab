package com.ahtat204.gitlab.reponses.json

val mockedIssues = """
  {
   "data": {
      "project": {
        "__typename": "Project",
        "issues": {
          "__typename": "IssuesConnection",
          "nodes": [
            {
              "__typename": "Issue",
              "id": "issue_001",
              "name": "bug-fix-auth",
              "title": "Fix authentication token expiration bypass",
              "state": "OPEN",
              "createdAt": "2026-09-26T16:26:00Z",
              "assignees": {
                "__typename": "AssigneeConnection",
                "nodes": [
                  {
                    "__typename": "User",
                    "name": "Alex Smith"
                  },
                  {
                    "__typename": "User",
                    "name": "Jordan Doe"
                  }
                ]
              }
            },
            {
              "__typename": "Issue",
              "id": "issue_002",
              "name": "feature-dark-mode",
              "title": "Implement dark mode theme across dashboard",
              "state": "CLOSED",
              "createdAt": "2026-09-25T10:00:00Z",
              "assignees": {
                "__typename": "AssigneeConnection",
                "nodes": [
                  {
                    "__typename": "User",
                    "name": "Taylor Swift"
                  }
                ]
              }
            }
          ],
          "pageInfo": {
            "__typename": "PageInfo",
            "hasNextPage": true,
            "endCursor": "eyJjdXJzb3IiOiJpc3N1ZV8wMDIifQ==",
            "startCursor": "eyJjdXJzb3IiOiJpc3N1ZV8wMDEifQ=="
          }
        }
      }
    }
}
""".trimIndent()