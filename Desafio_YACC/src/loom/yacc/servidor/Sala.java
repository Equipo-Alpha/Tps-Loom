package loom.yacc.servidor;

import loom.yacc.common.HistorialSala;
import loom.yacc.common.MensajeTipo;

import java.util.*;

public class Sala {
    private final int idSala;
    private final HistorialSala historial;
    private final ArrayList<ClientListener> clientes;
    private final Map<ClientListener, Date> tiempoClientes;
    private final ClientListener creador;
    public String nombre;

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

        for (ClientListener cliente : clientes) {
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

    public List<ClientListener> getClientes() {
        return Collections.unmodifiableList(clientes);
    }

    public void desconectarCliente(ClientListener cliente) {
        this.clientes.remove(cliente);
        cliente.quitarSala(this);
    }

    public Date getTiempoCliente(ClientListener cliente) {
        return tiempoClientes.get(cliente);
    }

    public void enviarHistorial(ClientListener cliente) {
        cliente.send(MensajeTipo.HISTORIAL, this.historial);
    }

}
