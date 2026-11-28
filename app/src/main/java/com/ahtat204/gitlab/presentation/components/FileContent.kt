package com.ahtat204.gitlab.presentation.components

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.File

@Composable
fun FileContent(file: File) {
    file?.let {
        val name = file.name
        val rawTextBlob = file.rawTextBlob
        if (name != null && rawTextBlob != null) {
            val regex = Regex("""\\.[^.]+$""")
            val matchResult = regex.find(name) ?: return
            val extension = ".${matchResult.value}"
            val language = LanguageParser.forExtension(extension) ?: return
            val source = LanguageParser.parse(rawTextBlob, language)
            Log.d("com.ahtat204.gitlab", source)
            Text(source)
        }
    }

}