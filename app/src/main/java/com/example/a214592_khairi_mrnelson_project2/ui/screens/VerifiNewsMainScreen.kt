package com.example.a214592_khairi_mrnelson_project2.ui.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.a214592_khairi_mrnelson_project2.R
import com.example.a214592_khairi_mrnelson_project2.model.NewsItemData
import com.example.a214592_khairi_mrnelson_project2.ui.components.StatusBadge
import com.example.a214592_khairi_mrnelson_project2.viewmodel.VerifiNewsViewModel

@Composable
fun VerifiNewsMainScreen(viewModel: VerifiNewsViewModel, navController: NavHostController) {
    val filteredNews = viewModel.allNews.filter {
        it.title.contains(viewModel.searchQuery, ignoreCase = true)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { HeaderSection(loggedInUser = viewModel.loggedInUser, navController = navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            SearchBarSection(searchQuery = viewModel.searchQuery) { viewModel.updateSearch(it) }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                if (viewModel.searchQuery.isEmpty()) {
                    item { FeaturedNewsSection() }
                }

                item {
                    Text(
                        text = "Semakan Fakta Terkini",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                }

                items(filteredNews) { item ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        NewsListItem(
                            item = item,
                            onReadMoreClick = {
                                viewModel.selectNews(item)
                                navController.navigate("Details")
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderSection(loggedInUser: String, navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.verifinews_logo), contentDescription = "Logo", modifier = Modifier.size(28.dp))
                    Text(text = "erifiNews", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                }
                Text(text = "A214592", fontSize = 10.sp)
            }
        },
        navigationIcon = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text("🏠 Home") }, onClick = { expanded = false; navController.navigate("Home") })
                DropdownMenuItem(text = { Text("👤 Profil") }, onClick = { expanded = false; navController.navigate("Profile") })
                DropdownMenuItem(text = { Text("🚨 Lapor Berita") }, onClick = { expanded = false; navController.navigate("Report") })
                DropdownMenuItem(text = { Text("📋 Senarai Laporan") }, onClick = { expanded = false; navController.navigate("ReportList") })
                DropdownMenuItem(text = { Text("🤖 AI Fact-Checker") }, onClick = { expanded = false; navController.navigate("AIScreen") })
                DropdownMenuItem(text = { Text("🔖 Penanda Buku") }, onClick = { expanded = false; navController.navigate("Bookmarks") })
            }
        },
        actions = {
            if (loggedInUser.isNotEmpty()) {
                Text(text = loggedInUser, modifier = Modifier.padding(end = 12.dp), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            } else {
                IconButton(onClick = { navController.navigate("Profile") }) {
                    Icon(Icons.Default.Person, contentDescription = "Login")
                }
            }
        }
    )
}

@Composable
fun SearchBarSection(searchQuery: String, onSearchQueryChange: (String) -> Unit) {
    Surface(color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text(text = stringResource(R.string.search_cari_berita), color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)) },
            modifier = Modifier.fillMaxWidth().padding(16.dp).clip(RoundedCornerShape(30.dp)),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@Composable
fun FeaturedNewsSection() {
    Card(modifier = Modifier.fillMaxWidth().height(200.dp).padding(20.dp), shape = RoundedCornerShape(20.dp)) {
        Box {
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black))))
            Text(text = "AWAS: Amaran Berita Palsu", color = Color.White, modifier = Modifier.align(Alignment.BottomStart).padding(16.dp), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun NewsListItem(item: NewsItemData, onReadMoreClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).animateContentSize(spring(dampingRatio = Spring.DampingRatioLowBouncy)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                // LOGIK BARU: Tentukan sama ada gambar ini dari API (URL) atau Firebase (Base64)
                if (!item.imageBase64.isNullOrEmpty()) {
                    val imageBitmap = remember(item.imageBase64) {
                        try {
                            val imageBytes = Base64.decode(item.imageBase64, Base64.DEFAULT)
                            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)?.asImageBitmap()
                        } catch (e: Exception) { null }
                    }

                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "Gambar Bukti",
                            modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(painterResource(R.drawable.ic_launcher_background), contentDescription = null, modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
                    }
                } else {
                    AsyncImage(
                        model = item.imageUrl ?: R.drawable.ic_launcher_background,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    StatusBadge(status = item.status)
                    Text(text = item.title, style = MaterialTheme.typography.titleMedium, maxLines = if (expanded) 10 else 2, overflow = TextOverflow.Ellipsis)
                }
            }

            if (expanded) {
                Text(text = item.description, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onReadMoreClick) { Text("BACA LANJUT") }
                }
            } else {
                TextButton(onClick = { expanded = true }, modifier = Modifier.align(Alignment.End)) { Text("Lihat Ringkasan", fontSize = 12.sp) }
            }
        }
    }
}