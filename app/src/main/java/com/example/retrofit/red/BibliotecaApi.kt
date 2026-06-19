package com.example.retrofit.red

import com.example.retrofit.modelo.Autor
import com.example.retrofit.modelo.Genero
import com.example.retrofit.modelo.Libro
import com.example.retrofit.modelo.LibroRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BibliotecaApi {


    @GET("api/generos")
    suspend fun obtenerGeneros(): List<Genero>

    @POST("api/generos")
    suspend fun crearGenero(@Body genero: Genero): Genero

    @PUT("api/generos/{id}")
    suspend fun actualizarGenero(@Path("id") id: Int, @Body genero: Genero): Genero

    @DELETE("api/generos/{id}")
    suspend fun eliminarGenero(@Path("id") id: Int)


    @GET("api/autores")
    suspend fun obtenerAutores(): List<Autor>

    @POST("api/autores")
    suspend fun crearAutor(@Body autor: Autor): Autor

    @PUT("api/autores/{id}")
    suspend fun actualizarAutor(@Path("id") id: Int, @Body autor: Autor): Autor

    @DELETE("api/autores/{id}")
    suspend fun eliminarAutor(@Path("id") id: Int)


    @GET("api/libros")
    suspend fun obtenerLibros(): List<Libro>

    @POST("api/libros")
    suspend fun crearLibro(@Body libro: LibroRequest): Libro

    @PUT("api/libros/{id}")
    suspend fun actualizarLibro(@Path("id") id: Int, @Body libro: LibroRequest): Libro

    @DELETE("api/libros/{id}")
    suspend fun eliminarLibro(@Path("id") id: Int)
}