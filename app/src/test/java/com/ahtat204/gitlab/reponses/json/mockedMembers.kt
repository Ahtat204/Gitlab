package com.ahtat204.gitlab.reponses.json

val mockedMembers = """
   { 
   "data":{
  "project": {
    "__typename": "Project",
    "id": "gid://gitlab/Project/101",
    "projectMembers": {
      "__typename": "ProjectMemberConnection",
      "nodes": [
        {
          "__typename": "ProjectMember",
          "id": "gid://gitlab/ProjectMember/1",
          "createdAt": "2023-10-27T10:00:00Z",
          "user": {
            "__typename": "User",
            "name": "Lahcen AHTAT",
            "username": "Ahtat204",
            "bio": "Android Engineer & Systems Architecture enthusiast."
          }
        },
        {
          "__typename": "ProjectMember",
          "id": "gid://gitlab/ProjectMember/2",
          "createdAt": "2023-11-01T08:15:22Z",
          "user": {
            "__typename": "User",
            "name": "GitLab Bot",
            "username": "project_101_bot",
            "bio": null
          }
        }
      ],
      "pageInfo": {
        "__typename": "PageInfo",
        "endCursor": "eyJpZCI6IjIifQ",
        "startCursor": "eyJpZCI6IjEifQ",
        "hasPreviousPage": false,
        "hasNextPage": true
      }
    }
  }
}
}
""".trimIndent()