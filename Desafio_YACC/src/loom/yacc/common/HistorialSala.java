package loom.yacc.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistorialSala {
    private final ArrayList<String> mensajes;
    public int idSala;

    public HistorialSala(int id) {
        this.mensajes = new ArrayList<>();
        this.idSala = id;
    }

    public List<String> getMensajes() {
        return Collections.unmodifiableList(mensajes);
    }

    public void agregarMensaje(String mensaje) {
        this.mensajes.add(mensaje);
    }
}
