package com.ahtat204.gitlab.reponses.json

val mockedAllProjects = """
    {
    "data":{
      "currentUser": {
        "__typename": "CurrentUser",
        "projectMemberships": {
          "__typename": "ProjectMembershipConnection",
          "pageInfo": {
            "__typename": "PageInfo",
            "hasPreviousPage": false,
            "hasNextPage": true,
            "endCursor": "eyJpZCI6IjEwMSJ9"
          },
          "nodes": [
            {
              "__typename": "ProjectMembership",
              "id": "gid://gitlab/ProjectMember/1",
              "createdAt": "2023-10-27T10:00:00Z",
              "project": {
                "__typename": "Project",
                "id": "gid://gitlab/Project/101",
                "fullPath": "android-dev/gitlab-client",
                "name": "GitLab Client",
                "description": "A native Android client for GitLab built with Compose.",
                "avatarUrl": "https://gitlab.com/uploads/project/avatar/101/logo.png",
                "visibility": "public",
                "topics": [
                  "android",
                  "kotlin",
                  "graphql",
                  "apollo"
                ],
                "languages": [
                  {
                    "__typename": "Language",
                    "name": "Kotlin",
                    "color": "#F18E33"
                  },
                  {
                    "__typename": "Language",
                    "name": "Java",
                    "color": "#b07219"
                  }
                ],
                "pipelines": {
                  "__typename": "PipelineConnection",
                  "nodes": [
                    {
                      "__typename": "Pipeline",
                      "id": "gid://gitlab/Ci::Pipeline/5001",
                      "status": "SUCCESS"
                    },
                    {
                      "__typename": "Pipeline",
                      "id": "gid://gitlab/Ci::Pipeline/5002",
                      "status": "RUNNING"
                    }
                  ]
                }
              }
            }
          ]
        }
       }
      }
    }
""".trimIndent()