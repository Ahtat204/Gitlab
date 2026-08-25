package com.ahtat204.gitlab.presentation.viewmodels.project.repository

import com.ahtat204.gitlab.presentation.viewmodels.TestBase
import com.ahtat204.gitlab.reponses.json.assertNotNullAndEquals
import com.ahtat204.gitlab.reponses.objects.mockBranchData
import com.ahtat204.gitlab.reponses.objects.mockCommitData
import com.ahtat204.gitlab.reponses.objects.mockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.whenever

@Suppress("DEPRECATION")
@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryViewModelTest : TestBase() {
    private lateinit var viewModel: RepositoryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RepositoryViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadProjectRepository() = runTest(testDispatcher) {
        val projectId = mockRepository.project!!.id
        whenever(repository.getProjectRepository(projectId, branch = null)).thenReturn(
            flowOf(
                mockRepository
            )
        )
        viewModel.loadProjectRepository(projectId)
        val repository = viewModel.repository.value
        Assert.assertNotNull(repository)
        val rootRef = repository?.rootRef
        assertNotNullAndEquals(rootRef, mockRepository.project.repository?.rootRef!!)
        val tree = repository?.tree
        Assert.assertNotNull(tree)
        assertNotNullAndEquals(
            tree?.lastCommit, mockRepository.project.repository.tree?.lastCommit!!
        )
        //blobs
        val blobs = tree?.blobs
        Assert.assertNotNull(blobs)
        val blobNodes = blobs?.nodes
        Assert.assertNotNull(blobNodes)
        for (i in 0 until blobNodes!!.size) {
            assertNotNullAndEquals(
                blobNodes[i]!!.id, mockRepository.project.repository.tree.blobs.nodes!![i]!!.id
            )
            assertNotNullAndEquals(
                blobNodes[i]!!.name, mockRepository.project.repository.tree.blobs.nodes[i]!!.name
            )
            assertNotNullAndEquals(
                blobNodes[i]!!.path, mockRepository.project.repository.tree.blobs.nodes[i]!!.path
            )
        }
        val blobPage = blobs.pageInfo
        assertNotNullAndEquals(
            blobPage.startCursor,
            mockRepository.project.repository.tree.blobs.pageInfo.startCursor!!
        )
        //trees
        val trees = tree.trees
        Assert.assertNotNull(trees)
        val treeNodes = trees.nodes
        Assert.assertNotNull(treeNodes)
        for (i in 0 until treeNodes!!.size) {
            assertNotNullAndEquals(
                treeNodes[i]!!.name,
                mockRepository.project.repository.tree.trees.nodes?.get(i)!!.name
            )
            assertNotNullAndEquals(
                treeNodes[i]!!.path, mockRepository.project.repository.tree.trees.nodes[i]!!.path
            )
            assertNotNullAndEquals(
                treeNodes[i]!!.id, mockRepository.project.repository.tree.trees.nodes[i]!!.id
            )
        }
        val page = trees.pageInfo
        assertNotNullAndEquals(
            page.startCursor, mockRepository.project.repository.tree.trees.pageInfo.startCursor!!
        )

    }

    @Test
    fun loadProjectCommits() = runTest(testDispatcher) {
        val projectId = mockCommitData.project!!.id
        whenever(
            repository.getProjectCommits(
                projectId, branch = "main", cursor = null
            )
        ).thenReturn(
            flowOf(
                mockCommitData
            )
        )
        viewModel.loadProjectCommits(projectId, "main")
        val commits = viewModel.commits.value
        val nodes = commits!!.nodes
        for (i in 0 until nodes!!.size) {
            assertNotNullAndEquals(
                nodes[i]!!.id,
                mockCommitData.project.repository!!.commits!!.nodes?.get(i)!!.id
            )
            assertNotNullAndEquals(
                nodes[i]!!.name,
                mockCommitData.project.repository.commits.nodes[i]!!.name!!
            )
            assertNotNullAndEquals(
                nodes[i]!!.sha,
                mockCommitData.project.repository.commits.nodes[i]!!.sha
            )
            assertNotNullAndEquals(
                nodes[i]!!.committedDate,
                mockCommitData.project.repository.commits.nodes[i]!!.committedDate!!
            )
            assertNotNullAndEquals(
                nodes[i]!!.authorName,
                mockCommitData.project.repository.commits.nodes[i]!!.authorName!!
            )
            assertNotNullAndEquals(
                nodes[i]!!.signature?.verificationStatus,
                mockCommitData.project.repository.commits.nodes[i]!!.signature!!.verificationStatus!!
            )
        }
        val page = commits.pageInfo
        assertNotNullAndEquals(
            page.startCursor,
            mockCommitData.project.repository?.commits?.pageInfo?.startCursor!!
        )
        assertNotNullAndEquals(
            page.endCursor,
            mockCommitData.project.repository.commits.pageInfo.endCursor!!
        )
        assertNotNullAndEquals(
            page.hasNextPage,
            mockCommitData.project.repository.commits.pageInfo.hasNextPage
        )
    }

    @Test
    fun loadRepositoryBranches() = runTest(testDispatcher) {
        val projectId = mockBranchData.project?.id!!
        whenever(
            repository.getRepositoryBranches(
                projectId, skip = 0
            )
        ).thenReturn(
            flowOf(
                mockBranchData
            )
        )
        viewModel.loadRepositoryBranches(projectId, null)
        val branches = viewModel.branches.value
        val nodes = branches!!.branchNames!!
        for (i in 0 until nodes.size) {
            assertNotNullAndEquals(nodes[i], mockBranchData.project.repository!!.branchNames!![i])
        }


    }

}