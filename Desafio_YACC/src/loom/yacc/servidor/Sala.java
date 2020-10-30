package loom.yacc.servidor;

import loom.yacc.common.HistorialSala;
import loom.yacc.common.MensajeTipo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Sala {
    public String nombre;
    private final int idSala;
    private final HistorialSala historial;
    private final ArrayList<ClientListener> clientes;
    private final Map<ClientListener, Date> tiempoClientes;
    private final ClientListener creador;

    public Sala(String nombre, ClientListener creador, int id) {
        this.nombre = nombre;
        this.creador = creador;
        idSala = id;
        this.historial = new HistorialSala(this.idSala);
        this.clientes = new ArrayList<>();
        this.tiempoClientes = new HashMap<>();
        agregarCliente(creador);
    }

    public void recibirMensaje(String mensaje) {
        historial.agregarMensaje(mensaje);

        for(ClientListener cliente : clientes){
            cliente.send(mensaje);
        }
    }

    public int getIdSala() {
        return idSala;
    }

    public int getCantClientes() {
        return clientes.size();
    }

    public void agregarCliente(ClientListener cliente) {
        clientes.add(cliente);
        Date tiempo = new Date(System.currentTimeMillis());
        tiempoClientes.put(cliente, tiempo);
    }

    public ArrayList<ClientListener> getClientes() {
        return clientes;
    }

    public Date getTiempoCliente(ClientListener cliente) {
        return tiempoClientes.get(cliente);
    }

    public void enviarHistorial(ClientListener cliente) {
        cliente.send(MensajeTipo.HISTORIAL, this.historial);
    }

}
