package com.example.retrofit.repositorio

import com.example.retrofit.modelo.Genero
import com.example.retrofit.red.BibliotecaApi
import com.example.retrofit.red.RetrofitClient

/**
 * Repositorio que gestiona todas las operaciones relacionadas con los géneros.
 */
class GeneroRepository {

    /**
     * Instancia de la API creada por Retrofit a partir de la interfaz BibliotecaApi.
     * RetrofitClient ya tiene configurada la URL base y el convertidor de JSON.
     */
    private val api: BibliotecaApi =
        RetrofitClient.retrofit.create(BibliotecaApi::class.java)

    /**
     * Pide al servidor la lista completa de géneros.
     * Si la petición falla, imprime el error en el log para depuración
     * y devuelve null para que la ViewModel pueda manejarlo sin que la app se rompa.
     */
    suspend fun obtenerGeneros(): List<Genero>? {
        return try {
            val resultado = api.obtenerGeneros()
            resultado
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Envía un género nuevo al servidor para guardarlo en la base de datos.
     * Devuelve el género creado con su ID asignado por el servidor,
     * o null si algo salió mal.
     */
    suspend fun crearGenero(genero: Genero): Genero? {
        return try {
            api.crearGenero(genero)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Actualiza los datos de un género existente en el servidor.
     * Necesita el ID para que la API sepa qué género modificar,
     * y el objeto género con los nuevos datos.
     * Devuelve el género actualizado o null si algo salió mal.
     */
    suspend fun actualizarGenero(id: Int, genero: Genero): Genero? {
        return try {
            api.actualizarGenero(id, genero)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Elimina un género del servidor usando su ID.
     * Devuelve true si se eliminó correctamente
     * o false si hubo algún error en la petición.
     */
    suspend fun eliminarGenero(id: Int): Boolean {
        return try {
            api.eliminarGenero(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}