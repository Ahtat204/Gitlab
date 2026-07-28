package com.ahtat204.gitlab.reponses

val mockedCommits = """
    {
      "data": {
        "project": {
         "id": "gid://gitlab/Project/1",
          "__typename": "Project",
          "repository": {
            "__typename": "Repository",
            "branchNames": [
              "main",
              "develop"
            ],
            "commits": {
              "__typename": "CommitConnection",
              "nodes": [
                {
                  "__typename": "Commit",
                  "id": "gid://gitlab/Commit/a1b2c3d4",
                  "sha": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0",
                  "name": "feat: implement repository history view",
                  "authorName": "Lahcen AHTAT",
                  "committedDate": "2023-10-27T14:30:00Z",
                  "signature": {
                    "__typename": "GpgSignature",
                    "verificationStatus": "VERIFIED"
                  }
                }
              ],
              "pageInfo": {
                "__typename": "PageInfo",
                "endCursor": "eyJpZCI6ImExYjJjM2Q0In0",
                "hasNextPage": true,
                "startCursor": "eyJpZCI6ImExYjJjM2Q0In0"
              }
            }
          }
        }
      }
    }
""".trimIndent()