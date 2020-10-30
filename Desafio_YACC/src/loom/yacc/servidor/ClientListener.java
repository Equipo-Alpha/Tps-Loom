package loom.yacc.servidor;

import com.google.gson.Gson;
import loom.yacc.common.HistorialSala;
import loom.yacc.common.MensajeNetwork;
import loom.yacc.common.MensajeTipo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientListener extends Thread {
    public int id;
    public String nombre;
    private BufferedReader input;
    private PrintWriter output;
    private ArrayList<Sala> salas; // pueden ser 3
    private Sala salaActual;
    private ServerNetworkManager snm;

    public ClientListener(Socket clienteSocket, int id) {
        try {
            this.input = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            this.output = new PrintWriter(clienteSocket.getOutputStream(), true);
            this.salas = new ArrayList<>();
            this.id = id;
            this.snm = new ServerNetworkManager();
            output.println("Ingrese un nombre para identificarse: ");
        } catch (Exception e) {
            System.out.println("Se desconecto el cliente " + this.id + " cuando estaba iniciando");
        }
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = input.readLine()) != null) {
                snm.processInput(this, inputLine);
            }
        } catch (IOException e) {
            if (!this.isInterrupted()) {
                System.out.println("Se desconecto incorrectamente el cliente " + this.id);
            }
        }
    }

    public void send(String mensaje) {
        send(MensajeTipo.MENSAJE, mensaje);
    }

    public void send(MensajeTipo type, Object message) {
        output.println((new Gson()).toJson(new MensajeNetwork(type, message)));
    }

    public void send(MensajeTipo type, HistorialSala message) {
        output.println((new Gson()).toJson(new MensajeNetwork(type, message)));
    }

    public ArrayList<Sala> getSalas() {
        return salas;
    }

    public void agregarSala(Sala sala) {
        this.salas.add(sala);
        this.salaActual = sala;
    }

    public Sala getSalaActual() {
        return salaActual;
    }

    public void setSalaActual(Sala salaActual) {
        this.salaActual = salaActual;
    }

    public void salirDeLaSala() {
        if (estaEnUnaSala()) {
            salas.remove(salaActual);
            salaActual = null;
        } else
            send("No se encuentra en ninguna sala");
    }

    public boolean puedeCrearSala() {
        return salas.size() < 3;
    }

    public boolean estaEnUnaSala() {
        return salaActual != null;
    }
}
