package com.example.retrofit.repositorio

import com.example.retrofit.modelo.Autor
import com.example.retrofit.red.BibliotecaApi
import com.example.retrofit.red.RetrofitClient

/**
 * Repositorio que gestiona todas las operaciones relacionadas con los autores.
 */
class AutorRepository {

    /**
     * Instancia de la API creada por Retrofit a partir de la interfaz BibliotecaApi.
     * RetrofitClient ya tiene configurada la URL base y el convertidor de JSON.
     */
    private val api: BibliotecaApi =
        RetrofitClient.retrofit.create(BibliotecaApi::class.java)

    /**
     * Pide al servidor la lista completa de autores.
     * Si la petición falla, imprime el error en el log para depuración
     * y devuelve null para que la ViewModel pueda manejarlo sin que la app se rompa.
     */
    suspend fun obtenerAutores(): List<Autor>? {
        return try {
            api.obtenerAutores()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Envía un autor nuevo al servidor para guardarlo en la base de datos.
     * Devuelve el autor creado con su ID asignado por el servidor,
     * o null si la petición falló. El error se imprime en el log para depuración.
     */
    suspend fun crearAutor(autor: Autor): Autor? {
        return try {
            api.crearAutor(autor)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Actualiza los datos de un autor existente en el servidor.
     * Necesita el ID para que la API sepa qué autor modificar,
     * y el objeto autor con los nuevos datos.
     * Devuelve el autor actualizado o null si la petición falló.
     * El error se imprime en el log para depuración.
     */
    suspend fun actualizarAutor(id: Int, autor: Autor): Autor? {
        return try {
            api.actualizarAutor(id, autor)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Elimina un autor del servidor usando su ID.
     * Devuelve true si se eliminó correctamente
     * o false si la petición falló. El error se imprime en el log para depuración.
     */
    suspend fun eliminarAutor(id: Int): Boolean {
        return try {
            api.eliminarAutor(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}