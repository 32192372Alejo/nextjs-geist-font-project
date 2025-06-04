package com.example.interviewface.repository

import android.util.Log
import com.example.interviewface.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

/**
 * Repositorio para gestionar la autenticación y las operaciones con usuarios
 */
class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")
    
    companion object {
        private const val TAG = "AuthRepository"
    }
    
    /**
     * Registra un nuevo usuario con email y contraseña
     */
    suspend fun registerUser(email: String, password: String, fullName: String, phoneNumber: String): Result<FirebaseUser> {
        return try {
            Log.d(TAG, "Iniciando registro de usuario con email: $email")
            
            // Paso 1: Crear usuario en Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            
            if (firebaseUser != null) {
                Log.d(TAG, "Usuario creado en Firebase Auth con ID: ${firebaseUser.uid}")
                
                // Paso 2: Crear documento de usuario en Firestore
                val user = User(
                    id = firebaseUser.uid,
                    email = email,
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    createdAt = System.currentTimeMillis()
                )
                
                try {
                    // Guardar usuario en Firestore
                    Log.d(TAG, "Guardando datos de usuario en Firestore...")
                    usersCollection.document(firebaseUser.uid).set(user.toMap()).await()
                    Log.d(TAG, "Datos de usuario guardados exitosamente en Firestore")
                } catch (firestoreException: Exception) {
                    // Si falla al guardar en Firestore, registramos el error pero continuamos
                    Log.e(TAG, "Error al guardar datos en Firestore, pero el usuario fue creado en Auth", firestoreException)
                }
                
                // Siempre devolvemos éxito si el usuario fue creado en Auth, incluso si Firestore falló
                return Result.success(firebaseUser)
            } else {
                Log.e(TAG, "Error: Firebase Auth devolvió un usuario nulo")
                Result.failure(Exception("Error al crear el usuario"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al registrar usuario", e)
            Result.failure(e)
        }
    }
    
    /**
     * Inicia sesión con email y contraseña
     */
    suspend fun loginUser(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            
            if (firebaseUser != null) {
                Result.success(firebaseUser)
            } else {
                Result.failure(Exception("Error al iniciar sesión"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al iniciar sesión", e)
            Result.failure(e)
        }
    }
    
    /**
     * Cierra la sesión del usuario actual
     */
    fun logoutUser() {
        auth.signOut()
    }
    
    /**
     * Obtiene el usuario actual
     */
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
    
    /**
     * Verifica si hay un usuario con sesión iniciada
     */
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
    
    /**
     * Obtiene los datos completos del usuario actual desde Firestore
     */
    suspend fun getUserData(userId: String): Result<User> {
        return try {
            val documentSnapshot = usersCollection.document(userId).get().await()
            if (documentSnapshot.exists()) {
                val user = documentSnapshot.toObject(User::class.java)
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("Error al convertir los datos del usuario"))
                }
            } else {
                Result.failure(Exception("No se encontró el usuario"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener datos del usuario", e)
            Result.failure(e)
        }
    }
    
    /**
     * Envía un correo para restablecer la contraseña
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error al enviar correo de restablecimiento", e)
            Result.failure(e)
        }
    }
}
