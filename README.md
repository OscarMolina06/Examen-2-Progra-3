# 🛠️ Sistema de Control de Laboratorio Técnico

¡Bienvenido al **Sistema de Control de Laboratorio**! Esta es una aplicación de escritorio desarrollada en **Java** diseñada para gestionar el acceso de estudiantes y docentes a laboratorios técnicos, permitiendo un control riguroso de entradas, salidas y tiempos de permanencia.

---

## 🚀 Características Principales

### 👥 Gestión de Usuarios
- **CRUD Completo:** Registro, consulta, actualización y eliminación de usuarios.
- **Validación en Tiempo Real:** El sistema impide el ingreso de letras en campos numéricos (ID) y números en campos de texto (Nombre).
- **Interfaz Interactiva:** Selección de usuarios directamente desde la tabla para edición rápida.

### 🔑 Control de Accesos
- **Registro de Entrada:** Captura fecha y hora exacta de ingreso.
- **Registro de Salida:** Calcula automáticamente el tiempo de estancia.
- **Reglas de Negocio:** - No permite salidas sin una entrada previa.
  - Evita duplicidad de entradas activas para un mismo usuario.

### 📊 Reportes e Historial
- **Historial Detallado:** Consulta de todos los movimientos de un usuario específico.
- **Tiempo Acumulado:** Cálculo automático del tiempo total que un usuario ha pasado en el laboratorio a lo largo de todas sus sesiones.

---

## 🏗️ Arquitectura del Proyecto

El sistema sigue un patrón de diseño por **capas**, lo que facilita su mantenimiento y escalabilidad:

* **`Presentacion`**: Interfaz gráfica desarrollada con `Swing` y `JTabbedPane`.
* **`LogicaNegocio`**: Procesamiento de reglas, validaciones y cálculos de tiempo (`java.time`).
* **`Entidades`**: Modelos de datos (`Usuario`).
* **`AccesoDatos`**: Persistencia mediante archivos de texto plano (`.txt`).

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Java 17+
- **IDE:** NetBeans / IntelliJ
- **Interfaz:** Java Swing
- **Persistencia:** I/O de archivos (Manejador de Archivos propio)

---

## 📦 Instalación y Ejecución

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/tu-usuario/nombre-del-repo.git](https://github.com/tu-usuario/nombre-del-repo.git)
    ```
2.  **Abrir en tu IDE favorito** (NetBeans recomendado).
3.  **Ejecutar el archivo principal:**
    Localizado en `Presentacion/FormularioLaboratorio.java`.

---

## 📝 Notas de Uso
- Los datos de los usuarios se guardan en `usuarios.txt`.
- El historial de accesos se almacena en `accesos.txt`.
- ¡Asegúrate de no borrar estos archivos para no perder la información!

---
Desarrollado por [Oscar] - 2026 🚀
