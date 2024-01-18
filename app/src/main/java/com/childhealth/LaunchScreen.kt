package com.childhealth

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.childhealth.destinations.ContentScreenDestination
import com.childhealth.ui.theme.ChildHealthTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay


@Composable
@Destination
@RootNavGraph(start = true)
fun LaunchScreen(navigator: DestinationsNavigator) {
    var isActive by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        delay(4000)
        isActive = true
    }

    if (isActive) {
        IntroScreen(onContinue = { navigator.navigate(ContentScreenDestination()) })
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
                val isCompact = LocalConfiguration.current.screenWidthDp < 600
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
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(id = R.drawable.hands),
            contentDescription = null,
            modifier = Modifier
                .padding(20.dp)
                .clip(RoundedCornerShape(30.dp)),
            contentScale = ContentScale.Inside
        )

        Text("When to Seek Help",
            fontSize = 18.sp,
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
                //              .size(200.dp)
                .padding(20.dp)
                .clip(RoundedCornerShape(30.dp)),
            contentScale = ContentScale.Inside
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
        Text("When to Seek Help", fontSize = 32.sp)

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
                Text("Child Health 2-60 Months")

                Image(
                    painter = painterResource(id = R.drawable.hands),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(30.dp)),
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Developed by: Dr. Pieter Jooste")

                Image(
                    painter = painterResource(id = R.drawable.developer),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(30.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LaunchScreenPreview() {
    ChildHealthTheme {
        DestinationsNavHost(navGraph = NavGraphs.root)
    }
}