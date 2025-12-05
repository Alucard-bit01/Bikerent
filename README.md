# BikeRent App

BikeRent es una aplicación de Android de ejemplo que demuestra un sistema de alquiler de bicicletas. Los usuarios pueden registrarse, iniciar sesión, explorar bicicletas disponibles y ver sus perfiles. La aplicación está construida con tecnologías modernas de Android.
Permite administrar un catálogo de bicicletas, registrar rentas y mantener un historial completo almacenado localmente en el dispositivo.
El proyecto fue desarrollado con fines educativos, enfocándose en implementar una arquitectura limpia, separación de responsabilidades, navegación estructurada y persistencia de datos con Room. Además, aprovecha Material Design 3 para una interfaz moderna y responsiva.

## Características Principales

*   **Autenticación de Usuarios:** Registro e inicio de sesión utilizando:
    *   Correo electrónico y contraseña.
    *   Google Sign-In.
*   **Catálogo de Bicicletas:** Muestra una lista de bicicletas disponibles para alquilar.
*   **Detalles de la Bicicleta:** Permite ver información detallada de cada bicicleta.
*   **Perfil de Usuario:** Los usuarios pueden ver y editar la información de su perfil.
*   **Navegación Moderna:** Implementado con Jetpack Navigation Compose para una experiencia de usuario fluida.
*   **Interfaz de Usuario Reactiva:** Construida completamente con Jetpack Compose.

## Tecnologías Utilizadas

