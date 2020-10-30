package loom.yacc.common;

import java.sql.Time;

public class MensajeNetwork {
    private final Time time;
    private final MensajeTipo tipo;
    private final String nombreCliente;
    private final Object message;
    private final HistorialSala historial; // el object me tira error al castear

    public MensajeNetwork(MensajeTipo tipo, String nombreCliente, Object message) {
        this.time = new Time(System.currentTimeMillis());
        this.tipo = tipo;
        this.nombreCliente = nombreCliente;
        this.message = message;
        this.historial = null;
    }

    public MensajeNetwork(MensajeTipo tipo, Object message) {
        this(tipo, null, message);
    }

    public MensajeNetwork(MensajeTipo tipo, HistorialSala historial) {
        this.time = new Time(System.currentTimeMillis());
        this.tipo = tipo;
        this.nombreCliente = null;
        this.message = null;
        this.historial = historial;
    }

    public MensajeTipo getType() {
        return tipo;
    }

    public Time getTime() {
        return time;
    }

    public String getCliente() {
        return nombreCliente;
    }

    public Object getMessage() {
        return message;
    }

    public HistorialSala getHistorial() {
        return historial;
    }

    @Override
    public String toString() {
        return "" + message;
    }
}
