package com.ahtat204.gitlab.reponses

import org.jetbrains.annotations.TestOnly
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

@TestOnly
fun assertNotNullAndEquals(actual: Any?, expected: Any) {
    assertNotNull(actual)
    assertEquals(actual, expected)
}

val mockedBranches = """
    {
      "data": {
        "project": {
          "__typename": "Project",
          "id": "gid://gitlab/Project/1",
          "repository": {
            "__typename": "Repository",
            "branchNames": [
              "main",
              "develop",
              "feature/authentication",
              "hotfix/issue-42",
              "release/v1.0.0"
            ]
          }
        }
      }
    }
""".trimIndent()