package com.childhealth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IntroScreen(onContinue: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Important:",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF007AFF),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(bottom = 16.dp),
            )
            Text(
                text = "☉ Scroll down to bottom of page to CONTINUE.\n\n",
                modifier = Modifier
                    .padding(top = 16.dp)
            )

            Text(
                text = "☉ Privacy Policy: No personal information is collected by this app.\n\n"
            )

            Text(
                text ="☉ Disclaimer: These are guidelines only and not medical advice. Ill babies should be seen by a trained health worker.\n\n"
            )

            Text(
                text = """
                        ☉ These guidelines will not cover all conditions that may need medical help.
                            
                        ☉ Premature babies are not covered by these guidelines.
                            
                        ☉ All healthy babies should be seen by a health worker immediately after birth, 2-3 days after delivery and at 6 weeks for a routine assessment.
                           
                        ☉ The mother's mental as well as physical health is very important for a baby's health.   
                    """.trimIndent(),
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { onContinue() }
                ) {
                    Text("Continue")
                }
            }
        }
    }
}
