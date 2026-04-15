# FlowGuide — Android App

Proyecto personal en desarrollo activo. Android nativo con enfoque en
arquitectura limpia, código mantenible y buenas prácticas desde el inicio.

---

## Arquitectura

Clean Architecture con separación estricta en tres capas por feature:

```
features/
└── [feature]/
├── data/        # DTOs, implementaciones de repositorio, fuentes de datos
├── domain/      # Modelos, interfaces, casos de uso
└── presentation/# ViewModels, Screens, componentes UI
```

Cada feature es autónoma. Las capas se comunican hacia adentro:
`presentation → domain ← data`. El dominio no sabe nada de Supabase,
Hilt ni Compose.

---

## Stack

| Capa | Tecnología |
|------|------------|
| UI | Jetpack Compose + Material 3 |
| Arquitectura | MVVM + Clean Architecture |
| DI | Hilt (Dagger) |
| Navegación | Navigation Compose — rutas type-safe con `@Serializable` |
| Backend | Supabase (Auth, Postgrest, Realtime) |
| Networking | Ktor + OkHttp |
| Animaciones | Lottie |
| Lenguaje | Kotlin 2.x |

---

## Decisiones técnicas destacadas

**Navegación type-safe**
Rutas definidas como `sealed class` con `@Serializable`. Sin strings
mágicos, sin crashes en tiempo de ejecución por argumentos mal tipados.

**Eventos de navegación con `Channel`**
Los ViewModels emiten eventos de navegación a través de `Channel<T>` en
lugar de `StateFlow`. Evita condiciones de carrera donde la UI procesa
un evento de navegación que ya no debería estar activo.

**`withProcedure {}` helper**
Función privada en `ProcedureViewModel` que encapsula el null-check del
estado actual antes de ejecutar cualquier operación. Reduce código
duplicado y hace explícita la precondición.

**Estado de UI como data class**
Cada feature tiene su propio `XState` que agrupa todo el estado de la
pantalla. Un único `StateFlow<XState>` en lugar de múltiples flows
independientes.

**`rememberUpdatedState` para callbacks**
Usado en componentes con `AndroidView` para evitar que callbacks
capturados en closures queden desactualizados entre recomposiciones.

---

## Estructura de features implementadas

- **Auth** — login/registro con email·contraseña + Google OAuth, gestión
  de sesión con deep links (`flowguide://login`)
- **Home** — navegación jerárquica Spaces → Folders → Procedures con
  expansión animada y carga lazy por nivel
- **Procedure** — visualización de pasos con `TreeCheckbox` (soporte para
  subnodos), marcado de completado con persistencia en Supabase
- **Settings** — logout con navegación limpia del back stack

---

## Changelog

El proyecto mantiene un [CHANGELOG.md](CHANGELOG.md) con historial de cambios y versiones.

---

## Autor

**Jeremías Cortés**
[linkedin.com/in/jeremias-cortes](https://linkedin.com/in/jeremias-cortes) ·
[jeremiasacortes@gmail.com](mailto:jeremiasacortes@gmail.com)

---

*Código visible con derechos reservados. Ver [LICENSE.md](LICENSE.md).*
