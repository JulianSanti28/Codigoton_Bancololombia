/*!
\file   GestionDB.java
\date   2021-10-25
\author Julián Martinez <juliansmartinez@unicauca.edu.co> 104618021321
\brief  Configura y realiza la conexión a la base de datos
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

import java.sql.*;

/**
 *
 * @author pipet
 */
public class GestionDB {

    private Connection conexion;
    private final String driver = "com.mysql.jdbc.Driver";
    private final String user = "root";
    private final String password = "";
    private final String url = "jdbc:mysql://localhost:3306/evalart_reto";

    public void conectar() {
        conexion = null;

        try {

            Class.forName(driver);
            conexion = DriverManager.getConnection(this.url, this.user, this.password);

            if (conexion != null) {
               // System.out.println("Conexión Establecida");
            } else {
                System.out.println("Conexión NO Establecida");
            }

        } catch (Exception e) {

        }

    }

    public Connection getConnection() {
        return this.conexion;
    }

    public ResultSet Execute_Query(String query) throws SQLException {
        Statement stmt = this.conexion.createStatement();
        return stmt.executeQuery(query);
    }
}
