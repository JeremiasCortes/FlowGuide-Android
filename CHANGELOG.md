# Changelog

## [Unreleased]

---

## [17-02-2026] - v0.1.0

### Added
- Feature completa de autenticación con arquitectura MVVM + Clean Architecture
- Login con email y contraseña
- Registro de usuarios con nombre, email, fecha de nacimiento y contraseña
- Autenticación con Google OAuth (funciona para login y registro)
- Cierre de sesión (logout)
- Verificación de sesión activa al iniciar la app
- Capa de dominio con modelos, repositorios y casos de uso
- Capa de datos con implementación de repositorio en Supabase
- Inyección de dependencias con Hilt
- Deep links para OAuth (flowguide://login)
- Selector de cuenta de Google (prompt=select_account)
- Comentarios explicativos en todo el código como plantilla para futuras features

### Changed
- Reorganización de pantallas según arquitectura MVVM
  - Home → features/home/presentation/HomeScreen
  - Splash → features/welcome/presentation/SplashScreen
- Actualización de navegación con animaciones y gestión del back stack
- BackHandler en Home para minimizar app en lugar de volver al navegador
- Migración de SupabaseClient del módulo core a la feature auth

### Removed
- Módulo core deprecado
- Logs de debug del repositorio (preparado para producción)

---

## [12-02-2026] - v0.0.2

### Added
- Creación del archivo de navegación total
- Implementaciones, Librerías y Plugins
- Implementación de Lottie
- Animación de Lottie
- Rutas de navegación
- Repositorio maven
- Pantalla Screen

### Changed
- Uso del nuevo archivo de navegación main
- Pruebas de animación con Lottie
- Ajustes en el consumo del gradle

### Removed
- Archivos deprecados
- Import que no se usa

---

## [09-02-2026] - v0.0.1

### Added

- Initial release of the project.