package com.aigal.pokedax.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import com.aigal.pokedax.R
import com.aigal.pokedax.ui.theme.*
import com.aigal.pokedax.util.Constants.PRIVACY_URL
import com.aigal.pokedax.util.Constants.TERMS_URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onBackClick: () -> Unit = {},
    onNavigateToWeb: (String, String) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PokedexRed)
            .padding(24.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.TopStart).padding(top = 16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Pokédax",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(64.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                // Sign in with Google
                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            try {
                                val googleIdOption = GetGoogleIdOption.Builder()
                                    .setFilterByAuthorizedAccounts(false)
                                    .setServerClientId(context.getString(R.string.default_web_client_id))
                                    .setAutoSelectEnabled(false)
                                    .build()

                                val request = GetCredentialRequest.Builder()
                                    .addCredentialOption(googleIdOption)
                                    .build()

                                val result = credentialManager.getCredential(
                                    context = context,
                                    request = request
                                )

                                Log.d("LoginScreen", "Credential result: ${result.credential}")

                                val credential = result.credential
                                if (credential is GoogleIdTokenCredential) {
                                    val firebaseCredential = GoogleAuthProvider.getCredential(credential.idToken, null)
                                    FirebaseAuth.getInstance().signInWithCredential(firebaseCredential)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                onLoginSuccess()
                                            } else {
                                                Log.e("LoginScreen", "FirebaseAuth failed", task.exception)
                                                errorMessage = task.exception?.message ?: "Firebase Authentication failed"
                                                isLoading = false
                                            }
                                        }
                                } else if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                    try {
                                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                        val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                                        FirebaseAuth.getInstance().signInWithCredential(firebaseCredential)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    onLoginSuccess()
                                                } else {
                                                    Log.e("LoginScreen", "FirebaseAuth failed (fallback)", task.exception)
                                                    errorMessage = task.exception?.message ?: "Firebase Authentication failed"
                                                    isLoading = false
                                                }
                                            }
                                    } catch (e: Exception) {
                                        Log.e("LoginScreen", "Failed to parse Google ID Token fallback", e)
                                        errorMessage = "Failed to parse Google ID Token: ${e.message}"
                                        isLoading = false
                                    }
                                } else {
                                    Log.e("LoginScreen", "Unexpected credential type: ${credential.type}")
                                    errorMessage = "Unexpected credential type: ${credential.type}"
                                    isLoading = false
                                }
                            } catch (e: NoCredentialException) {
                                Log.e("LoginScreen", "NoCredentialException: ${e.message}", e)
                                errorMessage = "No Google accounts found."
                                isLoading = false
                            } catch (e: Exception) {
                                Log.e("LoginScreen", "Sign-in failed: ${e.message}", e)
                                errorMessage = "Sign-in failed: ${e.message}"
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google_logo),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Sign in with Google", color = Color.Gray)
                }
            }
        }

        // Bottom text
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "By continuing, you are indicating that you accept our",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            Row {
                Text(
                    "Terms of Service",
                    color = Color.White,
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { 
                        val encodedUrl = URLEncoder.encode(TERMS_URL, StandardCharsets.UTF_8.toString())
                        val encodedTitle = URLEncoder.encode("Terms of Service", StandardCharsets.UTF_8.toString())
                        onNavigateToWeb(encodedUrl, encodedTitle)
                    }
                )
                Text(" and ", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                Text(
                    "Privacy Policy",
                    color = Color.White,
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { 
                        val encodedUrl = URLEncoder.encode(PRIVACY_URL, StandardCharsets.UTF_8.toString())
                        val encodedTitle = URLEncoder.encode("Privacy Policy", StandardCharsets.UTF_8.toString())
                        onNavigateToWeb(encodedUrl, encodedTitle)
                    }
                )
                Text(".", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    PokedaxTheme {
        LoginScreen(onLoginSuccess = {})
    }
}
