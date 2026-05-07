package com.asue24.gitlab.presentation.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.presentation.ui.theme.Background
import com.asue24.gitlab.presentation.ui.theme.customFontFamily

@Composable
fun ProjectItem(data: GetMyProjectsQuery.Node) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Background)


        ) {
            data.project?.let { project ->
                Text(
                    text = project.name,
                    fontSize = 17.sp,
                    color = Color.White,
                    modifier = Modifier.offset(10.dp,0.dp),
                    fontFamily = customFontFamily
                )
                project.description?.let {
                    Text(
                        text = it,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = 9.sp,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .offset(10.dp, 0.dp),
                        fontFamily = customFontFamily,
                    )
                }
                val languages=project.languages
                if(languages?.isNotEmpty() == true){
                    val language=languages[0]
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(14.dp,0.dp)){
                        Language(Color(hexColorToLong(language.color.toString())),"")
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


@Composable
fun Language(color: Color,language:String) {

    Canvas(
        modifier = Modifier
    ) {
        // Coordinates for the point (center of the canvas)
        val center = Offset(1.0f, size.height / 2)

        // Draw a single point
        drawPoints(
            points = listOf(center),
            pointMode = PointMode.Points,
            color = color,
            strokeWidth = 26f, // Controls point size
            cap = StrokeCap.Round // Makes the point circular
        )
    }}



fun hexColorToLong(hex: String): Long {
    val cleanHex = hex.removePrefix("#").trim()
    require(cleanHex.matches(Regex("^[0-9A-Fa-f]{6}$"))) {
        "Invalid hex color format. Expected #RRGGBB."
    }

    // Parse as unsigned long from hex
    return cleanHex.toLong(16)
}
