# FlowGuide — Android App

FlowGuide es una aplicación Android nativa desarrollada como proyecto personal para organizar procedimientos, espacios de trabajo y pasos de forma estructurada.

El objetivo principal del proyecto no es solo construir una app funcional, sino practicar y demostrar una arquitectura Android moderna, mantenible y escalable usando Kotlin, Jetpack Compose, Clean Architecture y Supabase.

> Proyecto en desarrollo pausado. Algunas funcionalidades pueden estar incompletas o sujetas a cambios.

---

## Sobre el proyecto

FlowGuide permite organizar información siguiendo una jerarquía sencilla:

Espacios de trabajo → Carpetas → Tareas/Procedimientos → Pasos

La idea es que un usuario pueda crear espacios, agrupar procedimientos en carpetas y consultar pasos de manera ordenada, incluyendo estados de completado.

Aunque es un proyecto personal, está planteado con una estructura cercana a una aplicación real: separación por features, capas bien definidas, gestión de estado, navegación type-safe y comunicación con backend.

---

## Objetivos técnicos

Este proyecto está pensado como una muestra práctica de:

- Desarrollo Android nativo con Kotlin
- UI declarativa con Jetpack Compose
- Arquitectura limpia por features
- Separación entre capas `data`, `domain` y `presentation`
- Gestión de estado con ViewModels y StateFlow
- Inyección de dependencias con Hilt
- Navegación type-safe con Navigation Compose
- Integración con Supabase para autenticación y persistencia
- Uso de patrones orientados a mantenibilidad y testabilidad

---

## Stack técnico

| Área | Tecnología |
|------|------------|
| Lenguaje | Kotlin 2.x |
| UI | Jetpack Compose |
| Diseño | Material 3 |
| Arquitectura | MVVM + Clean Architecture |
| DI | Hilt / Dagger |
| Navegación | Navigation Compose |
| Backend | Supabase Auth + PostgREST |
| Networking | Ktor + OkHttp |
| Animaciones | Lottie |
| Build | Gradle Kotlin DSL |

---

## Features implementadas

### Auth

- Login y registro con email y contraseña
- Inicio de sesión con Google OAuth
- Gestión de sesión
- Deep links para retorno de autenticación

### Home

- Visualización jerárquica de espacios, carpetas y procedimientos
- Carga lazy por niveles
- Expansión y colapso de elementos
- Creación de espacios y carpetas

### Procedure

- Visualización de pasos de un procedimiento
- Soporte para pasos anidados
- Marcado de pasos como completados
- Persistencia del estado en Supabase

### Settings

- Cierre de sesión
- Limpieza del back stack tras logout

---

## Decisiones técnicas destacadas

### Navegación type-safe

Las rutas están modeladas evitando strings mágicos y reduciendo errores en tiempo de ejecución relacionados con argumentos mal tipados.

### Estado de UI centralizado

Cada pantalla trabaja con un estado propio representado mediante una data class. Esto permite tener una única fuente de verdad para la UI y simplifica el mantenimiento.

### Eventos one-shot para navegación

Los eventos de navegación se gestionan de forma separada al estado de pantalla, evitando que acciones puntuales se reprocesen tras recomposiciones o cambios de configuración.

### Features autónomas

Cada feature agrupa sus propias capas de datos, dominio y presentación. Esto facilita escalar el proyecto sin convertir la estructura en una carpeta gigante de clases mezcladas.

### Backend desacoplado del dominio

Supabase se utiliza como backend, pero los detalles de implementación quedan aislados en la capa `data`. La lógica de dominio trabaja con interfaces y modelos propios.

---

## Estado actual

El proyecto se encuentra en desarrollo y todavía hay áreas por mejorar, entre ellas:

- Cobertura de tests
- Validaciones de formularios más completas
- Mejor manejo de errores globales
- Pulido visual de algunas pantallas
- Mejoras de accesibilidad
- Refactorización de algunos componentes reutilizables

---

## Qué demuestra este proyecto

Este repositorio puede servir como ejemplo de mi forma de trabajar en Android:

- Organización del código desde fases tempranas
- Interés por escribir código mantenible
- Uso de herramientas modernas del ecosistema Android
- Separación clara de responsabilidades
- Aprendizaje práctico aplicado a un proyecto real

No pretende ser una aplicación terminada ni un producto comercial, sino una muestra honesta de desarrollo Android en progreso.

---

## Changelog

El proyecto mantiene un historial de cambios en [CHANGELOG.md](CHANGELOG.md).

---

## Autor

**Jeremías Cortés**

[LinkedIn](https://linkedin.com/in/jeremias-cortes) · [Email](mailto:jeremiasacortes@gmail.com)

---

## Licencia

Código visible con derechos reservados. Ver [LICENSE.md](LICENSE.md).