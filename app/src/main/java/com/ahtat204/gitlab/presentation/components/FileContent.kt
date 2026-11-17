package com.ahtat204.gitlab.presentation.components

import androidx.compose.runtime.Composable
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.File
import com.mikepenz.markdown.m3.Markdown

@Composable
fun FileContent(file: File) {
    file?.let {
        if (file.fileType?.equals("readme") == true) {
            file.rawTextBlob?.let { content -> Markdown(content.trimIndent()) }
        }
    }

}