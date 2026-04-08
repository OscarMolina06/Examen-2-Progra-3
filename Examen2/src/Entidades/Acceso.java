/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

public class Acceso {
    private String idUsuario;
    private String fechaEntrada;
    private String fechaSalida;

    public Acceso(String idUsuario, String fechaEntrada, String fechaSalida) {
        this.idUsuario = idUsuario;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }

    public String getIdUsuario() { return idUsuario; }
    public String getFechaEntrada() { return fechaEntrada; }
    public String getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(String fechaSalida) { this.fechaSalida = fechaSalida; }

    @Override
    public String toString() { return idUsuario + "|" + fechaEntrada + "|" + (fechaSalida == null ? "NULL" : fechaSalida); }
}
