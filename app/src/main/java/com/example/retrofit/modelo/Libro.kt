package com.example.retrofit.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Libro(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val imagen: String? = null,
    val autor: Autor,
    val genero: Genero
)