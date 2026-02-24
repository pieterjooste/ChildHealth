package com.childhealth.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.childhealth.extensions.getResourceIdentifier
import com.childhealth.models.Sheet
import com.childhealth.navigateToSheet

enum class TypeSelected(val stringValue: String) {
    IMAGE("image"),
    TITLE_TEXT("titletext"),
    RED_TEXT("redtext"),
    GREEN_TEXT("greentext"),
    GRAY_TEXT("graytext"),
    LINK("link"),
    BUTTON("button"),
    TEXT("text"),
}

@Composable
fun TypeBuilder(
    navController: NavHostController,
    parentTopicId: String,
    content: String,
    type: String,
    linkUrl: String?,
    sheet: Sheet?,
    onLinkClick: (link: String) -> Unit
) {
    when (TypeSelected.entries.firstOrNull { it.stringValue == type }) {
        TypeSelected.IMAGE -> {
            Image(
                painter = painterResource(id = content.getResourceIdentifier(LocalContext.current)),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(30.dp))
            )
        }

        TypeSelected.TITLE_TEXT -> {
            Text(
                text = content,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
        }

        TypeSelected.RED_TEXT -> {
            Text(
                text = content,
                modifier = Modifier
                    .background(
                        Color.Red,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                    )
                    .padding(8.dp)
            )
        }

        TypeSelected.GREEN_TEXT -> {
            Text(
                text = content,
                modifier = Modifier
                    .background(
                        Color.Green,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                    )
                    .padding(8.dp)
            )
        }

        TypeSelected.GRAY_TEXT -> {
            Text(
                text = content,
                modifier = Modifier
                    .background(
                        Color.Gray,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                    )
                    .padding(8.dp)
            )
        }

        TypeSelected.LINK -> {
            Text(
                text = content,
                color = Color.White,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable {
                        onLinkClick(linkUrl ?: "")
                    }
                    .background(
                        Color(0xFF007AFF),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                    )
                    .padding(8.dp)
            )
        }

        TypeSelected.BUTTON -> {
            sheet?.let {
                Button(
                    onClick = {
                        navController.navigateToSheet(parentTopicId, sheet.title)
                    },
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = content,
                        color = Color.White
                    )
                }
            }
        }

        TypeSelected.TEXT -> {
            Text(
                text = "☉ $content",
                modifier = Modifier
                    .padding(vertical = 4.dp)
            )
        }
        null -> {
            // Do nothing for unknown types
        }
    }
}