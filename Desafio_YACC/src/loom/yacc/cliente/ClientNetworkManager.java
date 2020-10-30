package loom.yacc.cliente;

import com.google.gson.Gson;
import loom.yacc.common.HistorialSala;
import loom.yacc.common.MensajeNetwork;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;

public class ClientNetworkManager {
    public void processInput(String input) {
        MensajeNetwork message = (new Gson()).fromJson(input, MensajeNetwork.class);
        switch (message.getType()) {
            case MENSAJE:
                mensaje(message);
                break;
            case HISTORIAL:
                historial(message);
                break;
        }
    }

    private static void mensaje(MensajeNetwork mensaje) {
        System.out.println(mensaje);
    }

    private static void historial(MensajeNetwork mensaje) {
        HistorialSala historia = mensaje.getHistorial();
        FileWriter archivo = null;
        PrintWriter pw = null;
        Time time = new Time(System.currentTimeMillis());

        try {
            String hora = time.toLocalTime().getHour() + "-" + time.toLocalTime().getMinute() + "-" + time.toLocalTime().getSecond();
            archivo = new FileWriter("historial_" + historia.idSala + "_" + hora + ".txt");
            pw = new PrintWriter(archivo);

            for (String msj : historia.getMensajes()) {
                pw.println(msj);
            }

            System.out.println("Archivo de historial generado correctamente");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (archivo != null && pw != null) {
                try {
                    archivo.close();
                    pw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
