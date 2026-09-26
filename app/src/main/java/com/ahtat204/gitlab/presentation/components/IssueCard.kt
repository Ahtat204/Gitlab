package com.ahtat204.gitlab.presentation.components

import androidx.compose.runtime.Composable
import com.ahtat204.gitlab.data.queries.GetProjectIssuesQuery

typealias Issue= GetProjectIssuesQuery.Node
@Composable
fun IssueCard(issue: Issue){}