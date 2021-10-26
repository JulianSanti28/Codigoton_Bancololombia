/*!
\file   AsignarMesas.java
\date   2021-10-25
\author Julián Martinez <juliansmartinez@unicauca.edu.co> 104618021321
\brief  Lógica para aplicar los filtros con respecto a cada una de las mesas
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julián Santiago Martinez Trullo
 */
public class AsignarMesas {

    private GestionDB GDB; //Inicialización de objeto para interacción con la base de datos.
    private ResultSet result_query; //Guarda el resultado de la consulta SQL
    private int cantidad_filas = 0; //Cuenta el número de filas d ela consulta
    private ArrayList<ArrayList<String>> LstMesas; //Almacena la Lista de Listas que llega desde el archivo
    private ArrayList<Mesa> MesasGeneradas = new ArrayList<Mesa>(); //Almacena las mesas generadas según sean los filtros
    private Stack<Cliente> LstClientes; //Almacena loa clientes filtrados para cada mesa

    /*Constructor*/
    public AsignarMesas(ArrayList<ArrayList<String>> LstMesas) throws IOException {
        this.LstMesas = LstMesas;
        generarMesas();//Se llama el método para iniciar con la generación de las mesas
    }

    /**
     * @return @throws SQLException
     * @brief Almacena las mesas con sus respectivos clientes y filtros
     */
    private void generarMesas() {

        this.LstMesas.forEach(i -> {
            try {
                this.MesasGeneradas.add(generarMesaFiltros(i.get(0), generarConsultaSQL(i))); //Guarda los objetos Mesa generados
            } catch (SQLException ex) {
                Logger.getLogger(AsignarMesas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AsignarMesas.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     *
     * @param nombre_mesa
     * @param query
     * @return
     * @throws SQLException
     * @brief Aplica los filtros respectivos a cada mesa
     */
    private Mesa generarMesaFiltros(String nombre_mesa, String query) throws SQLException, IOException {

        
        HashMap<Integer, String> map = new HashMap<>(); //Map Hash para validar que no ingresen clientes de la misma compania
        this.cantidad_filas = 0; //Inicializa la cantidas de filas en 0 para cada consulta
        this.LstClientes = new Stack<Cliente>(); // Lista para guardar los clientes de cada mesa
        int genero_actual = 0; //Me guarda el género de la última persona agregada
        GDB = new GestionDB();
        GDB.conectar();
        this.result_query = GDB.Execute_Query(query); //Ejecuta y guarda el resultado de la consulta
        while (this.result_query.next()) { //Recorre cada fila retornada por la consulta

            if (this.LstClientes.size() < 8) { //Si aún no se han ingresado 8 clientes entonces siga
                int key = this.result_query.getInt(9); //Toma como clave del map el codigo de la compania del cliente
                String value = this.result_query.getString(5); //Toma como el valor de la clave el codigo del cliente

                if (!map.isEmpty() && !map.containsKey(key)) { //Si el map no está vacio y ademas la clave no existe aún entonces siga
                    map.put(key, value); //Inserta la clave y al valor el map
                    if (this.result_query.getInt(6) != genero_actual) {
                        if (this.result_query.getInt(10) == 1) { //Si el código está encriptado
                            /*Se envía como código el valor retornado por el método desencriptarPw*/
                            Cliente Client = new Cliente(desencriptarPw(this.result_query.getString(5)), this.result_query.getInt(6), this.result_query.getInt(9)); //Se crea el objeto Cliente con la información
                            this.LstClientes.add(Client); //Se guarda el cliente para esa mesa
                            genero_actual = this.result_query.getInt(6); //Se actualiza el valor del género
                        } else { //Si el código no está encriptado

                            Cliente Client = new Cliente(this.result_query.getString(5), this.result_query.getInt(6), this.result_query.getInt(9)); //Se crea el objeto Cliente con la información
                            this.LstClientes.add(Client); //Se guarda el cliente para esa mesa
                            genero_actual = this.result_query.getInt(6); //Se actualiza el valor del género

                        }

                    }

                } else if (map.isEmpty()) { //Si el map está vacío, entonces siga
                    map.put(key, value); //Inserta la clave y al valor el map
                    genero_actual = this.result_query.getInt(6);//Se guarda el valor del género de la primera persona que ingresó
                    Cliente Client = new Cliente(this.result_query.getString(5), this.result_query.getInt(6), this.result_query.getInt(9)); //Se crea el objeto Cliente con la información
                    this.LstClientes.add(Client);//Se guarda el cliente para esa mesa
                }
            }
            this.cantidad_filas += 1; //Incrementa el número de filas de la consulta
        }

        Mesa M = new Mesa(nombre_mesa, this.LstClientes); //Se crea la mesa con su respectiva lista de clientes
        return M;//Se retorna el objeto Mesa creado
    }

    /**
     *
     * @param MesaFiltros
     * @return String
     * @brief Genera las consultas SQL dependiendo los filtros de la mesa
     */
    private String generarConsultaSQL(ArrayList<String> MesaFiltros) {

        /*Variable boooleana para los casos de filtros RI, RF*/
        boolean flag = false;
        /*Valor general para todas las consultas*/
        String query = "SELECT *, SUM(account.balance) as 'Sum'  FROM account, client WHERE account.client_id = client.id";
        for (String i : MesaFiltros) {
            String[] filtros = i.split(":");
            if (filtros.length > 1) {
                if (filtros[0].equals("TC")) {
                    query = query + " " + "AND client.type = " + filtros[1] + " ";
                } else if (filtros[0].equals("UG")) {
                    query = query + " " + "AND client.location = " + filtros[1] + " ";
                } else if (filtros[0].equals("RI")) {
                    flag = true;
                    query = query + " " + " GROUP BY client_id HAVING Sum >  " + filtros[1] + " ";
                } else if (filtros[0].equals("RF")) {

                    flag = true;
                    query = query + " " + "GROUP BY client_id HAVING Sum <  " + filtros[1] + " ";
                }

            }
        }
        /*Si se aplicó algún filtro de RI, RF*/
        if (flag) {
            query = query + "ORDER BY Sum DESC, client.code ASC ";
        } else {
            query = query
                    + "GROUP BY client_id\n"
                    + "ORDER BY Sum DESC, client.code ASC";
        }
        /*Retorna la consulta aplicando los filtros correspondientes*/
        return query;
    }

    /**
     * @return ArrayList<Mesa>
     * @brief Muestra la información de cada mesa y valida si se asigna o se
     * cancela.
     */
    public ArrayList<Mesa> mostrarMesas() {
        for (Mesa i : this.MesasGeneradas) {
            System.out.println(i.getNombre_mesa());
            if (i.getLstClientes().size() < 4 || i.getLstClientes().size() % 2 != 0) {
                System.out.println("CANCELADA");

            } else {
                for (int j = 0; j < i.getLstClientes().size(); j++) {
                    if (j != i.getLstClientes().size() - 1) {
                        System.out.print(i.getLstClientes().get(j).getCodigo() + ",");
                    } else {
                        System.out.print(i.getLstClientes().get(j).getCodigo());
                    }

                }
                System.out.println("");
            }
        }
        return this.MesasGeneradas;
    }

    /**
     * @param pass_encriptada
     * @return String
     * @brief Consume un servicio web y retorna la respuesta de la petición.
     */
    private String desencriptarPw(String pass_encriptada) throws MalformedURLException, IOException {

        String url_consultar = "https://test.evalartapp.com/extapiquest/code_decrypt/" + pass_encriptada; //Se consume la url seguido de la password encriptada
        URL url = new URL(url_consultar); //Se inicia la URL
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET"); //Se usa la petición a través del método GET
        int status = con.getResponseCode();
        
        /*Se lee la respuesta de la petición*/
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer pass_descencriptada = new StringBuffer(); //Guarda el valor de la respuesta
        while ((inputLine = in.readLine()) != null) {
            pass_descencriptada.append(inputLine); 
        }

        in.close();//Cerrar el BufferedReader
        con.disconnect(); //Desconectar la conexión

        return pass_descencriptada.toString(); //Retornar la respuesta

    }

}
