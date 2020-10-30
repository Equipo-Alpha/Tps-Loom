package loom.yacc.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor extends Thread {
    private static int idSala = 0; // el id de la sala a crear
    private static Servidor INSTANCE;
    private final int port;
    private ServerSocket serverSocket = null;
    private List<ClientListener> clientes;
    private List<Sala> salas; // todas las salas creadas
    private boolean isRunning;

    public Servidor(int port) {
        this.port = port;
        this.isRunning = false;
        this.clientes = new ArrayList<>();
        this.salas = new ArrayList<>();
        INSTANCE = this;
    }

    public static Servidor getINSTANCE() {
        return INSTANCE;
    }

    public static void main(String[] args) {
        Servidor server = new Servidor(20000);
        server.start();
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

    public void crearSala(String nombre, ClientListener cliente) {
        Sala sala = new Sala(nombre, cliente, idSala++);
        salas.add(sala);
        cliente.agregarSala(sala);
    }

    public List<Sala> getSalas() {
        return salas;
    }
}
