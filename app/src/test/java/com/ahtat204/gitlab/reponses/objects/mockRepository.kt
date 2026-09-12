package com.ahtat204.gitlab.reponses.objects

import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Author
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Blobs
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Data
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Edge
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Edge1
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.LastCommit
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Node
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Node1
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.PageInfo
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.PageInfo1
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Project
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Repository
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Tree
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Trees

val mockRepository = Data(
    project = Project(
        __typename = "Project",
        id = "project-id-123",
        name = "awesome-api-service",
        repository = Repository(
            __typename = "Repository",
            rootRef = "main",
            tree = Tree(
                __typename = "Tree",
                lastCommit = LastCommit(
                    __typename = "Commit",
                    message = "feat: initial commit with api setup",
                    id = "commit-sha-abc123xff",
                    committedDate = "2026-08-23T20:15:00Z", // Passed as String to match 'Any?'
                    author = Author(
                        __typename = "User",
                        name = "John Doe"
                    )
                ),
                trees = Trees(
                    __typename = "TreeConnection",
                    nodes = listOf(
                        Node(
                            __typename = "TreeEntry",
                            id = "node-tree-1",
                            name = "src",
                            path = "src"
                        )
                    ),
                    pageInfo = PageInfo(
                        __typename = "PageInfo",
                        startCursor = "tree-cursor-start"
                    ),
                    edges = listOf(
                        Edge(
                            __typename = "TreeEdge",
                            cursor = "tree-edge-1"
                        )
                    )
                ),
                blobs = Blobs(
                    __typename = "BlobConnection",
                    nodes = listOf(
                        Node1(
                            __typename = "BlobEntry",
                            id = "node-blob-1",
                            name = "README.md",
                            path = "README.md"
                        )
                    ),
                    pageInfo = PageInfo1(
                        __typename = "PageInfo",
                        startCursor = "blob-cursor-start"
                    ),
                    edges = listOf(
                        Edge1(
                            __typename = "BlobEdge",
                            cursor = "blob-edge-1"
                        )
                    )
                )
            )
        )
    )
)
