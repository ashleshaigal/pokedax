package com.aigal.pokedax.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aigal.pokedax.ui.auth.AuthViewModel
import com.aigal.pokedax.ui.components.CommonTopBar
import com.aigal.pokedax.ui.theme.PokedaxTheme

@Composable
fun EditProfileScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    val user by viewModel.user.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    EditProfileContent(
        initialDisplayName = user?.displayName ?: "",
        initialPhotoUrl = user?.photoUrl?.toString() ?: "",
        isLoading = isLoading,
        onBackClick = { navController.popBackStack() },
        onSaveClick = { name, url ->
            isLoading = true
            viewModel.updateProfile(name, url) { success ->
                isLoading = false
                if (success) {
                    navController.popBackStack()
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    initialDisplayName: String,
    initialPhotoUrl: String,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onSaveClick: (String, String) -> Unit
) {
    var displayName by remember { mutableStateOf(initialDisplayName) }
    var photoUrl by remember { mutableStateOf(initialPhotoUrl) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CommonTopBar(
                title = "Edit Profile",
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Display Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = photoUrl,
                onValueChange = { photoUrl = it },
                label = { Text("Profile Picture URL") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { onSaveClick(displayName, photoUrl) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    PokedaxTheme {
        EditProfileContent(
            initialDisplayName = "John Doe",
            initialPhotoUrl = "",
            isLoading = false,
            onBackClick = {},
            onSaveClick = { _, _ -> }
        )
    }
}
