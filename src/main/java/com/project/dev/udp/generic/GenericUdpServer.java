/*
 * @fileoverview    {GenericUdpServer}
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
package com.project.dev.udp.generic;

import com.project.dev.udp.generic.listener.UdpServerListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * FIXME: Description of {@code GenericUdpServer}. Contiene las operaciones básicas para iniciar un
 * servidor udp genérico y ejecutar acciones al recibir peticiones de un cliente udp genérico.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class GenericUdpServer implements GenericUdp, Runnable {

    private static final byte TIMEOUT_REQUEST = 'O';                                                // Código que indica petición actual con timeout alcanzado.
    private static final int MAX_MESAGE_LENGTH = Integer.MAX_VALUE - 1;                             // Tamaño máximo de los mensajes que se recibirán o enviarán.

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private DatagramSocket serverSocket;                                                            // Como se envian los paquetes.
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private DatagramPacket packet;                                                                  // Cada paquete que se enviará y recibirá.
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private InetAddress clientInetAddress;                                                          // Usada para obtener la ip desde donde llega cada paquete.

    @Setter(AccessLevel.NONE)
    private String serverIpAddress = "";                                                            // Dirección ip del servidor.
    @Setter(AccessLevel.NONE)
    private int serverPort = 2;                                                                     // Puerto del servidor.
    @Setter(AccessLevel.NONE)
    private int maxMessageSize = 10;                                                                // Tamaño de los ensaje que se recibirán.

    @Setter(AccessLevel.NONE)
    private int clientPort;                                                                         // Usada para obtener el puerto desde donde llegó un paquete.
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private byte[] clientRequest;                                                                   // Usada para recibir paquetes desde un cliente.
    @Setter(AccessLevel.NONE)
    private byte[] clientMessage;                                                                   // Mensajes enviados por el cliente.

    @Setter(AccessLevel.NONE)
    private boolean isStartServer = false;                                                          // Si el servidor está levantado.

    private UdpServerListener onRequestListener = null;                                             // Crea listener del servidor udp.

    /**
     * TODO: Description of {@code GenericUdpServer}.
     *
     * @param maxMessageSize es el tamaño máximo de los mensajes que se recibirán.
     * @throws java.lang.Exception
     */
    public GenericUdpServer(int maxMessageSize) throws Exception {
        if (maxMessageSize < 1) {                                                                   // Si el tamaño del mensaje es menor que uno.
            throw new Exception("messageSize must be major than zero");                             // Lanza una exception indicando tamaño de mensajes inválido.
        } else if (maxMessageSize > MAX_MESAGE_LENGTH) {                                            // Si el tamaño de los mensajes es mayor al tamaño máximo permitido.
            throw new Exception("Too long messageSize");                                            // Lanza un exception indicando tamaño demasiado grande.
        }
        this.serverPort = 1;                                                                        // Inicializa el puerto del servidor.
        this.maxMessageSize = maxMessageSize;                                                       // Inicializa el tamaño de los mensajes que se recibirán.

        while (true) {                                                                              // Ejecuta un ciclo infinito.
            try {
                this.serverSocket = new DatagramSocket(++serverPort);
                this.clientRequest = new byte[maxMessageSize + 2];
                this.clientMessage = MESSAGE_VOID;
                this.serverIpAddress = assignServerIpAddress();
                break;
            } catch (Exception ignored) {
                //System.out.println("Error in port: " + this.serverPort);
            }
        }
    }

    /**
     * TODO: Description of {@code GenericUdpServer}.
     *
     * @param maxMessageSize    es el tamaño máximo de los mensajes que se recibirán.
     * @param onRequestListener es el listener del servidor.
     * @throws java.lang.Exception
     */
    public GenericUdpServer(int maxMessageSize, UdpServerListener onRequestListener) throws Exception {
        this(maxMessageSize);
        this.onRequestListener = onRequestListener;
    }

    /**
     * TODO: Description of {@code GenericUdpServer}.
     *
     * @param serverPort     es el puerto del servidor.
     * @param maxMessageSize es el tamaño máximo de los mensajes que se recibirán.
     * @throws java.net.SocketException
     */
    public GenericUdpServer(int serverPort, int maxMessageSize) throws SocketException, Exception {
        if (maxMessageSize < 1) {
            throw new Exception("messageSize must be major than zero");
        } else if (maxMessageSize > MAX_MESAGE_LENGTH) {
            throw new Exception("Too long messageSize");
        }
        this.serverPort = serverPort;
        this.maxMessageSize = maxMessageSize;

        this.serverSocket = new DatagramSocket(this.serverPort);
        this.clientRequest = new byte[maxMessageSize + 2];
        this.clientMessage = MESSAGE_VOID;
        this.serverIpAddress = assignServerIpAddress();
    }

    /**
     * TODO: Description of {@code GenericUdpServer}.
     *
     * @param serverPort        es el puerto del servidor.
     * @param maxMessageSize    es el tamaño máximo de los mensajes que se recibirán.
     * @param onRequestListener es el listener del servidor.
     * @throws java.lang.Exception
     */
    public GenericUdpServer(int serverPort, int maxMessageSize, UdpServerListener onRequestListener) throws Exception {
        this(serverPort, maxMessageSize);
        this.onRequestListener = onRequestListener;
    }

    /**
     * TODO: Description of {@code setMaxMessageSize}.
     *
     * @param maxMessageSize
     */
    public final void setMaxMessageSize(int maxMessageSize) {
        this.clientRequest = new byte[maxMessageSize + 2];
    }

    /**
     * TODO: Description of {@code setServerPort}.
     *
     * @param serverPort
     * @throws java.net.SocketException
     */
    public final void setServerPort(int serverPort) throws SocketException {
        this.serverPort = serverPort;
        this.serverSocket = new DatagramSocket(this.serverPort);
    }

    /**
     * TODO: Description of {@code getStringClientRequest}.
     *
     * @return
     */
    public final String getStringClientRequest() {
        String requestAux = new String(clientRequest);
        requestAux = requestAux.substring(0, requestAux.indexOf(0));
        return requestAux;
    }

    /**
     * TODO: Description of {@code getClientRequest}.
     *
     * @return
     */
    public final byte[] getClientRequest() {
        return getStringClientRequest().getBytes();
    }

    /**
     * TODO: Description of {@code getStringClientMessage}.
     *
     * @return
     */
    public final String getStringClientMessage() {
        return new String(clientMessage);
    }

    /**
     * FIXME: Description of {@code assignServerIpAddress}. Almacena dirección ip del servidor.
     *
     * @return la dirección ip del servidor.
     */
    private String assignServerIpAddress() {
        String ipAddress = "";                                                          // Dirección ip del servidor.
        int portAux = serverPort;                                                       // Puerto através del cual se obtendrá la ip del servidor.
        while (true) {                                                                  // Ejecuta un ciclo infinito.
            try {
                DatagramSocket socketAux = new DatagramSocket();                        // Crea nuevo datagrama en el puerto especificado.
                socketAux.connect(InetAddress.getByName("8.8.8.8"), ++portAux);         // Intenta conectarse al puerto indicado.
                ipAddress = socketAux.getLocalAddress().getHostAddress();               // Obtiene la dirección ip del servidor.
                break;                                                                  // Sale del ciclo infinito.
            } catch (SocketException | UnknownHostException e) {                        // Si no es posble usar el puerto actual.
                //System.out.println("Error in port: " + portAux);
            }
        }

        return ipAddress;                                                               // Devuelve la dirección ip del servidor.
    }

    /**
     * FIXME: Description of {@code setServerTimeOut}. Asigna tiempo máximo de espera del servidor
     * para recibir un paquete.
     *
     * @param timeOut es el nuevo tiempo máximo de espera.
     * @return si fue posible asignar el tiempo de espera indicado.
     */
    public boolean setServerTimeOut(int timeOut) {
        try {                                                                           // Intenta asignar timeOut.
            serverSocket.setSoTimeout(timeOut);                                         // Pone timeOut al servidor.
            return true;                                                                // Devulve true indicando operación exitosa.
        } catch (SocketException ex) {                                                  // Si no fue posible asignar el timeOut.
            return false;                                                               // Devuelve indicando operación no exitosa.
        }
    }

    /**
     * FIXME: Description of {@code startServer}. Inicia el servidor.
     */
    public final void startServer() {
        isStartServer = true;                                                                       // Indica que el servidor está levantado.
        while (isStartServer) {                                                                     // Mientras esté levantado el servidor.
            //System.out.println("...");

            clientRequest[0] = TIMEOUT_REQUEST;                                                     // Pone el paquete enviado por el usuario como no válido.
            packet = new DatagramPacket(clientRequest, maxMessageSize + 1);                         // Inicializa paquete para recibir mensajes.

            try {                                                                                   // Espera indefinidamente a que llegue un paquete.
                serverSocket.receive(packet);                                                       // Intenta obtener un paquete.
            } catch (IOException e) {                                                               // Si no pudo recibir un paquete.
            }

            if (isStartServer) {                                                                    // Si el servidor está levantado.
                clientInetAddress = packet.getAddress();                                            // Obtiene la ip del cliente que envió el paquete.
                clientPort = packet.getPort();                                                      // Obtiene el puerto del cliente que envió el paquete.
                String requestAux = getStringClientRequest();                                       // Obtiene la petición del cliente.
                clientMessage = MESSAGE_VOID;                                                       // Pone vacío el mensaje del cliente.

                switch (clientRequest[0]) {                                                         // Evalúa la petición del cliente.
                    case REQUEST_TEST_CONNECTION:                                                   // Si es una petición de prueba de conexión.
                        try {                                                                       // Intenta enviar un paquete al cliente.
                        sendConfirmResponse();                                                      // Envía mensaje de aceptación al cliente.
                        //if (onRequestListener != null)                                            // Si el servidor tiene un listener asociado.
                        //    onRequestListener.onTestConnectionRequest();                          // Ejecuta acción para una petición de prueba de conexión.
                    } catch (IOException e) {                                                       // Si al cliente no le llegó el paquete.
                    }
                    break;                                                                          // Termina de evaluar la petición.

                    case REQUEST_CONFIRM:                                                           // Si es una petición de prueba de conexión.
                        try {                                                                       // Intenta enviar un paquete al cliente.
                        sendConfirmResponse();                                                      // Envía mensaje de aceptación al cliente.
                        if (onRequestListener != null) {                                            // Si el servidor tiene un listener asociado.
                            onRequestListener.onConfirmRequest();                                   // Ejecuta acción para una petición de prueba de confirmación.
                        }
                    } catch (IOException e) {                                                       // Si al cliente no le llegó el paquete.
                    }
                    break;                                                                          // Termina de evaluar la petición.

                    case REQUEST_CONNECT:                                                           // Si la petición es comenzar conexión.
                        try {                                                                       // Intenta enviar un paquete al cliente.
                        sendConfirmResponse();                                                      // Envía mensaje de aceptación al cliente.
                    } catch (IOException e) {                                                       // Si al cliente no le llegó el paquete.
                    }
                    break;                                                                          // Termina de evaluar la petición.

                    case REQUEST_DISCONNECT:                                                        // Si la petición es terminar conexión.
                        try {                                                                       // Intenta enviar un paquete al cliente.
                        sendConfirmResponse();                                                      // Envía mensaje de aceptación al cliente.
                        if (onRequestListener != null) {                                            // Si el servidor tiene un listener asociado.
                            onRequestListener.onDisconnectRequest();                                // Ejecuta acción para una petición de desconexión.
                        }
                    } catch (IOException e) {                                                       // Si al cliente no le llegó el paquete.
                    }
                    break;                                                                          // Termina de evaluar la petición.

                    case REQUEST_GENERIC:                                                           // Si la petición es una petición genérica.
                        clientMessage = requestAux.substring(1).getBytes();                         // Obtiene el mensaje de la petición.
                        try {                                                                       // Intenta enviar un paquete al cliente.
                            if (onRequestListener != null) {                                        // Si el servidor tiene un listener asociado.
                                if (onRequestListener.onGenericRequest(getClientMessage())) {       // Ejecuta acción para una petición genérica.
                                    sendConfirmResponse();                                          // Envía mensaje de aceptación al cliente.
                                } else {                                                            // Si al procesar la respuesta genérica se devolvió false.
                                    sendRejectResponse();                                           // Devuelve respuesta negativa al cliente.
                                }
                            } else {
                                sendConfirmResponse();                                              // Envía mensaje de aceptación al cliente.
                            }
                        } catch (IOException e) {                                                   // Si al cliente no le llegó el paquete.
                        }
                        break;                                                                      // Termina de evaluar la petición.

                    case REQUEST_GENERIC_WITH_GENERIC_RESPONSE:                                     // Si la petición es una petición genérica que pide respuesta genérica.
                        clientMessage = requestAux.substring(1).getBytes();                         // Obtiene el mensaje de la petición.
                        if (onRequestListener != null) {                                            // Si el servidor tiene un listener asociado.
                            try {                                                                   // Intenta enviar un paquete al cliente.
                                sendResponse(
                                        onRequestListener.onGenericRequestWithGenericResponse(getClientMessage())
                                );                                                                  // Ejecuta acción para una petición genérica con respuesta genérica y envía respuesta genérica.
                            } catch (IOException e) {                                               // Si al cliente no le llegó el paquete.
                            }
                        }
                        break;
                    case REQUEST_GET_MESSAGE_SIZE:                                                  // Si la petición es una petición para obtener el tamaño de los mensajes del servidor.
                        try {                                                                       // Intenta enviar un paquete al cliente.
                        sendResponse((maxMessageSize + ""));                                        // Envía mensaje con el tamaño máximo de los paquetes al cliente.
                        if (onRequestListener != null) {                                            // Si el servidor tiene un listener asociado.
                            onRequestListener.onConnectRequest();                                   // Ejecuta acción para una petición de conexión.
                        }
                    } catch (IOException e) {                                                       // Si al cliente no le llegó el paquete.
                    }
                    break;                                                                          // Termina de evaluar la petición.

                    case TIMEOUT_REQUEST:                                                           // Si se alcanzó el timeout máximo del paquete actual.
                        if (onRequestListener != null) {                                            // Si el servidor tiene un listener asociado.
                            onRequestListener.onTimeOutRequest();                                   // Ejecuta acción para una petición no válida.
                        }
                        break;                                                                      // Termina de evaluar la petición.

                    default:                                                                        // Si la petición es una petición no válida.
                        try {                                                                       // Intenta enviar un paquete al cliente.
                        sendRejectResponse();                                                       // Envía mensaje de rechazo al cliente.
                    } catch (IOException e) {                                                       // Si al cliente no le llegó el paquete.
                    }
                    break;                                                                          // Termina de evaluar la petición.
                }

                //System.out.println("Getting request: '" + requestAux + "'");
                //System.out.println("Getting message: '" + new String(clientMessage) + "'");
            }
        }
    }

    /**
     * FIXME: Description of {@code stopServer}. Detiene el servidor que tiene {Runnable},
     */
    public final void stopServer() {
        isStartServer = false;                                                                      // Indica que el servidor no está levantado.
        serverSocket.close();                                                                       // Cierra el socket para que no inetente recibir más paquetes.
    }

    /**
     * FIXME: Description of {@code run}. Inicia el servidor qu tiene el {Runnable}.
     */
    @Override
    public final void run() {
        startServer();
    }

    /**
     * FIXME: Description of {@code stop}. Detiene el servidor.
     */
    public final void stop() {
        stopServer();
    }

    /**
     * FIXME: Description of {@code sendResponse}. Envía respuestas al cliente.
     *
     * @param message es el mensaje que se le enviará al cliente.
     * @throws java.io.IOException
     */
    public final void sendResponse(byte[] message) throws IOException {
        String respone = new String(message);
        respone += "\0";
        packet = new DatagramPacket(respone.getBytes(), respone.length(), clientInetAddress, clientPort);   // Crea paquete para enviar al cliente.
        serverSocket.send(packet);                                                                          // Envía el paquete al cliente.
    }

    /**
     * FIXME: Description of {@code sendResponse}. Envía respuestas al cliente.
     *
     * @param message es el mensaje que se le enviará al cliente.
     * @throws java.io.IOException
     */
    public final void sendResponse(String message) throws IOException {
        String respone = message + "\0";
        packet = new DatagramPacket(respone.getBytes(), respone.length(), clientInetAddress, clientPort);   // Crea paquete para enviar al cliente.
        serverSocket.send(packet);                                                                          // Envía el paquete al cliente.
    }

    /**
     * FIXME: Description of {@code sendConfirmResponse}. Envía mensaje de confirmación al cliente.
     *
     * @throws java.io.IOException
     */
    private void sendConfirmResponse() throws IOException {
        sendResponse(RESPONSE_ACCEPT);                                                                  // Envía mensaje de confirmación al cliente.
    }

    /**
     * FIXME: Description of {@code sendRejectResponse}. Envía mensaje de rechazo al cliente.
     *
     * @throws java.io.IOException
     */
    private void sendRejectResponse() throws IOException {
        sendResponse(RESPONSE_REJECT);                                                                      // Envía mensaje de rechazo al cliente.
    }

    /**
     * FIXME: Description of {@code onTestConnectionRequest}. Realiza una acción luego de que se
     * obtenga una petición de probar la conexión.
     */
    public void onTestConnectionRequest() {
    }
}
