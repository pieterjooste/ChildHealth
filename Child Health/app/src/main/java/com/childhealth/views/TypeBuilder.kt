package com.childhealth.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.childhealth.extensions.getResourceIdentifier
import com.childhealth.models.Sheet
import com.childhealth.navigateToSheet
import com.childhealth.ui.theme.getColorFromName
import com.childhealth.ui.theme.getOnColorFromName

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
    val selectedType = TypeSelected.entries.firstOrNull { it.stringValue == type }

    when (selectedType) {
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
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        TypeSelected.RED_TEXT -> {
            TextComponent(content, "red")
        }

        TypeSelected.GREEN_TEXT -> {
            TextComponent(content, "green")
        }

        TypeSelected.GRAY_TEXT -> {
            TextComponent(content, "gray")
        }

        TypeSelected.LINK -> {
            Surface(
                color = getColorFromName("blue"),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { onLinkClick(linkUrl ?: "") }
            ) {
                Text(
                    text = content,
                    color = getOnColorFromName("blue"),
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        TypeSelected.BUTTON -> {
            sheet?.let {
                Button(
                    onClick = {
                        navController.navigateToSheet(parentTopicId, sheet.title)
                    },
                    modifier = Modifier
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = getColorFromName("blue"),
                        contentColor = getOnColorFromName("blue")
                    )
                ) {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        TypeSelected.TEXT -> {
            Text(
                text = "☉ $content",
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        null -> {}
    }
}

@Composable
fun TextComponent(content: String, colorName: String) {
    Surface(
        color = getColorFromName(colorName),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = content,
            modifier = Modifier.padding(8.dp),
            color = getOnColorFromName(colorName),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
