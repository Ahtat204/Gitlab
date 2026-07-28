package com.ahtat204.gitlab

open class Responses {
    val mockedProject = """
          {"data":
          {
              "project": {
                "__typename": "Project",
                "pipelineCounts": {
                  "__typename": "PipelineCounts",
                  "pending": 0,
                  "running": 1
                },
                "namespace": {
                  "__typename": "Namespace",
                  "path": "gitlab-org"
                },
                "openIssuesCount": 42,
                "fullPath": "gitlab-org/gitlab",
                "openMergeRequestsCount": 15,
                "forksCount": 5000,
                "starCount": 12000,
                "id": "gid://gitlab/Project/123",
                "name": "gitlab",
                "description": "The GitLab community edition codebase."
              }
            }
           }
        """.trimIndent()
    val mockedProjects = """
        {"data":{
          "currentUser": {
            "__typename": "User",
            "avatarUrl": "https://gitlab.com/uploads/-/system/user/avatar/123/avatar.png",
            "projectMemberships": {
              "__typename": "ProjectMemberships",
              "nodes": [
                {
                  "__typename": "ProjectMember",
                  "id": "gid://gitlab/ProjectMember/1",
                  "project": {
                    "__typename": "Project",
                    "topics": ["kotlin", "graphql", "android"],
                    "lastActivityAt": "2026-06-20T10:00:00Z",
                    "languages": [
                      {
                        "__typename": "Language",
                        "color": "#A97BFF",
                        "name": "Kotlin"
                      }
                    ],
                    "name": "GitLab-Client",
                    "fullPath": "username/gitlab-client",
                    "description": "A powerful GraphQL client for GitLab.",
                    "visibility": "public",
                    "pipelines": {
                      "__typename": "Pipelines",
                      "nodes": [
                        {
                          "__typename": "Pipeline",
                          "id": "gid://gitlab/Pipeline/999",
                          "status": "SUCCESS"
                        }
                      ]
                    }
                  }
                }
              ],
              "pageInfo": {
                "__typename": "PageInfo",
                "hasNextPage": false,
                "endCursor": "eyJpZCI6IjEiLCJfY291bnQiOjF9"
              }
            }
          }
        }
       }
    """.trimIndent()
}