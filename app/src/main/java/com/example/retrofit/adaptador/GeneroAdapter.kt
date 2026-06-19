package com.example.retrofit.adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit.R
import com.example.retrofit.modelo.Genero

/**
 * Adaptador para mostrar la lista de géneros en un RecyclerView
 * Recibe dos funciones como parámetros para manejar las acciones
 * de editar y eliminar cada género de la lista.
 * -> Unit significa que esa función no devuelve nada, es el equivalente a void en Java
 */
class GeneroAdapter(
    private var generos: List<Genero>,
    private val onEditar: (Genero) -> Unit,
    private val onEliminar: (Genero) -> Unit
) : RecyclerView.Adapter<GeneroAdapter.GeneroViewHolder>() {

    /**
     * ViewHolder que representa cada fila de la lista.
     * Guarda en las variables conseguidas por el id
     */
    inner class GeneroViewHolder(itemView: android.view.View) :
        RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreGenero)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditarGenero)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminarGenero)
    }

    /**
     * Inflate (infla) el layout de cada fila y devuelve su ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_genero, parent, false)
        return GeneroViewHolder(view)
    }

    /**
     * Rellena cada fila con los datos del género correspondiente
     * y asigna los listeners a los botones de editar y eliminar.
     */
    override fun onBindViewHolder(holder: GeneroViewHolder, position: Int) {
        val genero = generos[position]
        holder.tvNombre.text = genero.nombre
        holder.btnEditar.setOnClickListener { onEditar(genero) }
        holder.btnEliminar.setOnClickListener { onEliminar(genero) }
    }

    /**
     * Devuelve el número total de géneros en la lista.
     */
    override fun getItemCount() = generos.size

    /**
     * Reemplaza la lista actual con una nueva y refresca el RecyclerView.
     * Se llama cada vez que los datos cambian desde la base de datos.
     */
    fun actualizarLista(nuevaLista: List<Genero>) {
        generos = nuevaLista
        notifyDataSetChanged()
    }
}