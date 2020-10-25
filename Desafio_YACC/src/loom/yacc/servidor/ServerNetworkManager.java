package loom.yacc.servidor;

import com.google.gson.Gson;
import loom.yacc.common.MensajeNetwork;
import loom.yacc.common.MensajeTipo;

public class ServerNetworkManager {
    public static void processInput(ClientListener cliente, String input) {
        try{
            MensajeNetwork mensaje =  (new Gson()).fromJson(input, MensajeNetwork.class);
            MensajeTipo tipo = mensaje.getType();
            switch (tipo){
                case CONEXION: nuevaConexion(cliente, mensaje); break;
                case MENSAJE: nuevoMensaje(cliente, mensaje); break;
                default: cliente.send("No se pudo procesar el mensaje"); break;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //TODO agregar comandos, que son las funcionalidades
    private static void nuevaConexion(ClientListener cliente, MensajeNetwork mensaje) {
        cliente.nombre = (String) mensaje.getMessage();
        System.out.println("Nombre del cliente: " + cliente.nombre);
        cliente.send("Bienvenido " + cliente.nombre + ". Los camandos son los siguientes:\n" +
                "?:CREARSALA nombre\n" +
                "?:VERSALAS\n" +
                "?:UNIRSESALA");
    }

    private static void nuevoMensaje(ClientListener cliente, MensajeNetwork mensajeEntrante) {
        String msg = (String) mensajeEntrante.getMessage();
        if(msg.startsWith("?:")){
            msg = msg.replace("?:", "");
            procesarComando(cliente, msg);
            return;
        }

        if(cliente.estaEnUnaSala()) {
            String mensajeFormateado = "[" + mensajeEntrante.getTime().toLocalTime() + "] " + mensajeEntrante.getCliente() + ": " + mensajeEntrante.getMessage();
            cliente.getSalaActual().recibirMensaje(mensajeFormateado, cliente);
        }
        else {
            cliente.send("No se encuentra en ninguna sala, el mensaje no fue procesado");
        }
    }

    private static void procesarComando(ClientListener cliente,String mensaje){
        String[] comando = mensaje.split(" ");
        switch (comando[0]){
            case "CREARSALA":  crearSala(cliente, mensaje); break;
            case "UNIRSESALA": unirseSala(cliente, mensaje); break;
            default: cliente.send("Comando desconocido");
        }
    }

    private static void crearSala(ClientListener cliente, String nombre) {
        //TODO checkea que pueda crearla y la crea
    }

    private static void unirseSala(ClientListener cliente, String nombre) {
        //TODO checkea que pueda unirse y lo une
    }


}
