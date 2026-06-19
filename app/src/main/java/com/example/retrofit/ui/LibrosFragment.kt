package com.example.retrofit.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit.R
import com.example.retrofit.adaptador.LibroAdapter
import com.example.retrofit.modelo.Autor
import com.example.retrofit.modelo.Genero
import com.example.retrofit.modelo.Libro
import com.example.retrofit.modelo.LibroRequest
import com.example.retrofit.repositorio.AutorRepository
import com.example.retrofit.repositorio.GeneroRepository
import com.example.retrofit.repositorio.LibroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Fragment que muestra la lista completa de libros y permite
 * crear, editar y eliminar cada uno mediante diálogos emergentes.
 * A diferencia de los otros fragments, este necesita cargar también
 * la lista de autores y géneros para poder mostrarlos en los Spinners
 * del formulario.
 */
class LibrosFragment : Fragment() {

    private val libroRepository = LibroRepository()
    private val autorRepository = AutorRepository()
    private val generoRepository = GeneroRepository()

    private lateinit var adapter: LibroAdapter
    private lateinit var recycler: RecyclerView

    /**
     * Listas auxiliares que se cargan al iniciar el fragment.
     * Se usan para rellenar los Spinners de autor y género
     * en los diálogos de crear y editar.
     */
    private var listaAutores: List<Autor> = emptyList()
    private var listaGeneros: List<Genero> = emptyList()

