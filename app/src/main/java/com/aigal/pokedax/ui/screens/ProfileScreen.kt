package com.aigal.pokedax.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aigal.pokedax.ui.auth.AuthViewModel
import com.aigal.pokedax.ui.theme.PokedaxTheme
import com.aigal.pokedax.ui.theme.PokedexRed
import com.aigal.pokedax.util.Constants.PRIVACY_URL
import com.aigal.pokedax.util.Constants.TERMS_URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val user by authViewModel.user.collectAsState()
    
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionName = packageInfo.versionName
    
    var activeBottomSheet by remember { mutableStateOf<BottomSheetType?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 32.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Profile Header
            item {
                if (user != null) {
                    ProfileHeader(
                        name = user?.displayName ?: "No Name",
                        email = user?.email ?: "No Email",
                        onEditClick = { navController.navigate("edit_profile") },
                        onSignOutClick = { authViewModel.signOut() }
                    )
                } else {
                    LoginHeader(onLoginClick = {
                        navController.navigate("login")
                    })
                }
                HorizontalDivider()
            }

            // Settings List
            item {
                SettingsListItem(
                    icon = Icons.Default.Star,
                    title = "Rate the app",
                    onClick = { /* TODO */ }
                )
            }

            item {
                SettingsListItem(
                    icon = Icons.AutoMirrored.Filled.Help,
                    title = "Help & support",
                    onClick = { /* TODO */ }
                )
            }
            
            item {
                SettingsListItem(
                    icon = Icons.Default.Description,
                    title = "Terms of Service",
                    onClick = { 
                        val encodedUrl = URLEncoder.encode(TERMS_URL, StandardCharsets.UTF_8.toString())
                        val encodedTitle = URLEncoder.encode("Terms of Service", StandardCharsets.UTF_8.toString())
                        navController.navigate("webview/$encodedUrl/$encodedTitle")
                    }
                )
            }

            item {
                SettingsListItem(
                    icon = Icons.Default.Lock,
                    title = "Privacy Policy",
                    onClick = { 
                        val encodedUrl = URLEncoder.encode(PRIVACY_URL, StandardCharsets.UTF_8.toString())
                        val encodedTitle = URLEncoder.encode("Privacy Policy", StandardCharsets.UTF_8.toString())
                        navController.navigate("webview/$encodedUrl/$encodedTitle")
                    }
                )
            }

            item {
                SettingsListItem(
                    icon = Icons.Default.Info,
                    title = "Disclaimer",
                    onClick = { activeBottomSheet = BottomSheetType.DISCLAIMER }
                )
            }

            item {
                SettingsListItem(
                    icon = Icons.Default.Copyright,
                    title = "Content and copyright",
                    onClick = { activeBottomSheet = BottomSheetType.COPYRIGHT }
                )
            }

            item {
                SettingsListItem(
                    icon = Icons.Default.Share,
                    title = "Social",
                    onClick = { activeBottomSheet = BottomSheetType.SOCIAL }
                )
            }

            // Footer
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Version $versionName",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }

    if (activeBottomSheet != null) {
        ModalBottomSheet(
            onDismissRequest = { activeBottomSheet = null },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            containerColor = Color.White
        ) {
            when (activeBottomSheet) {
                BottomSheetType.DISCLAIMER -> {
                    CommonBottomSheet(title = "Disclaimer", onClose = { activeBottomSheet = null }) {
                        Text(
                            "dataDex is an unofficial, free fan made app and is NOT affiliated, endorsed or supported by Nintendo, GAME FREAK or The Pokémon company in any way.\n\n" +
                            "Some images used in this app are copyrighted and are supported under fair use.\n\n" +
                            "Pokémon and Pokémon character names are trademarks of Nintendo.\n\n" +
                            "No copyright infringement intended.\n\n" +
                            "Pokémon © 2002-2022 Pokémon.\n\n" +
                            "© 1995-2022 Nintendo/Creatures Inc./GAME FREAK Inc.",
                            fontSize = 14.sp
                        )
                    }
                }
                BottomSheetType.COPYRIGHT -> {
                    CommonBottomSheet(title = "Content and copyright", onClose = { activeBottomSheet = null }) {
                        Column {
                            Text(
                                "This is an unofficial, fan-made, and free-to-use app that contains detailed info about Pokémon, games, moves, abilities, location, and much more.",
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Arts, visuals and names are properties of Game Freak, Nintendo and The Pokémon Company. This app is not official and is not linked to the companies mentioned above.",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                BottomSheetType.SOCIAL -> {
                    CommonBottomSheet(title = "Social", onClose = { activeBottomSheet = null }) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SocialButton("Like us on Facebook", Icons.Default.Facebook, Color(0xFF1877F2)) { /* TODO */ }
                            SocialButton("Follow us on Twitter", Icons.AutoMirrored.Filled.Send, Color(0xFF1DA1F2)) { /* TODO */ }
                            SocialButton("Follow us on Instagram", Icons.Default.CameraAlt, Color(0xFFE4405F)) { /* TODO */ }
                        }
                    }
                }
                null -> {}
            }
        }
    }
}

enum class BottomSheetType {
    DISCLAIMER, COPYRIGHT, SOCIAL
}

@Composable
fun CommonBottomSheet(
    title: String,
    onClose: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
        
        content()
        
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Close, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("CLOSE", fontWeight = FontWeight.Bold)
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

@Composable
fun ProfileHeader(name: String, email: String, onEditClick: () -> Unit, onSignOutClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.take(1).uppercase(),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = email, fontSize = 14.sp, color = Color.Gray)
        }
        IconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
        }
        IconButton(onClick = onSignOutClick) {
            Icon(Icons.Default.ExitToApp, contentDescription = "Sign Out")
        }
    }
}

@Composable
fun LoginHeader(onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome Guest", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Sign in to sync your data across devices.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(containerColor = PokedexRed)
        ) {
            Text("Sign In")
        }
    }
}

@Composable
fun SettingsListItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(16.dp))
        Column {
            Text(text = title, fontSize = 16.sp)
            if (subtitle != null) {
                Text(text = subtitle, fontSize = 14.sp, color = Color.Gray)
            }
        }
        Spacer(Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    PokedaxTheme {
        // Mock content for preview
    }
}
