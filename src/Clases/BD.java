package Clases;

import java.sql.*;
public class BD {
static String url="jdbc:mysql://82.197.82.62:3306/u984447967_op2024b";
static String user="u984447967_unipaz";
static String pass="estudiantesPoo2024B.*";

public static Connection conectar()
{
    Connection con = null;
    try
    {
        con=DriverManager.getConnection(url,user,pass);
        System.out.println("Conexion exitosa");
    } catch (SQLException e)
    {
        System.out.println("Error al conectar");
    }
    return con;
}
}
