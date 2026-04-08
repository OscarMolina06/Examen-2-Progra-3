/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaNegocio;

import Entidades.Usuario;
import AccesoDatos.ManejadorArchivos;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ServicioSistema {
    private ManejadorArchivos datos = new ManejadorArchivos();
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // --- GESTIÓN DE USUARIOS ---

    public void registrarUsuario(String id, String nombre, String rol) throws Exception {
        if (id.isEmpty() || nombre.isEmpty()) throw new Exception("Todos los campos son obligatorios.");
        
        List<String> actuales = datos.leerTodo(true);
        for (String s : actuales) {
            String[] p = s.split(",");
            // Validar ID único
            if (p[0].equals(id)) throw new Exception("El ID '" + id + "' ya está registrado.");
            // Validar Nombre único (ignorando mayúsculas)
            if (p[1].equalsIgnoreCase(nombre)) throw new Exception("El nombre '" + nombre + "' ya existe.");
        }
        datos.guardarLinea(id + "," + nombre + "," + rol, true);
    }

    public List<Usuario> consultarUsuarios() throws Exception {
        List<String> lineas = datos.leerTodo(true);
        List<Usuario> lista = new ArrayList<>();
        for (String s : lineas) {
            String[] p = s.split(",");
            lista.add(new Usuario(p[0], p[1], p[2]));
        }
        return lista;
    }

    public void actualizarUsuario(String id, String nombre, String rol) throws Exception {
        if (id.isEmpty() || nombre.isEmpty()) throw new Exception("Datos incompletos para actualizar.");
        
        List<String> lineas = datos.leerTodo(true);
        List<String> nuevas = new ArrayList<>();
        boolean encontrado = false;

        for (String s : lineas) {
            String[] p = s.split(",");
            // Validar que el nuevo nombre no lo use OTRO usuario
            if (!p[0].equals(id) && p[1].equalsIgnoreCase(nombre)) {
                throw new Exception("El nombre ya está siendo usado por otro usuario.");
            }

            if (p[0].equals(id)) {
                nuevas.add(id + "," + nombre + "," + rol);
                encontrado = true;
            } else {
                nuevas.add(s);
            }
        }
        if (!encontrado) throw new Exception("Usuario no encontrado.");
        datos.sobreescribirArchivo(nuevas, true);
    }

    public void eliminarUsuario(String id) throws Exception {
        List<String> lineas = datos.leerTodo(true);
        List<String> nuevas = new ArrayList<>();
        boolean encontrado = false;
        for (String s : lineas) {
            if (!s.split(",")[0].equals(id)) nuevas.add(s);
            else encontrado = true;
        }
        if (!encontrado) throw new Exception("No se encontró el usuario para eliminar.");
        datos.sobreescribirArchivo(nuevas, true);
    }

    // --- REGISTRO DE ACCESOS Y REPORTES ---

    public void registrarEntrada(String id) throws Exception {
        // 1. Verificar existencia del usuario
        boolean existe = false;
        for (String s : datos.leerTodo(true)) {
            if (s.split(",")[0].equals(id)) { existe = true; break; }
        }
        if (!existe) throw new Exception("El ID ingresado no pertenece a ningún usuario.");

        // 2. Regla: No permitir doble entrada sin salida
        for (String s : datos.leerTodo(false)) {
            String[] p = s.split("\\|");
            if (p[0].equals(id) && p[2].equals("NULL")) {
                throw new Exception("El usuario ya tiene una entrada activa.");
            }
        }
        
        // Registrar: ID | FechaEntrada | NULL (porque aún no sale)
        datos.guardarLinea(id + "|" + LocalDateTime.now().format(fmt) + "|NULL", false);
    }

    public String registrarSalida(String id) throws Exception {
        List<String> lineas = datos.leerTodo(false);
        List<String> nuevas = new ArrayList<>();
        boolean encontrado = false;
        String tiempoCalculado = "";

        for (String s : lineas) {
            String[] p = s.split("\\|");
            // Buscar la entrada activa (marcada con NULL en la salida)
            if (p[0].equals(id) && p[2].equals("NULL") && !encontrado) {
                LocalDateTime entrada = LocalDateTime.parse(p[1], fmt);
                LocalDateTime ahora = LocalDateTime.now();
                
                // Calcular duración
                Duration d = Duration.between(entrada, ahora);
                tiempoCalculado = String.format("%02d:%02d:%02d", d.toHours(), d.toMinutesPart(), d.toSecondsPart());
                
                // Actualizar línea con la fecha de salida
                s = p[0] + "|" + p[1] + "|" + ahora.format(fmt);
                encontrado = true;
            }
            nuevas.add(s);
        }

        if (!encontrado) throw new Exception("Error: No existe una entrada previa para este usuario.");
        
        datos.sobreescribirArchivo(nuevas, false);
        return tiempoCalculado;
    }

    public List<String[]> obtenerHistorialPorUsuario(String id) throws Exception {
        if (id.isEmpty()) throw new Exception("Ingrese un ID para buscar historial.");
        
        List<String[]> historial = new ArrayList<>();
        for (String s : datos.leerTodo(false)) {
            String[] p = s.split("\\|");
            if (p[0].equals(id)) {
                String duracion = "En curso...";
                if (!p[2].equals("NULL")) {
                    LocalDateTime inicio = LocalDateTime.parse(p[1], fmt);
                    LocalDateTime fin = LocalDateTime.parse(p[2], fmt);
                    Duration d = Duration.between(inicio, fin);
                    duracion = String.format("%02d:%02d:%02d", d.toHours(), d.toMinutesPart(), d.toSecondsPart());
                }
                // Retornamos: {FechaEntrada, FechaSalida, TiempoTotal}
                historial.add(new String[]{p[1], p[2], duracion});
            }
        }
        return historial;
    }
}
