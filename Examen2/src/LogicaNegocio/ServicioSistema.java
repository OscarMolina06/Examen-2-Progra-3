/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaNegocio;

import Entidades.*;
import AccesoDatos.ManejadorArchivos;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServicioSistema {
    private ManejadorArchivos datos = new ManejadorArchivos();
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void registrarUsuario(String id, String nombre, String rol) throws Exception {
        if (id.isEmpty() || nombre.isEmpty()) throw new Exception("Datos incompletos.");
        
        // Validar ID duplicado
        List<String> actuales = datos.leerTodo(true);
        for (String s : actuales) {
            if (s.split(",")[0].equals(id)) throw new Exception("ID ya existe.");
        }
        
        Usuario u = new Usuario(id, nombre, rol);
        datos.guardarLinea(u.toString(), true);
    }

    public void registrarEntrada(String id) throws Exception {
        if (id.isEmpty()) throw new Exception("Ingrese un ID.");
        
        // Verificar si el usuario existe
        boolean existe = false;
        for (String s : datos.leerTodo(true)) {
            if (s.split(",")[0].equals(id)) { existe = true; break; }
        }
        if (!existe) throw new Exception("Usuario no registrado.");

        // Verificar doble entrada
        List<String> accesos = datos.leerTodo(false);
        for (String s : accesos) {
            String[] p = s.split("\\|");
            if (p[0].equals(id) && p[2].equals("NULL")) throw new Exception("Ya tiene una entrada activa.");
        }

        String linea = id + "|" + LocalDateTime.now().format(fmt) + "|NULL";
        datos.guardarLinea(linea, false);
    }

    public void registrarSalida(String id) throws Exception {
        List<String> lineas = datos.leerTodo(false);
        boolean encontrado = false;
        List<String> nuevasLineas = new ArrayList<>();

        for (String s : lineas) {
            String[] p = s.split("\\|");
            if (p[0].equals(id) && p[2].equals("NULL") && !encontrado) {
                s = p[0] + "|" + p[1] + "|" + LocalDateTime.now().format(fmt);
                encontrado = true;
            }
            nuevasLineas.add(s);
        }

        if (!encontrado) throw new Exception("No hay entrada previa registrada.");
        datos.sobreescribirAccesos(nuevasLineas);
    }
}