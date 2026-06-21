package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.Branches
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.RepositoryViewModel

@Composable
fun BranchesList(
    branches: Branches,
    repositoryViewModel: RepositoryViewModel,
    projectPath: String,
    currentBranch: MutableState<String?>,
    x: PaddingValues
) {
    branches?.branchNames?.let {
        branches
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentPadding = x,
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(it) { branch ->
                Card(
                    {
                        repositoryViewModel.loadProjectRepository(
                            projectPath, branch
                        )
                        currentBranch.value = branch
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 10.dp)
                        .background(Color.Black)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                            .height(50.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.branch),
                            contentDescription = branch,
                            Modifier
                                .size(30.dp)
                                .padding(3.dp),
                            tint = Orange
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = branch,
                            fontFamily = customFontFamily,
                            modifier = Modifier.weight(0.9f)
                        )
                    }
                }
            }
        }
    }
}