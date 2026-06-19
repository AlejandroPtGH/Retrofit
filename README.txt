# Biblioteca App — Android con Retrofit

Aplicación Android desarrollada en Kotlin que permite gestionar una biblioteca digital (libros, autores y géneros) consumiendo una API REST mediante Retrofit.

## Tecnologías

- Lenguaje: Kotlin
- Arquitectura: MVC con Fragments
- Networking: Retrofit 2 + kotlinx.serialization
- UI: RecyclerView, BottomNavigationView, AlertDialog
- Mínimo SDK: Android 8+ (ver `build.gradle.kts`)

## Estructura del proyecto

```
app/src/main/java/com/example/retrofit/
├── adaptador/        # RecyclerView Adapters (Autor, Genero, Libro)
├── modelo/           # Data classes (Autor, Genero, Libro, LibroRequest)
├── red/              # RetrofitClient + BibliotecaApi (interfaz REST)
├── repositorio/      # Capa de acceso a datos (AutorRepository, etc.)
├── ui/               # Fragments (AutoresFragment, GenerosFragment, LibrosFragment)
└── MainActivity.kt   # Actividad principal con BottomNavigationView
```

## API

La app consume la API REST alojada en:

```
https://biblioteca.guappi.com/
```

Endpoints principales:
- `GET/POST/PUT/DELETE api/generos`
- `GET/POST/PUT/DELETE api/autores`
- `GET/POST/PUT/DELETE api/libros`

>  La URL base está configurada en `RetrofitClient.kt`. Si quieres apuntar a otro servidor, cambia el valor de `BASE_URL` en ese archivo.

##  Cómo ejecutar

1. Clona el repositorio:
   ```bash
   git clone https://github.com/TU_USUARIO/TU_REPO.git
   ```
2. Ábrelo en **Android Studio**.
3. Sincroniza Gradle (`Sync Now`).
4. Ejecuta en emulador o dispositivo real.

> No se necesita ninguna configuración adicional de API keys ni credenciales.

## Funcionalidades

- Listar, crear, editar y eliminar **géneros**
- Listar, crear, editar y eliminar **autores**
- Listar, crear, editar y eliminar **libros** (con relación a autor y género)
- Navegación por pestañas con `BottomNavigationView`

## Autor

José Alejandro Pampa Taguada — Proyecto de prácticas DAM