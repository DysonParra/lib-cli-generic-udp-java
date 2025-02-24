/*
 * @fileoverview    {UdpServerListener}
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
package com.project.dev.udp.generic.listener;

/**
 * FIXME: Description of {@code UdpServerListener}. Contiene las acciones que ejecuta un servidor udp
 * genérico en algún momento específico.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
public interface UdpServerListener {

    /**
     * FIXME: Description of {@code onConnectRequest}. Realiza una acción luego de que se obtenga una
     * petición de conexión.
     */
    public abstract void onConnectRequest();

    /**
     * FIXME: Description of {@code onDisconnectRequest}. Realiza una acción luego de que se obtenga
     * una petición de desconexión.
     */
    public abstract void onDisconnectRequest();

    /**
     * FIXME: Description of {@code onGenericRequest}. Realiza una acción luego de que se obtenga una
     * petición genérica.
     *
     * @param requestMessage es el mensaje recibido en la petición.
     * @return el tipo de respuesta que se envía al cliente.
     */
    public abstract boolean onGenericRequest(byte[] requestMessage);

    /**
     * FIXME: Description of {@code onGenericRequestWithGenericResponse}. Realiza una acción luego de
     * que se obtenga una petición genérica que indique además que devuelva una respuesta genérica.
     *
     * @param requestMessage es el mensaje recibido en la petición.
     * @return la respuesta genérica.
     */
    public abstract String onGenericRequestWithGenericResponse(byte[] requestMessage);

    /**
     * FIXME: Description of {@code onTimeOutRequest}. Realiza una acción luego de que no se pueda
     * obtener un paquete en el timeOut del servidor.
     */
    public abstract void onTimeOutRequest();

    /**
     * FIXME: Description of {@code onConfirmRequest}. Realiza una acción luego de que se obtenga una
     * petición de confirmación del cliente.
     */
    public abstract void onConfirmRequest();
}
