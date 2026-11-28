package com.ahtat204.gitlab.presentation.components

enum class Language(val extension: String) {
    JSON(".json"),
    //PYTHON(".py"),
    //CPP(".cpp"),
    // JAVA(".java"),
    // add more as needed
}

object LanguageParser {

    init {
        System.loadLibrary("ts_bridge")
    }

    fun parse(source: String, language: Language): String = when (language) {
        Language.JSON -> parseJsonToSExp(source)
        //   Language.PYTHON -> parsePythonToSExp(source)
        // Language.CPP    -> parseCppToSExp(source)
        //Language.JAVA   -> parseJavaToSExp(source)


    }

    fun forExtension(ext: String): Language? = Language.entries.find { it.extension == ext }

    // The JNI externals
    private external fun parseJsonToSExp(source: String): String
//    private external fun parsePythonToSExp(source: String): String
    //  private external fun parseCppToSExp(source: String): String
    //private external fun parseJavaToSExp(source: String): String
}