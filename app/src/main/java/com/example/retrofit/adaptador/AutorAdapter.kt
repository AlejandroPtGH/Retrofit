package com.example.retrofit.adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit.R
import com.example.retrofit.modelo.Autor

/**
 * Adaptador para mostrar la lista de autores en un RecyclerView.
 * Recibe dos funciones como parámetros para manejar las acciones
 * de editar y eliminar cada autor de la lista.
 * -> Unit significa que esa función no devuelve nada, es el equivalente a void en Java
 */
class AutorAdapter(
    private var autores: List<Autor>,
    private val onEditar: (Autor) -> Unit,
    private val onEliminar: (Autor) -> Unit
) : RecyclerView.Adapter<AutorAdapter.AutorViewHolder>() {

    /**
     * ViewHolder que representa cada fila de la lista.
     * Guarda en las variables conseguidas por el id
     */
    inner class AutorViewHolder(itemView: android.view.View) :
        RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.textViewNombreAutor)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditarAutor)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminarAutor)
    }

    /**
     * Inflate (infla) el layout de cada fila y devuelve su ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_autor, parent, false)
        return AutorViewHolder(view)
    }

    /**
     * Rellena cada fila con los datos del autor correspondiente
     * y asigna los listeners a los botones de editar y eliminar
     */
    override fun onBindViewHolder(holder: AutorViewHolder, position: Int) {
        val autor = autores[position]
        holder.tvNombre.text = autor.nombre
        holder.btnEditar.setOnClickListener { onEditar(autor) }
        holder.btnEliminar.setOnClickListener { onEliminar(autor) }
    }

    /**
     * Devuelve el número total de autores en la lista.
     */
    override fun getItemCount() = autores.size

    /**
     * Reemplaza la lista actual con una nueva y refresca el RecyclerView.
     * Se llama cada vez que los datos cambian desde la base de datos.
     */
    fun actualizarLista(nuevaLista: List<Autor>) {
        autores = nuevaLista
        notifyDataSetChanged()
    }
}