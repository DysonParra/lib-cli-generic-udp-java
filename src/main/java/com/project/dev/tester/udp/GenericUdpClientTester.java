/*
 * @fileoverview    {GenericUdpClientTester}
 *
 * @version         2.0
 *
 * @author          Dyson Arley Parra Tilano <dysontilano@gmail.com>
 *
 * @copyright       Dyson Parra
 * @see             github.com/DysonParra
 *
 * History
 * @version 1.0     Implementación realizada.
 * @version 2.0     Documentación agregada.
 */
package com.project.dev.tester.udp;

import com.project.dev.udp.generic.GenericUdpClient;

/**
 * FIXME: Definición de {@code GenericUdpClientTester}. Contiene un ejemplo de como enviar
 * peticiones a un servidor udp genérico.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class GenericUdpClientTester {

    /**
     * Entrada principal del sistema.
     *
     * @param args argumentos de la linea de comandos.
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // crea un cliente udp genérico y le asigna una dirección ip y un puerto de conexión al servidor.
        GenericUdpClient udpClient = new GenericUdpClient();
        udpClient.setServerIpAddress("192.168.1.2");
        udpClient.setServerPort(2);

        // Envía una petición en la que verifica si es posible comunicarse con el servidor.
        System.out.println("Sending test request:");
        if (udpClient.sendTestConnectionRequest(3000)) {                         // Si se pudo comunicar con el servidor.
            System.out.println(true + "\n");

            // Envía una petición de conexión al servidor y muestra que el tamaño de los mensajes del cliente ya es el mismo que el del servidor luego la conexión.
            System.out.println("Sending connect request:");
            System.out.println("MessageSize: " + udpClient.getMaxMessageSize());
            System.out.println(udpClient.sendConnectRequest(3000));
            System.out.println("MessageSize: " + udpClient.getMaxMessageSize() + "\n");

            //udpClient.setServerIpAddress("192.168.1.2");
            //udpClient.setServerPort(0);
            // Envía una petición genérica y le indica al servidor que no le envíe simplemente si fue aprobada.
            // Si no que le devuelva cualquier mensaje que dependerá del servidor escogerlo basandose en el mensaje enviado desde aquí.
            System.out.println("Sending generic request:");
            if (udpClient.sendGenericRequest(3000, "YYYNNNYYYYYYYY", true))
                System.out.println("Generic response: '" + udpClient.getStringResponse() + "'\n");
            else
                System.out.println(false + "\n");

            // Envía una petición genérica al servidor.
            System.out.println("Sending generic request:");
            System.out.println(udpClient.sendGenericRequest(3000, "YYNN".getBytes(), false) + "\n");

            // Envía una petición genérica al servidor (que es aceptada por el servidor.).
            System.out.println("Sending generic request (With message not acceptable for the server):");
            System.out.println(udpClient.sendGenericRequest(3000, "Error".getBytes(), false) + "\n");

            // Envía una petición gén la que le confirma al servidor algo, en este caso no es muy útil,
            // pero dependiendo de quien use la biblioteca Generic udp, es posible que en algún momento el program,
            // necesite que el cliente confirme al servidor que ya proceso o hizo algo para que el programa realice alguna acción.
            System.out.println("Sending confirm request:");
            System.out.println(udpClient.sendConfirmRequest(3000) + "\n");

            // Envía una petición de desconexión al servidor.
            System.out.println("Sending disconnect request:");
            System.out.println(udpClient.sendDisconnectRequest(3000) + "\n");

            // Envía una petición genérica al servidor con el que sabe su ip y su puerto pero ya no está conectado,
            // la cual debe ser recazada a pesar de que el servidor si existe.
            System.out.println("Sending generic request when is disconnected from server:");
            System.out.println(udpClient.sendGenericRequest(3000, "NNYY".getBytes(), false) + "\n");
        } else                                                                    // Si no se puede comunicar con el servidor.
            System.out.println(false + "\n");
    }
}
