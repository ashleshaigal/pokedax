package com.aigal.pokedax.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aigal.pokedax.ui.auth.AuthViewModel
import com.aigal.pokedax.ui.detail.PokemonDetailScreen
import com.aigal.pokedax.ui.list.PokemonListScreen
import com.aigal.pokedax.ui.navigation.BottomBarScreen
import com.aigal.pokedax.ui.screens.EditProfileScreen
import com.aigal.pokedax.ui.screens.LoginScreen
import com.aigal.pokedax.ui.screens.ProfileScreen
import com.aigal.pokedax.ui.screens.SavedScreen
import com.aigal.pokedax.ui.screens.WebViewScreen
import com.aigal.pokedax.ui.settings.AboutScreen
import com.aigal.pokedax.ui.theme.PokedaxTheme
import com.aigal.pokedax.ui.theme.PokedexRed
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun MainScreen(viewModel: AuthViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val user by viewModel.user.collectAsState()
    val isLoggedIn = user != null

    Scaffold(
        bottomBar = {
            if (currentRoute != "login") {
                BottomBar(
                    navController = navController,
                    isLoggedIn = isLoggedIn
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Home.route,
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
        ) {
            composable(BottomBarScreen.Home.route) {
                PokemonListScreen(navController = navController)
            }
            composable(BottomBarScreen.Saved.route) {
                SavedScreen(navController = navController)
            }
            composable(BottomBarScreen.Profile.route) {
                ProfileScreen(navController = navController)
            }
            composable("about") {
                AboutScreen(navController = navController)
            }
            composable("edit_profile") {
                EditProfileScreen(navController = navController, viewModel = viewModel)
            }
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.popBackStack()
                    },
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNavigateToWeb = { url, title ->
                        navController.navigate("webview/$url/$title")
                    }
                )
            }
            composable(
                "pokemon_detail_screen/{pokemonId}",
                arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
            ) {
                PokemonDetailScreen(navController = navController)
            }
            composable(
                "webview/{url}/{title}",
                arguments = listOf(
                    navArgument("url") { type = NavType.StringType },
                    navArgument("title") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val encodedUrl = backStackEntry.arguments?.getString("url") ?: ""
                val url = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
                val encodedTitle = backStackEntry.arguments?.getString("title") ?: ""
                val title = URLDecoder.decode(encodedTitle, StandardCharsets.UTF_8.toString())
                WebViewScreen(navController = navController, url = url, title = title)
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, isLoggedIn: Boolean) {
    val screens = if (isLoggedIn) {
        listOf(
            BottomBarScreen.Home,
            BottomBarScreen.Saved,
            BottomBarScreen.Profile,
        )
    } else {
        listOf(
            BottomBarScreen.Home,
            BottomBarScreen.Profile,
        )
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = PokedexRed,
        contentColor = Color.White
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                label = {
                    Text(text = screen.title)
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = "Navigation Icon"
                    )
                },
                selected = currentDestination?.hierarchy?.any {
                    it.route == screen.route
                } == true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = Color.White.copy(alpha = 0.5f),
                    unselectedTextColor = Color.White.copy(alpha = 0.5f)
                ),
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    PokedaxTheme {
        BottomBar(navController = rememberNavController(), isLoggedIn = true)
    }
}
