import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interfaz5 {
    private JPanel recibo;
    private JList list1;
    private JButton Sbtn;
    private DefaultListModel listModel;
    private Interfaz2 interfaz2;
    private Interfaz3 interfaz3;
    private Interfaz4 interfaz4;

    public Interfaz5(JFrame frame) {
        listModel = new DefaultListModel();

        listModel.addElement("=== FACTURA DE VUELO ===");
        listModel.addElement("");

        if (Interfaz2.aeropuertoSeleccionado != null) {
            listModel.addElement("Aeropuerto: " + Interfaz2.aeropuertoSeleccionado.toString());
        } else {
            listModel.addElement("Aeropuerto: No seleccionado");
        }
        listModel.addElement("");

        if (Interfaz3.compaSeleccionado != null) {
            listModel.addElement("Compañía: " + Interfaz3.compaSeleccionado.toString());
        } else {
            listModel.addElement("Compañía: No seleccionada");
        }
        listModel.addElement("");

        if (Interfaz4.vueloselccionado != null) {
            listModel.addElement("Vuelo: " + Interfaz4.vueloselccionado);
        } else {
            listModel.addElement("Vuelo: No seleccionado");
        }

        listModel.addElement("");
        listModel.addElement("=== DATOS DEL PASAJERO ===");
        listModel.addElement("Nombre de usuario: " + Interfaz.nombreUsuario);
        listModel.addElement("Pasaporte: " + Interfaz.pasaporteUsuario);
        listModel.addElement("Nacionalidad: " + Interfaz.nacionalidadUsuario);
        listModel.addElement("ID pasajero: " + Interfaz.idUsuario);

        list1.setModel(listModel);

        Sbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "¡Gracias por su compra! Disfrute de su viaje");
                frame.dispose();
            }
        });
    }

    public JPanel getrecibo() {
        return recibo;
    }
}