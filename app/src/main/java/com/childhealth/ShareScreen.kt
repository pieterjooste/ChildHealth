package com.childhealth

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import com.childhealth.destinations.ContentScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable

fun ShareScreen(navigator: DestinationsNavigator) {

    val context = LocalContext.current
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Share Child Health App")
        putExtra(
            Intent.EXTRA_TEXT,
            "https://play.google.com/store/apps/details?id=com.childhealth&pcampaignid=web_share"
        )

    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(context, shareIntent, null)
    navigator.navigate(
        ContentScreenDestination()
    )
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
        putExtra(Intent.EXTRA_TEXT,
            "Comments or suggestions to doctor@childhealthforall.com on how we can improve the App are welcome:"
        )
    }
    if(mailIntent.resolveActivity(packageManager) != null) {
        val shareMailIntent = Intent.createChooser(mailIntent, null)
        startActivity(context, shareMailIntent, null)
        navigator.navigate(
            ContentScreenDestination()
        )
    } else {
        navigator.navigate(
            ContentScreenDestination()
        )
    }
}
