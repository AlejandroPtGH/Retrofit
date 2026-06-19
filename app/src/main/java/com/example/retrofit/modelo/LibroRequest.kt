package com.example.retrofit.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Es diferente al modelo Libro normal porque
 * aquí el autor y el género se mandan solo con su ID numérico,
 * no con el objeto completo.
 */
@Serializable
data class LibroRequest(
    val titulo: String,
    val descripcion: String,

    /**
     * La API espera el campo como "autor_id" en el JSON,
     * pero en Kotlin lo llamamos autorId para seguir la convención.
     * @SerialName hace esa traducción automáticamente.
     */
    @SerialName("autor_id")
    val autorId: Int,

    /**
     * Lo mismo que autorId pero para el género.
     */
    @SerialName("genero_id")
    val generoId: Int,

    /**
     * La imagen es opcional, si no se manda se envía como null
     * y la API lo interpreta como que el libro no tiene portada.
     */
    val imagen: String? = null
)