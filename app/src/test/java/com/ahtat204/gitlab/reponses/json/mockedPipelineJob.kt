package com.ahtat204.gitlab.reponses.json

val mockedPipelineJob = """
    {"data":
    {
      "project": {
        "__typename": "Project",
        "id": "gid://gitlab/Project/12345",
        "job": {
          "__typename": "CiJob",
          "id": "gid://gitlab/Ci::Build/987654",
          "duration": 345,
          "createdAt": "2023-10-27T10:00:00Z",
          "name": "build-android-debug"
        }
      }
    }
    }
""".trimIndent()