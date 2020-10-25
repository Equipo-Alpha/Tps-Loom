package loom.yacc.common;

import java.util.ArrayList;

public class HistorialSala {
    private final ArrayList<String> mensajes;
    public int idSala;

    public HistorialSala(ArrayList<String> mensajes, int id) {
        this.mensajes = mensajes;
        this.idSala = id;
    }

    public ArrayList<String> getMensajes() {
        return mensajes;
    }

    public void agregarMensaje(String mensaje) {
        this.mensajes.add(mensaje);
    }
}
