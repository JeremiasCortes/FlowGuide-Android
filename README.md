# FlowGuide

> Aplicación móvil de productividad y organización personal en desarrollo.

## Sobre el proyecto

FlowGuide es una aplicación Android diseñada para ayudar a los usuarios a gestionar su flujo de trabajo diario, tareas y objetivos personales. El proyecto se encuentra actualmente en fase de desarrollo activo.

**Estado actual:** v0.1.0 - Autenticación implementada

## Stack Tecnológico

| Categoría | Tecnología |
|-----------|------------|
| **Lenguaje** | Kotlin 2.3.10 |
| **UI** | Jetpack Compose + Material 3 |
| **Arquitectura** | MVVM + Clean Architecture |
| **Inyección de dependencias** | Hilt (Dagger) |
| **Navegación** | Navigation Compose con rutas type-safe |
| **Backend** | Supabase (Auth, Postgrest, Realtime) |
| **Networking** | Ktor + OkHttp |
| **Animaciones** | Lottie |
| **Serialización** | Kotlinx Serialization |

## Arquitectura

El proyecto sigue los principios de **Clean Architecture** con una estructura modular por features:

```
app/src/main/java/com/jeremiascortes/flowguide/
├── features/
│   └── [feature]/
│       ├── data/           # Implementaciones de repositorios, DTOs
│       ├── di/             # Módulos Hilt
│       ├── domain/         # Modelos, interfaces, casos de uso
│       └── presentation/   # ViewModels, Screens
└── navigation/             # Configuración de navegación
```

### Características implementadas

- Sistema de autenticación completo (email/password + Google OAuth)
- Gestión de sesión con verificación automática
- Navegación type-safe con animaciones fluidas
- Deep links para OAuth

## Autor

**Jeremías Cortés**
- GitHub: [@jeremiascortes](https://github.com/jeremiascortes)
- Email: jeremiasacortes@gmail.com

## Licencia

Este proyecto está protegido bajo una licencia personalizada de **Código Visible con Derechos Reservados (CVDR)**.

El código es públicamente visible con propósitos de portfolio, aprendizaje y revisión técnica. **No está permitido** usar, copiar o distribuir este código para crear productos derivados o comerciales.

Ver [LICENSE.md](LICENSE.md) para más detalles.

---

*Desarrollado como parte de mi portfolio profesional.*
