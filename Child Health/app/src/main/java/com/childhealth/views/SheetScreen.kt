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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.core.net.toUri
import com.childhealth.models.Sheet
import com.google.android.play.core.review.ReviewManagerFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetScreen(
    navController: NavHostController,
    sheet: Sheet,
    parentTopicId: String
) {

    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val activity = context as? ComponentActivity

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
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
                    IconButton(
                        onClick = {
                            if (activity != null) {
                                val reviewManager = ReviewManagerFactory.create(activity)
                                val request = reviewManager.requestReviewFlow()
                                request.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val reviewInfo = task.result
                                        val flow = reviewManager.launchReviewFlow(activity, reviewInfo)
                                        flow.addOnCompleteListener { _ ->
                                            navController.popBackStack()
                                        }
                                    } else {
                                        navController.popBackStack()
                                    }
                                }
                            } else {
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Arrow Back",
                            modifier = Modifier.size(32.dp)
                        )
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
                .background(color = Color.LightGray) // Changed for better contrast
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Adaptive constraint: Limits width on large screens (API 36+)
            Column(
                modifier = Modifier
                    .widthIn(max = 800.dp)
                    .padding(8.dp)
                    .background(
                        color = Color.Gray,
                        shape = RoundedCornerShape(10.dp)
                    )
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
                            TypeBuilder(
                                content = section.content,
                                type = section.type,
                                linkUrl = section.linkUrl,
                                sheet = section.sheet,
                                navController = navController,
                                parentTopicId = parentTopicId,
                                onLinkClick = { link ->
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, link.toUri())
                                        if (intent.resolveActivity(context.packageManager) != null) {
                                            (context as? ComponentActivity)?.startActivity(intent)
                                        } else {
                                            Toast.makeText(context, "Cannot open external link: Internet connection?", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error opening external link: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
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
