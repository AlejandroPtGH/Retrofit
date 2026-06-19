package com.example.retrofit.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit.R
import com.example.retrofit.adaptador.AutorAdapter
import com.example.retrofit.modelo.Autor
import com.example.retrofit.repositorio.AutorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Fragment que muestra la lista completa de autores y permite
 * crear, editar y eliminar cada uno mediante diálogos emergentes.
 */
class AutoresFragment : Fragment() {

    private val repository = AutorRepository()
    private lateinit var adapter: AutorAdapter
    private lateinit var recycler: RecyclerView

    /**
     * Infla el layout del fragment (fragment_autores.xml)
     * y lo devuelve para que Android lo dibuje en pantalla.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_autores, container, false)
    }

    /**
     * Se ejecuta justo después de que el layout ya está creado y visible.
     * Aquí inicializamos el RecyclerView, el adaptador y el botón de agregar,
     * y hacemos la primera carga de autores desde el servidor.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recyclerAutores)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        adapter = AutorAdapter(
            emptyList(),
            onEditar = { autor -> mostrarDialogoEditar(autor) },
            onEliminar = { autor -> mostrarDialogoEliminar(autor) }
        )
        recycler.adapter = adapter

        view.findViewById<Button>(R.id.btnAgregarAutor).setOnClickListener {
            mostrarDialogoAgregar()
        }

        cargarAutores()
    }

    /**
     * Pide la lista de autores al repositorio y la manda al adaptador.
     * Si la petición falla, imprime el error en el log para depuración
     * y muestra un Toast para avisar al usuario.
     */
    private fun cargarAutores() {
        GlobalScope.launch(Dispatchers.Main) {
            val autores = repository.obtenerAutores()
            if (autores != null) {
                adapter.actualizarLista(autores)
            } else {
                Toast.makeText(requireContext(), "Error al cargar autores", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Muestra un diálogo con un campo de texto para escribir el nombre
     * del nuevo autor. Al pulsar Guardar llama al repositorio para crearlo
     * y recarga la lista si todo fue bien.
     * Los parámetros _, _ son obligatorios que Android exige en el listener
     * del botón aunque no los usemos — representan el diálogo y el botón pulsado.
     * Se lee como: "cuando pulsen Guardar, ejecuta esto,
     * y los dos parámetros que Android me pasa me los salto porque no los necesito".
     */
    private fun mostrarDialogoAgregar() {
        val input = EditText(requireContext())
        input.hint = "Nombre del autor"

        AlertDialog.Builder(requireContext())
            .setTitle("Nuevo Autor")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = input.text.toString()
                if (nombre.isNotEmpty()) {
                    GlobalScope.launch(Dispatchers.Main) {
                        val nuevo = repository.crearAutor(Autor(0, nombre))
                        if (nuevo != null) {
                            cargarAutores()
                            Toast.makeText(requireContext(), "Autor creado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Muestra un diálogo con el nombre actual del autor ya escrito en el campo
     * para que el usuario pueda modificarlo. Al pulsar Guardar llama al repositorio
     * para actualizarlo y recarga la lista si todo fue bien.
     * Los parámetros _, _ son obligatorios que Android exige en el listener
     * del botón aunque no los usemos — representan el diálogo y el botón pulsado.
     * Se lee como: "cuando pulsen Guardar, ejecuta esto,
     * y los dos parámetros que Android me pasa me los salto porque no los necesito".
     */
    private fun mostrarDialogoEditar(autor: Autor) {
        val input = EditText(requireContext())
        input.setText(autor.nombre)

        AlertDialog.Builder(requireContext())
            .setTitle("Editar Autor")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = input.text.toString()
                if (nombre.isNotEmpty()) {
                    GlobalScope.launch(Dispatchers.Main) {
                        val actualizado = repository.actualizarAutor(autor.id, Autor(autor.id, nombre))
                        if (actualizado != null) {
                            cargarAutores()
                            Toast.makeText(requireContext(), "Autor actualizado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Muestra un diálogo de confirmación antes de eliminar al autor.
     * Si el usuario confirma llama al repositorio para eliminarlo
     * y recarga la lista si todo fue bien.
     * Los parámetros _, _ son obligatorios que Android exige en el listener
     * del botón aunque no los usemos — representan el diálogo y el botón pulsado.
     * Se lee como: "cuando pulsen Eliminar, ejecuta esto,
     * y los dos parámetros que Android me pasa me los salto porque no los necesito".
     */
    private fun mostrarDialogoEliminar(autor: Autor) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Autor")
            .setMessage("¿Seguro que quieres eliminar a ${autor.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                GlobalScope.launch(Dispatchers.Main) {
                    val eliminado = repository.eliminarAutor(autor.id)
                    if (eliminado) {
                        val autores = repository.obtenerAutores()
                        if (autores != null) adapter.actualizarLista(autores)
                        Toast.makeText(requireContext(), "Autor eliminado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}