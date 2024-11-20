import Clases.BD;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Interfaz2 {
    private JPanel Aero;
    private JList list1;
    private JList list2;
    private JButton Volver;
    private JButton nbtn;
    private JFrame parentFrame;
    static Connection connection;
    private DefaultListModel<Interfaz2> modeloprivado;
    private DefaultListModel<Interfaz2> modelopublico;
    int id;
    String nombre;
    String ciudad;
    String pais;
    boolean privado;
    boolean publico;
    double subvencion;
    private static Interfaz2 aeropuertoSeleccionado;

    public Interfaz2(int id, String nombre, String ciudad, String pais,
                     boolean privado, boolean publico, double subvencion){
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
        this.privado = privado;
        this.publico = publico;
        this.subvencion = subvencion;
    }

    @Override
    public String toString() {
        return nombre + " (" + ciudad + ", " + pais + ")";
    }

    public Interfaz2(JFrame frame) {
        this.parentFrame = frame;

        modeloprivado = new DefaultListModel<>();
        modelopublico = new DefaultListModel<>();

        list1.setModel(modeloprivado);
        list2.setModel(modelopublico);

        conexion();
        Caeropuerto();

        Volver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.dispose();

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
        });

        nbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Interfaz2 aeropuertoSeleccionadoPrivado = (Interfaz2) list1.getSelectedValue();
                Interfaz2 aeropuertoSeleccionadoPublico = (Interfaz2) list2.getSelectedValue();

                if (aeropuertoSeleccionadoPrivado != null || aeropuertoSeleccionadoPublico != null) {
                    aeropuertoSeleccionado = aeropuertoSeleccionadoPrivado != null ?
                            aeropuertoSeleccionadoPrivado :
                            aeropuertoSeleccionadoPublico;
                    PasarCompañia();
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Por favor, seleccione un aeropuerto",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        list1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    aeropuertoSeleccionado = (Interfaz2) list1.getSelectedValue();
                    if (aeropuertoSeleccionado != null) {
                        list2.clearSelection();
                        PasarCompañia();
                    }
                } else if (evt.getClickCount() == 1) {
                    list2.clearSelection();
                }
            }
        });

        list2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    aeropuertoSeleccionado = (Interfaz2) list2.getSelectedValue();
                    if (aeropuertoSeleccionado != null) {
                        list1.clearSelection();
                        PasarCompañia();
                    }
                } else if (evt.getClickCount() == 1) {
                    list1.clearSelection();
                }
            }
        });
    }

    private void PasarCompañia() {
        if (aeropuertoSeleccionado != null) {
            JFrame tercerFrame = new JFrame("Compañías del aeropuerto " + aeropuertoSeleccionado.nombre);
            Interfaz3 terceraVentana = new Interfaz3(tercerFrame);

            tercerFrame.setContentPane(terceraVentana.getcompa());
            tercerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            tercerFrame.pack();
            tercerFrame.setSize(500, 650);
            tercerFrame.setLocationRelativeTo(null);
            tercerFrame.setResizable(false);
            tercerFrame.setVisible(true);

            parentFrame.dispose();
        }
    }

    private void configurarEventosLista(JList<Interfaz2> lista) {
        lista.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    aeropuertoSeleccionado = lista.getSelectedValue();
                    PasarCompañia();
                    if (aeropuertoSeleccionado != null) {
                        modeloprivado.clear();
                        modelopublico.clear();
                        lista.setSelectedValue(aeropuertoSeleccionado, true);

                        JOptionPane.showMessageDialog(parentFrame,
                                "Aeropuerto seleccionado: " + aeropuertoSeleccionado.nombre,
                                "Selección",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
    }


    private void Caeropuerto(){
        String query = "SELECT idAeropuerto, nombre, ciudad, pais, privado, publico, subvencion " +
                "FROM u984447967_op2024b.aeropuertos";
        try{
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Interfaz2 aeropuerto = new Interfaz2(
                        rs.getInt("idAeropuerto"),
                        rs.getString("nombre"),
                        rs.getString("ciudad"),
                        rs.getString("pais"),
                        rs.getBoolean("privado"),
                        rs.getBoolean("publico"),
                        rs.getDouble("subvencion")
                );

                if (aeropuerto.privado) {
                    modeloprivado.addElement(aeropuerto);
                }
                if (aeropuerto.publico) {
                    modelopublico.addElement(aeropuerto);
                }
            }

            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("Error al cargar los datos: " + e.getMessage());
        }
    }

    private void conexion(){
        try{
            connection = BD.conectar();
        } catch (Exception e){
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    public JPanel getAero() {
        return Aero;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Aeropuertos");
                Interfaz2 ventana = new Interfaz2(frame);
                frame.setContentPane(ventana.getAero());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setSize(650, 500);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setVisible(true);
            }
        });
    }
}