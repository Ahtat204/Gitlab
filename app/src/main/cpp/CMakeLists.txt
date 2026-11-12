cmake_minimum_required(VERSION 3.22.1)
project("gitlab")

include(FetchContent)

# Check if a local directory was provided via local.properties
if (DEFINED TREESITTER_DIR AND EXISTS "${TREESITTER_DIR}/tree-sitter/lib/src/lib.c")
    message(STATUS "Using local Tree-sitter sources from: ${TREESITTER_DIR}")
    set(TS_CORE_DIR "${TREESITTER_DIR}/tree-sitter")
    set(TS_JSON_DIR "${TREESITTER_DIR}/tree-sitter-json")
else ()
    message(STATUS "Fetching Tree-sitter sources automatically via FetchContent...")

    FetchContent_Declare(
            treesitter
            GIT_REPOSITORY https://github.com/tree-sitter/tree-sitter
            GIT_TAG v0.25.1
    )
    FetchContent_Declare(
            treesitter_json
            GIT_REPOSITORY https://github.com/tree-sitter/tree-sitter-json
            GIT_TAG v0.24.8
    )
    FetchContent_MakeAvailable(treesitter treesitter_json)

    set(TS_CORE_DIR "${treesitter_SOURCE_DIR}")
    set(TS_JSON_DIR "${treesitter_json_SOURCE_DIR}")
endif ()

add_library(gitlab SHARED
        tree_sitter_jni.cpp
        "${TS_CORE_DIR}/lib/src/lib.c"
        "${TS_JSON_DIR}/src/parser.c"
)

target_include_directories(gitlab PRIVATE
        "${TS_CORE_DIR}/lib/include"
        "${TS_CORE_DIR}/lib/src"
        "${TS_JSON_DIR}/src"
)

find_library(log-lib log)
target_link_libraries(gitlab ${log-lib})
