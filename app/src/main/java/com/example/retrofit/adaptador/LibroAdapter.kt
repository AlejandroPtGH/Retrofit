package com.example.retrofit.adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofit.R
import com.example.retrofit.modelo.Libro

/**
 * Adaptador para mostrar la lista de libros en un RecyclerView.
 * Cada fila muestra la portada, título, autor, descripción y género del libro
 * Recibe dos funciones como parámetros para manejar las acciones
 * de editar y eliminar cada libro de la lista.
 */
class LibroAdapter(
    private var libros: List<Libro>,
    private val onEditar: (Libro) -> Unit,
    private val onEliminar: (Libro) -> Unit
) : RecyclerView.Adapter<LibroAdapter.LibroViewHolder>() {

    /**
     * ViewHolder que representa cada fila de la lista.
     * Guarda en las variables conseguidas por el id
     */
    inner class LibroViewHolder(itemView: android.view.View) :
        RecyclerView.ViewHolder(itemView) {
        val ivImagen: ImageView = itemView.findViewById(R.id.imagenLibro)
        val tvTitulo: TextView = itemView.findViewById(R.id.textViewTituloLibro)
        val tvAutor: TextView = itemView.findViewById(R.id.textViewAutorLibro)
        val tvDescripcion: TextView = itemView.findViewById(R.id.textViewDescripcionLibro)
        val tvGenero: TextView = itemView.findViewById(R.id.textViewGeneroLibro)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditarLibro)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminarLibro)
    }

    /**
     * Infla el layout de cada fila  y devuelve su ViewHolder.
     * este método es cuando necesita crear una fila nueva,
     * no para cada elemento de la lista.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_libro, parent, false)
        return LibroViewHolder(view)
    }

    /**
     * - Si la URL ya es completa la usa tal cual.
     * - Si solo es una ruta relativa como "portadas/libro.jpg",
     *   le añade "https://biblioteca.guappi.com/" por delante para formar la URL completa.
     */

    override fun onBindViewHolder(holder: LibroViewHolder, position: Int) {
        val libro = libros[position]
        holder.tvTitulo.text = libro.titulo
        holder.tvAutor.text = "Autor: ${libro.autor.nombre}"
        holder.tvDescripcion.text = "Descripción: ${libro.descripcion}"
        holder.tvGenero.text = "Género: ${libro.genero.nombre}"

        if (libro.imagen != null && libro.imagen.isNotEmpty()) {
            val urlImagen = if (libro.imagen.startsWith("http")) {
                libro.imagen
            } else {
                "https://biblioteca.guappi.com/${libro.imagen}"
            }

            Glide.with(holder.itemView.context)
                .load(urlImagen)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.ivImagen)

        } else {
            holder.ivImagen.setImageResource(R.drawable.ic_launcher_foreground)
        }

        holder.btnEditar.setOnClickListener { onEditar(libro) }
        holder.btnEliminar.setOnClickListener { onEliminar(libro) }
    }

    /**
     * Devuelve el número total de libros en la lista.
     * Android lo usa internamente para saber cuántas filas dibujar.
     */
    override fun getItemCount() = libros.size

    /**
     * Reemplaza la lista actual con una nueva y refresca el RecyclerView
     * para que los cambios se vean en pantalla inmediatamente.
     * Se llama cada vez que los datos cambian desde la base de datos.
     */
    fun actualizarLista(nuevaLista: List<Libro>) {
        libros = nuevaLista
        notifyDataSetChanged()
    }
}