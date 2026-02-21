package com.childhealth.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalDensity
import com.childhealth.R

@Composable
fun LaunchScreen(navController: NavHostController) {
    var isActive by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        delay(4000)
        isActive = true
    }

    if (isActive) {
        navController.navigate(ContentRoute)
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF007AFF))
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                val isCompact = LocalWindowInfo.current.containerSize.width < with(LocalDensity.current) { 600.dp.toPx() }
                if (isCompact) {
                    CompactLayout()
                } else {
                    ExpandedLayout()
                }
            }
        }
    }
}

@Composable
fun CompactLayout() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Child Health 2 - 60 Months",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .fillMaxWidth()
        )

        Image(
            painter = painterResource(id = R.drawable.hands),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(20.dp)
                .clip(RoundedCornerShape(30.dp))
        )

        Text("When to Seek Help",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text("Developed by:")
        Text("Dr. Pieter Jooste")

        Image(
            painter = painterResource(id = R.drawable.developer),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(20.dp)
                .clip(RoundedCornerShape(30.dp))
        )
    }
}

@Composable
fun ExpandedLayout() {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("When to Seek Help",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Child Health 2-60 Months",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )

                Image(
                    painter = painterResource(id = R.drawable.hands),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(30.dp))
                )
            }

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Developed by:")
                Text("Dr. Pieter Jooste")

                Image(
                    painter = painterResource(id = R.drawable.developer),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(30.dp))
                )
            }
        }
    }
}