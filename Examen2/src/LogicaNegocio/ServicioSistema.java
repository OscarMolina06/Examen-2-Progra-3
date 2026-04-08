/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaNegocio;

import Entidades.Usuario;
import AccesoDatos.ManejadorArchivos;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServicioSistema {
    private ManejadorArchivos datos = new ManejadorArchivos();
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void registrarUsuario(String id, String nombre, String rol) throws Exception {
        if (id.isEmpty() || nombre.isEmpty()) throw new Exception("Debe completar todos los campos.");
        
        List<String> actuales = datos.leerTodo(true);
        for (String s : actuales) {
            String[] p = s.split(",");
            if (p[0].equals(id)) throw new Exception("El ID ya está registrado.");
            if (p[1].equalsIgnoreCase(nombre)) throw new Exception("El nombre ya está registrado.");
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

    public void eliminarUsuario(String id) throws Exception {
        List<String> lineas = datos.leerTodo(true);
        List<String> nuevas = new ArrayList<>();
        boolean encontrado = false;
        for (String s : lineas) {
            if (!s.split(",")[0].equals(id)) nuevas.add(s);
            else encontrado = true;
        }
        if (!encontrado) throw new Exception("Usuario no encontrado.");
        datos.sobreescribirArchivo(nuevas, true);
    }

    public void actualizarUsuario(String id, String nombre, String rol) throws Exception {
        if (id.isEmpty() || nombre.isEmpty()) throw new Exception("Debe seleccionar un usuario.");
        
        List<String> lineas = datos.leerTodo(true);
        List<String> nuevas = new ArrayList<>();
        boolean encontrado = false;

        for (String s : lineas) {
            String[] p = s.split(",");
            // Si el ID es diferente, el nombre no debe coincidir (evita duplicar nombres de otros)
            if (!p[0].equals(id) && p[1].equalsIgnoreCase(nombre)) {
                throw new Exception("El nombre ya pertenece a otro usuario.");
            }

            if (p[0].equals(id)) {
                nuevas.add(id + "," + nombre + "," + rol);
                encontrado = true;
            } else {
                nuevas.add(s);
            }
        }
        if (!encontrado) throw new Exception("No se encontró el registro original.");
        datos.sobreescribirArchivo(nuevas, true);
    }

    public void registrarEntrada(String id) throws Exception {
        boolean existe = false;
        for (String s : datos.leerTodo(true)) {
            if (s.split(",")[0].equals(id)) { existe = true; break; }
        }
        if (!existe) throw new Exception("El ID no corresponde a un usuario registrado.");

        for (String s : datos.leerTodo(false)) {
            String[] p = s.split("\\|");
            if (p[0].equals(id) && p[2].equals("NULL")) throw new Exception("El usuario ya se encuentra adentro.");
        }
        datos.guardarLinea(id + "|" + LocalDateTime.now().format(fmt) + "|NULL", false);
    }

    public String registrarSalida(String id) throws Exception {
        List<String> lineas = datos.leerTodo(false);
        List<String> nuevas = new ArrayList<>();
        boolean encontrado = false;
        String tiempo = "";

        for (String s : lineas) {
            String[] p = s.split("\\|");
            if (p[0].equals(id) && p[2].equals("NULL") && !encontrado) {
                LocalDateTime entrada = LocalDateTime.parse(p[1], fmt);
                LocalDateTime ahora = LocalDateTime.now();
                java.time.Duration d = java.time.Duration.between(entrada, ahora);
                tiempo = String.format("%02d:%02d:%02d", d.toHours(), d.toMinutesPart(), d.toSecondsPart());
                s = p[0] + "|" + p[1] + "|" + ahora.format(fmt);
                encontrado = true;
            }
            nuevas.add(s);
        }
        if (!encontrado) throw new Exception("No se encontró una entrada activa para este ID.");
        datos.sobreescribirArchivo(nuevas, false);
        return tiempo;
    }
}
