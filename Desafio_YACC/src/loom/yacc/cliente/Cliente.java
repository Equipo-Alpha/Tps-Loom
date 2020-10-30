package loom.yacc.cliente;

import com.google.gson.Gson;
import loom.yacc.common.MensajeNetwork;
import loom.yacc.common.MensajeTipo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente extends Thread {
    private final String ip;
    private final int port;
    private final Scanner scanner;
    private String nombre;
    private Socket socketCliente;
    private PrintWriter output;
    private BufferedReader input;
    private boolean isRunning;

    public Cliente(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.scanner = new Scanner(System.in);
        this.isRunning = false;
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente("localhost", 20000);
        cliente.connect();
        cliente.start();
        Thread serverListener = new ServerListener(cliente);
        serverListener.start();
    }

    @Override
    public void run() {
        this.isRunning = true;
        try {
            while (isRunning) {
                if (scanner.hasNextLine()) {
                    String mensaje = scanner.nextLine();
                    send(MensajeTipo.MENSAJE, mensaje);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void connect() {
        try {
            socketCliente = new Socket(ip, port);
            output = new PrintWriter(socketCliente.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            System.out.println(input.readLine());
            nombre = scanner.nextLine();
            send(MensajeTipo.CONEXION, "" + this.nombre);
        } catch (Exception ex) {
            System.out.println("Fallo al recibir del servidor");
            ex.printStackTrace();
            System.exit(0);
        }
    }

    public void send(MensajeTipo type, Object message) {
        output.println((new Gson()).toJson(new MensajeNetwork(type, this.nombre, message)));
    }

    public BufferedReader getInput() {
        return input;
    }
}
