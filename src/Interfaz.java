import Clases.BD;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Interfaz {
    public static String idUsuario;
    public static String nacionalidadUsuario;
    public static String pasaporteUsuario;
    public static String nombreUsuario;
    JTextField No;
    private JTextField Na;
    JTextField Pa;
    JTextField ID;
    private JButton sbtn;
    private JButton cbtn;
    private JPanel usuario;
    static Connection connection;
    JFrame frame;
    BD base = new BD();

    public Interfaz() {
        frame = new JFrame();

        cbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Guardar los datos en las variables estáticas antes de insertarlos
                nombreUsuario = No.getText();
                pasaporteUsuario = Pa.getText();
                nacionalidadUsuario = Na.getText();
                idUsuario = ID.getText();

                // Insertar en la base de datos
                insertarusuario(Integer.parseInt(ID.getText()), No.getText(), Pa.getText(), Na.getText());

                JFrame segundoFrame = new JFrame("Aeropuertos");
                Interfaz2 segundaVentana = new Interfaz2(segundoFrame);

                segundoFrame.setContentPane(segundaVentana.getAero());
                segundoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                segundoFrame.pack();
                segundoFrame.setSize(650, 500);
                segundoFrame.setLocationRelativeTo(null);
                segundoFrame.setResizable(false);
                segundoFrame.setVisible(true);

                frame.dispose();
            }
        });

        sbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    public boolean insertarusuario(int idPasajero, String Nombre, String Pasaporte, String Nacionalidad){
        String queryUsuario = "INSERT INTO  u984447967_op2024b.pasajeros (idPasajero, nombre, pasaporte, nacionalidad) VALUES (?,?,?,?)";
        try{
            connection = BD.conectar();
            PreparedStatement ps = connection.prepareStatement(queryUsuario);
            ps.setInt(1, idPasajero);
            ps.setString(2, Nombre);
            ps.setString(3, Pasaporte);
            ps.setString(4, Nacionalidad);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Datos insertados correctamente");
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "no se logro insertar los datos");
            return false;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Registro");
        Interfaz interfaz = new Interfaz();
        interfaz.frame = frame;
        frame.setContentPane(interfaz.getUsuario());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(650, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JPanel getUsuario() {
        return usuario;
    }
}