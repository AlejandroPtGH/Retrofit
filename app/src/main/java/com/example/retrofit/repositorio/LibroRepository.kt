package com.example.retrofit.repositorio

import com.example.retrofit.modelo.Libro
import com.example.retrofit.modelo.LibroRequest
import com.example.retrofit.red.BibliotecaApi
import com.example.retrofit.red.RetrofitClient

/**
 * Repositorio que gestiona todas las operaciones relacionadas con los libros.
 */
class LibroRepository {

    /**
     * Instancia de la API creada por Retrofit a partir de la interfaz BibliotecaApi.
     * RetrofitClient ya tiene configurada la URL base y el convertidor de JSON.
     */
    private val api: BibliotecaApi =
        RetrofitClient.retrofit.create(BibliotecaApi::class.java)

    /**
     * Pide al servidor la lista completa de libros.
     * Si la petición falla, imprime el error en el log para depuración
     * y devuelve null para que la ViewModel pueda manejarlo sin que la app se rompa.
     */
    suspend fun obtenerLibros(): List<Libro>? {
        return try {
            api.obtenerLibros()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Envía un libro nuevo al servidor para guardarlo en la base de datos.
     * Usa LibroRequest en lugar de Libro porque la API espera solo los IDs
     * del autor y del género, no los objetos completos.
     * Devuelve el libro creado con todos sus datos o null si la petición falló.
     * El error se imprime en el log para depuración.
     */
    suspend fun crearLibro(libro: LibroRequest): Libro? {
        return try {
            api.crearLibro(libro)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Actualiza los datos de un libro existente en el servidor.
     * Necesita el ID para que la API sepa qué libro modificar,
     * y un LibroRequest con los nuevos datos.
     * Devuelve el libro actualizado o null si la petición falló.
     * El error se imprime en el log para depuración.
     */
    suspend fun actualizarLibro(id: Int, libro: LibroRequest): Libro? {
        return try {
            api.actualizarLibro(id, libro)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Elimina un libro del servidor usando su ID.
     * Devuelve true si se eliminó correctamente
     * o false si la petición falló. El error se imprime en el log para depuración.
     */
    suspend fun eliminarLibro(id: Int): Boolean {
        return try {
            api.eliminarLibro(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}