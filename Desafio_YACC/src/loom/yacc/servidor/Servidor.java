package loom.yacc.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor extends Thread{
    private final int port;
    private ServerSocket serverSocket = null;
    private static List<ClientListener> clientes;
    private static ArrayList<Sala> salas = new ArrayList<>(); // todas las salas creadas
    private static int idSala = 0; // el id de la sala a crear
    private boolean isRunning;

    public Servidor(int port) {
        this.port = port;
        this.isRunning = false;
        clientes = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        this.isRunning = true;
        Socket clientSocket;
        int id = 1;
        try {
            while (isRunning) {
                clientSocket = serverSocket.accept();
                System.out.println("Se conecto un nuevo cliente: " + clientSocket);
                ClientListener cliente = new ClientListener(clientSocket, id++);
                cliente.start();
                clientes.add(cliente);
                System.out.println("Cliente agregado correctamente");
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.stopServer();
        }
    }

    public void stopServer() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void crearSala(String nombre, ClientListener cliente){
        Sala sala = new Sala(nombre, cliente, idSala++);
        salas.add(sala);
        cliente.setSalaActual(sala);
    }

    public static void main(String[] args) {

        Servidor server = new Servidor(20000);
        server.start();

    }
}
