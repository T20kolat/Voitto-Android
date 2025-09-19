package com.voitto.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.voitto.R
import com.voitto.ui.cashflow.CashFlowScreen
import com.voitto.ui.tips.TipsScreen
import com.voitto.ui.resources.ResourcesScreen
import com.voitto.ui.HomeScreen
import com.voitto.ui.theme.VoittoTheme

private enum class Dest(val route: String, val labelRes: Int, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Home("home", R.string.nav_home, Icons.Default.Home),
    CashFlow("cashflow", R.string.nav_cashflow, Icons.Default.TrendingUp),
    Tips("tips", R.string.nav_tips, Icons.Default.Lightbulb),
    Resources("resources", R.string.nav_resources, Icons.Default.Info)
}

@Composable
fun VoittoApp() {
    VoittoTheme {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    listOf(Dest.Home, Dest.CashFlow, Dest.Tips, Dest.Resources).forEach { dest ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true,
                            onClick = { navController.navigate(dest.route) },
                            icon = { 
                                Icon(
                                    imageVector = dest.icon, 
                                    contentDescription = stringResource(id = dest.labelRes)
                                ) 
                            },
                            label = { Text(text = stringResource(id = dest.labelRes)) }
                        )
                    }
                }
            }
        ) { padding ->
            NavHost(navController = navController, startDestination = Dest.Home.route) {
                composable(Dest.Home.route) { HomeScreen(contentPadding = padding) }
                composable(Dest.CashFlow.route) { CashFlowScreen() }
                composable(Dest.Tips.route) { TipsScreen(contentPadding = padding) }
                composable(Dest.Resources.route) { ResourcesScreen(contentPadding = padding) }
            }
        }
    }
}

