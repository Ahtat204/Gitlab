package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FileOpen
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.RepositoryViewModel

/**
 * Displays a repository tree item representing a file.
 *
 * ## Purpose
 * - Renders a card with an icon and filename for a given file node.
 * - Intended for use in project repository views where files are listed.
 *
 * ## Parameters
 * @param item The file node from [GetProjectRepositoryQuery.Node1].
 *             If `null` or has no name, nothing is rendered.
 *
 * ## Behavior
 * - Shows a file icon tinted with [Orange].
 * - Displays the file name using [customFontFamily].
 * - Card has a black background, padding, and fixed height.
 *
 * ## Layout
 * - Root: [Card] with full width and padding.
 * - Content: [Row] containing:
 *   - File icon (30.dp size).
 *   - Spacer for separation.
 *   - File name text.
 *
 * ## Example
 * ```
 * TreeItemCard(item = fileNode)
 * ```
 */
@Composable
fun TreeItemCard(item: GetProjectRepositoryQuery.Node1?){
    item?.name?.let{
        Card(
            {},
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
                    painter = painterResource(R.drawable.file),
                    contentDescription = item.id,
                    Modifier
                        .size(30.dp)
                        .padding(3.dp),
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

}

/**
 * Displays a repository tree item representing a folder.
 *
 * ## Purpose
 * - Renders a card with an icon and folder name for a given directory node.
 * - Intended for use in project repository views where folders are listed.
 *
 * ## Parameters
 * @param item The folder node from [GetProjectRepositoryQuery.Node].
 *             If `null` or has no name, nothing is rendered.
 *
 * ## Behavior
 * - Shows a folder icon tinted with a dark gray color.
 * - Displays the folder name using [customFontFamily].
 * - Card has a black background, padding, and fixed height.
 *
 * ## Layout
 * - Root: [Card] with full width and padding.
 * - Content: [Row] containing:
 *   - Folder icon (30.dp size).
 *   - Spacer for separation.
 *   - Folder name text.
 *
 * ## Example
 * ```
 * TreeItemCard(item = folderNode)
 * ```
 */
@Composable
fun TreeItemCard(item: GetProjectRepositoryQuery.Node?,repositoryViewModel: RepositoryViewModel,path:String?,project:String,branch:String?,/*open:(path: String?)->Unit*/){
    Card(
        onClick = {  repositoryViewModel.loadProjectRepository(
            branch =branch,
            path = path,
            projectPath = project
        ) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp)
            .background(Color.Black)
    ) {
        item?.name?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .height(50.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.Folder,
                    contentDescription = it,
                    Modifier
                        .size(30.dp)
                        .padding(3.dp),
                    tint = Orange
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = it,
                    fontFamily = customFontFamily,
                    modifier = Modifier.weight(0.9f)
                )
            }
        }

    }
}