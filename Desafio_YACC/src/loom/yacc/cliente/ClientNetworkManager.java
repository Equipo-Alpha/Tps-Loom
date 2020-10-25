package loom.yacc.cliente;

import com.google.gson.Gson;
import loom.yacc.common.HistorialSala;
import loom.yacc.common.MensajeNetwork;

public class ClientNetworkManager {
    public static void processInput(String input) {
        MensajeNetwork message = (new Gson()).fromJson(input, MensajeNetwork.class);
        switch(message.getType()){
            case MENSAJE: mensaje(message); break;
            case HISTORIAL: historial(message); break;
        }
    }

    private static void mensaje(MensajeNetwork mensaje) {
        System.out.println(mensaje);
    }

    private static void historial(MensajeNetwork mensaje) {
        //TODO tendria que crear un archivo con el historial de la sala
        HistorialSala historia = (HistorialSala) mensaje.getMessage();
    }
}
