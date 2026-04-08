/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import LogicaNegocio.ServicioSistema;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FormularioLaboratorio extends JFrame {

    // Instancia de la lógica (Cumpliendo la restricción de no tocar AccesoDatos)
    private final ServicioSistema negocio = new ServicioSistema();

    // Componentes de la interfaz
    private JTextField txtID, txtNombre, txtIDAcceso;
    private JComboBox<String> cbRol;
    private JButton btnRegistrar, btnEntrada, btnSalida;

    public FormularioLaboratorio() {
        super("Sistema de Control de Acceso a Laboratorio");
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        this.setSize(500, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        // Panel Superior: Registro de Usuarios
        JPanel panelRegistro = new JPanel(new GridLayout(4, 2, 5, 5));
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Gestión de Usuarios"));

        panelRegistro.add(new JLabel(" ID Usuario:"));
        txtID = new JTextField();
        panelRegistro.add(txtID);

        panelRegistro.add(new JLabel(" Nombre:"));
        txtNombre = new JTextField();
        panelRegistro.add(txtNombre);

        panelRegistro.add(new JLabel(" Rol:"));
        cbRol = new JComboBox<>(new String[]{"Estudiante", "Docente"});
        panelRegistro.add(cbRol);

        btnRegistrar = new JButton("Registrar Usuario");
        panelRegistro.add(new JLabel(""));
        panelRegistro.add(btnRegistrar);

        // Panel Inferior: Control de Accesos
        JPanel panelAcceso = new JPanel(new GridLayout(3, 2, 5, 5));
        panelAcceso.setBorder(BorderFactory.createTitledBorder("Control de Entrada/Salida"));

        panelAcceso.add(new JLabel(" ID para Acceso:"));
        txtIDAcceso = new JTextField();
        panelAcceso.add(txtIDAcceso);

        btnEntrada = new JButton("Registrar ENTRADA");
        btnEntrada.setBackground(new Color(144, 238, 144)); // Verde claro
        btnSalida = new JButton("Registrar SALIDA");
        btnSalida.setBackground(new Color(255, 182, 193)); // Rojo claro

        panelAcceso.add(btnEntrada);
        panelAcceso.add(new JLabel(""));
        panelAcceso.add(btnSalida);

        // Agregar paneles al frame
        this.add(panelRegistro, BorderLayout.NORTH);
        this.add(panelAcceso, BorderLayout.CENTER);

        // --- ASIGNACIÓN DE EVENTOS ---
        
        btnRegistrar.addActionListener((ActionEvent e) -> {
            try {
                negocio.registrarUsuario(txtID.getText(), txtNombre.getText(), cbRol.getSelectedItem().toString());
                JOptionPane.showMessageDialog(this, "Usuario registrado correctamente en TXT.");
                limpiarCampos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnEntrada.addActionListener((ActionEvent e) -> {
            try {
                negocio.registrarEntrada(txtIDAcceso.getText());
                JOptionPane.showMessageDialog(this, "¡Entrada registrada con éxito!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSalida.addActionListener((ActionEvent e) -> {
            try {
                negocio.registrarSalida(txtIDAcceso.getText());
                JOptionPane.showMessageDialog(this, "¡Salida registrada con éxito!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void limpiarCampos() {
        txtID.setText("");
        txtNombre.setText("");
        txtIDAcceso.setText("");
    }

    public static void main(String[] args) {
        // Establecer apariencia del sistema operativo
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        java.awt.EventQueue.invokeLater(() -> {
            new FormularioLaboratorio().setVisible(true);
        });
    }
}
