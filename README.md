 ![Status](https://img.shields.io/badge/status-ongoing-green?style=for-the-badge)

# Gitlab

> An Android client to view GitLab repositories without using a web browser.

## 📑 Table of Contents

- [Description](#description)
- [Key Features](#key-features)
- [Use Cases](#use-cases)
- [Tech Stack](#tech-stack)
- [Quick Start](#quick-start)
- [Project Structure](#project-structure)
- [Contributing](#contributing)

## 📝 Description

This application serves as a dedicated Android client for GitLab, designed for developers who want a quick, native way to view and browse their repositories on mobile devices. By eliminating the need to log in via a mobile web browser, the app streamlines how users check their project list on the go.

Under the hood, the project is written in Kotlin for Android and utilizes Jetpack Compose to deliver a clean and responsive user interface. It integrates Apollo Kotlin to query GitLab's GraphQL API for efficient data loading, and handles secure user sessions using the native GitLab OAuth2 authentication flow.

## ✨ Key Features

- **📱 Modern Jetpack Compose UI** — Provides a clean, fully native user interface built using Jetpack Compose.
- **⚡ Apollo GraphQL Integration** — Queries GitLab's API efficiently using Apollo Kotlin for optimized data loading.
- **🔐 GitLab OAuth2 Authentication** — Secures user access through GitLab's official OAuth2 authorization flow.
- **📁 Mobile Repository Browsing** — Allows users to quickly access and view their GitLab projects directly on an Android device.

## 🎯 Use Cases

- Checking repository lists and project details on a mobile device without opening a web browser.
- Accessing GitLab data securely on the go using native OAuth2 login.

## 🛠️ Tech Stack

- 🤖 **Android (Native)**
- 🟪 **Kotlin**

## ⚡ Quick Start

```bash

# 1. Clone the repository
git clone https://github.com/Ahtat204/Gitlab.git

# See the Development Setup section below
```

## 📁 Project Structure

```
.
├── app
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src
│       ├── androidTest
│       │   └── java
│       │       └── com
│       │           └── ahtat204
│       │               └── gitlab
│       │                   └── ExampleInstrumentedTest.kt
│       ├── main
│       │   ├── AndroidManifest.xml
│       │   ├── graphql
│       │   │   └── com
│       │   │       └── ahtat204
│       │   │           ├── GetProjectDetails.graphql
│       │   │           ├── ProjectsList.graphql
│       │   │           └── schema.graphqls
│       │   ├── java
│       │   │   └── com
│       │   │       └── ahtat204
│       │   │           └── gitlab
│       │   │               ├── GitlabApp.kt
│       │   │               ├── data
│       │   │               │   ├── remote
│       │   │               │   │   └── AuthenticationInterceptor.kt
│       │   │               │   ├── repositories
│       │   │               │   │   └── project
│       │   │               │   │       ├── ProjectRepository.kt
│       │   │               │   │       └── ProjectRepositoryImpl.kt
│       │   │               │   └── security
│       │   │               │       └── CryptoUtility.kt
│       │   │               ├── domain
│       │   │               │   ├── di
│       │   │               │   │   ├── ApolloModule.kt
│       │   │               │   │   └── ProjectRepositoryModule.kt
│       │   │               │   ├── models
│       │   │               │   │   ├── MergeRequest.kt
│       │   │               │   │   ├── Project.kt
│       │   │               │   │   └── ProjectElements/
│       │   │               │   └── usecase
│       │   │               │       └── authentication
│       │   │               │           ├── AuthStateSerializer.kt
│       │   │               │           ├── SafeStore.kt
│       │   │               │           ├── constants
│       │   │               │           │   ├── AuthConfig.kt
│       │   │               │           │   └── Tokens.kt
│       │   │               │           └── utility
│       │   │               │               └── Helper.kt
│       │   │               └── presentation
│       │   │                   ├── activities
│       │   │                   │   ├── AuthenticationActivity.kt
│       │   │                   │   ├── LauncherActivity.kt
│       │   │                   │   ├── MainActivity.kt
│       │   │                   │   └── ui
│       │   │                   │       └── theme
│       │   │                   │           ├── Color.kt
│       │   │                   │           ├── Theme.kt
│       │   │                   │           └── Type.kt
│       │   │                   ├── components
│       │   │                   │   ├── BottomBar.kt
│       │   │                   │   ├── Category.kt
│       │   │                   │   ├── CoilCache.kt
│       │   │                   │   ├── LanguageCircle.kt
│       │   │                   │   ├── LatestUpdates.kt
│       │   │                   │   ├── MergeRequestsSummary.kt
│       │   │                   │   ├── Notifications.kt
│       │   │                   │   ├── ProjectDetailsScreen.kt
│       │   │                   │   ├── ProjectItem.kt
│       │   │                   │   ├── ToDoItems.kt
│       │   │                   │   ├── TodoList.kt
│       │   │                   │   ├── TopAppBar.kt
│       │   │                   │   ├── TopBar.kt
│       │   │                   │   ├── WorkItem.kt
│       │   │                   │   └── WorkItems.kt
│       │   │                   ├── navigation
│       │   │                   │   ├── BottomBarScreen.kt
│       │   │                   │   ├── NavigationGraph.kt
│       │   │                   │   └── UIState.kt
│       │   │                   ├── screens
│       │   │                   │   ├── Home.kt
│       │   │                   │   ├── Issues.kt
│       │   │                   │   ├── MergeRequests.kt
│       │   │                   │   ├── PersonalProjects.kt
│       │   │                   │   ├── Profile.kt
│       │   │                   │   ├── Projects.kt
│       │   │                   │   ├── SplashScreen.kt
│       │   │                   │   └── StarrtedProjects.kt
│       │   │                   ├── ui
│       │   │                   │   └── theme
│       │   │                   │       ├── Color.kt
│       │   │                   │       ├── Theme.kt
│       │   │                   │       └── Type.kt
│       │   │                   └── viewmodels
│       │   │                       └── ProjectViewModel.kt
│       │   └── res
│       │       ├── drawable
│       │       │   ├── gitlab.png
│       │       │   ├── group.png
│       │       │   ├── ic_launcher_background.xml
│       │       │   ├── ic_launcher_foreground.xml
│       │       │   ├── issues.png
│       │       │   ├── logo.png
│       │       │   ├── mergerequest.png
│       │       │   ├── milestone.png
│       │       │   ├── project.png
│       │       │   ├── star.png
│       │       │   └── workspaces.png
│       │       ├── font
│       │       │   ├── regular.ttf
│       │       │   ├── sansserif.ttf
│       │       │   └── topbarfont.ttf
│       │       ├── values
│       │       │   ├── colors.xml
│       │       │   ├── strings.xml
│       │       │   └── themes.xml
│       │       └── xml
│       │           ├── backup_rules.xml
│       │           └── data_extraction_rules.xml
│       └── test
│           └── java
│               └── com
│                   └── asue24
│                       └── gitlab
│                           └── ExampleUnitTest.kt
├── build.gradle.kts
├── gradle
│   ├── libs.versions.toml
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
├── homescreen.jpg
├── projectlist.jpg
└── settings.gradle.kts

```

## 👥 Contributing

Contributions are welcome! Here's the standard flow:

1. **Fork** the repository
2. **Clone** your fork: `git clone https://github.com/Ahtat204/Gitlab.git`
3. **Branch**: `git checkout -b feature/your-feature`
4. **Commit**: `git commit -m 'feat: add some feature'`
5. **Push**: `git push origin feature/your-feature`
6. **Open** a pull request

