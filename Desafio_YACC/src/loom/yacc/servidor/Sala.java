package loom.yacc.servidor;

import java.util.ArrayList;

public class Sala {
    public String nombre;
    private final int idSala;
    private final ArrayList<String> mensajes;
    private final ArrayList<ClientListener> clientes;
    private final ClientListener creador;

    public Sala(String nombre, ClientListener creador, int id) {
        this.nombre = nombre;
        this.creador = creador;
        this.mensajes = new ArrayList<>();
        this.clientes = new ArrayList<>();
        clientes.add(creador);
        idSala = id;
    }

    public void recibirMensaje(String mensaje, ClientListener sender) {
        mensajes.add(mensaje);

        for(ClientListener cliente : clientes){
            if(cliente != sender){
                cliente.send(mensaje);
            }
        }
    }

    public void agregarCliente(ClientListener cliente) {
        clientes.add(cliente);
    }

    public void enviarHistorial(ClientListener cliente) {

    }

}
