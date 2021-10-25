/*!
\file   GestionArchivo.java
\date   2021-10-25
\author Julián Martinez <juliansmartinez@unicauca.edu.co> 104618021321
\brief  Lee y gestiona la información del archivo
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GestionArchivo {

    private FileReader archivo;
    private File archivoFuente = null;
    private BufferedReader bf = null;

    public GestionArchivo() {
    }

     /**
     * @return ArrayList<ArrayList<String>>
     * @param ruta
     * @brief Retorna una lista de listas, cada lista interna es el nombre d ela mesa y todos sus filtros.
     */
    public ArrayList<ArrayList<String>> leerArchivo(String ruta) {

        ArrayList<ArrayList<String>> LstMesas = new ArrayList<ArrayList<String>>();
        /*No se inicializa memoria, sólo se da el new cuando se cumpla la condición de que es una nueva mesa*/
        ArrayList<String> LstMesasFiltro = null;

        try {
            archivoFuente = new File(ruta);
            archivo = new FileReader(archivoFuente);
            bf = new BufferedReader(archivo);
            String[] entrada;
            int mesa = 0;
            if (bf != null) {
                String linea;
                while ((linea = bf.readLine()) != null) {

                    /*Si la linea que se lee tiene un signo < quiere decir que es una mesa*/
                    if (linea.contains("<")) {

                        /*Si ya se creo una mesa con sus respectivos filtros*/
                        if (LstMesasFiltro != null) {
                            /*Agrega a la lista de mesas la mesa con sus filtros respectivamente*/
                            LstMesas.add(LstMesasFiltro);
                        }

                        /*Asigna memoria nueva a la lista para esa mesa y sus filtros*/
                        LstMesasFiltro = new ArrayList<String>();
                        LstMesasFiltro.add(linea);

                        /*Si la línea no contiene el signo < quiere decir que es un filtro de la mesa que sí lo contiene*/
                    } else {
                        LstMesasFiltro.add(linea);
                    }

                }
                /*Se guarda la última mesa del archivo debido a que después de esta el ciclo termina (porque la siguiente línea es null)*/
                LstMesas.add(LstMesasFiltro);

                return LstMesas;
            } else {
                System.out.println("EL archivo está vacío");
            }
            bf.close();
        } catch (FileNotFoundException f) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Archivo con información no válida");
        }
        return null;
    }


}
