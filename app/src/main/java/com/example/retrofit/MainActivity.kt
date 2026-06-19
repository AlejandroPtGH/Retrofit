package com.example.retrofit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.retrofit.ui.AutoresFragment
import com.example.retrofit.ui.GenerosFragment
import com.example.retrofit.ui.LibrosFragment

/**
 * Actividad principal de la app. Es la única pantalla que existe —
 * en lugar de navegar entre actividades, cambiamos el contenido
 * del contenedor central intercambiando fragments según la pestaña
 * que el usuario pulse en la barra de navegación inferior.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cargamos LibrosFragment como pantalla inicial al abrir la app
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedor, LibrosFragment())
            .commit()

        /**
         * Escuchamos los toques en la barra de navegación inferior
         * y reemplazamos el fragment del contenedor según la pestaña pulsada
         */
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navLibros -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contenedor, LibrosFragment())
                        .commit()
                    // Devolvemos true para indicar que el clic fue procesado correctamente
                    true
                }
                R.id.navAutores -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contenedor, AutoresFragment())
                        .commit()
                    // procesado correctamente
                    true
                }
                R.id.navGeneros -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contenedor, GenerosFragment())
                        .commit()
                    // procesado correctamente
                    true
                }
                // Si el item no coincide con ninguno de los anteriores no hacemos nada
                else -> false
            }
        }
    }
}