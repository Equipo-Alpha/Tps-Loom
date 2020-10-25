package loom.yacc.cliente;

import java.io.BufferedReader;

public class ServerListener extends Thread{
    private final BufferedReader inputClient;

    public ServerListener(Cliente client) {
        inputClient = client.getInput();
    }

    public void run() {
        try {
            String input;
            while ((input = inputClient.readLine()) != null) {
                ClientNetworkManager.processInput(input);

            }
        } catch (Exception ex) {
            System.out.println("Fallo al recibir del servidor");
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
