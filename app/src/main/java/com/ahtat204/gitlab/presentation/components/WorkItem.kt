package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily
import kotlinx.serialization.Serializable

/**
 * Represents a generic item that can be displayed in the UI.
 *
 * @property name The display name of the item.
 * @property route The navigation route associated with this item.
 * @property Id The resource ID of the icon representing this item.
 * @property count An optional count value associated with the item (e.g., number of tasks).
 */
@Serializable
data class Item(val name: String, val route: String, val Id: Int,val count:Int?=null)

/**
 * Displays a clickable card representing a [Item].
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
 * WorkItem(
 *     item = Item(
 *         name = "Repositories",
 *         route = "repositories_screen",
 *         Id = R.drawable.repo_icon,
 *         count = 10
 *     ),
 *     openScreen = { navController.navigate(item.route) }
 * )
 * ```
 */
@Composable
fun WorkItem(item: Item, openScreen: () -> Unit) {
    Card(
        openScreen,
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
                painter = painterResource(item.Id),
                contentDescription = item.name,
                Modifier.size(30.dp).padding(3.dp),
                tint = Orange
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = item.name,
                fontFamily = customFontFamily,
                modifier = Modifier.weight(0.9f)
            )
        }
    }
}
