package com.aigal.pokedax.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val _user = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val user = _user.asStateFlow()

    init {
        auth.addAuthStateListener {
            _user.value = it.currentUser
            it.currentUser?.let { user ->
                syncUserToFirestore(user)
            }
        }
    }

    private fun syncUserToFirestore(user: FirebaseUser) {
        val userMap = hashMapOf(
            "uid" to user.uid,
            "displayName" to user.displayName,
            "email" to user.email,
            "photoUrl" to user.photoUrl?.toString(),
            "lastLogin" to com.google.firebase.Timestamp.now()
        )

        firestore.collection("users").document(user.uid)
            .set(userMap, com.google.firebase.firestore.SetOptions.merge())
    }

    fun signOut() {
        auth.signOut()
    }

    fun updateProfile(displayName: String, photoUrl: String, onComplete: (Boolean) -> Unit) {
        val user = auth.currentUser
        val profileUpdates = com.google.firebase.auth.userProfileChangeRequest {
            this.displayName = displayName
            this.photoUri = android.net.Uri.parse(photoUrl)
        }

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.value = auth.currentUser
                    auth.currentUser?.let { syncUserToFirestore(it) }
                }
                onComplete(task.isSuccessful)
            }
    }
}
