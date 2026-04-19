# Changelog

## [Unreleased]

---

## [Unreleased] – v0.5.0

### Added
- Componente `LoadingIndicator` para indicar el estado de carga
- Implementación de `LoadingIndicator` en `HomeScreen` para mostrar el estado de carga
- Implementación de `LoadingIndicator` en `ProcedureScreen` para mostrar el estado de carga
- Implementación de `LoadingIndicator` en `SettingScreen` para mostrar el estado de carga
- Selección automática del primer espacio disponible al iniciar la aplicación

### Removed
- Errores de SonaarQube

### Fixed
- Corrección de condiciones de carrera en login/logout

---

## [22-03-2026] - v0.4.0

### Added
- Feature completa de Settings con arquitectura MVVM + Clean Architecture
- Pantalla `SettingsScreen` con opción de logout
- Caso de uso `LogoutUseCase` y repositorio `SettingsRepository`
- Componente `MainTopBar` reutilizable
- Componente `BottomBar` con items configurables
- Eventos de navegación con `Channel` para evitar condiciones de carrera

### Changed
- Refactorización de la barra de navegación AutoCollapsableTopAppBar
- Corrección de condiciones de carrera en login/logout

### Fixed
- Logout navega a Splash correctamente sin reenvío a Home

### Removed
- Logs en toda la aplicación

---

## [06-03-2026] - v0.3.0

### Added
- Feature completa de visualización y gestión de procedimientos (Procedure)
- ProcedureScreen con diseño basado en tarjetas para los pasos (steps)
- Sistema de Checkbox de árbol (TreeCheckbox) para completar pasos
- Casos de uso: GetProcedureWithStepsUseCase y UpdateStepCompletion
- Repositorio ProcedureRepository con implementación para Supabase
- Módulo Hilt ProcedureModule para inyección de dependencias
- UI: AutoCollapsableTopAppBar para una barra de navegación dinámica

### Changed
- Reorganización de modelos de Home a Procedure (HomeResult → ProcedureResult, etc.)
- Navegación actualizada para incluir la ruta a ProcedureScreen pasando el procedureId
- Mejoras generales en la interfaz de usuario de las carpetas y tareas

### Removed
- Visualización de las tareas huérfanas. Las tareas siempre estarán dentro de una carpeta

---

## [26-02-2026] - v0.2.0

### Added
- Feature completa de Home con navegación jerárquica Spaces → Folders → Procedures
- HomeViewModel con gestión de estado usando StateFlow
- Casos de uso para obtener spaces, folders, procedures y steps desde Supabase
- Componentes de UI: SpaceTabs, SpaceChip, FolderList, FolderItem, ProcedureItem
- Animación de expandir/colapsar en carpetas
- Estados vacíos para carpetas sin procedimientos
- Repositorio HomeRepository con implementación en Supabase
- DTOs para Space, Folder, Procedure y Step con serialización correcta (snake_case)
- Módulo Hilt HomeModule para inyección de dependencias

### Changed
- Migración de tabla procedures en Supabase de bigint a UUID
- HomeScreen reorganizada a carpeta presentation/screen
- Navegación actualizada para soportar la nueva estructura de Home

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