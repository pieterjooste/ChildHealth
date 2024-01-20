package com.childhealth

import android.content.res.AssetManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.childhealth.destinations.SendEmailScreenDestination
import com.childhealth.destinations.ShareScreenDestination
import com.childhealth.destinations.TopicScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


fun AssetManager.readFile(fileName : String): String = open(fileName).bufferedReader().use{it.readText()}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun ContentScreen(navigator: DestinationsNavigator) {

    val context = LocalContext.current

    val jsonString =  context.assets.readFile("childhealth.json")
    val topics: List<TopicItem> = Json.decodeFromString(jsonString)

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("When To Seek Help")
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = {
                        navigator.navigate(
                            ShareScreenDestination()
                        )
                    }) {
                        Icon(Icons.Filled.Share, contentDescription = "Share app", tint = Color(0xFF007AFF))
                    }
                    IconButton(onClick = {
                        navigator.navigate(
                            SendEmailScreenDestination()
                        )
                    }) {
                        Icon(Icons.Filled.Email, contentDescription = "Email developer", tint = Color(0xFF007AFF))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Child Health 2-60 Months",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
                    .padding(10.dp),
                textAlign = TextAlign.Center
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .background(Color.LightGray),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)

            ) {
                items(topics) {
                        topic ->
                    Card (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        elevation = CardDefaults.cardElevation(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF007AFF)
                        ),
                        onClick = {
                            navigator.navigate(
                                TopicScreenDestination(
                                    topic
                                )
                            )
                        }
                    ) {
                        Text (
                            text = topic.name,
                            textAlign = TextAlign.Center,
//                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp)
                        )
                    }
                }
            }
        }
    }
}
