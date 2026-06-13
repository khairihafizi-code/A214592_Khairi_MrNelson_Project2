package com.example.a214592_khairi_mrnelson_project2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatusBadge(status: String) {
    val (textColor, backgroundColor) = when {
        status.contains("SAHIH", ignoreCase = true) ->
            Pair(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
        status.contains("PALSU", ignoreCase = true) ->
            Pair(MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.errorContainer)
        else ->
            Pair(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondaryContainer)
    }

    Text(
        text = status,
        color = textColor,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp,
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
fun SnopesClaimCard(claim: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "DAKWAAN (CLAIM)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "\"$claim\"", fontSize = 16.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ---> NEW FEATURE: Reusable Comment Card with Like Button to prevent spaghetti code <---
@Composable
fun CommentCard(comment: com.example.a214592_khairi_mrnelson_project2.model.CommentData, onLikeClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "User",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = comment.username, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = comment.text, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            // Like Button Area
            IconButton(onClick = onLikeClick) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Like",
                        // Changes color to red if user liked it, else stays gray
                        tint = if (comment.isLikedByMe) MaterialTheme.colorScheme.error else Color.Gray
                    )
                    Text(text = "${comment.likes}", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}