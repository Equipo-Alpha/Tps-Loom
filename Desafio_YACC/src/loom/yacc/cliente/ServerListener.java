package loom.yacc.cliente;

import java.io.BufferedReader;

public class ServerListener extends Thread {
    private final BufferedReader inputClient;
    private final ClientNetworkManager cnm;

    public ServerListener(Cliente client) {
        inputClient = client.getInput();
        cnm = new ClientNetworkManager();
    }

    public void run() {
        try {
            String input;
            while ((input = inputClient.readLine()) != null) {
                cnm.processInput(input);

            }
        } catch (Exception ex) {
            System.out.println("Fallo al recibir del servidor");
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
