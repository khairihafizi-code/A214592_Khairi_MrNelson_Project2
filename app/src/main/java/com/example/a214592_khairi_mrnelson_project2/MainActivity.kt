package com.example.a214592_khairi_mrnelson_project2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a214592_khairi_mrnelson_project2.ui.screens.BookmarkListScreen
import com.example.a214592_khairi_mrnelson_project2.ui.screens.FactCheckAIScreen
import com.example.a214592_khairi_mrnelson_project2.ui.screens.NewsDetailScreen
import com.example.a214592_khairi_mrnelson_project2.ui.screens.ProfileScreen
import com.example.a214592_khairi_mrnelson_project2.ui.screens.ReportListScreen
import com.example.a214592_khairi_mrnelson_project2.ui.screens.ReportScreen
import com.example.a214592_khairi_mrnelson_project2.ui.screens.VerifiNewsMainScreen
import com.example.a214592_khairi_mrnelson_project2.ui.theme.A214592_Khairi_MrNelson_Project2Theme
import com.example.a214592_khairi_mrnelson_project2.viewmodel.VerifiNewsViewModel

enum class VerifiNewsScreen { Home, Details, Profile, Report, ReportList, Bookmarks, AIScreen }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            A214592_Khairi_MrNelson_Project2Theme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    VerifiNewsApp()
                }
            }
        }
    }
}

@Composable
fun VerifiNewsApp(
    viewModel: VerifiNewsViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = VerifiNewsScreen.Home.name,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = VerifiNewsScreen.Home.name) { VerifiNewsMainScreen(viewModel, navController) }
        composable(route = VerifiNewsScreen.Profile.name) { ProfileScreen(viewModel, navController) }
        composable(route = VerifiNewsScreen.Details.name) { NewsDetailScreen(viewModel, navController) }
        composable(route = VerifiNewsScreen.Report.name) { ReportScreen(viewModel, navController) }
        composable(route = VerifiNewsScreen.ReportList.name) { ReportListScreen(viewModel, navController) }
        composable(route = VerifiNewsScreen.Bookmarks.name) { BookmarkListScreen(viewModel, navController) }
        composable(route = VerifiNewsScreen.AIScreen.name) { FactCheckAIScreen(viewModel, navController) }
    }
}