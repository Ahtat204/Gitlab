package com.ahtat204.gitlab.reponses.objects

import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery.Commits
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery.Data
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery.Node
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery.PageInfo
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery.Project
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery.Repository
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery.Signature
import com.ahtat204.gitlab.data.queries.type.VerificationStatus

val mockCommitData = Data(
    project = Project(
        __typename = "Project",
        id = "project-id-567",
        repository = Repository(
            __typename = "Repository",
            branchNames = listOf("main", "feature/auth", "bugfix/ui"),
            commits = Commits(
                __typename = "CommitConnection",
                nodes = listOf(
                    Node(
                        __typename = "Commit",
                        id = "gid://gitlab/Commit/sha123",
                        sha = "sha1234567890abcdef1234567890abcdef1234",
                        name = "feat: implement repository history tracking",
                        authorName = "Jane Doe",
                        committedDate = "2026-08-23T21:40:00Z", // Passed as String for 'Any?'
                        signature = Signature(
                            __typename = "GpgSignature",
                            verificationStatus = VerificationStatus.VERIFIED // Assuming enum variant
                        )
                    ),
                    Node(
                        __typename = "Commit",
                        id = "gid://gitlab/Commit/sha456",
                        sha = "sha4567890abcdef1234567890abcdef12345678",
                        name = "fix: resolve edge case in pagination cursor",
                        authorName = "John Smith",
                        committedDate = "2026-08-23T18:15:00Z",
                        signature = Signature(
                            __typename = "GpgSignature",
                            verificationStatus = VerificationStatus.VERIFIED_SYSTEM // Assuming enum variant
                        )
                    )
                ),
                pageInfo = PageInfo(
                    __typename = "PageInfo",
                    endCursor = "eyJpZCI6ImNvbW1pdC00NTYifQ==",
                    hasNextPage = true,
                    startCursor = "eyJpZCI6ImNvbW1pdC0xMjMifQ=="
                )
            )
        )
    )
)
