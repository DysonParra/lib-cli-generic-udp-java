/*
 * @fileoverview    {GenericUdpServerTester}
 *
 * @version         2.0
 *
 * @author          Dyson Arley Parra Tilano <dysontilano@gmail.com>
 *
 * @copyright       Dyson Parra
 * @see             github.com/DysonParra
 *
 * History
 * @version 1.0     Implementation done.
 * @version 2.0     Documentation added.
 */
package com.project.dev.tester.udp;

import com.project.dev.udp.generic.GenericUdpServer;
import com.project.dev.udp.generic.listener.UdpServerListener;

/**
 * FIXME: Definición de {@code GenericUdpServerTester}. Contiene un ejemplo de como ejecutar un
 * servidor udp, asignarle un listener y que este listener ejecute acciones en momentos específicos.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class GenericUdpServerTester {

    /**
     * Entrada principal del sistema.
     *
     * @param args argumentos de la linea de comandos.
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // 2147483647 // 347602922.
        GenericUdpServer udpServer = new GenericUdpServer(50);                  // Crea un servidor Udp con tamaño de mensajes de 50 bytes.
        udpServer.setServerTimeOut(0);                                          // Indica que el servidor no ejecutará acciones cuando pase un tiempo sin recibir peticiones.
        udpServer.setOnRequestListener(new UdpServerListener() {                // Asigna un listener al servidor udp.
            // Si recibe una petición de conexión.
            @Override
            public void onConnectRequest() {
                System.out.println("Processing connection request to server...\n");
            }

            // Si recibe una petición de desconexión.
            @Override
            public void onDisconnectRequest() {
                System.out.println("Processing disconnection request to server...\n");
            }

            // Si recibe una petición con cualquier mensaje y que simplemente devuelve al cliente indicando exitosa.
            @Override
            public boolean onGenericRequest(byte[] requestMessage) {
                System.out.println("Processing generic request to server...");
                System.out.println("'" + new String(requestMessage) + "'");
                System.out.println("");
                return !new String(requestMessage).equals("Error");
            }

            // Si recibe una petición con cualquier mensaje y que además indica que se responda con un mensaje cualquiera.
            @Override
            public String onGenericRequestWithGenericResponse(byte[] requestMessage) {
                System.out.println("Processing generic request with generic response to server...");
                System.out.println("'" + new String(requestMessage) + "'");
                System.out.println("");
                return "Here is a generic response...";                         // El cliente recibirá el mensaje indicado.
            }

            // Si recibe una petición en la que el cliente le indica al servidor confirmación,
            // que dependiendo del contexto puede significar que el cliente ya realizó algo, ya proceso algo, etc,
            // Es una forma de que el servidor sepa que el cliente ya hizo algo.
            @Override
            public void onConfirmRequest() {
                System.out.println("The client confirm something...\n");
            }

            // Si el servidor no recibe ningún tipo de petición en el timeOut que tenga asignado.
            @Override
            public void onTimeOutRequest() {
                System.out.println("Could not get a request...\n");
            }
        });

        // Muestra en pantalla dirección ip y puerto de conexión al servidor.
        System.out.println(udpServer.getServerIpAddress() + ":" + udpServer.getServerPort() + "\n");
        udpServer.startServer();

        /*
         * -
         * try { Thread.sleep(5000); System.out.println("Stopping..."); udpServer.stop(); } catch
         * (Exception e) {
         *
         * }
         */
    }
}
