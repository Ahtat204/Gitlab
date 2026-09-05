package com.ahtat204.gitlab.reponses.json

val mockedPipelines = """
    {
      "data": {
        "project": {
          "__typename": "Project",
          "id": "project-id-123",
          "pipelines": {
            "__typename": "PipelineConnection",
            "nodes": [
              {
                "__typename": "Pipeline",
                "id": "pipeline-id-001",
                "status": "SUCCESS",
                "type": "CI_BRANCH",
                "duration": 320,
                "finishedAt": "2026-08-25T20:00:00Z",
                "ref": "main",
                "mergeRequest": {
                  "__typename": "MergeRequest",
                  "name": "implementing Kafka Consumer"
                },
                "commit": {
                  "__typename": "Commit",
                  "name": "feat: implement user authentication"
                },
                "user": {
                  "__typename": "User",
                  "name": "Alex Developer"
                }
              },
              {
                "__typename": "Pipeline",
                "id": "pipeline-id-002",
                "status": "FAILED",
                "type": "CI_MERGE_REQUEST",
                "duration": 45,
                "finishedAt": "2026-08-25T21:15:00Z",
                "ref": "feature/api-fix",
                "mergeRequest": {
                  "__typename": "MergeRequest",
                  "name": "Resolve API payload serialization bug"
                },
                "commit": {
                  "__typename": "Commit",
                  "name": "fix: update payload constraints"
                },
                "user": {
                  "__typename": "User",
                  "name": "****"
                }
              }
            ],
            "pageInfo": {
              "__typename": "PageInfo",
              "hasNextPage": true,
              "startCursor": "cursor-start-abc",
              "endCursor": "cursor-end-xyz",
              "hasPreviousPage": false
            }
          }
        }
      }
    }

""".trimIndent()