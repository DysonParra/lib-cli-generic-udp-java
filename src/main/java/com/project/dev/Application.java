/*
 * @fileoverview    {Application}
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
package com.project.dev;

import com.project.dev.tester.udp.GenericUdpServerTester;

/**
 * TODO: Description of {@code Application}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
public class Application {

    /**
     * Entrada principal del sistema.
     *
     * @param args argumentos de la linea de comandos.
     */
    public static void main(String[] args) {
        try {
            GenericUdpServerTester.startTesting(args);
            //GenericUdpClientTester.startTesting(args);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

}
