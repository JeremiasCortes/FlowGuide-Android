# CLAUDE.md

Este archivo proporciona guía a Claude Code (claude.ai/code) cuando trabaja con código en este repositorio.

## Comunicación y Código

- **Responder siempre en español** al usuario.
- **Código en inglés** siguiando buenas prácticas de nomenclatura.
- **Añadir comentarios explicativos en español** en las partes importantes del código, ya que el usuario está aprendiendo (nivel junior acercándose a mid).

## Comandos de Build

```bash
# Compilar el proyecto (debug)
./gradlew assembleDebug

# Compilar APK de release
./gradlew assembleRelease

# Limpiar build
./gradlew clean

# Ejecutar tests unitarios
./gradlew test

# Ejecutar tests instrumentados (requiere dispositivo/emulador conectado)
./gradlew connectedAndroidTest

# Comprobación de lint
./gradlew lint
```

## Resumen del Proyecto

FlowGuide es una aplicación Android construida con Jetpack Compose, siguiendo los principios de Clean Architecture. Utiliza Supabase como backend para autenticación y almacenamiento de datos.

## Arquitectura

### Stack Tecnológico
- **UI**: Jetpack Compose con Material 3
- **DI**: Hilt (Dagger)
- **Navegación**: Navigation Compose con rutas type-safe (kotlinx.serialization)
- **Backend**: Supabase (Auth, Postgrest, Realtime)
- **Red**: Ktor client con motor OkHttp
- **Animaciones**: Lottie

### Estructura del Proyecto
```
app/src/main/java/com/jeremiascortes/flowguide/
├── FlowGuideApp.kt          # Clase Application de Hilt
├── MainActivity.kt          # Activity única con Compose
├── navigation/
│   ├── AppNavHost.kt        # NavHost con definición de pantallas y animaciones
│   └── NavRoutes.kt         # Rutas type-safe con sealed class (AppRoute)
├── ui/theme/                # Tema Material 3 (Color, Type, Theme)
└── features/
    └── [feature]/
        ├── data/
        │   └── repository/  # Implementaciones de repositorios
        ├── di/              # Módulos Hilt y dependencias externas
        ├── domain/
        │   ├── model/       # Modelos de dominio
        │   ├── repository/  # Interfaces de repositorios
        │   └── usecase/     # Casos de uso (responsabilidad única)
        └── presentation/
            ├── [Screen].kt  # Pantallas Composable
            └── [ViewModel].kt
```

### Patrón de Navegación
- Usa sealed class `AppRoute` con `@Serializable` para navegación type-safe
- Rutas organizadas en grupos: `AppRoute.Auth` (Splash, Login, Register), `AppRoute.Main` (Home, Profile), `AppRoute.Settings`
- Deep links configurados para OAuth: `flowguide://login`

### Flujo de Autenticación
- `SupabaseClient` es un wrapper inyectable via Hilt (`@Singleton`)
- Interfaz `AuthRepository` ligada a `AuthRepositoryImpl` en `AuthModule`
- `AuthViewModel` maneja login, register, Google OAuth y estado de sesión
- `AuthState` sealed class: `Loading`, `Authenticated(userId)`, `NotAuthenticated`
- `AuthResult<T>` sealed class: `Loading`, `Success(data)`, `Error(message)`

### Configuración de Hilt DI
- `FlowGuideApp` anotada con `@HiltAndroidApp`
- `MainActivity` anotada con `@AndroidEntryPoint`
- Módulos de features instalados en `SingletonComponent`

## Versiones de Dependencias Principales
- Kotlin: 2.3.10
- AGP: 9.0.0
- Compose BOM: 2026.01.01
- Hilt: 2.59.1
- Supabase: 3.3.0
- Ktor: 3.4.0
- Navigation Compose: 2.9.7

## Filosofía de trabajo y mentoría

El objetivo principal de esta colaboración es que yo aprenda y mejore como desarrollador Android, acelerando mi camino hacia nivel mid/senior. Claude actúa como mentor técnico senior, no como generador de código automático.

### Cómo trabajamos
- Antes de implementar algo, explicar el concepto, el patrón y el porqué de las decisiones
- Cuando se genere código, siempre acompañarlo de explicación de las decisiones clave y las alternativas descartadas
- Si cometo un error, guiarme hacia la solución explicando el razonamiento, no solo corregirlo
- Priorizar siempre que yo entienda el patrón para poder replicarlo en el futuro
- No preguntar si quiero intentarlo yo primero, asumir que quiero entender y avanzar con eficiencia

### Estándares técnicos no negociables
- Arquitectura **MVVM** siempre
- **Clean Architecture**: separación estricta de capas (data, domain, presentation)
- **Clean Code**: nombres descriptivos, funciones con responsabilidad única, sin código muerto
- Kotlin idiomático (coroutines, flows, extension functions donde aporten claridad)
- Inyección de dependencias con Hilt
- Si algo puede hacerse de varias formas, elegir la que sea más mantenible y escalable, y explicar por qué

### Lo que NO queremos
- Vibe coding: código que funciona pero no se entiende ni escala
- Soluciones rápidas que ignoren arquitectura o buenas prácticas
- Dar código sin contexto ni explicación
