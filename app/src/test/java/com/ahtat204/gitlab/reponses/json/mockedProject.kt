package com.ahtat204.gitlab.reponses.json

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