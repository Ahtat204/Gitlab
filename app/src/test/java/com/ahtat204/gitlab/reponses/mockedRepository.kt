package com.ahtat204.gitlab.reponses

val mockedRepository = """
        {
          "data": {
            "project": {
              "__typename": "Project",
              "id": "gid://gitlab/Project/12345",
              "name": "awesome-android-app",
              "repository": {
                "__typename": "Repository",
                "rootRef": "main",
                "tree": {
                  "__typename": "Tree",
                  "lastCommit": {
                    "__typename": "Commit",
                    "id": "gid://gitlab/Commit/abc123def456",
                    "message": "feat: implement repository layer with caching",
                    "committedDate": "2023-10-27T10:00:00Z",
                    "author": {
                      "__typename": "Author",
                      "name": "Lahcen AHTAT"
                    }
                  },
                  "trees": {
                    "__typename": "TreeConnection",
                    "nodes": [
                      {
                        "__typename": "TreeEntry",
                        "id": "gid://gitlab/Tree/789",
                        "name": "src",
                        "path": "src"
                      },
                      {
                        "__typename": "TreeEntry",
                        "id": "gid://gitlab/Tree/012",
                        "name": "gradle",
                        "path": "gradle"
                      }
                    ],
                    "pageInfo": {
                      "__typename": "PageInfo",
                      "startCursor": "eyJpZCI6Ijc4OSJ9"
                    },
                    "edges": [
                      {
                        "__typename": "TreeEdge",
                        "cursor": "eyJpZCI6Ijc4OSJ9"
                      },
                      {
                        "__typename": "TreeEdge",
                        "cursor": "eyJpZCI6IjAxMiJ9"
                      }
                    ]
                  },
                  "blobs": {
                    "__typename": "BlobConnection",
                    "nodes": [
                      {
                        "__typename": "BlobEntry",
                        "id": "gid://gitlab/Blob/456",
                        "name": "README.md",
                        "path": "README.md"
                      },
                      {
                        "__typename": "BlobEntry",
                        "id": "gid://gitlab/Blob/789",
                        "name": "build.gradle.kts",
                        "path": "build.gradle.kts"
                      }
                    ],
                    "pageInfo": {
                      "__typename": "PageInfo",
                      "startCursor": "eyJpZCI6IjQ1NiJ9"
                    },
                    "edges": [
                      {
                        "__typename": "BlobEdge",
                        "cursor": "eyJpZCI6IjQ1NiJ9"
                      },
                      {
                        "__typename": "BlobEdge",
                        "cursor": "eyJpZCI6Ijc4OSJ9"
                      }
                    ]
                  }
                }
              }
            }
          }
        }
    """.trimIndent()