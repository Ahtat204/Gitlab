package com.asue24.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.navigation.NavHostController
import com.asue24.gitlab.presentation.ui.theme.Orange
import com.asue24.gitlab.presentation.ui.theme.customFontFamily

@Composable
fun ProjectsCategoryList(navController: NavHostController, x: PaddingValues) {
    val categories = listOf("Contributed", "Personal", "Starred")
    Column(
        modifier = Modifier.padding(x),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        categories.forEach { category ->
            Card(
                {navController.navigate(category)}, modifier = Modifier
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
                    Text(text =category, fontFamily = customFontFamily, modifier = Modifier)

                }
            }
        }
    }

}