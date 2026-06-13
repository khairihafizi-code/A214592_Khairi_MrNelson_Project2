package com.example.a214592_khairi_mrnelson_project2.ui.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.a214592_khairi_mrnelson_project2.R
import com.example.a214592_khairi_mrnelson_project2.ui.components.CommentCard
import com.example.a214592_khairi_mrnelson_project2.ui.components.SnopesClaimCard
import com.example.a214592_khairi_mrnelson_project2.ui.components.StatusBadge
import com.example.a214592_khairi_mrnelson_project2.viewmodel.VerifiNewsViewModel

@Composable
fun NewsDetailScreen(viewModel: VerifiNewsViewModel, navController: NavHostController) {
    val newsItem = viewModel.currentSelectedNews

    if (newsItem == null) {
        navController.navigateUp()
        return
    }

    var newCommentText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
            // LOGIK GAMBAR BESAR
            if (!newsItem.imageBase64.isNullOrEmpty()) {
                val imageBitmap = remember(newsItem.imageBase64) {
                    try {
                        val imageBytes = Base64.decode(newsItem.imageBase64, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)?.asImageBitmap()
                    } catch (e: Exception) { null }
                }
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "Gambar Penuh Bukti",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(painterResource(R.drawable.ic_launcher_background), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                }
            } else {
                AsyncImage(
                    model = newsItem.imageUrl ?: R.drawable.ic_launcher_background,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Butang Back
            IconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier.padding(16.dp).statusBarsPadding().background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(50))
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = newsItem.title, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 30.sp)
            Spacer(modifier = Modifier.height(16.dp))
            SnopesClaimCard(claim = newsItem.claim)
            Spacer(modifier = Modifier.height(16.dp))
            StatusBadge(status = newsItem.status)

            // LOCAL PERSISTENCE - Add to Room
            Button(
                onClick = { viewModel.addBookmark(newsItem) },
                modifier = Modifier.padding(top = 8.dp)
            ) { Text("Save Offline (Room)") }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "FAKTA SEBENAR", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = newsItem.description, fontSize = 16.sp, lineHeight = 24.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "RUANGAN KOMUNITI (${newsItem.comments.size} Komen)", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newCommentText,
                    onValueChange = { newCommentText = it },
                    placeholder = { Text("Kongsi pandangan anda...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (newCommentText.isNotBlank()) {
                        viewModel.submitComment(newsItem.id, newCommentText)
                        newCommentText = ""
                    }
                }) { Text("Hantar") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (newsItem.comments.isEmpty()) {
                Text(text = "Belum ada komen.", color = Color.Gray, fontSize = 14.sp)
            } else {
                newsItem.comments.forEach { comment ->
                    CommentCard(
                        comment = comment,
                        onLikeClick = { viewModel.toggleLikeComment(newsItem.id, comment.id) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}