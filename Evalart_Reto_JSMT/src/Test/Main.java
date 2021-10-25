/*!
\file   Main.java
\date   2021-10-25
\author Julián Martinez <juliansmartinez@unicauca.edu.co> 104618021321
\brief  Método principal del programa.
\par Copyright
Information contained herein is proprietary to and constitutes valuable
confidential trade secrets of unicauca, and
is subject to restrictions on use and disclosure.
\par
Copyright (c) unicauca 2021. All rights reserved.
\par
The copyright notices above do not evidence any actual or
intended publication of this material.
*******************************************************************************/
package Test;
import Model.AsignarMesas;
import Model.GestionArchivo;
import Model.GestionDB;
import java.io.IOException;
import java.sql.*;

/**
 *
 * @author Julián Santiago Martinez Trullo
 *
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, IOException {

        GestionArchivo A = new GestionArchivo();//Se instancia la clase para gestionar el archivo
        /**
         * Se instancia la clase de asignación de mesas y se le envía a su
         * constructor el valor retornado por el método leerArchivo(Es una lista
         * de listas) cada lista interna tiene [<Mesa> , Filtro 1, Filtro 2, ...
         * Filtro n]
         */
        AsignarMesas AM = new AsignarMesas(A.leerArchivo("entrada.txt"));
        AM.mostrarMesas(); //Muestra la información de cada mesa generada

    }

}
