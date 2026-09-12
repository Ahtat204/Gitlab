package com.ahtat204.gitlab.reponses.objects

import com.ahtat204.gitlab.data.queries.GetMyProfileQuery.CurrentUser
import com.ahtat204.gitlab.data.queries.GetMyProfileQuery.Data
import com.ahtat204.gitlab.data.queries.GetMyProfileQuery.Status
import com.ahtat204.gitlab.data.queries.type.AvailabilityEnum

val mockedProfile = Data(
    currentUser = CurrentUser(
        __typename = "CurrentUser",
        id = "gid://gitlab/User/4221",
        name = "Sarah Jenkins",
        username = "sjenkins_dev",
        publicEmail = "sarah.jenkins@company.com",
        avatarUrl = "https://example.com",
        webUrl = "https://example.com",
        bio = "Senior Backend Engineer | Working on Kotlin microservices and data pipelines.",
        location = "Austin, TX",
        github = "sjenkins-codes",
        jobTitle = "Staff Software Engineer",
        projectCount = 34,
        linkedin = "sarah-jenkins-dev",
        status = Status(
            __typename = "UserStatus",
            availability = AvailabilityEnum.BUSY,
            emoji = "💻",
            message = "Deep focus mode - expecting delays in MR reviews"
        )
    )
)
