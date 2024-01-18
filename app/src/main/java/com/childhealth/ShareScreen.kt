package com.childhealth

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import com.childhealth.destinations.ContentScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable

fun ShareScreen() {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Share(text = "Download *Child Health 2-60 Months* from Google's PlayStore", context = LocalContext.current)
        }
    }
}



@Composable
fun Share(text: String, context: Context) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(context, shareIntent, null)
}

@Destination
@Composable
fun SendEmailScreen(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val mailIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_EMAIL, "doctor@childhealthforall.com")
        putExtra(Intent.EXTRA_SUBJECT, "Comments Child Health App")
        putExtra(Intent.EXTRA_TEXT, "Comments or suggestions to doctor@childhealthforall.com on how we can improve the App are welcome:")
    }
    if(mailIntent.resolveActivity(packageManager) != null) {
        val shareMailIntent = Intent.createChooser(mailIntent, null)
        startActivity(context, shareMailIntent, null)
    } else {
        navigator.navigate(
            ContentScreenDestination()
        )
    }
}
