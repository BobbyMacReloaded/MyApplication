package com.example.myapplication.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NetworkStatusBar(
    modifier: Modifier = Modifier,
    backGroundColor: Color,
    message:String,
    isConnected:Boolean
) {
    AnimatedVisibility(visible =isConnected,
        modifier = Modifier,
        enter = fadeIn()+ slideInVertically (animationSpec = tween(durationMillis = 600)){ h-> h  },
        exit = fadeOut()+ slideOutVertically(animationSpec = tween(durationMillis = 600)) {h-> h }
    ) {
        Box (
        modifier = Modifier
            .fillMaxWidth()
            .background(backGroundColor)
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ){
        Text(text = message,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall)
    }
    }
}














