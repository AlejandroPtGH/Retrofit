package com.example.retrofit.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Representa un género literario que viene de la API.
 */
@Serializable
data class Genero(
    //cambios segunda rama
    //segundo cambio2
    /**
     * Identificador único del género en la base de datos.
     * Tiene 0 como valor por defecto para poder crear un
     * objeto vacío cuando aún no tenemos respuesta de la API.
     */
    val id: Int = 0,

    /**
     * La API devuelve este campo como "genero" en el JSON,
     * pero lo renombramos a "nombre" en Kotlin para que sea
     * más claro y consistente con el resto de modelos.
     */
    @SerialName("genero")
    val nombre: String
)