package com.asue24.gitlab.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * this Component shows a List of Work Items on your Gitlab , examples: MRs , Issues ,To-do items,Starred Projects...
 */
@Composable
 fun MyWorkItems(navController: NavController) {
    val myWorkItems = listOf("issues", "merge requests", "To-do items")

    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        myWorkItems.forEach { item ->
        WorkItem(item) {
            navController.navigate(item)
        }

        }
    }
}