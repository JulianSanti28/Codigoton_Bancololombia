/*!
\file   Mesa.java
\date   2021-10-25
\author Julián Martinez <juliansmartinez@unicauca.edu.co> 104618021321
\brief  Clase Cliente para la información de los clientes
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
package Model;

import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author 57322
 */
public class Mesa {

    private String nombre_mesa;
    private Stack<Cliente> LstClientes;

    /*Constructor*/
    public Mesa(String nombre_mesa, Stack<Cliente> LstClientes) {
        this.nombre_mesa = nombre_mesa;
        this.LstClientes = LstClientes;
    }

    /*Getters and Setters*/
    public String getNombre_mesa() {
        return nombre_mesa;
    }

    public void setNombre_mesa(String nombre_mesa) {
        this.nombre_mesa = nombre_mesa;
    }

    public Stack<Cliente> getLstClientes() {
        return LstClientes;
    }

    public void setLstClientes(Stack<Cliente> LstClientes) {
        this.LstClientes = LstClientes;
    }




}
