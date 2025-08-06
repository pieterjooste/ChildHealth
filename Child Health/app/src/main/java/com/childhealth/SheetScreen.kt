package com.childhealth

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.core.net.toUri
//import com.childhealth.models.Sheet
//import com.childhealth.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun SheetScreen(
    navController: NavHostController,
    sheet: Sheet,
    viewModel: AppViewModel,
    parentTopicId: String
) {

    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(sheet.title,
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
                    Text(text = "Back to Topic",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    )
                }
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    color = Color.Gray,
                    shape = RoundedCornerShape(10.dp)
                )
                .verticalScroll(rememberScrollState())
        ) {

            sheet.sheetContent.forEach { section ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        viewModel.TypeBuilder(
                            //id = section.id,
                            content = section.content,
                            type = section.type,
                            linkUrl = section.linkUrl,
                            sheet = section.sheet,
                            navController = navController,
                            parentTopicId = parentTopicId,
                            onLinkClick = { link ->
//                                    val intent = Intent(Intent.ACTION_VIEW, link.toUri())
//                                    (context as? ComponentActivity)?.startActivity(intent)
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
