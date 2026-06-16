package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.ahtat204.gitlab.data.queries.GetMyProjectsPaginatedQuery
import com.ahtat204.gitlab.presentation.ui.theme.Background
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Composable that renders a single GitLab project item card.
 *
 * ## Overview
 * Displays project details including:
 * - User avatar (if available)
 * - Project name and visibility (public/private icon)
 * - Project description (truncated to one line)
 * - Project topics (up to 3 shown)
 * - Primary language with color indicator
 *
 * ## Parameters
 * @param data The [GetMyProjectsPaginatedQuery.CurrentUser] object containing user metadata
 *             (used to load avatar image if available).
 * @param project The [GetMyProjectsPaginatedQuery.Project] object representing the project
 *                whose details will be displayed.
 * @param imageLoader The [ImageLoader] instance used by Coil to load images asynchronously.
 *
 * ## UI Behavior
 * - Wraps content in a [Card] with fixed height.
 * - Displays avatar image using [AsyncImage] with caching and crossfade enabled.
 * - Shows project name alongside a visibility icon (`Public` or `Lock`).
 * - Shows project description if present, truncated with ellipsis.
 * - Displays up to three topics styled in orange.
 * - Displays the first language with a colored circle and name.
 *
 * ## Example
 * ```kotlin
 * ProjectItem(
 *     data = userData,
 *     project = project,
 *     imageLoader = imageLoader
 * )
 * ```
 *
 * ## Notes
 * - Uses Coil’s [AsyncImage] for image loading with memory and disk caching.
 * - Applies custom font family and colors for consistent styling.
 * - Topics and languages are optional and only shown if available.
 * @author Lahcen AHTAT
 */
@Composable
fun ProjectItem(
    data: GetMyProjectsPaginatedQuery.CurrentUser?,
    project: GetMyProjectsPaginatedQuery.Project,
    imageLoader: ImageLoader,
    navController: NavHostController
) {
    val encodedId = URLEncoder.encode(project.fullPath, StandardCharsets.UTF_8.toString())
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { navController.navigate("project?projectId=$encodedId") })
            .fillMaxHeight()
            .height(120.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            data?.let {
                it.avatarUrl?.let { url ->
                    val avatar = "https://gitlab.com/$url"

                    AsyncImage(
                        imageLoader = imageLoader,
                        model = ImageRequest.Builder(LocalContext.current).data(avatar) // Image URL
                            .crossfade(true).memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED) // Smooth fade-in
                            .build(),
                        contentDescription = "Sample Image",
                        modifier = Modifier
                            .padding(10.dp)
                            .size(40.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        onState = { state ->
                            when (state) {
                                is AsyncImagePainter.State.Loading -> {}
                                is AsyncImagePainter.State.Success -> {}
                                else -> {}
                            }
                        })
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Background)
            ) {
                project.pipelines?.nodes?.get(0)?.status?.let { PipeLineStatusIcon(it) }

            //    project.pipelines?.nodes?.get(0)?.status?.let { PipeLineStatusIcon(it) }

                project.let { project ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = project.name,
                            fontSize = 17.sp,
                            color = Color.White,
                            modifier = Modifier.offset(10.dp, 0.dp),
                            fontFamily = customFontFamily
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        val visibilityIcon: ImageVector = when (project.visibility) {
                            "public" -> Icons.Default.Public
                            else -> Icons.Default.Lock
                        }
                        project.visibility?.let {
                            Icon(
                                visibilityIcon,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }

                    project.description?.let {
                        Text(
                            text = it,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontSize = 10.sp,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .offset(10.dp, 0.dp),
                            fontFamily = customFontFamily,
                        )
                    }
                    project.topics?.let { topics ->
                        Row(horizontalArrangement = Arrangement.Start) {
                            topics.onEachIndexed { index, topic ->
                                if (index < 3) {
                                    Text(
                                        text = topic,
                                        fontSize = 11.sp,
                                        color = Orange,
                                        modifier = Modifier
                                            .offset(0.dp, 0.dp)
                                            .padding(10.dp, 0.dp),
                                        fontFamily = customFontFamily
                                    )
                                }
                            }
                        }
                    }
                    val languages = project.languages
                    if (languages?.isNotEmpty() == true) {
                        val language = languages[0]
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(14.dp, 0.dp)
                        ) {
                            Language(language.color.toString().toColorInt())
                            Text(
                                text = language.name,
                                fontSize = 11.sp,
                                color = Color.White,
                                modifier = Modifier.offset(10.dp, 0.dp),
                                fontFamily = customFontFamily
                            )
                        }
                    }
                }
            }
        }
    }

}/*


fun hexColorToLong(hex: String): Long {
    val cleanHex = hex.removePrefix("#").trim()
    require(cleanHex.matches(Regex("^[0-9A-Fa-f]{6}$"))) {
        "Invalid hex color format. Expected #RRGGBB."
    }

    // Parse as unsigned long from hex
    return cleanHex.toLong(16)
}*/
