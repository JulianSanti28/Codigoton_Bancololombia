/*!
\file   Cliente.java
\date   2021-10-25
\author Julián Martinez <juliansmartinez@unicauca.edu.co> 104618021321
\brief  Clase Cliente del programa.
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

/**
 *
 * @author Julián Santiago Martinez Trullo
 */
public class Cliente {

    /*Atributos*/
    private String codigo;
    private int genero;
    private int compania;

    /*Constructor parametrizado*/
    public Cliente(String codigo, int genero, int compania) {
        this.codigo = codigo;
        this.genero = genero;
        this.compania = compania;
    }

    /*Getters and Setters*/
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getGenero() {
        return genero;
    }

    public void setGenero(int genero) {
        this.genero = genero;
    }

    public int getCompania() {
        return compania;
    }

    public void setCompania(int compania) {
        this.compania = compania;
    }

}
