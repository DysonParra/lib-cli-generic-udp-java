/*
 * @fileoverview {UdpServerListener} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {UdpServerListener} fue realizada el 31/07/2022.
 * @Dev - La primera version de {UdpServerListener} fue escrita por Dyson A. Parra T.
 */
package com.project.dev.udp.generic.listener;

/**
 * FIXME: Definición de {@code UdpServerListener}. Contiene las acciones que ejecuta un servidor udp
 * genérico en algún momento específico.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public interface UdpServerListener {

    /**
     * FIXME: Definición de {@code onConnectRequest}. Realiza una acción luego de que se obtenga una
     * petición de conexión.
     */
    public abstract void onConnectRequest();

    /**
     * FIXME: Definición de {@code onDisconnectRequest}. Realiza una acción luego de que se obtenga
     * una petición de desconexión.
     */
    public abstract void onDisconnectRequest();

    /**
     * FIXME: Definición de {@code onGenericRequest}. Realiza una acción luego de que se obtenga una
     * petición genérica.
     *
     * @param requestMessage es el mensaje recibido en la petición.
     * @return el tipo de respuesta que se envía al cliente.
     */
    public abstract boolean onGenericRequest(byte[] requestMessage);

    /**
     * FIXME: Definición de {@code onGenericRequestWithGenericResponse}. Realiza una acción luego de
     * que se obtenga una petición genérica que indique además que devuelva una respuesta genérica.
     *
     * @param requestMessage es el mensaje recibido en la petición.
     * @return la respuesta genérica.
     */
    public abstract String onGenericRequestWithGenericResponse(byte[] requestMessage);

    /**
     * FIXME: Definición de {@code onTimeOutRequest}. Realiza una acción luego de que no se pueda
     * obtener un paquete en el timeOut del servidor.
     */
    public abstract void onTimeOutRequest();

    /**
     * FIXME: Definición de {@code onConfirmRequest}. Realiza una acción luego de que se obtenga una
     * petición de confirmación del cliente.
     */
    public abstract void onConfirmRequest();
}
