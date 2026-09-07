package com.ahtat204.gitlab.reponses.json

val mockedPipeline = """
  {
    "data":{
  "project": {
    "__typename": "Project",
    "id":"gid://gitlab/Project/1",
    "pipeline": {
      "__typename": "Pipeline",
      "id": "gid://gitlab/Ci::Pipeline/8812345",
      "name": "Production Deploy",
      "type": "CI_PIPE",
      "computeMinutes": 12.5,
      "jobs": {
        "__typename": "JobConnection",
        "nodes": [
          {
            "__typename": "CiJob",
            "id": "gid://gitlab/Ci::Build/123456",
            "createdAt": "2023-11-01T14:30:00Z",
            "status": "SUCCESS",
            "duration": 180
          },
          {
            "__typename": "CiJob",
            "id": "gid://gitlab/Ci::Build/123457",
            "createdAt": "2023-11-01T14:35:00Z",
            "status": "FAILED",
            "duration": 45
          }
        ],
        "pageInfo": {
          "__typename": "PageInfo",
          "endCursor": "eyJpZCI6IjEyMzQ1NyJ9",
          "startCursor": "eyJpZCI6IjEyMzQ1NiJ9",
          "hasNextPage": false
      }
    }
   }
  }
 }
}
""".trimIndent()