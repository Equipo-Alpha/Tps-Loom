package loom.yacc.cliente;

import loom.yacc.common.MensajeNetwork;
import loom.yacc.common.MensajeTipo;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

public class Cliente extends Thread{
    private static Cliente INSTANCE = null;

    private final String ip;
    private final int port;
    private String nombre;

    private Socket socketCliente;
    private PrintWriter output;
    private BufferedReader input;
    private final Scanner scanner;

    public Cliente(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.scanner = new Scanner(System.in);
        INSTANCE = this;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if(scanner.hasNextLine()) {
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
            send(MensajeTipo.CONEXION,"" + this.nombre);

        } catch (Exception ex) {
            System.out.println("Fallo al recibir del servidor");
            ex.printStackTrace();
            System.exit(0);
        }
    }

    public void send(MensajeTipo type, Object message) {
        output.println((new Gson()).toJson(new MensajeNetwork(type, message)));
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente("localhost", 20000);
        cliente.connect();
        cliente.start();
        Thread serverListener = new ServerListener(cliente);
        serverListener.start();
    }

    public BufferedReader getInput() {
        return input;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public static Cliente getINSTANCE() {
        return INSTANCE;
    }
}
