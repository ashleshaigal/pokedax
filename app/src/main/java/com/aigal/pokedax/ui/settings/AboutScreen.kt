package com.aigal.pokedax.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aigal.pokedax.ui.components.CommonTopBar

@Composable
fun AboutScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CommonTopBar(title = "About", showBackButton = true, onBackClick = { navController.popBackStack() })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Social Links
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SocialButton("Like us on Facebook", Icons.Default.Facebook, Color(0xFF1877F2)) { /* TODO */ }
                        SocialButton("Follow us on Twitter", Icons.Default.Chat, Color(0xFF1DA1F2)) { /* TODO */ }
                        SocialButton("Follow us on Instagram", Icons.Default.CameraAlt, Color(0xFFE4405F)) { /* TODO */ }
                    }
                }
            }

            // Legal Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Legal", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF455A64))
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "By using dataDex, you confirm that you have read the Privacy Policy and have agreed to the Terms of Service. Read again by tapping the buttons below.",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        OutlinedButton(
                            onClick = { /* TODO */ },
                            modifier = Modifier.fillMaxWidth(),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFB0BEC5)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("PRIVACY POLICY", color = Color.Gray)
                        }
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { /* TODO */ },
                            modifier = Modifier.fillMaxWidth(),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFB0BEC5)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("TERMS OF SERVICE", color = Color.Gray)
                        }
                    }
                }
            }

            // Disclaimer Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Disclaimer", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF455A64))
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "dataDex is an unofficial, free fan made app and is NOT affiliated, endorsed or supported by Nintendo, GAME FREAK or The Pokémon company in any way.\n" +
                            "Some images used in this app are copyrighted and are supported under fair use.\n" +
                            "Pokémon and Pokémon character names are trademarks of Nintendo.\n" +
                            "No copyright infringement intended.",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Start
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Pokémon © 2002-2022 Pokémon.\n" +
                            "© 1995-2022 Nintendo/Creatures Inc./GAME FREAK Inc.",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SocialButton(label: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        border = androidx.compose.foundation.BorderStroke(1.dp, color),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(Modifier.weight(1f))
        Text(label, color = color, textAlign = TextAlign.Center)
        Spacer(Modifier.weight(1f))
    }
}
