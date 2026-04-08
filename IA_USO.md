Informe de Uso de Inteligencia Artificial - Sistema de Laboratorio
1. Prompts Utilizados (Resumen)
Para el desarrollo de este proyecto, se utilizaron los siguientes prompts de forma iterativa:

Prompt 1 (Interfaz Interactiva): "Haz que al seleccionar una fila en la JTable, los datos del usuario se carguen automáticamente en los campos de texto para poder editarlos o eliminarlos."

Prompt 2 (Validación de Datos): "Implementa restricciones mediante KeyListeners para que el campo ID solo acepte números y el campo Nombre solo acepte letras, bloqueando caracteres inválidos en tiempo real."

Prompt 3 (Lógica de Accesos): "Desarrolla la lógica para registrar entradas y salidas con marcas de tiempo (LocalDateTime), validando que no se pueda marcar salida sin entrada o doble entrada activa."

Prompt 4 (Reportes y Cálculos): "Crea una sección de historial que filtre por ID de usuario y calcule la suma total del tiempo de permanencia en formato HH:mm:ss a partir de las sesiones registradas."

Prompt 5 (Estructura UI): "Refactoriza la presentación usando un JTabbedPane para organizar la gestión de usuarios y la consulta de reportes en pestañas separadas."

2. Qué resolvió cada prompt
Prompt 1: Solucionó la comunicación entre la vista (Tabla) y el controlador del formulario, mejorando la experiencia de usuario (UX).

Prompt 2: Garantizó la integridad de los datos desde la entrada, evitando errores de formato antes de que la información llegue a la lógica de negocio.

Prompt 3: Resolvió las reglas de negocio críticas, asegurando un control de flujo lógico y coherente en el archivo accesos.txt.

Prompt 4: Implementó el procesamiento de datos complejos y operaciones matemáticas con la librería java.time para generar estadísticas de uso.

Prompt 5: Permitió una interfaz limpia, organizada y escalable siguiendo estándares modernos de aplicaciones de escritorio.

3. Ajustes realizados manualmente
A pesar de contar con el apoyo de la IA, se realizaron intervenciones manuales para asegurar la calidad del software:

Gestión de archivos: Se ajustaron los delimitadores de texto (cambio de , a | en el historial) para evitar conflictos con nombres que pudieran contener comas.

Seguridad de IDs: Se configuró manualmente el bloqueo de edición de los JTextField (setEditable(false)) al momento de actualizar para evitar cambios accidentales en la llave primaria.

Sincronización de Capas: Se ajustaron los nombres de métodos y variables para mantener la coherencia con el estándar de arquitectura por capas (Entidades, Acceso a Datos, Lógica de Negocio y Presentación).

Manejo de Errores: Se personalizaron los bloques try-catch para mostrar mensajes específicos y amigables mediante JOptionPane.

4. Justificación técnica del uso de IA
El uso de Inteligencia Artificial en este proyecto se fundamenta en:

Productividad: Agilización en la creación de componentes visuales repetitivos (Swing), permitiendo priorizar el diseño de la arquitectura.

Precisión Algorítmica: Facilidad para implementar cálculos de tiempo precisos entre objetos LocalDateTime y Duration, minimizando el error humano.

Calidad de Código: Adopción de mejores prácticas en la implementación de Listeners y manejo de eventos que garantizan un software robusto.

Escalabilidad: Generación de una estructura base modular que permite añadir nuevas funcionalidades al sistema de forma sencilla en el futuro.
