/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AccesoDatos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ManejadorArchivos {
    private final String RUTA_USUARIOS = "usuarios.txt";
    private final String RUTA_ACCESOS = "accesos.txt";

    public void guardarLinea(String linea, boolean esUsuario) throws IOException {
        String archivo = esUsuario ? RUTA_USUARIOS : RUTA_ACCESOS;
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(archivo, true)))) {
            out.println(linea);
        }
    }

    public List<String> leerTodo(boolean esUsuario) throws IOException {
        List<String> lineas = new ArrayList<>();
        String archivo = esUsuario ? RUTA_USUARIOS : RUTA_ACCESOS;
        File f = new File(archivo);
        if (!f.exists()) f.createNewFile();
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) lineas.add(linea);
            }
        }
        return lineas;
    }

    public void sobreescribirArchivo(List<String> lineas, boolean esUsuario) throws IOException {
        String archivo = esUsuario ? RUTA_USUARIOS : RUTA_ACCESOS;
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(archivo, false)))) {
            for (String s : lineas) out.println(s);
        }
    }
}