*   **Lenguaje:** [Kotlin](https://kotlinlang.org/)
*   **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) para una interfaz de usuario declarativa y moderna.
*   **Arquitectura:** MVVM (Model-View-ViewModel).
*   **Navegación:** [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation).
*   **Backend y Autenticación:** [Firebase](https://firebase.google.com/) (Authentication, Firestore).
*   **Asincronía:** Coroutines y Flow de Kotlin.

## Estructura del proyecto
* **bikerent/
* ├── .gradle/
* ├── .idea/
* ├── app/
* │   ├── build/
* │   ├── libs/
* │   ├── src/
* │   │   ├── androidTest/
* │   │   │   └── java/
* │   │   │       └── mx/
* │   │   │           └── edu/
* │   │   │               └── utng/
* │   │   │                   └── eapd/
* │   │   │                       └── bikerent/
* │   │   │                           └── ExampleInstrumentedTest.kt
* │   │   ├── main/
* │   │   │   ├── java/
* │   │   │   │   └── mx/
* │   │   │   │       └── edu/
* │   │   │   │           └── utng/
* │   │   │   │               └── eapd/
* │   │   │   │                   └── bikerent/
* │   │   │   │                       ├── data/
* │   │   │   │                       │   ├── BikeDao.kt
* │   │   │   │                       │   └── BikeRepository.kt
* │   │   │   │                       ├── model/
* │   │   │   │                       │   ├── Bike.kt
* │   │   │   │                       │   ├── Rent.kt
* │   │   │   │                       │   ├── RentDao.kt
* │   │   │   │                       │   ├── RentDatabase.kt
* │   │   │   │                       │   ├── RentRepository.kt
* │   │   │   │                       │   └── User.kt
* │   │   │   │                       ├── navigation/
* │   │   │   │                       │   ├── BikeRentNavHost.kt
* │   │   │   │                       │   └── Screen.kt
* │   │   │   │                       ├── ui/
* │   │   │   │                       │   ├── screens/
* │   │   │   │                       │   │   ├── auth/
* │   │   │   │                       │   │   │   ├── LoginScreen.kt
* │   │   │   │                       │   │   │   └── SignUpScreen.kt
* │   │   │   │                       │   │   ├── BikeAddScreen.kt
* │   │   │   │                       │   │   ├── BikeDetailScreen.kt
* │   │   │   │                       │   │   ├── BikeEditScreen.kt
* │   │   │   │                       │   │   ├── BikeListScreen.kt
* │   │   │   │                       │   │   ├── BikesScreen.kt
* │   │   │   │                       │   │   ├── EditProfileScreen.kt
* │   │   │   │                       │   │   ├── HomeScreen.kt
* │   │   │   │                       │   │   ├── ProfileScreen.kt
* │   │   │   │                       │   │   └── RentScreen.kt
* │   │   │   │                       │   └── theme/
* │   │   │   │                       │       ├── Color.kt
* │   │   │   │                       │       ├── Theme.kt
* │   │   │   │                       │       └── Type.kt
* │   │   │   │                       ├── viewmodel/
* │   │   │   │                       │   ├── AuthViewModel.kt
* │   │   │   │                       │   ├── BikeViewModel.kt
* │   │   │   │                       │   └── RentViewModel.kt
* │   │   │   │                       └── MainActivity.kt
* │   │   │   ├── res/
* │   │   │   │   ├── values/
* │   │   │   │   │   ├── colors.xml
* │   │   │   │   │   ├── strings.xml
* │   │   │   │   │   └── themes.xml
* │   │   │   │   └── xml/
* │    │   │   └── AndroidManifest.xml
* │   │   └── test
* │   ├── build.gradle.kts
* │   ├── google-services.json**


## Ejemplo de codigo documentado

   /**
     * Se crea un lanzador para el resultado de una actividad, en este caso, el flujo de inicio de sesión de Google.
     * Utiliza el contrato [ActivityResultContracts.StartActivityForResult], que inicia una actividad y espera un resultado.
     *
     * El callback `onResult` se ejecuta cuando el usuario regresa a la aplicación después del intento de inicio de sesión.
     */
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            // Se obtiene la tarea para extraer el resultado del intent de inicio de sesión.
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Si la tarea es exitosa, se obtiene la cuenta del usuario.
                val account = task.getResult(ApiException::class.java)!!
                // Se llama al ViewModel para realizar la autenticación en Firebase con el idToken de la cuenta de Google.
                authViewModel.signInWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Si ocurre un error (por ejemplo, el usuario cancela el flujo), se muestra un mensaje.
                Toast.makeText(context, "Google Sign-in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    )

    
    /**
     * Efecto secundario que se ejecuta cada vez que el estado de `firebaseUser` cambia.* `LaunchedEffect` es ideal para llamar funciones de suspensión (como navegar) en respuesta a un cambio de estado.
     *
     * @param firebaseUser El estado del usuario de Firebase. Cuando este valor cambia de `null` a un objeto `FirebaseUser`,
     *                     indica que el inicio de sesión fue exitoso.
     */
    LaunchedEffect(firebaseUser) {
        // Si el usuario no es nulo, significa que el registro o inicio de sesión fue exitoso.
        if (firebaseUser != null) {
            // Se navega a la ruta "home" (o la pantalla principal de la aplicación).
            navController.navigate("home") {
                // Se elimina la pila de navegación hasta la ruta "login" (inclusive).
                // Esto previene que el usuario pueda volver a las pantallas de login/signup presionando el botón de retroceso.
                popUpTo("login") { inclusive = true }
            }
        }
    }

## Configuración del Proyecto

Sigue estos pasos para poner en marcha el proyecto:

1.  **Clonar el repositorio:**
    ```bash
    git clone <URL_DEL_REPOSITORIO>
    ```

2.  **Configurar Firebase:**
    *   Ve a la [Consola de Firebase](https://console.firebase.google.com/).
    *   Crea un nuevo proyecto de Firebase o usa uno existente.
    *   Registra tu aplicación de Android con el nombre del paquete `mx.edu.utng.eapd.bikerent`.
    *   Descarga el archivo `google-services.json` y colócalo en el directorio `app/` de tu proyecto.
    *   En la sección de Autenticación, habilita los proveedores de "Correo electrónico/Contraseña" y "Google".
    *   Configura las reglas de seguridad de Cloud Firestore para permitir la lectura y escritura.

3.  **Abrir en Android Studio:**
    *   Abre el proyecto en la última versión de Android Studio.
    *   Espera a que la sincronización de Gradle se complete.

4.  **Ejecutar la aplicación:**
    *   Selecciona un emulador o un dispositivo físico y ejecuta la configuración `app`.

