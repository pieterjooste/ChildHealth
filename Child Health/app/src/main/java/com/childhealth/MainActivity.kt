package com.childhealth

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.childhealth.ui.theme.ChildHealthTheme
import com.childhealth.viewmodel.AppViewModel
import com.childhealth.views.ContentRoute
import com.childhealth.views.ContentScreen
import com.childhealth.views.LaunchRoute
import com.childhealth.views.LaunchScreen
import com.childhealth.views.SheetRoute
import com.childhealth.views.SheetScreen
import com.childhealth.views.TopicRoute
import com.childhealth.views.TopicScreen

//import com.childhealth.utils.theme.CildHealthTheme
//import com.childhealth.viewmodel.AppViewModel
//import com.childhealth.views.ContentRoute
//import com.childhealth.views.ContentScreen
//import com.childhealth.views.LaunchRoute
//import com.childhealth.views.LaunchScreen
//import com.childhealth.views.SheetRoute
//import com.childhealth.views.SheetScreen
//import com.childhealth.views.TopicRoute
//import com.childhealth.views.TopicScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            ChildHealthTheme {

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val appViewModel: AppViewModel = viewModel()

                    // Trigger data loading once
                    LaunchedEffect(Unit) {
                        appViewModel.loadTopics(applicationContext)
                    }
                    AppNavigator(navController = rememberNavController(), appViewModel = appViewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigator(navController: NavHostController, appViewModel: AppViewModel) {

    NavHost(
        navController = navController,
        startDestination = LaunchRoute
    ) {

        composable<LaunchRoute> {
            LaunchScreen(navController = navController)
        }

//        composable<IntroRoute> {
//            IntroScreen(
//                onContinue = {
//                    navController.navigate(ContentRoute) {
//                        popUpTo(IntroRoute) { inclusive = true }
//                        launchSingleTop = true
//                    }
//                }
//            )
//        }

        composable<ContentRoute> {
            ContentScreen(navController = navController, appViewModel = appViewModel)
        }

        composable<TopicRoute> { backStackEntry ->
            val routeArgs = try {
                backStackEntry.toRoute<TopicRoute>()
            } catch (e: IllegalArgumentException) {
                Log.e("AppNavigator", "TopicRoute: Failed to parse arguments.", e)
                navController.popBackStack()
                return@composable
            }
            val topicId = routeArgs.name // topicId will not be null if toRoute succeeded

            // Log the topicId you're trying to find
            Log.d("AppNavigator", "TopicRoute: Attempting to find topic with ID = '$topicId'")

            // Collect the TopicItem from the ViewModel
            val topicItem by appViewModel.getTopicById(topicId).collectAsState(initial = null)
            val isLoadingTopics by appViewModel.isLoading.collectAsState()

            Log.d("AppNavigator", "TopicRoute: For ID '$topicId', topicItem is null: ${topicItem == null}, isLoading: $isLoadingTopics")


            if (topicItem != null) {
                Log.d("AppNavigator", "TopicRoute: TopicItem found for ID '$topicId'. Name: '${topicItem?.name}'. Navigating to TopicScreen.")
                TopicScreen(
                    navController = navController,
                    viewModel = appViewModel,
                    topic = topicItem!! // Now this is safe because of the 'topicItem != null' check
                )
            } else {
                // topicItem is null, so display loading or not found state
                Log.w("AppNavigator", "TopicRoute: TopicItem is NULL for ID '$topicId'. Displaying loading/not found indicator.")
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        if (isLoadingTopics) { // Optional: Check global loading state
                            Text("Loading topic details...")
                        } else {
                            // If not globally loading and topicItem is still null, it might be an invalid ID or still processing
                            Text("Topic '$topicId' not found or still loading.")
                            Log.e("AppNavigator", "TopicRoute: Topic '$topicId' still not found (or data not ready).")
                        }
                    }
                }
            }
        }

        composable<SheetRoute> { backStackEntry ->
            val routeArgs = try {
                backStackEntry.toRoute<SheetRoute>()
            } catch (e: IllegalArgumentException) {
                Log.e("AppNavigator", "SheetRoute: Failed to parse arguments.", e)
                navController.popBackStack()
                return@composable
            }
            val parentTopicId = routeArgs.name
            val sheetTitle = routeArgs.title // This comes from your SheetRoute definition

            Log.d("AppNavigator", "SheetRoute: Attempting to find sheet with parentTopicId='$parentTopicId', sheetTitle='$sheetTitle'")

            // Observe the specific sheet from the ViewModel
            val sheet by appViewModel.getSheetByTitle(parentTopicId, sheetTitle).collectAsState(initial = null)

            Log.d("AppNavigator", "SheetRoute: For parentId='$parentTopicId', title='$sheetTitle', sheet is null: ${sheet == null}")


            if (sheet != null) {
                Log.d("AppNavigator", "SheetRoute: Sheet found for title '$sheetTitle' in topic '$parentTopicId'. Navigating to SheetScreen.")
                SheetScreen(
                    navController = navController,
                    viewModel = appViewModel,
                    sheet = sheet!!,
                    parentTopicId = parentTopicId
                )
            } else {
                // sheet is null, so display loading or not found state
                Log.w("AppNavigator", "SheetRoute: Sheet is NULL for title '$sheetTitle' in topic '$parentTopicId'. Displaying loading/not found indicator.")
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        // if (isLoadingSheet) { // Optional: Check sheet specific loading state
                        // Text("Loading sheet details...")
                        // } else {
                        Text("Sheet '$sheetTitle' not found or still loading.")
                        Log.e("AppNavigator", "SheetRoute: Sheet '$sheetTitle' in topic '$parentTopicId' still not found (or data not ready).")
                        // }
                    }
                }
            }
        }
    }
}

// --- Helper functions for navigation ---

fun NavHostController.navigateToTopic(topicId: String) {
    this.navigate(TopicRoute(name = topicId))
}

// Updated to take sheetTitle
fun NavHostController.navigateToSheet(topicId: String, sheetTitle: String) {
    this.navigate(SheetRoute(name = topicId, title = sheetTitle))
}


