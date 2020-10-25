package loom.yacc.servidor;

import com.google.gson.Gson;
import loom.yacc.common.MensajeNetwork;
import loom.yacc.common.MensajeTipo;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientListener extends Thread{
    private BufferedReader input;
    private PrintWriter output;
    private ArrayList<Sala> salas; // pueden ser 3
    private Sala salaActual; //claramente tiene que estar en salas
    public int id;
    public String nombre;

    public ClientListener(Socket clienteSocket, int id) {
        try {
            this.input = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            this.output = new PrintWriter(clienteSocket.getOutputStream(), true);
            this.id = id;
            output.println("Ingrese un nombre para identificarse: ");
        }catch (Exception e){
            System.out.println("Se desconecto el cliente " + this.id + " cuando estaba iniciando");
            e.printStackTrace();
            this.close();
        }
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = input.readLine()) != null) {
                ServerNetworkManager.processInput(this, inputLine);
            }
        } catch (IOException e) {
            if (!this.isInterrupted()) {
                System.out.println("Se desconecto incorrectamente el cliente " + this.id);
                e.printStackTrace();
                this.close();
            }
        }
    }

    public void send(String mensaje) {
        send(MensajeTipo.MENSAJE, mensaje);
    }

    public void send(MensajeTipo type, Object message) {
        output.println((new Gson()).toJson(new MensajeNetwork(type, message)));
    }

    public void close(){
        this.interrupt();
        //eliminar salas
        //desconectar gente
        //
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

    public boolean salirDeLaSala() {
        if(estaEnUnaSala()){
            salaActual = null;
            return true;
        }
        return false;
    }

    public boolean estaEnUnaSala(){
        return salaActual != null;
    }
}
