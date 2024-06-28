package com.example.blinkexampleapp.blink

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.blinkexampleapp.action.ActionError
import com.example.blinkexampleapp.action.ActionLinks
import com.example.blinkexampleapp.action.LinkedAction
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch
import java.net.URL


@Composable
fun Blink(
    icon: String,
    title: String,
    description: String,
    label: String,
    url: String,
    disabled: Boolean? = null,
    links: ActionLinks? = null,
    error: ActionError? = null
) {
    Column(
        modifier = Modifier.shadow(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Image section with black background
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp, 18.dp, 0.dp, 0.dp))
                .background(Color.Black)
                .padding(vertical = 6.dp)
        ) {
            val imageHeight = maxWidth * 0.75f

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
            ) {
                AsyncImage(
                    model = icon,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(18.dp))
                )
            }
        }

        // Title + description + buttons section with lighter background
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(0.dp, 0.dp, 18.dp, 18.dp))
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Link Icon",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(end = 6.dp).size(12.dp)
                )
                Text(text = URL(url).host, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 4.dp)
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )


                // Non Parameter Actions
                NonParameterizedActionsRow(actions = links?.actions ?: listOf(LinkedAction(href = "", label)), disabled = disabled ?: false)

                if (links?.actions != null) {
                    // Parameterized Actions with user input
                    ParameterizedActionsRow(actions = links.actions, disabled = disabled ?: false)
                }


            if (error != null) {
                Text(
                    text = error.message,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun ActionButton(
    label: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = {  },
        enabled = enabled,
        modifier = modifier // Apply the passed modifier
    ) {
        Text(text = label)
    }
}


@Composable
fun NonParameterizedActionsRow(actions: List<LinkedAction>, disabled: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val nonParameterizedActions = actions.filter { it.parameters == null } // Filter actions that don't have a .parameter field
        nonParameterizedActions.forEachIndexed { index, linkedAction ->
            ActionButton(
                label = linkedAction.label,
                enabled = !disabled,
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = if (index == 0) 0.dp else 4.dp,
                        end = if (index == nonParameterizedActions.lastIndex) 0.dp else 4.dp
                    ) // Conditional padding
            )
        }
    }
}

@Composable
fun ParameterizedActionsRow(actions: List<LinkedAction>, disabled: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val parameterizedActions = actions.filter { it.parameters != null } // Filter actions that don't have a .parameter field
        parameterizedActions.forEach { linkedAction ->
            var userInput by remember { mutableStateOf("") }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    placeholder = { Text(text = linkedAction.parameters?.first()?.label ?: "") },
                    modifier = Modifier.weight(0.6f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                Button(
                    onClick = { /* Handle the action with userInput */ },
                    enabled = !disabled,
                    modifier = Modifier
                        .weight(0.35f)
                ) {
                    Text(text = linkedAction.label)
                }
            }
        }
    }
}
