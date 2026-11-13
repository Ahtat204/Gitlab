package com.ahtat204.gitlab.reponses.json

val mockedMrs = """
    {
      "data": {
        "project": {
        "id":"gid://gitlab/Project/123",
          "__typename": "Project",
          "mergeRequests": {
            "__typename": "MergeRequestConnection",
            "nodes": [
              {
                "__typename": "MergeRequest",
                "id": "gid://gitlab/MergeRequest/12345",
                "name": "Features/implement-auth-flow",
                "author": {
                  "__typename": "User",
                  "name": "Jane Doe"
                },
                "iid": "42",
                "conflicts": false,
                "pipelines": {
                  "__typename": "PipelineConnection",
                  "edges": [
                    {
                      "__typename": "PipelineEdge",
                      "node": {
                        "__typename": "Pipeline",
                        "status": "SUCCESS",
                        "id": "gid://gitlab/Ci::Pipeline/98765"
                      }
                    }
                  ]
                },
                "createdAt": "2026-10-04T21:40:00Z",
                "state": "opened",
                "sourceBranch": "features/implement-auth-flow",
                "updatedAt": "2026-10-04T21:45:00Z",
                "targetBranch": "main",
                "labels": {
                  "__typename": "LabelConnection",
                  "nodes": [
                    {
                      "__typename": "Label",
                      "id": "gid://gitlab/Label/789",
                      "title": "bugfix",
                      "color": "#FF0000"
                    }
                  ]
                }
              }
            ],
            "pageInfo": {
              "__typename": "PageInfo",
              "startCursor": "eyJpZCI6IjEyMzQ1In0",
              "hasNextPage": false,
              "endCursor": "eyJpZCI6IjEyMzQ1In0",
              "hasPreviousPage": false
            }
          }
        }
      }
    }

""".trimIndent()