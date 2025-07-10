/*
 * @overview        {GenericUdpClient}
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
 * FIXME: Description of {@code GenericUdpClient}. Contiene las operaciones para enviar peticiones y
 * mensajes a un servidor udp genérico.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class GenericUdpClient implements GenericUdp {

    private final int DEFAULT_TIMEOUT = 3000;                                                           // Tiempo máximo de envío/recepción de paquetes por defecto.

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final DatagramSocket socket;                                                                // Como se envian y reciben los paquetes al servidor.
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private DatagramPacket packet;                                                                      // Cada paquete que se enviará y recibirá.
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private InetAddress serverInetAddress;                                                              // Usada para obtener la ip desde donde llega cada paquete.

    private int serverPort = 2;                                                                         // Puerto del servidor.
    @Setter(AccessLevel.NONE)
    private int maxMessageSize = 0;                                                                     // Tamaño máximo de los mensajes que se enviarán al servidor.

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private byte[] request;                                                                             // Usada para enviar paquetes de bytes.
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private byte[] response;                                                                            // Usada para recibir paquetes de bytes.

    @Setter(AccessLevel.NONE)
    private boolean connectedToServer;                                                                  // Si ya se conectó al servidor.

    /**
     * TODO: Description of method {@code GenericUdpClient}.
     *
     * @throws java.net.SocketException Si hay un problema creando el socket.
     */
    public GenericUdpClient() throws SocketException {
        this.request = new byte[10];
        this.response = new byte[20];
        this.socket = new DatagramSocket();
        this.connectedToServer = false;
    }

    /**
     * TODO: Description of method {@code GenericUdpClient}.
     *
     * @param serverIpAddres es la ip del servidor.
     * @param serverPort     es el puerto del servidor.
     * @throws java.net.UnknownHostException
     * @throws java.net.SocketException
     */
    public GenericUdpClient(String serverIpAddres, int serverPort) throws UnknownHostException, SocketException {
        this();
        this.serverInetAddress = InetAddress.getByName(serverIpAddres);                                 // Obtiene la direccion IP del Server especificada como parámetro.
        this.serverPort = serverPort;
    }

    /**
     * TODO: Description of method {@code getServerIpAddress}.
     *
     * @return
     */
    public final String getServerIpAddress() {
        return serverInetAddress.getHostAddress();
    }

    /**
     * TODO: Description of method {@code setServerIpAddress}.
     *
     * @param ipAddres
     * @throws java.net.UnknownHostException
     */
    public final void setServerIpAddress(String ipAddres) throws UnknownHostException {
        if (connectedToServer) {                                                                        // Si está conectado a un servidor.
            this.connectedToServer = false;                                                             // Indica que no se ha conectado a un servidor.
            sendRequest(REQUEST_DISCONNECT, request, DEFAULT_TIMEOUT);                                  // Envía petición de desconexión al servidor.
        }
        this.serverInetAddress = InetAddress.getByName(ipAddres);                                       // Obtiene la direccion IP del Server especificada como parámetro.
    }

    /**
     * TODO: Description of method {@code setServerPort}.
     *
     * @param serverPort
     */
    public final void setServerPort(int serverPort) {
        if (connectedToServer) {                                                                        // Si está conectado a un servidor.
            this.connectedToServer = false;                                                             // Indica que no se ha conectado a un servidor.
            sendRequest(REQUEST_DISCONNECT, request, DEFAULT_TIMEOUT);                                  // Envía petición de desconexión al servidor.
        }
        this.serverPort = serverPort;                                                                   // Cambia el puerto del servidor.
    }

    /**
     * TODO: Description of method {@code getStringResponse}.
     *
     * @return
     */
    public final String getStringResponse() {
        String responseAux = new String(response);                                                     // Obtiene la respuesta.
        responseAux = responseAux.substring(0, responseAux.indexOf(0));
        return responseAux;
    }

    /**
     * TODO: Description of method {@code getResponse}.
     *
     * @return
     */
    public final byte[] getResponse() {
        return getStringResponse().getBytes();
    }

    /**
     * FIXME: Description of method {@code sendTestConnectionRequest}. Envía una petición de prueba de la
     * conexión con el servidor.
     *
     * @param timeout es la cantidad de tiempo que se intenta enviar la petición.
     * @return si fue posible realizar la petición.
     */
    public final boolean sendTestConnectionRequest(int timeout) {
        return sendRequest(REQUEST_TEST_CONNECTION, MESSAGE_VOID, timeout);                             // Intenta enviar una petición de prueba de conexión.
    }

    /**
     * FIXME: Description of method {@code sendConfirmRequest}. Envía una petición de confirmación al
     * servidor.
     *
     * @param timeout es la cantidad de tiempo que se intenta enviar la petición.
     * @return si fue posible realizar la petición.
     */
    public final boolean sendConfirmRequest(int timeout) {
        return sendRequest(REQUEST_CONFIRM, MESSAGE_VOID, timeout);                                     // Intenta enviar una petición de confirmación al servidor.
    }

    /**
     * FIXME: Description of method {@code sendGetMessageSizeRequest}. Envía una petición para obtener el
     * tamaño de los mensajes del servidor.
     *
     * @param timeout es la cantidad de tiempo que se intenta enviar la petición.
     * @return si fue posible realizar la petición.
     */
    private boolean sendGetMessageSizeRequest(int timeout) {
        if (sendRequest(REQUEST_GET_MESSAGE_SIZE, MESSAGE_VOID, timeout)) {                             // Si se obtuvo una respuesta.
            try {                                                                                       // Intenta obtener el tamaño máximo de los mesnajes.
                String maxMessageSizeAux = new String(response);                                        // Obtiene la respuesta.
                maxMessageSizeAux = maxMessageSizeAux.substring(0, maxMessageSizeAux.indexOf(0));
                this.maxMessageSize = Integer.parseInt(maxMessageSizeAux);                              // Obtiene el tamaño máximo de los mensajes.
                this.request = new byte[maxMessageSize + 2];
                this.response = new byte[maxMessageSize + 2];
                return true;                                                                            // Indica operación exitosa.
            } catch (Exception e) {                                                                     // Si no pudo obtener el tamaño de los paquetes.
                return false;                                                                           // Indica operación fallida.
            }
        } else {
            return false;                                                                               // Indica operación fallida.
        }
    }

    /**
     * FIXME: Description of method {@code sendConnectRequest}. Envía una petición de conexión.
     *
     * @param timeout es la cantidad de tiempo que se intenta enviar la petición.
     * @return si fue posible realizar la petición.
     */
    public final boolean sendConnectRequest(int timeout) {
        if (connectedToServer) {                                                                        // Si está conectado a un servidor devuelve indicando petición fallida.
            return false;
        }
        if (sendRequest(REQUEST_CONNECT, MESSAGE_VOID, timeout) && sendGetMessageSizeRequest(timeout)) {// Intenta enviar una petición de conección y una de obtener el tamaño de los mensajes.
            connectedToServer = true;                                                                   // Indica que se ha conectado al servidor.
            return true;                                                                                // Devuelve indicando petición exitosa.
        } else {                                                                                        // Si ocurrió algún error.
            return false;                                                                               // Devuelve indicando petición fallida.
        }
    }

    /**
     * FIXME: Description of method {@code sendDisconnectRequest}. Envía una petición de desconexión.
     *
     * @param timeout es la cantidad de tiempo que se intenta enviar la petición.
     * @return si fue posible realizar la petición.
     */
    public final boolean sendDisconnectRequest(int timeout) {
        if (connectedToServer) {                                                                        // Si está conectado a un servidor.
            this.connectedToServer = false;                                                             // Indica que no se ha conectado a un servidor.
            return sendRequest(REQUEST_DISCONNECT, MESSAGE_VOID, timeout);                              // Devuelve si al servidor le llegó la petición.
        } else {                                                                                        // Si no está conectado al servidor.
            return false;                                                                               // Rechaza la petición.
        }
    }

    /**
     * FIXME: Description of method {@code sendGenericRequest}. Envía una petición de genérica al servidor.
     *
     * @param timeout         Cantidad de tiempo que se intenta enviar la petición.
     * @param message         Mensaje que se enviará.
     * @param genericResponse Si la petición genérica tendrá además una respuessa genérica.
     * @return si fue posible realizar la petición.
     */
    public final boolean sendGenericRequest(int timeout, byte[] message, boolean genericResponse) {
        if (connectedToServer) {                                                                        // Si está conectado a un servidor.
            if (message.length > maxMessageSize) {                                                      // Si el tamaño del mensaje a enviar es mayor quel tamaño máximo del servidor.
                return false;                                                                           // Devuelve indicando petición fallida.
            } else if (!genericResponse) {                                                              // Si se indicó que la petición no tendrá una respuesta genérica.
                return sendRequest(REQUEST_GENERIC, message, timeout);                                  // procesa la petición genérica sin respuesta genérica.
            } else {                                                                                    // Si se indicó que la petición tendrá una respuesta genérica.
                return sendRequest(REQUEST_GENERIC_WITH_GENERIC_RESPONSE, message, timeout);            // Devuelve si se procesó la petición genérica con respuesta genérica.
            }
        } else {                                                                                        // Si no está conectado al servidor.
            return false;                                                                               // Rechaza la petición.
        }
    }

    /**
     * FIXME: Description of method {@code sendGenericRequest}. Envía una petición de desconexión.
     *
     * @param timeout         Cantidad de tiempo que se intenta enviar la petición.
     * @param message         Mensaje que se enviará.
     * @param genericResponse Si la petición genérica tendrá además una respuessa genérica.
     * @return si fue posible realizar la petición.
     */
    public final boolean sendGenericRequest(int timeout, String message, boolean genericResponse) {
        return sendGenericRequest(timeout, message.getBytes(), genericResponse);                        // Envía la petición al servidor.
    }

    /**
     * FIXME: Description of method {@code sendRequest}. Envía mensajes al servidor.
     *
     * @param requestType es el tipo de petición que se enviará al servidor.
     * @param message     es el array con el mensaje que se enviará al servidor.
     * @param timeout     es la cantidad de tiempo que se intentará enviar la petición.
     * @return si fue posible realizar la petición.
     */
    private boolean sendRequest(byte requestType, byte[] message, int timeout) {
        switch (requestType) {                                                                          // Evalúa el tipo de petición que se enviará.
            case REQUEST_GENERIC:                                                                       // Si la petición es una petición genérica.
            case REQUEST_GENERIC_WITH_GENERIC_RESPONSE:                                                 // Si la petición es una petición genérica con respuesta genérica.
                this.request[0] = requestType;                                                          // Almacena en request el tipo de petición.
                System.arraycopy(message, 0, request, 1, message.length);                               // Copia mensaje al request.
                this.request[message.length + 1] = 0;                                                   // Copia marca de fin de cadena en la petición.
                break;                                                                                  // Termina de evaluar la petición.

            default:                                                                                    // Si es cualquier otra petición.
                this.request[0] = requestType;                                                          // Almacena en request el tipo de petición.
                this.request[1] = 0;                                                                    // Marca fin de la cadena en la petición.
                break;                                                                                  // Termina de evaluar la petición.
        }

        /*
         * -
         * String requestAux = new String(request); requestAux = requestAux.substring(0,
         * requestAux.indexOf(0)); System.out.println("Sending request: '" +
         * Arrays.toString(request) + "'"); System.out.println("Sending request: '" + requestAux +
         * "'"); System.out.println("Sending message: '" + Arrays.toString(message) + "'");
         */
        packet = new DatagramPacket(request, message.length + 2, serverInetAddress, serverPort);        // Inicializa paquete para enviar mensajes.
        try {                                                                                           // Intenta enviar un paquete al servidor.
            socket.setSoTimeout(timeout);                                                               // Indica tiempo máximo de espera para enviar un paquete.
            socket.send(packet);                                                                        // Envía un paquete al servidor.
        } catch (Exception ignored) {                                                                   // Si no pudo enviar el paquete.
            return false;                                                                               // Devuelve indicando que no fue posible realizar la petición al servidor.
        }

        packet = new DatagramPacket(response, response.length);                                         // Crea paquete para recibir respuesta del servidor.
        try {                                                                                           // Espera a que llegue un paquete del servidor.
            socket.receive(packet);                                                                     // Intenta obtener un paquete del servidor.
        } catch (Exception ignored) {                                                                   // Si no pudo recibir un paquete.
            return false;                                                                               // Devuelve indicando que no fue posible realizar la petición al servidor.
        }

        String responseAux = getStringResponse();                                                       // Obtiene la respuesta.
        //System.out.println("Response: '" + Arrays.toString(response) + "'");
        //System.out.println("Response: '" + new String(response) + "'");
        //System.out.println("Response: '" + responseAux + "'");

        return responseAux.equals(RESPONSE_ACCEPT) // Devuelve si la petición al servidor devuelve aceptada.
                || (requestType == REQUEST_GET_MESSAGE_SIZE && !responseAux.equals(RESPONSE_REJECT)) // Si envío petición de obtener tamaño de los paquetes y la petición no fue rechazada.
                || (requestType == REQUEST_GENERIC_WITH_GENERIC_RESPONSE);                              // Si envío petición genérica con respuesta genérica.
    }
}
