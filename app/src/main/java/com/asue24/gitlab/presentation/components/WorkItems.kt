package com.asue24.gitlab.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

/**
 * this Component shows a List of Work Items on your Gitlab , examples: MRs , Issues ,To-do items,Starred Projects...
 */
@Composable
public fun MyWorkItems(navController: NavController) {
    val myWorkItems = listOf("issues", "merge requests", "To-do items")

    Column() {
        myWorkItems.forEach { item ->
        WorkItem(item) {
            navController.navigate(item)
        }

        }
    }
}