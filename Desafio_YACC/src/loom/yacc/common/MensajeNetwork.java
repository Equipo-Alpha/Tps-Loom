package loom.yacc.common;

import java.sql.Time;

public class MensajeNetwork {
    private final Time time;
    private final MensajeTipo tipo;
    private final String nombreCliente;
    private final Object message;

    public MensajeNetwork(MensajeTipo tipo, String nombreCliente, Object message) {
        this.time = new Time(System.currentTimeMillis());
        this.tipo = tipo;
        this.nombreCliente = nombreCliente;
        this.message = message;
    }

    public MensajeNetwork(MensajeTipo tipo, Object message) {
        this(tipo, null, message);
    }

    public MensajeNetwork(MensajeTipo tipo, String cliente) {
        this(tipo, cliente, null);
    }

    public MensajeNetwork(MensajeTipo tipo) {
        this(tipo, null, null);
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

    @Override
    public String toString() {
        return "" + message;
    }
}
