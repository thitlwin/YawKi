package com.yawki.navigator

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class YawKiScreens(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    val name: String = route.appendArguments(navArguments)

    // onboarding
    data object GettingStarted : YawKiScreens("GettingStarted")

    // dashboard
    data object Dashboard : YawKiScreens(
        "Dashboard",
        navArguments = listOf(navArgument("channelid") { type = NavType.StringType })
    ) {
        fun createRoute(channelId: String) =
            route.replace("{${navArguments.first().name}}", channelId)
    }

    data object AudioListUIScreen : YawKiScreens("AudioListUIScreen")
    data object PlayerUIScreen : YawKiScreens("PlayerUIScreen")
}

sealed class YawKiRoute(val name: String) {
    data object OnBoarding: YawKiRoute("onboarding")
    data object Dashboard: YawKiRoute("dashboard")
}

private fun String.appendArguments(navArguments: List<NamedNavArgument>): String {
    val mandatoryArguments = navArguments.filter {
        it.argument.defaultValue == null
    }.takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "/", prefix = "/") { "{${it.name}}" }
        .orEmpty()
    val optionalArguments = navArguments.filter {
        it.argument.defaultValue != null
    }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "&", prefix = "?") {
            "${it.name}={${it.name}}"
        }.orEmpty()
    return "$this$mandatoryArguments$optionalArguments"
}
