package com.ahtat204.gitlab.reponses.json

val mockContributedProjects = """
    {"data":{
  "currentUser": {
    "__typename": "CurrentUser",
    "contributedProjects": {
      "__typename": "ContributedProjects",
      "pageInfo": {
        "__typename": "PageInfo",
        "startCursor": "eyJjdXJzb3IiOiIxIn0=",
        "hasNextPage": true,
        "hasPreviousPage": false,
        "endCursor": "eyJjdXJzb3IiOiIxMCJ9"
      },
      "nodes": [
        {
          "__typename": "Project",
          "avatarUrl": "https://example.com",
          "id": "gid://gitlab/Project/1",
          "topics": [
            "kotlin",
            "graphql",
            "android"
          ],
          "lastActivityAt": "2026-10-24T20:05:00Z",
          "languages": [
            {
              "__typename": "Language",
              "color": "#A97BFF",
              "name": "Kotlin"
            },
            {
              "__typename": "Language",
              "color": "#E34C26",
              "name": "HTML"
            }
          ],
          "name": "mock-api-generator",
          "fullPath": "developer/mock-api-generator",
          "description": "A tool to generate mock data structures.",
          "visibility": "public",
          "pipelines": {
            "__typename": "PipelineConnection",
            "nodes": [
              {
                "__typename": "Pipeline",
                "id": "gid://gitlab/Pipeline/42",
                "status": "SUCCESS"
              }
            ]
          }
        }
      ]
    }
  }
}
}
""".trimIndent()