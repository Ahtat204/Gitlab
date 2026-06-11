# 📱 GitLab Client

[![Status](https://img.shields.io/badge/status-ongoing-green?style=for-the-badge)](https://github.com/Ahtat204/Gitlab)
[![Language](https://img.shields.io/badge/language-Kotlin-7F52FF?style=for-the-badge)](https://kotlinlang.org)
[![Platform](https://img.shields.io/badge/platform-Android-3DDC84?style=for-the-badge)](https://www.android.com)
[![License](https://img.shields.io/badge/license-MIT-blue?style=for-the-badge)](LICENSE)

> A native Android client for GitLab that brings your repositories to your fingertips. Browse projects, view merge requests, and manage issues without opening a web browser.

## 📸 Screenshots

|          Home Screen           |             Project List              |            Project Overview            |              Profile               | 
|:------------------------------:|:-------------------------------------:|:--------------------------------------:|:----------------------------------:| 
| ![Home Screen](homescreen.jpg) | ![Project List](personalprojects.jpg) | ![Project Details](projectdetails.jpg) | ![Developper Profile](profile.jpg) | 

---

## 📑 Table of Contents

- [Overview](#overview)
- [✨ Key Features](#-key-features)
- [🛠️ Tech Stack](#️-tech-stack)
- [🚀 Quick Start](#-quick-start)
- [📁 Project Structure](#-project-structure)
- [⚙️ Configuration](#️-configuration)
- [🔒 Security](#-security)
- [📚 Architecture](#-architecture)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)

---

## Overview

**GitLab Client** is a modern Android application built with **Kotlin** and **Jetpack Compose** that provides a native, efficient way to interact with GitLab. Whether you're a developer on the go or prefer a dedicated mobile experience, this app eliminates the need to open a web browser while maintaining full access to your GitLab workspace.

The application leverages **Apollo Kotlin** for GraphQL queries and **OAuth2** for secure authentication, ensuring a seamless and secure user experience.

---

## ✨ Key Features

- 📱 **Modern Jetpack Compose UI** — Clean, responsive, and fully native Android interface built with Jetpack Compose for an intuitive user experience.
- 🔐 **GitLab OAuth2 Authentication** — Secure login using GitLab's official OAuth2 authorization flow.
- ⚡ **GraphQL Integration** — Efficient data fetching using Apollo Kotlin for optimized performance.
- 📂 **Repository Browsing** — Quickly view and navigate your GitLab projects on mobile.
- 🔍 **Project Details** — Access comprehensive project information including merge requests, issues, and statistics.
- 💾 **Secure Token Storage** — Encrypted storage of authentication tokens for safe credential management.
- 🎨 **Dark & Light Themes** — Adaptive UI themes for comfortable viewing in any lighting condition.

---

## 🛠️ Tech Stack

| Layer | Technologies |
|:---:|:---|
| **UI Framework** | Jetpack Compose, Material Design 3 |
| **Language** | Kotlin |
| **Data Layer** | Apollo Kotlin (GraphQL), OkHttp |
| **Authentication** | OAuth2 |
| **Security** | Crypto/Encryption utilities |
| **Dependency Injection** | Hilt (inferred from structure) |
| **Image Loading** | Coil |
| **Platform** | Android (Native) |
| **Build System** | Gradle (Kotlin DSL) |

---

## 🚀 Quick Start

### Prerequisites

- Android Studio Giraffe or later
- Android SDK 24 (or higher)
- Kotlin 1.9+
- Git

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/Ahtat204/Gitlab.git
cd Gitlab

# 2. Open in Android Studio
# File > Open > Select the project directory

# 3. Configure GitLab OAuth2 credentials
# Update AuthConfig.kt with your GitLab app credentials

# 4. Sync Gradle and run the app
./gradlew build
# Then run on emulator or physical device
```

### Configuration

1. **Register a GitLab Application**:
   - Go to GitLab → Preferences → Applications
   - Create a new application with redirect URI: `gitlab://oauth-callback`
   - Note your Application ID and Secret

2. **Update AuthConfig.kt**:
   ```kotlin
   object AuthConfig {
       const val GITLAB_CLIENT_ID = "your_client_id"
       const val GITLAB_CLIENT_SECRET = "your_client_secret"
       const val REDIRECT_URI = "gitlab://oauth-callback"
   }
   ```

3. **Build and Run**:
   ```bash
   ./gradlew installDebug
   ```

---

## 📁 Project Structure

```
Gitlab/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/ahtat204/gitlab/
│   │   │   │   ├── data/                    # Data layer
│   │   │   │   │   ├── remote/              # API & network
│   │   │   │   │   │   └── AuthenticationInterceptor.kt
│   │   │   │   │   ├── repositories/        # Repository implementations
│   │   │   │   │   │   └── project/
│   │   │   │   │   └── security/            # Encryption utilities
│   │   │   │   ├── domain/                  # Domain layer
│   │   │   │   │   ├── di/                  # Dependency injection
│   │   │   │   │   │   ├── ApolloModule.kt
│   │   │   │   │   │   └── ProjectRepositoryModule.kt
│   │   │   │   │   ├── models/              # Data models
│   │   │   │   │   │   ├── Project.kt
│   │   │   │   │   │   └── MergeRequest.kt
│   │   │   │   │   └── usecase/             # Business logic
│   │   │   │   │       └── authentication/
│   │   │   │   ├── presentation/            # Presentation layer
│   │   │   │   │   ├── activities/          # Activities
│   │   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   │   ├── AuthenticationActivity.kt
│   │   │   │   │   │   └── LauncherActivity.kt
│   │   │   │   │   ├── screens/             # Composable screens
│   │   │   │   │   │   ├── Home.kt
│   │   │   │   │   │   ├── Projects.kt
│   │   │   │   │   │   ├── MergeRequests.kt
│   │   │   │   │   │   ├── Issues.kt
│   │   │   │   │   │   ├── Profile.kt
│   │   │   │   │   │   └── PersonalProjects.kt
│   │   │   │   │   ├── components/          # Reusable Compose components
│   │   │   │   │   │   ├── ProjectItem.kt
│   │   │   │   │   │   ├── BottomBar.kt
│   │   │   │   │   │   └── TopAppBar.kt
│   │   │   │   │   ├── viewmodels/          # ViewModels
│   │   │   │   │   │   └── ProjectViewModel.kt
│   │   │   │   │   ├── navigation/          # Navigation graph
│   │   │   │   │   │   ├── NavigationGraph.kt
│   │   │   │   │   │   └── BottomBarScreen.kt
│   │   │   │   │   └── ui/theme/            # Theme & styling
│   │   │   │   │       ├── Color.kt
│   │   │   │   │       ├── Theme.kt
│   │   │   │   │       └── Type.kt
│   │   │   │   └── GitlabApp.kt             # Application class
│   │   │   ├── graphql/                     # GraphQL queries & schema
│   │   │   │   └── com/ahtat204/
│   │   │   │       ├── ProjectsList.graphql
│   │   │   │       ├── GetProjectDetails.graphql
│   │   │   │       └── schema.graphqls
│   │   │   ├── res/                         # Resources
│   │   │   │   ├── drawable/                # Icons & images
│   │   │   │   ├── values/                  # Strings, colors, themes
│   │   │   │   └── font/                    # Custom fonts
│   │   │   └── AndroidManifest.xml
│   │   └── test/                            # Unit tests
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   ├── libs.versions.toml                   # Dependency versions
│   └── wrapper/
├── build.gradle.kts                         # Root build configuration
├── settings.gradle.kts
├── gradle.properties
├── gradlew & gradlew.bat
└── README.md
```

---

## ⚙️ Configuration

### Environment Variables

```properties
# gradle.properties
# Add your GitLab instance URL if using a self-hosted GitLab
GITLAB_API_URL=https://gitlab.com/api/v4
```

### Dependencies

Key dependencies are managed in `gradle/libs.versions.toml`. Update versions as needed:

- **Apollo Kotlin**: GraphQL client
- **Jetpack Compose**: UI framework
- **Coil**: Image loading library
- **OkHttp**: HTTP client with interceptors
- **Material Design 3**: Design components

---

## 🔒 Security

- **OAuth2 Flow**: Secure authentication without storing passwords
- **Encrypted Token Storage**: Access tokens are encrypted using Android's Crypto API
- **HTTPS Only**: All API communications are encrypted
- **ProGuard Rules**: Code obfuscation for production builds

### Best Practices

1. Never commit API credentials to the repository
2. Store OAuth secrets in a secure configuration file (not in version control)
3. Regularly update dependencies for security patches
4. Review GraphQL queries to minimize exposed data

---

## 📚 Architecture

This project follows **Clean Architecture** principles with a layered approach:

### Layers

1. **Presentation Layer** (UI)
   - Composable screens using Jetpack Compose
   - ViewModels for state management
   - Navigation graph for screen transitions

2. **Domain Layer** (Business Logic)
   - Use cases for business operations
   - Domain models
   - Repository interfaces

3. **Data Layer** (Data Sources)
   - Repository implementations
   - Remote data sources (API)
   - Local caching (if applicable)
   - Interceptors for request/response handling

### Design Patterns

- **MVVM**: ViewModel-based state management
- **Repository Pattern**: Abstraction of data sources
- **Dependency Injection**: Using Hilt for DI
- **Single Responsibility Principle**: Each component has one responsibility

---

## 🤝 Contributing

Contributions are welcome! Whether it's bug fixes, feature requests, or improvements, please follow these guidelines:

### Development Workflow

1. **Fork** the repository
   ```bash
   git clone https://github.com/Ahtat204/Gitlab.git
   ```

2. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make your changes**
   - Follow Kotlin coding conventions
   - Add comments for complex logic
   - Test your changes locally

4. **Commit with clear messages**
   ```bash
   git commit -m "feat: add awesome feature"
   # or
   git commit -m "fix: resolve issue with X"
   ```

5. **Push to your fork**
   ```bash
   git push origin feature/your-feature-name
   ```

6. **Open a Pull Request**
   - Provide a clear description of your changes
   - Link to any related issues
   - Include screenshots for UI changes

### Commit Convention

- `feat:` - New feature
- `fix:` - Bug fix
- `refactor:` - Code refactoring
- `docs:` - Documentation
- `style:` - Code style (formatting, etc.)
- `test:` - Adding or updating tests
- `chore:` - Build, dependencies, etc.

### Code Style

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Keep functions small and focused
- Add KDoc comments for public APIs

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- [GitLab](https://gitlab.com) for the GraphQL API
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for the modern UI framework
- [Apollo Kotlin](https://www.apollographql.com/docs/kotlin/) for GraphQL client
- The Android development community

---

## 📞 Support

Have questions or found a bug? 

- Open an [Issue](https://github.com/Ahtat204/Gitlab/issues)
- Check existing issues for similar problems
- Provide detailed error messages and reproduction steps

---

**Made with ❤️ by [Ahtat204](https://github.com/Ahtat204)**

Last updated: May 2026
