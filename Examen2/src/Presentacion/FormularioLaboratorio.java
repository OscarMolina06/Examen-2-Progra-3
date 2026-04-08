/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import Entidades.Usuario;
import LogicaNegocio.ServicioSistema;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class FormularioLaboratorio extends JFrame {
    private final ServicioSistema negocio = new ServicioSistema();
    
    // Variables de la interfaz
    private JTextField txtID, txtNombre, txtIDAcceso, txtBuscarHistorial;
    private JComboBox<String> cbRol;
    private JTable tablaUsuarios, tablaHistorial;
    private DefaultTableModel modeloUsuarios, modeloHistorial; // Asegúrate de que estos nombres coincidan

    public FormularioLaboratorio() {
        super("Sistema de Control de Laboratorio Técnico");
        this.setSize(900, 700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JTabbedPane pestanas = new JTabbedPane();

        // --- PESTAÑA 1: GESTIÓN DE USUARIOS Y ACCESOS ---
        JPanel pnlGestion = new JPanel(new BorderLayout(10, 10));
        
        // Formulario
        JPanel pnlNorte = new JPanel(new GridLayout(5, 2, 5, 5));
        pnlNorte.setBorder(BorderFactory.createTitledBorder("Datos de Usuario"));
        txtID = new JTextField(); 
        txtNombre = new JTextField();
        cbRol = new JComboBox<>(new String[]{"Estudiante", "Docente"});
        
        // Validaciones de teclado
        txtID.addKeyListener(new KeyAdapter() { public void keyTyped(KeyEvent e) { if(!Character.isDigit(e.getKeyChar())) e.consume(); }});
        txtNombre.addKeyListener(new KeyAdapter() { public void keyTyped(KeyEvent e) { if(Character.isDigit(e.getKeyChar())) e.consume(); }});

        pnlNorte.add(new JLabel(" ID:")); pnlNorte.add(txtID);
        pnlNorte.add(new JLabel(" Nombre:")); pnlNorte.add(txtNombre);
        pnlNorte.add(new JLabel(" Rol:")); pnlNorte.add(cbRol);
        
        JButton btnCrear = new JButton("Registrar");
        JButton btnActu = new JButton("Actualizar");
        JButton btnElim = new JButton("Eliminar");
        JPanel pnlBotones = new JPanel();
        pnlBotones.add(btnCrear); pnlBotones.add(btnActu); pnlBotones.add(btnElim);
        pnlNorte.add(new JLabel(" Acciones:")); pnlNorte.add(pnlBotones);

        // Tabla de Usuarios
        modeloUsuarios = new DefaultTableModel(new Object[]{"ID", "Nombre", "Rol"}, 0);
        tablaUsuarios = new JTable(modeloUsuarios);
        
        // Evento de selección
        tablaUsuarios.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int f = tablaUsuarios.getSelectedRow();
                if(f != -1){
                    txtID.setText(modeloUsuarios.getValueAt(f, 0).toString());
                    txtNombre.setText(modeloUsuarios.getValueAt(f, 1).toString());
                    cbRol.setSelectedItem(modeloUsuarios.getValueAt(f, 2).toString());
                    txtID.setEditable(false);
                }
            }
        });

        // Panel de Accesos (Abajo)
        JPanel pnlSur = new JPanel();
        pnlSur.setBorder(BorderFactory.createTitledBorder("Control de Accesos (Entrada/Salida)"));
        txtIDAcceso = new JTextField(10);
        JButton btnEnt = new JButton("Entrada");
        JButton btnSal = new JButton("Salida");
        pnlSur.add(new JLabel("ID Usuario:")); pnlSur.add(txtIDAcceso);
        pnlSur.add(btnEnt); pnlSur.add(btnSal);

        pnlGestion.add(pnlNorte, BorderLayout.NORTH);
        pnlGestion.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);
        pnlGestion.add(pnlSur, BorderLayout.SOUTH);

        // --- PESTAÑA 2: REPORTES E HISTORIAL ---
        JPanel pnlReporte = new JPanel(new BorderLayout(10, 10));
        pnlReporte.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel pnlFiltro = new JPanel();
        txtBuscarHistorial = new JTextField(10);
        JButton btnVerHistorial = new JButton("Ver Historial");
        pnlFiltro.add(new JLabel("ID a Consultar:")); pnlFiltro.add(txtBuscarHistorial);
        pnlFiltro.add(btnVerHistorial);

        modeloHistorial = new DefaultTableModel(new Object[]{"Entrada", "Salida", "Duración"}, 0);
        tablaHistorial = new JTable(modeloHistorial);
        
        pnlReporte.add(pnlFiltro, BorderLayout.NORTH);
        pnlReporte.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);

        // --- LÓGICA DE BOTONES ---
        btnCrear.addActionListener(e -> {
            try {
                negocio.registrarUsuario(txtID.getText().trim(), txtNombre.getText().trim(), cbRol.getSelectedItem().toString());
                cargarUsuarios();
                limpiar();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        btnEnt.addActionListener(e -> {
            try {
                negocio.registrarEntrada(txtIDAcceso.getText().trim());
                JOptionPane.showMessageDialog(this, "Entrada registrada.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        btnSal.addActionListener(e -> {
            try {
                String tiempo = negocio.registrarSalida(txtIDAcceso.getText().trim());
                JOptionPane.showMessageDialog(this, "Salida registrada. Tiempo: " + tiempo);
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        btnVerHistorial.addActionListener(e -> {
            try {
                modeloHistorial.setRowCount(0);
                List<String[]> lista = negocio.obtenerHistorialPorUsuario(txtBuscarHistorial.getText().trim());
                for(String[] r : lista) modeloHistorial.addRow(r);
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        pestanas.addTab("Gestión y Accesos", pnlGestion);
        pestanas.addTab("Historial/Reportes", pnlReporte);
        this.add(pestanas);
        
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        try {
            modeloUsuarios.setRowCount(0);
            for (Usuario u : negocio.consultarUsuarios()) {
                modeloUsuarios.addRow(new Object[]{u.getId(), u.getNombre(), u.getRol()});
            }
        } catch (Exception e) {
            System.out.println("Error al cargar tabla: " + e.getMessage());
        }
    }

    private void limpiar() {
        txtID.setText("");
        txtNombre.setText("");
        txtID.setEditable(true);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new FormularioLaboratorio().setVisible(true));
    }
}