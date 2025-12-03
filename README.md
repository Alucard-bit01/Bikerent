# BikeRent App

BikeRent es una aplicación de Android de ejemplo que demuestra un sistema de alquiler de bicicletas. Los usuarios pueden registrarse, iniciar sesión, explorar bicicletas disponibles y ver sus perfiles. La aplicación está construida con tecnologías modernas de Android.

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
*   **Carga de Imágenes:** [Coil](https://coil-kt.github.io/coil/) para la carga eficiente de imágenes.
*   **Asincronía:** Coroutines y Flow de Kotlin.

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

