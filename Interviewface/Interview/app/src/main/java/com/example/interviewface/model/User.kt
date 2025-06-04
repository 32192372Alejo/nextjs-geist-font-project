package com.example.interviewface.model

/**
 * Modelo de datos para representar un usuario en la aplicación
 */
data class User(
    val id: String = "",
    val email: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val educationLevel: String = "",
    val specialty: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    // Constructor vacío requerido por Firestore
    constructor() : this("", "", "", "", "", "", 0L)
    
    // Método para convertir a un mapa para Firestore
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "email" to email,
            "fullName" to fullName,
            "phoneNumber" to phoneNumber,
            "educationLevel" to educationLevel,
            "specialty" to specialty,
            "createdAt" to createdAt
        )
    }
}
