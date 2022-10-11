/*
 * @fileoverview {GenericUdp} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {GenericUdp} fue realizada el 31/07/2022.
 * @Dev - La primera version de {GenericUdp} fue escrita por Dyson A. Parra T.
 */
package com.project.dev.udp.generic;

/**
 * FIXME: Definición de {@code GenericUdp}. Contiene los mensajes y las respuestas que tienen un
 * cliente y un servidor para comunicarse entre si.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public interface GenericUdp {

    public static final byte REQUEST_TEST_CONNECTION = 'T';                     // Código que indica petición de prueba de conexión con el servidor.
    public static final byte REQUEST_CONNECT = 'C';                             // Código que indica petición de inicio de conexión.
    public static final byte REQUEST_DISCONNECT = 'D';                          // Código que indica petición de fin de conexión.
    public static final byte REQUEST_GET_MESSAGE_SIZE = 'S';                    // Código que indica petición de obtener el tamaño de los paquetes.
    public static final byte REQUEST_CONFIRM = 'K';                             // Código que indica una petición de confirmación de algo al servidor.
    public static final byte REQUEST_GENERIC = 'G';                             // Código que indica petición genérica.
    public static final byte REQUEST_GENERIC_WITH_GENERIC_RESPONSE = 'W';       // Código que indica petición genérica con respuesta genérica.

    public static final String RESPONSE_ACCEPT = "Accepted";                    // Código que indica petición aceptada.
    public static final String RESPONSE_REJECT = "Rejected";                    // Código que indica petición denegada.

    public static final byte[] MESSAGE_VOID = {0};                              // Mensajes vacío que se devuelve si la petición del cliente no tiene mensaje o se enviarán al servidor si es una petición que no necesita mensajes.
}
