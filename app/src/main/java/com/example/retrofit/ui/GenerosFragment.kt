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
import com.example.retrofit.adaptador.GeneroAdapter
import com.example.retrofit.modelo.Genero
import com.example.retrofit.repositorio.GeneroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Fragment que muestra la lista completa de géneros y permite
 * crear, editar y eliminar cada uno mediante diálogos emergentes.
 */
class GenerosFragment : Fragment() {

    private val repository = GeneroRepository()
    private lateinit var adapter: GeneroAdapter
    private lateinit var recycler: RecyclerView

    /**
     * Infla el layout del fragment
     * y lo devuelve para verlo en pantalla.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_generos, container, false)
    }

    /**
     * Se ejecuta justo después de que el layout ya está creado y visible.
     * Aquí inicializamos el RecyclerView, el adaptador y el botón de agregar,
     * y hacemos la primera carga de géneros desde el servidor.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recyclerGeneros)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        adapter = GeneroAdapter(
            emptyList(),
            onEditar = { genero -> mostrarDialogoEditar(genero) },
            onEliminar = { genero -> mostrarDialogoEliminar(genero) }
        )
        recycler.adapter = adapter

        view.findViewById<Button>(R.id.btnAgregarGenero).setOnClickListener {
            mostrarDialogoAgregar()
        }

        cargarGeneros()
    }

    /**
     * Pide la lista de géneros al repositorio y la manda al adaptador.
     * Si la petición falla, imprime el error en el log para depuración
     * y muestra un Toast para avisar al usuario.
     */
    private fun cargarGeneros() {
        GlobalScope.launch(Dispatchers.Main) {
            val generos = repository.obtenerGeneros()
            if (generos != null) {
                adapter.actualizarLista(generos)
            } else {
                Toast.makeText(requireContext(), "Error al cargar géneros", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Muestra un diálogo con un campo de texto para escribir el nombre
     * del nuevo género. Al pulsar Guardar llama al repositorio para crearlo
     * y recarga la lista si todo fue bien.
     * Los parámetros _, _ son obligatorios que Android exige en el listener
     * del botón aunque no los usemos — representan el diálogo y el botón pulsado.
     * Se lee como: "cuando pulsen Guardar, ejecuta esto,
     * y los dos parámetros que Android me pasa me los salto porque no los necesito".
     */
    private fun mostrarDialogoAgregar() {
        val input = EditText(requireContext())
        input.hint = "Nombre del género"

        AlertDialog.Builder(requireContext())
            .setTitle("Nuevo Género")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = input.text.toString()
                if (nombre.isNotEmpty()) {
                    GlobalScope.launch(Dispatchers.Main) {
                        val nuevo = repository.crearGenero(Genero(0, nombre))
                        if (nuevo != null) {
                            cargarGeneros()
                            Toast.makeText(requireContext(), "Género creado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Muestra un diálogo con el nombre actual del género ya escrito en el campo
     * para que el usuario pueda modificarlo. Al pulsar Guardar llama al repositorio
     * para actualizarlo y recarga la lista si todo fue bien.
     * Los parámetros _, _ son obligatorios que Android exige en el listener
     * del botón aunque no los usemos — representan el diálogo y el botón pulsado.
     * Se lee como: "cuando pulsen Guardar, ejecuta esto,
     * y los dos parámetros que Android me pasa me los salto porque no los necesito".
     */
    private fun mostrarDialogoEditar(genero: Genero) {
        val input = EditText(requireContext())
        input.setText(genero.nombre)

        AlertDialog.Builder(requireContext())
            .setTitle("Editar Género")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = input.text.toString()
                if (nombre.isNotEmpty()) {
                    GlobalScope.launch(Dispatchers.Main) {
                        val actualizado = repository.actualizarGenero(genero.id, Genero(genero.id, nombre))
                        if (actualizado != null) {
                            cargarGeneros()
                            Toast.makeText(requireContext(), "Género actualizado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Muestra un diálogo de confirmación antes de eliminar el género.
     * Si el usuario confirma llama al repositorio para eliminarlo
     * y recarga la lista si todo fue bien.
     * Los parámetros _, _ son obligatorios que Android exige en el listener
     * del botón aunque no los usemos — representan el diálogo y el botón pulsado.
     * Se lee como: "cuando pulsen Eliminar, ejecuta esto,
     * y los dos parámetros que Android me pasa me los salto porque no los necesito".
     */
    private fun mostrarDialogoEliminar(genero: Genero) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Género")
            .setMessage("¿Seguro que quieres eliminar ${genero.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                GlobalScope.launch(Dispatchers.Main) {
                    val eliminado = repository.eliminarGenero(genero.id)
                    if (eliminado) {
                        val generos = repository.obtenerGeneros()
                        if (generos != null) adapter.actualizarLista(generos)
                        Toast.makeText(requireContext(), "Género eliminado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}