package com.childhealth


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
import androidx.lifecycle.ViewModel
import com.childhealth.destinations.SheetScreenDestination
import com.childhealth.extensions.getResourceIdentifier
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class ScreenViewModel: ViewModel() {

    fun getColorFromName(colorName: String): Color {
        return when (colorName.lowercase()) {
            "clear" -> Color.Transparent
            "black" -> Color.Black
            "white" -> Color.White
            "gray" -> Color.Gray
            "red" -> Color.Red
            "green" -> Color(0xFF4CD964)
            "blue" -> Color(0xFF007AFF)
            "orange" -> Color(0xFFFFA500)
            "yellow" -> Color.Yellow
            "pink" -> Color(0xFFFFC0CB)
            "purple" -> Color.Magenta
            "primary" -> Color(0xFF6200EE) // Your primary color's hex value
            "secondary" -> Color(0xFF03DAC5) // Your secondary color's hex value
            else -> Color.White
        }
    }

    @Composable
    fun TypeBuilder(
        navigator: DestinationsNavigator,
        id: Int,
        content: String,
        type: String,
        linkUrl: String?,
        sheet: Sheet?,
        onLinkClick: (link: String) -> Unit
    ) {
        when (type) {
            TypeSelected.IMAGE.stringValue -> {
                Image(
                    painter = painterResource(id = content.getResourceIdentifier(LocalContext.current)),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }

            TypeSelected.TITLE_TEXT.stringValue -> {
                Text(
                    text = content,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }

            TypeSelected.RED_TEXT.stringValue -> {
                Text(
                    text = content,
                    modifier = Modifier
                        .background(
                            Color.Red,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                )
            }

            TypeSelected.GREEN_TEXT.stringValue -> {
                Text(
                    text = content,
                    modifier = Modifier
                        .background(
                            Color(0xFF4CD964),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                )
            }

            TypeSelected.LINK.stringValue -> {
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
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                )
            }

            TypeSelected.BUTTON.stringValue -> {
                sheet?.let {
                    Button(
                        onClick = {
                            navigator.navigate(
                                SheetScreenDestination(
                                    it
                                )
                            )
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

            TypeSelected.TEXT.stringValue -> {
                Text(
                    text = "☉ $content",
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}

