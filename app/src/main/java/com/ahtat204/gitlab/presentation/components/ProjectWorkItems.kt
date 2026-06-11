package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahtat204.gitlab.presentation.ui.theme.Gray
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.topBarFont

/**
 * Displays a clickable card representing an [Item].
 *
 * The card includes:
 * - An icon loaded from the item's resource ID.
 * - The item's name styled with a custom font.
 *
 * When clicked, the card triggers the [openScreen] callback, typically used
 * to navigate to the item's associated screen.
 *
 * @param item The [Item] to be displayed inside the card.
 * @param openScreen A callback function invoked when the card is clicked.
 *
 * Example usage:
 * ```
 * ProjectWorkItems(
 *     item = Item(
 *         name = "Merge Requests",
 *         route = "project/{id}/merge_requests",
 *         Id = R.drawable.mergerequest,
 *         count = project.openMergeRequestsCount
 *     ),
 *     openScreen = { navController.navigate(item.route) }
 * )
 * ```
 */
@Composable
fun ProjectWorkItems(item: Item, openScreen: () -> Unit) {
    Card(
        openScreen, modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 1.dp).clickable(onClick = openScreen)
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .height(60.dp).fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(item.Id),
                contentDescription = item.name,
                Modifier
                    .size(27.dp)
                    .padding(0.dp,3.dp),
                tint = Orange
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = item.name,
                fontSize = 18.sp,
                fontFamily = topBarFont,
                modifier = Modifier.weight(0.9f).padding(10.dp),
                letterSpacing = 1.sp
            )
            Text(
                text = item.count?.toString()?:"",
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.End,
                fontFamily = topBarFont,
                modifier = Modifier.weight(0.9f).padding(10.dp)
            )
        }
    }
}