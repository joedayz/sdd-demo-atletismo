# Requisitos

Este documento contiene los requisitos funcionales y no funcionales de la aplicación de Gestión de Competiciones de Atletismo (Pista y Campo).

## Requisitos Funcionales

| ID | Historia de Usuario | Prioridad | Estado |
|----|----------------------|-----------|--------|
| FR-001 | Como administrador, quiero crear, editar y eliminar competiciones para poder gestionar los eventos de atletismo. | Alta | No iniciado |
| FR-002 | Como administrador, quiero definir categorías con género, año de nacimiento mínimo y año de nacimiento máximo para que los atletas puedan agruparse correctamente. | Alta | No iniciado |
| FR-003 | Como administrador, quiero asignar pruebas (eventos) a las categorías para definir en qué competencias participarán los atletas de cada categoría. | Alta | No iniciado |
| FR-004 | Como administrador, quiero registrar atletas con su información personal (nombre, año de nacimiento y género) para que puedan participar en las competiciones. | Alta | No iniciado |
| FR-005 | Como administrador, quiero asignar atletas a clubes para mantener el registro de sus afiliaciones. | Media | No iniciado |
| FR-006 | Como sistema, quiero asignar automáticamente a los atletas a una categoría según su año de nacimiento y género para garantizar una clasificación consistente y precisa. | Alta | No iniciado |
| FR-007 | Como administrador, quiero registrar los resultados de cada prueba para cada atleta con el fin de llevar el seguimiento de su desempeño. | Alta | No iniciado |
| FR-008 | Como sistema, quiero calcular los puntos a partir de los resultados de las pruebas utilizando las fórmulas oficiales de puntuación de la IAAF para medir objetivamente el rendimiento de los atletas. | Alta | No iniciado |
| FR-009 | Como sistema, quiero clasificar automáticamente a los atletas dentro de su categoría una vez finalizadas todas las pruebas para determinar a los ganadores. | Alta | No iniciado |
| FR-010 | Como usuario, quiero visualizar las listas de clasificación agrupadas por categoría, mostrando el nombre del atleta, los resultados de cada prueba y el puntaje total, para conocer los resultados de la competición. | Alta | No iniciado |
| FR-011 | Como usuario, quiero consultar los resultados y la puntuación de cada prueba para cada atleta dentro de la clasificación, para comprender cómo se obtuvo la clasificación final. | Media | No iniciado |
| FR-012 | Como administrador, quiero editar o eliminar los registros de atletas para corregir errores o retirar participantes. | Media | No iniciado |
| FR-013 | Como administrador, quiero editar o eliminar los registros de clubes para mantener actualizada la información de los clubes. | Baja | No iniciado |
| FR-014 | Como sistema, quiero garantizar que el cálculo de puntos según la IAAF sea preciso y cumpla con las fórmulas oficiales para asegurar resultados válidos y justos. | Alta | No iniciado |

## Requisitos No Funcionales

| ID | Requisito | Prioridad | Estado |
|----|-----------|-----------|--------|
| NFR-001 | La interfaz de usuario debe ser intuitiva y accesible para los administradores, requiriendo una capacitación mínima. | Media | No iniciado |
| NFR-002 | La aplicación debe seguir principios de diseño responsivo para funcionar correctamente en equipos de escritorio y tabletas. | Alta | No iniciado |
| NFR-003 | La aplicación debe mantener la integridad de los datos y prevenir conflictos por modificaciones concurrentes cuando varios administradores trabajen simultáneamente. | Media | No iniciado |

## Definición de Estados

- **No iniciado**: El requisito aún no ha comenzado a implementarse.
- **En progreso**: La implementación del requisito ha comenzado.
- **Completado**: El requisito ha sido implementado y probado completamente.
- **Bloqueado**: La implementación está detenida debido a dependencias o problemas pendientes.

## Definición de Prioridades

- **Alta**: Requisito crítico que debe implementarse para que la aplicación funcione correctamente.
- **Media**: Requisito importante que mejora significativamente la funcionalidad.
- **Baja**: Requisito deseable que aporta valor adicional, pero no es esencial para el funcionamiento de la aplicación.