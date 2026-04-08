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
    
    // Componentes Gestión
    private JTextField txtID, txtNombre, txtIDAcceso;
    private JComboBox<String> cbRol;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloUsuarios;

    // Componentes Reportes
    private JTextField txtBuscarHistorial;
    private JTable tablaHistorial;
    private DefaultTableModel modeloHistorial;
    private JLabel lblTiempoTotal;

    public FormularioLaboratorio() {
        super("Sistema de Control de Laboratorio Técnico");
        this.setSize(950, 700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JTabbedPane pestanas = new JTabbedPane();

        // --- PESTAÑA 1: GESTIÓN Y ACCESOS ---
        pestanas.addTab("Gestión y Accesos", crearPanelGestion());

        // --- PESTAÑA 2: REPORTES E HISTORIAL ---
        pestanas.addTab("Historial y Tiempos", crearPanelReportes());

        this.add(pestanas);
        cargarUsuarios();
    }

    private JPanel crearPanelGestion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Formulario Superior
        JPanel pnlNorte = new JPanel(new GridLayout(5, 2, 5, 5));
        pnlNorte.setBorder(BorderFactory.createTitledBorder("Registro de Usuarios"));
        txtID = new JTextField(); 
        txtNombre = new JTextField();
        cbRol = new JComboBox<>(new String[]{"Estudiante", "Docente"});
        
        // Validaciones: Solo números en ID, solo letras en Nombre
        txtID.addKeyListener(new KeyAdapter() { public void keyTyped(KeyEvent e) { if(!Character.isDigit(e.getKeyChar())) e.consume(); }});
        txtNombre.addKeyListener(new KeyAdapter() { public void keyTyped(KeyEvent e) { if(Character.isDigit(e.getKeyChar())) e.consume(); }});

        pnlNorte.add(new JLabel(" ID (Solo números):")); pnlNorte.add(txtID);
        pnlNorte.add(new JLabel(" Nombre Completo:")); pnlNorte.add(txtNombre);
        pnlNorte.add(new JLabel(" Rol:")); pnlNorte.add(cbRol);
        
        JButton btnCrear = new JButton("Registrar");
        JButton btnActu = new JButton("Actualizar");
        JButton btnElim = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        
        JPanel pnlBotones = new JPanel();
        pnlBotones.add(btnCrear); pnlBotones.add(btnActu); pnlBotones.add(btnElim); pnlBotones.add(btnLimpiar);
        pnlNorte.add(new JLabel(" Acciones:")); pnlNorte.add(pnlBotones);

        // Tabla Usuarios
        modeloUsuarios = new DefaultTableModel(new Object[]{"ID", "Nombre", "Rol"}, 0);
        tablaUsuarios = new JTable(modeloUsuarios);
        tablaUsuarios.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int f = tablaUsuarios.getSelectedRow();
                if(f != -1){
                    txtID.setText(modeloUsuarios.getValueAt(f, 0).toString());
                    txtNombre.setText(modeloUsuarios.getValueAt(f, 1).toString());
                    cbRol.setSelectedItem(modeloUsuarios.getValueAt(f, 2).toString());
                    txtID.setEditable(false);
                    txtID.setBackground(new Color(230, 230, 230));
                }
            }
        });

        // Panel Accesos (Sur)
        JPanel pnlSur = new JPanel();
        pnlSur.setBorder(BorderFactory.createTitledBorder("Control de Entrada/Salida"));
        txtIDAcceso = new JTextField(12);
        JButton btnEnt = new JButton("Entrada");
        JButton btnSal = new JButton("Salida");
        pnlSur.add(new JLabel("ID Usuario:")); pnlSur.add(txtIDAcceso);
        pnlSur.add(btnEnt); pnlSur.add(btnSal);

        // Eventos
        btnCrear.addActionListener(e -> {
            try { negocio.registrarUsuario(txtID.getText().trim(), txtNombre.getText().trim(), cbRol.getSelectedItem().toString()); cargarUsuarios(); limpiar(); } catch(Exception ex){ JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        btnActu.addActionListener(e -> {
            try { negocio.actualizarUsuario(txtID.getText().trim(), txtNombre.getText().trim(), cbRol.getSelectedItem().toString()); cargarUsuarios(); limpiar(); } catch(Exception ex){ JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        btnElim.addActionListener(e -> {
            try { negocio.eliminarUsuario(txtID.getText().trim()); cargarUsuarios(); limpiar(); } catch(Exception ex){ JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        btnLimpiar.addActionListener(e -> limpiar());
        btnEnt.addActionListener(e -> {
            try { negocio.registrarEntrada(txtIDAcceso.getText().trim()); JOptionPane.showMessageDialog(this, "Entrada registrada."); } catch(Exception ex){ JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        btnSal.addActionListener(e -> {
            try { String t = negocio.registrarSalida(txtIDAcceso.getText().trim()); JOptionPane.showMessageDialog(this, "Salida registrada. Tiempo: " + t); } catch(Exception ex){ JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        panel.add(pnlNorte, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);
        panel.add(pnlSur, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel pnlFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscarHistorial = new JTextField(15);
        JButton btnBuscar = new JButton("Consultar Reporte");
        pnlFiltro.add(new JLabel("ID a consultar:")); pnlFiltro.add(txtBuscarHistorial);
        pnlFiltro.add(btnBuscar);

        modeloHistorial = new DefaultTableModel(new Object[]{"Entrada", "Salida", "Duración"}, 0);
        tablaHistorial = new JTable(modeloHistorial);

        lblTiempoTotal = new JLabel("TIEMPO TOTAL ACUMULADO: 00:00:00");
        lblTiempoTotal.setFont(new Font("Arial", Font.BOLD, 15));
        lblTiempoTotal.setForeground(new Color(0, 102, 204));

        btnBuscar.addActionListener(e -> {
            try {
                modeloHistorial.setRowCount(0);
                List<String[]> historial = negocio.obtenerHistorialPorUsuario(txtBuscarHistorial.getText().trim());
                long segTotales = 0;
                for (String[] r : historial) {
                    modeloHistorial.addRow(r);
                    if (!r[2].contains("curso")) {
                        String[] t = r[2].split(":");
                        segTotales += (Long.parseLong(t[0]) * 3600) + (Long.parseLong(t[1]) * 60) + Long.parseLong(t[2]);
                    }
                }
                long h = segTotales / 3600;
                long m = (segTotales % 3600) / 60;
                long s = segTotales % 60;
                lblTiempoTotal.setText(String.format("TIEMPO TOTAL ACUMULADO: %02d:%02d:%02d", h, m, s));
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        panel.add(pnlFiltro, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);
        panel.add(lblTiempoTotal, BorderLayout.SOUTH);
        return panel;
    }

    private void cargarUsuarios() {
        try {
            modeloUsuarios.setRowCount(0);
            for (Usuario u : negocio.consultarUsuarios()) {
                modeloUsuarios.addRow(new Object[]{u.getId(), u.getNombre(), u.getRol()});
            }
        } catch (Exception e) {}
    }

    private void limpiar() {
        txtID.setText(""); txtNombre.setText(""); txtID.setEditable(true);
        txtID.setBackground(Color.WHITE); tablaUsuarios.clearSelection();
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        java.awt.EventQueue.invokeLater(() -> new FormularioLaboratorio().setVisible(true));
    }
}