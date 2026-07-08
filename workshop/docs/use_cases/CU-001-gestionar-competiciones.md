# Caso de Uso: Gestionar Competiciones

## Información General

**ID del Caso de Uso:** CU-001  
**Nombre del Caso de Uso:** Gestionar Competiciones  
**Actor Principal:** Administrador  
**Objetivo:** Permitir a los administradores crear, consultar, editar y eliminar competiciones de atletismo.  
**Estado:** No iniciado

---

# Precondiciones

- El administrador ha iniciado sesión y posee los permisos correspondientes.
- El sistema se encuentra operativo y la base de datos está disponible.

---

# Flujo Principal de Éxito

1. El administrador accede a la interfaz de gestión de competiciones.
2. El sistema muestra la lista de competiciones existentes junto con su información principal (nombre, fecha, ubicación y estado).
3. El administrador selecciona la opción **Crear nueva competición**.
4. El sistema presenta el formulario de registro de competición.
5. El administrador ingresa la siguiente información:
    - Nombre (obligatorio)
    - Fecha (obligatoria)
    - Ubicación (obligatoria)
    - Estado (valor predeterminado: **Planificada**)
6. El administrador envía el formulario.
7. El sistema valida la información ingresada.
8. El sistema crea el registro de la competición asignándole un identificador único.
9. El sistema muestra un mensaje de confirmación y actualiza la lista de competiciones.
10. Finaliza el caso de uso.

---

# Flujos Alternativos

## A1: Editar una Competición

**Disparador:** El administrador selecciona la opción **Editar** en el paso 3.

### Flujo

1. El sistema recupera la información actual de la competición y la muestra en un formulario editable.
2. El administrador modifica uno o más campos.
3. El administrador guarda los cambios.
4. El sistema valida la información actualizada.
5. El sistema actualiza el registro de la competición.
6. El sistema muestra un mensaje de éxito.
7. El flujo retorna al paso 9 del flujo principal.

---

## A2: Eliminar una Competición

**Disparador:** El administrador selecciona la opción **Eliminar** en el paso 3.

### Flujo

1. El sistema muestra un cuadro de confirmación advirtiendo sobre la eliminación.
2. El administrador confirma la operación.
3. El sistema verifica si la competición posee información relacionada (categorías, atletas o resultados).
4. El sistema elimina el registro de la competición.
5. El sistema muestra un mensaje de éxito.
6. El flujo retorna al paso 9 del flujo principal.

---

## A3: Error de Validación

**Disparador:** La validación del sistema falla en el paso 7 o en el paso A1.4.

### Flujo

1. El sistema muestra uno o varios mensajes indicando los campos con errores.
2. El formulario permanece abierto conservando la información ingresada.
3. El flujo retorna al paso 5 (o al paso A1.2 en el caso de edición).

---

## A4: Cancelar la Operación

**Disparador:** El administrador cancela la creación o edición durante el paso 5, 6 o A1.2.

### Flujo

1. El sistema descarta todos los cambios no guardados.
2. El sistema regresa a la lista de competiciones.
3. El flujo retorna al paso 2.

---

## A5: Consultar los Detalles de una Competición

**Disparador:** El administrador selecciona la opción **Ver detalles** en el paso 3.

### Flujo

1. El sistema muestra la información detallada de la competición, incluyendo:
    - Todos los atributos de la competición.
    - Categorías asociadas.
    - Cantidad de atletas registrados.
    - Resumen de los resultados registrados.
2. El administrador revisa la información.
3. El administrador puede editar la competición o regresar a la lista.
4. El flujo retorna al paso 2.

---

# Postcondiciones

## En caso de éxito

- Para la creación:
    - Existe un nuevo registro de competición en la base de datos con un identificador único y estado **Planificada**.

- Para la edición:
    - La competición queda actualizada con la información modificada.

- Para la eliminación:
    - La competición y todos los datos dependientes son eliminados de la base de datos.

Además:

- La lista de competiciones refleja el estado actualizado de todas las competiciones.
- Todos los cambios quedan almacenados de forma permanente en la base de datos.

---

## En caso de fallo

- No se realiza ninguna modificación en la base de datos.
- El usuario recibe mensajes claros indicando el error.
- El sistema permanece en un estado consistente.
- El usuario puede corregir la información y volver a intentarlo o cancelar la operación.

---

# Reglas de Negocio

## RN-001: Unicidad del Nombre de la Competición

- El nombre de una competición debe ser único dentro del sistema.
- No se permiten nombres duplicados.

---

## RN-002: Validez de la Fecha

- Al crear una competición, la fecha no puede ser anterior a la fecha actual.
- Al editar una competición, se permite asignar fechas pasadas para mantener registros históricos.

---

## RN-003: Campos Obligatorios

Los siguientes campos son obligatorios:

- Nombre
- Fecha
- Ubicación

Si no se especifica un estado, el sistema asignará automáticamente el estado **Planificada**.

---

## RN-004: Estados Permitidos

Los estados válidos para una competición son:

- Planificada
- En curso
- Finalizada
- Cancelada

Las transiciones entre estados deben respetar una secuencia lógica, por ejemplo:

```
Planificada → En curso → Finalizada
```

---

## RN-005: Restricciones para la Eliminación

- Si la competición posee resultados registrados, el sistema debe mostrar una advertencia antes de eliminarla.
- El sistema debe solicitar la confirmación explícita del administrador.
- Al eliminar una competición, también deben eliminarse sus entidades relacionadas (categorías, pruebas y resultados).

---

## RN-006: Validación de Datos

- **Nombre:** entre 1 y 255 caracteres.
- **Ubicación:** entre 1 y 255 caracteres.
- **Fecha:** debe corresponder a una fecha válida.

---

## RN-007: Auditoría

El sistema debe registrar todas las operaciones de:

- Creación
- Modificación
- Eliminación

Cada registro de auditoría debe incluir:

- Fecha y hora de la operación.
- Usuario que realizó la acción.