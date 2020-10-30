package loom.yacc.servidor;

import com.google.gson.Gson;
import loom.yacc.common.MensajeNetwork;
import loom.yacc.common.MensajeTipo;

import java.time.LocalTime;
import java.util.Date;

public class ServerNetworkManager {
    public void processInput(ClientListener cliente, String input) {
        try {
            MensajeNetwork mensaje = (new Gson()).fromJson(input, MensajeNetwork.class);
            MensajeTipo tipo = mensaje.getType();
            switch (tipo) {
                case CONEXION:
                    nuevaConexion(cliente, mensaje);
                    break;
                case MENSAJE:
                    nuevoMensaje(cliente, mensaje);
                    break;
                default:
                    cliente.send("No se pudo procesar el mensaje");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nuevaConexion(ClientListener cliente, MensajeNetwork mensaje) {
        cliente.nombre = (String) mensaje.getMessage();
        System.out.println("Nombre del cliente: " + cliente.nombre);
        cliente.send("Bienvenido " + cliente.nombre + ". Los camandos son los siguientes:\n" +
                "?:CREARSALA nombre\n" +
                "?:VERSALAS\n" +
                "?:VERTIEMPO\n" +
                "?:UNIRSESALA nombre\n" +
                "?:SALIRSALA\n" +
                "?:DESCARGARHISTORIAL\n" +
                "Para mensaje privado: @nombre mensaje");
    }

    private void nuevoMensaje(ClientListener cliente, MensajeNetwork mensajeEntrante) {
        String msg = (String) mensajeEntrante.getMessage();
        if (msg.startsWith("?:")) {
            msg = msg.replace("?:", "");
            procesarComando(cliente, msg);
            return;
        }

        if (cliente.estaEnUnaSala()) {
            if (msg.startsWith("@")) {
                msg = msg.replace("@", "");
                mensajePrivado(cliente, msg);
            }

            String mensajeFormateado = "[" + mensajeEntrante.getTime().toLocalTime() + "] " + mensajeEntrante.getCliente() + ": " + mensajeEntrante.getMessage();
            cliente.getSalaActual().recibirMensaje(mensajeFormateado);
        } else {
            cliente.send("No se encuentra en ninguna sala, el mensaje no fue procesado");
        }
    }

    private void mensajePrivado(ClientListener cliente, String mensaje) {
        String[] msj = mensaje.split(" ");
        if (msj.length == 1) {
            cliente.send("No escribio mensaje");
        }
        String mensajeSinNombre = mensaje.replace(msj[1], "");

        for (ClientListener clientes : cliente.getSalaActual().getClientes()) {
            if (clientes.nombre.equals(msj[0])) {
                clientes.send("(MP) " + cliente.nombre + ": " + mensajeSinNombre);
            }
        }
    }

    private void procesarComando(ClientListener cliente, String mensaje) {
        String[] comando = mensaje.split(" ");
        if (comando.length == 1) {
            switch (comando[0]) {
                case "VERSALAS":
                    mostrarSalas(cliente);
                    break;
                case "VERTIEMPO":
                    verTiempo(cliente);
                    break;
                case "SALIRSALA":
                    cliente.salirDeLaSala();
                    break;
                case "DESCARGARHISTORIAL":
                    cliente.getSalaActual().enviarHistorial(cliente);
                    break;
                default:
                    cliente.send("Comando desconocido o incompleto");
            }
        } else {
            switch (comando[0]) {
                case "CREARSALA":
                    crearSala(cliente, comando[1]);
                    break;
                case "UNIRSESALA":
                    unirseSala(cliente, comando[1]);
                    break;
                default:
                    cliente.send("Comando desconocido");

            }
        }
    }

    private void crearSala(ClientListener cliente, String nombre) {
        if (cliente.puedeCrearSala()) {
            Servidor.getINSTANCE().crearSala(nombre, cliente);
            cliente.send("Sala creada correctamente.");
        } else {
            cliente.send("Ya esta conectado en 3 salas. No puede crear otra.");
        }
    }

    private void mostrarSalas(ClientListener cliente) {
        if (Servidor.getINSTANCE().getSalas().isEmpty()) {
            cliente.send("No hay ninguna sala disponible");
            return;
        }
        String mensaje = String.format("%20s %4s %20S\n", "NOMBRE DE LA SALA", "ID", "CANTIDAD DE USUARIOS");
        for (Sala sala : Servidor.getINSTANCE().getSalas()) {
            mensaje = mensaje.concat(String.format("%20s %4d %20d\n", sala.nombre, sala.getIdSala(), sala.getCantClientes()));
        }
        cliente.send(mensaje);
    }

    private void unirseSala(ClientListener cliente, String nombre) {
        if (cliente.getSalas().size() >= 3) {
            cliente.send("No se puede unir, ya se encuentra en 3 salas.");
        }
        for (Sala sala : Servidor.getINSTANCE().getSalas()) {
            if (sala.nombre.equals(nombre)) {
                if (sala.getClientes().contains(cliente)) {
                    cliente.send("Ya se encuentra conectado a esta sala.");
                    return;
                } else {
                    sala.agregarCliente(cliente);
                    cliente.setSalaActual(sala);
                    return;
                }
            }
        }
        cliente.send("No se encontro la sala con el nombre " + nombre);
    }

    private void verTiempo(ClientListener cliente) {
        /// Como usuario dentro de la sala puedo ver mi tiempo y el de los usuarios conectados a la misma
        if (!cliente.estaEnUnaSala()) {
            cliente.send("No se encuentra en ninguna sala.");
            return;
        }
        Sala sala = cliente.getSalaActual();
        Date tiempoAct = new Date(System.currentTimeMillis());
        String mensaje = String.format("%20s %20s\n", "NOMBRE CLIENTE", "TIEMPO EN LA SALA");
        for (ClientListener clientes : sala.getClientes()) {
            LocalTime tiempoCli = LocalTime.ofSecondOfDay((tiempoAct.getTime() - sala.getTiempoCliente(clientes).getTime()) / 1000);
            mensaje = mensaje.concat(String.format("%20s %20s", clientes.nombre, tiempoCli));
        }
        cliente.send(mensaje);
    }

}