    /**
     * Infla el layout del fragment
     * y lo devuelve para ver en pantalla.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_libros, container, false)
    }

    /**
     * Se ejecuta justo después de que el layout ya está creado y visible.
     * Aquí inicializamos el RecyclerView, el adaptador y el botón de agregar,
     * y hacemos la primera carga de datos desde el servidor.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recyclerLibros)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        adapter = LibroAdapter(
            emptyList(),
            onEditar = { libro -> mostrarDialogoEditar(libro) },
            onEliminar = { libro -> mostrarDialogoEliminar(libro) }
        )
        recycler.adapter = adapter

        view.findViewById<Button>(R.id.btnAgregarLibro).setOnClickListener {
            mostrarDialogoAgregar()
        }

        cargarDatos()
    }

    /**
     * Carga en paralelo los autores, géneros y libros desde el servidor.
     * Los autores y géneros se guardan en las listas auxiliares para los Spinners.
     * Si la carga de libros falla, imprime el error en el log para depuración
     * y muestra un Toast para avisar al usuario.
     */
    private fun cargarDatos() {
        GlobalScope.launch(Dispatchers.Main) {
            val autoresTemp = autorRepository.obtenerAutores()
            val generosTemp = generoRepository.obtenerGeneros()

            if (autoresTemp != null) listaAutores = autoresTemp
            if (generosTemp != null) listaGeneros = generosTemp

            val libros = libroRepository.obtenerLibros()
            if (libros != null) {
                adapter.actualizarLista(libros)
            } else {
                Toast.makeText(requireContext(), "Error al cargar libros", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Muestra un diálogo con el formulario completo para crear un libro nuevo.
     * El formulario tiene campos de texto para el título y la descripción,
     * y dos Spinners para seleccionar el autor y el género.
     * Al pulsar Guardar construye un LibroRequest con los datos y lo manda al repositorio.
     * Los parámetros _, _ son obligatorios que Android exige en el listener
     * del botón aunque no los usemos — representan el diálogo y el botón pulsado.
     * Se lee como: "cuando pulsen Guardar, ejecuta esto,
     * y los dos parámetros que Android me pasa me los salto porque no los necesito".
     */
    private fun mostrarDialogoAgregar() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_libro, null)
        val etTitulo = view.findViewById<EditText>(R.id.editTextTitulo)
        val etDescripcion = view.findViewById<EditText>(R.id.editTextDescripcion)
        val spinnerAutor = view.findViewById<Spinner>(R.id.spinnerAutor)
        val spinnerGenero = view.findViewById<Spinner>(R.id.spinnerGenero)

        configurarSpinners(spinnerAutor, spinnerGenero)

        AlertDialog.Builder(requireContext())
            .setTitle("Nuevo Libro")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val titulo = etTitulo.text.toString()
                val desc = etDescripcion.text.toString()

                if (titulo.isNotEmpty() && desc.isNotEmpty() && listaAutores.isNotEmpty() && listaGeneros.isNotEmpty()) {
                    val autorId = listaAutores[spinnerAutor.selectedItemPosition].id
                    val generoId = listaGeneros[spinnerGenero.selectedItemPosition].id

                    GlobalScope.launch(Dispatchers.Main) {
                        val request = LibroRequest(titulo, desc, autorId, generoId)
                        val nuevo = libroRepository.crearLibro(request)
                        if (nuevo != null) {
                            cargarDatos()
                            Toast.makeText(requireContext(), "Libro creado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Muestra un diálogo con el formulario ya relleno con los datos actuales del libro
     * para que el usuario pueda modificarlos. Los Spinners se preseleccionan
     * en el autor y género que ya tiene el libro asignados.
     * Al pulsar Guardar construye un LibroRequest con los nuevos datos y lo manda al repositorio.
     * Los parámetros _, _ son obligatorios que Android exige en el listener
     * del botón aunque no los usemos — representan el diálogo y el botón pulsado.
     * Se lee como: "cuando pulsen Guardar, ejecuta esto,
     * y los dos parámetros que Android me pasa me los salto porque no los necesito".
     */
    private fun mostrarDialogoEditar(libro: Libro) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_libro, null)
        val etTitulo = view.findViewById<EditText>(R.id.editTextTitulo)
        val etDescripcion = view.findViewById<EditText>(R.id.editTextDescripcion)
        val spinnerAutor = view.findViewById<Spinner>(R.id.spinnerAutor)
        val spinnerGenero = view.findViewById<Spinner>(R.id.spinnerGenero)

        etTitulo.setText(libro.titulo)
        etDescripcion.setText(libro.descripcion)
        configurarSpinners(spinnerAutor, spinnerGenero)

        val indexAutor = listaAutores.indexOfFirst { it.id == libro.autor.id }
        val indexGenero = listaGeneros.indexOfFirst { it.id == libro.genero.id }
        if (indexAutor >= 0) spinnerAutor.setSelection(indexAutor)
        if (indexGenero >= 0) spinnerGenero.setSelection(indexGenero)

        AlertDialog.Builder(requireContext())
            .setTitle("Editar Libro")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val titulo = etTitulo.text.toString()
                val desc = etDescripcion.text.toString()

                if (titulo.isNotEmpty() && desc.isNotEmpty()) {
                    val autorId = listaAutores[spinnerAutor.selectedItemPosition].id
                    val generoId = listaGeneros[spinnerGenero.selectedItemPosition].id

                    GlobalScope.launch(Dispatchers.Main) {
                        val request = LibroRequest(titulo, desc, autorId, generoId, libro.imagen)
                        val actualizado = libroRepository.actualizarLibro(libro.id, request)
                        if (actualizado != null) {
                            cargarDatos()
                            Toast.makeText(requireContext(), "Libro actualizado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Muestra un diálogo de confirmación antes de eliminar el libro.
     * Si el usuario confirma llama al repositorio para eliminarlo
     * y recarga la lista si todo fue bien.
     * Los parámetros _, _ son obligatorios que Android exige en el listener
     * del botón aunque no los usemos — representan el diálogo y el botón pulsado.
     * Se lee como: "cuando pulsen Eliminar, ejecuta esto,
     * y los dos parámetros que Android me pasa me los salto porque no los necesito".
     */
    private fun mostrarDialogoEliminar(libro: Libro) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Libro")
            .setMessage("¿Seguro que quieres eliminar ${libro.titulo}?")
            .setPositiveButton("Eliminar") { _, _ ->
                GlobalScope.launch(Dispatchers.Main) {
                    val eliminado = libroRepository.eliminarLibro(libro.id)
                    if (eliminado) {
                        cargarDatos()
                        Toast.makeText(requireContext(), "Libro eliminado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Rellena los dos Spinners con los nombres de los autores y géneros
     * cargados previamente en listaAutores y listaGeneros.
     * Usa ArrayAdapter con el layout estándar de Android para los Spinners.
     */
    private fun configurarSpinners(spinAutor: Spinner, spinGenero: Spinner) {
        val adapterAutor = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listaAutores.map { it.nombre }
        )
        val adapterGenero = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listaGeneros.map { it.nombre }
        )

        adapterAutor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinAutor.adapter = adapterAutor
        spinGenero.adapter = adapterGenero
    }
}