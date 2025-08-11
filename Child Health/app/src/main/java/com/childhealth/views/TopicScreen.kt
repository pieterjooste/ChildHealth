package com.childhealth.views

import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.core.net.toUri
import com.childhealth.viewmodel.AppViewModel
import com.childhealth.models.TopicItem

//import com.childhealth.models.TopicItem
//import com.childhealth.viewmodel.AppViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun TopicScreen(
    navController: NavHostController,
    topic: TopicItem,
    viewModel: AppViewModel
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(topic.name,
                        style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Arrow Back")
                    }
                    Text(text = "Back to Topics",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            topic.sections.forEach { section ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(
                            color = viewModel.getColorFromName(section.background),
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        section.content.forEach { content ->
                            viewModel.TypeBuilder(
                                //id = content.id,
                                parentTopicId = topic.id,
                                content = content.content,
                                type = content.type,
                                linkUrl = content.linkUrl,
                                sheet = content.sheet,
                                navController = navController,
                                onLinkClick = { link ->
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, link.toUri())
                                        if (intent.resolveActivity(context.packageManager) != null) {
                                            (context as? ComponentActivity)?.startActivity(intent)
                                        } else {
                                            Toast.makeText(context, "Cannot open external link: Internet connection?", Toast.LENGTH_SHORT).show()
//                                            Log.w("TopicScreen", "No activity found to handle ACTION_VIEW for URI: $link")
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error opening external link: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
//                                        Log.e("TopicScreen", "Error creating or starting intent for URI: $link", e)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
