package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.EmojiSupportMatch
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.titleFont

/**
 * A specialized header component for the GitLab user profile or account dashboard.
 *
 * This composable renders a user's primary profile information, including their avatar,
 * full name, username, and current status. It is designed to be used at the top of
 * profile-centric screens to provide immediate identity context.
 *
 * @param name The full display name of the user.
 * @param username The unique GitLab handle of the user (prefixed with '@' automatically).
 * @param status The current status string or presence indicator of the user.
 * @param imageLoader The [ImageLoader] instance used for handling image requests,
 * injected to ensure optimal memory management and singleton reuse.
 * @param avatar The URL suffix for the user's avatar image; if null, the avatar
 * section will not be rendered.
 *
 * @note This component utilizes [Coil] for asynchronous image loading with active
 * memory and disk caching policies to maintain high-performance rendering.
 *
 * @example
 * ```kotlin
 * Header(
 * name = "John Doe",
 * username = "jdoe",
 * status = "Available",
 * imageLoader = myImageLoader,
 * avatar = "uploads/user/avatar.png"
 * )
 * ```
 * @author Lahcen AHTAT
 */
@Composable
fun Header(
    name: String, username: String, status: String, imageLoader: ImageLoader, avatar: String?
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp, 18.dp)
            .clip(RoundedCornerShape(40.dp))
    ) {
        avatar?.let {
            AsyncImage(
                imageLoader = imageLoader,
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://gitlab.com/$avatar") // Image URL
                    .crossfade(true).memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED).build(),
                contentDescription = "Sample Image",
                modifier = Modifier
                    .padding(4.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(40.dp)),
                /*   onState = { state ->
                       when (state) {
                           is AsyncImagePainter.State.Loading -> {}
                           is AsyncImagePainter.State.Success -> {}
                           else -> {}
                       }
                   }*/
            )
        }
        Column(
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
        ) {
            Text(
                name,
                fontFamily = titleFont,
                fontSize = 25.sp,
                letterSpacing = 1.sp,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp, 3.dp),
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                "@$username",
                fontFamily = titleFont,
                fontSize = 25.sp,
                letterSpacing = 1.sp,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp, 3.dp),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                status,
                fontFamily = titleFont,
                fontSize = 15.sp,
                letterSpacing = 1.sp,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp, 2.dp),
                color = Orange,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        emojiSupportMatch = EmojiSupportMatch.Default
                    )
                )
            )
        }
    }

}