package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.titleFont

/**
 * Displays general repository details such as name, description,
 * star count, and fork count in a styled layout.
 *
 * This composable arranges the repository information in a vertical
 * column with the following sections:
 * - Repository name styled as a bold title.
 * - Repository description with smaller font size.
 * - A horizontal row showing star and fork counts with icons.
 *
 * @param forkCount The number of forks for the repository.
 * @param startCount The number of stars for the repository.
 * @param name The name of the repository.
 * @param description A short description of the repository.
 *
 * Example usage:
 * ```
 * GeneralDetails(
 *     forkCount = 42,
 *     startCount = 120,
 *     name = "Awesome Project",
 *     description = "This project demonstrates Compose UI with GitLab integration."
 * )
 * ```
 */
@Composable
fun GeneralDetails(
    forkCount: Int, startCount: Int, name: String, description: String
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
    ) {
        Text(
            name,
            fontFamily = titleFont,
            fontSize = 25.sp,
            letterSpacing = 1.sp,
            maxLines = 2,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(20.dp, 11.dp),
            color = Color.White,
            textAlign = TextAlign.Center
        )

        AutoLinkText(
            description
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 5.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.TwoTone.Star,
                contentDescription = null,
                modifier = Modifier.padding(5.dp),
                tint = Orange
            )
            Text(text = startCount.toString(), color = Color.White, fontSize = 15.sp)
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                painterResource(R.drawable.fork),
                contentDescription = null,
                modifier = Modifier.padding(5.dp),
                tint = Orange
            )
            Text(text = forkCount.toString(), color = Color.White, fontSize = 15.sp)
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
}
