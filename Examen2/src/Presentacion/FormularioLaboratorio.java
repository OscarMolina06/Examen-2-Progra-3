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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FormularioLaboratorio extends JFrame {
    private final ServicioSistema negocio = new ServicioSistema();
    private JTextField txtID, txtNombre, txtIDAcceso;
    private JComboBox<String> cbRol;
    private JTable tablaUsuarios;
    private DefaultTableModel modelo;

    public FormularioLaboratorio() {
        super("Sistema de Control de Laboratorio Técnico");
        this.setSize(850, 650);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));

        // --- PANEL SUPERIOR: FORMULARIO ---
        JPanel pnlNorte = new JPanel(new GridLayout(5, 2, 5, 5));
        pnlNorte.setBorder(BorderFactory.createTitledBorder("Gestión de Usuarios"));
        
        txtID = new JTextField(); 
        txtNombre = new JTextField();
        cbRol = new JComboBox<>(new String[]{"Estudiante", "Docente"});
        
        // VALIDACIÓN: Solo números en ID
        txtID.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });

        // VALIDACIÓN: Solo letras y espacios en Nombre
        txtNombre.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
        
        pnlNorte.add(new JLabel(" ID (Solo números):")); pnlNorte.add(txtID);
        pnlNorte.add(new JLabel(" Nombre (Solo letras):")); pnlNorte.add(txtNombre);
        pnlNorte.add(new JLabel(" Rol:")); pnlNorte.add(cbRol);
        
        JButton btnCrear = new JButton("Registrar");
        JButton btnActu = new JButton("Actualizar");
        JButton btnElim = new JButton("Eliminar"); 
        JButton btnLimpiar = new JButton("Limpiar");

        JPanel pnlBotones = new JPanel();
        pnlBotones.add(btnCrear); 
        pnlBotones.add(btnActu); 
        pnlBotones.add(btnElim); 
        pnlBotones.add(btnLimpiar);
        
        pnlNorte.add(new JLabel(" Acciones:")); pnlNorte.add(pnlBotones);

        // --- PANEL CENTRAL: TABLA ---
        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Rol"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaUsuarios = new JTable(modelo);
        
        // EVENTO: Seleccionar de la tabla
        tablaUsuarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaUsuarios.getSelectedRow();
                if (fila != -1) {
                    txtID.setText(modelo.getValueAt(fila, 0).toString());
                    txtNombre.setText(modelo.getValueAt(fila, 1).toString());
                    cbRol.setSelectedItem(modelo.getValueAt(fila, 2).toString());
                    txtID.setEditable(false);
                    txtID.setBackground(new Color(230, 230, 230));
                }
            }
        });

        // --- PANEL INFERIOR: ACCESOS ---
        JPanel pnlSur = new JPanel();
        pnlSur.setBorder(BorderFactory.createTitledBorder("Registro de Accesos"));
        txtIDAcceso = new JTextField(12);
        
        // VALIDACIÓN: Solo números en ID de Acceso
        txtIDAcceso.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) e.consume();
            }
        });

        JButton btnEnt = new JButton("Entrada");
        JButton btnSal = new JButton("Salida");
        pnlSur.add(new JLabel("ID Usuario:")); pnlSur.add(txtIDAcceso);
        pnlSur.add(btnEnt); pnlSur.add(btnSal);

        this.add(pnlNorte, BorderLayout.NORTH);
        this.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);
        this.add(pnlSur, BorderLayout.SOUTH);

        // --- EVENTOS DE BOTONES ---

        btnCrear.addActionListener(e -> {
            try { 
                negocio.registrarUsuario(txtID.getText().trim(), txtNombre.getText().trim(), cbRol.getSelectedItem().toString()); 
                cargarTabla(); 
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Usuario registrado.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });

        btnActu.addActionListener(e -> {
            try { 
                negocio.actualizarUsuario(txtID.getText().trim(), txtNombre.getText().trim(), cbRol.getSelectedItem().toString()); 
                cargarTabla(); 
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Usuario actualizado.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });

        btnElim.addActionListener(e -> {
            String id = txtID.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar ID: " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try { 
                    negocio.eliminarUsuario(id); 
                    cargarTabla(); 
                    limpiarFormulario();
                    JOptionPane.showMessageDialog(this, "Usuario eliminado.");
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
            }
        });

        btnLimpiar.addActionListener(e -> limpiarFormulario());

        btnEnt.addActionListener(e -> {
            try { 
                negocio.registrarEntrada(txtIDAcceso.getText().trim()); 
                JOptionPane.showMessageDialog(this, "Entrada registrada."); 
                txtIDAcceso.setText("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        btnSal.addActionListener(e -> {
            try { 
                String tiempo = negocio.registrarSalida(txtIDAcceso.getText().trim()); 
                JOptionPane.showMessageDialog(this, "Salida registrada.\nTiempo total: " + tiempo); 
                txtIDAcceso.setText("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        try {
            modelo.setRowCount(0);
            List<Usuario> lista = negocio.consultarUsuarios();
            for (Usuario u : lista) {
                modelo.addRow(new Object[]{u.getId(), u.getNombre(), u.getRol()});
            }
        } catch (Exception e) {}
    }

    private void limpiarFormulario() {
        txtID.setText("");
        txtNombre.setText("");
        txtIDAcceso.setText("");
        txtID.setEditable(true);
        txtID.setBackground(Color.WHITE);
        tablaUsuarios.clearSelection();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        java.awt.EventQueue.invokeLater(() -> {
            new FormularioLaboratorio().setVisible(true);
        });
    }
}