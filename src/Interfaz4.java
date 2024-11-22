import Clases.BD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Interfaz4 {
    private JPanel vuelos;
    private JList<flight> list1;
    private JButton continuarButton;
    private JButton volverButton;
    private JFrame parentFrame;
    private DefaultListModel<flight> listModel;
    static flight vueloselccionado;
    static Connection connection;

    public Interfaz4(JFrame frame) {
        this.parentFrame = frame;
        listModel = new DefaultListModel<>();
        list1.setModel(listModel);

        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list1.setLayoutOrientation(JList.VERTICAL);
        list1.setVisibleRowCount(-1);

        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    vueloselccionado = list1.getSelectedValue();
                    PasarVuelos();
                }
            }
        });

        initialize();

        continuarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flight selectedVuelo = list1.getSelectedValue();
                if (selectedVuelo != null) {
                    System.out.println("Vuelo seleccionado: " + selectedVuelo);
                    PasarVuelos();
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Por favor, seleccione un vuelo",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame tercerFrame = new JFrame("Compañias");
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
        });
    }

    private static class flight {
        private int idVuelo;
        private String identificador;
        private String ciudadOrigen;
        private String ciudadDestino;
        private int precio;
        private int numMaxPasajeros;

        public flight(int idVuelo, String identificador, String ciudadOrigen, String ciudadDestino, int precio, int numMaxPasajeros) {
            this.idVuelo = idVuelo;
            this.identificador = identificador;
            this.ciudadOrigen = ciudadOrigen;
            this.ciudadDestino = ciudadDestino;
            this.precio = precio;
            this.numMaxPasajeros = numMaxPasajeros;
        }

        @Override
        public String toString() {
            return String.format("Vuelo %s: %s -> %s | Precio: %d€ | Capacidad: %d pasajeros",
                    identificador, ciudadOrigen, ciudadDestino, precio, numMaxPasajeros);
        }

        public int getIdVuelo() { return idVuelo; }
        public String getIdentificador() { return identificador; }
        public String getCiudadOrigen() { return ciudadOrigen; }
        public String getCiudadDestino() { return ciudadDestino; }
        public int getPrecio() { return precio; }
        public int getNumMaxPasajeros() { return numMaxPasajeros; }
    }

    private void initialize() {
        connectToDatabase();
        if (connection != null) {
            cargarVuelosAsync();
        }
    }

    private void connectToDatabase() {
        try {
            connection = BD.conectar();
            System.out.println("Conexión establecida correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame,
                    "Error al conectar con la base de datos: " + e.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarVuelosAsync() {
        JDialog loadingDialog = createLoadingDialog();

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                System.out.println("Iniciando carga de vuelos...");
                cargarVuelos();
                return null;
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    get();
                    System.out.println("Carga de vuelos completada");
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(parentFrame,
                            "Error durante la carga: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute(); // ¡Añadido el execute() que faltaba!
        loadingDialog.setVisible(true);
    }

    private JDialog createLoadingDialog() {
        JDialog loadingDialog = new JDialog(parentFrame, "Cargando vuelos...", false);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        loadingDialog.add(progressBar);
        loadingDialog.setSize(200, 60);
        loadingDialog.setLocationRelativeTo(parentFrame);
        return loadingDialog;
    }

    private void cargarVuelos() {
        String query = "SELECT idVuelo, identificador, ciudadOrigen, ciudadDestino, precio, numMaxPasajeros FROM u984447967_op2024b.vuelos";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            SwingUtilities.invokeLater(() -> listModel.clear());

            int count = 0;
            while (rs.next()) {
                count++;
                final int idVuelo = rs.getInt("idVuelo");
                final String identificador = rs.getString("identificador");
                final String ciudadOrigen = rs.getString("ciudadOrigen");
                final String ciudadDestino = rs.getString("ciudadDestino");
                final int precio = rs.getInt("precio");
                final int numMaxPasajeros = rs.getInt("numMaxPasajeros");

                System.out.println("Cargando vuelo: " + identificador);

                final flight vuelo = new flight(idVuelo, identificador, ciudadOrigen, ciudadDestino, precio, numMaxPasajeros);

                SwingUtilities.invokeLater(() -> {
                    listModel.addElement(vuelo);
                    System.out.println("Vuelo agregado al modelo: " + vuelo);
                });
            }

            final int finalCount = count;
            SwingUtilities.invokeLater(() -> {
                System.out.println("Total de vuelos cargados: " + finalCount);
                if (finalCount == 0) {
                    JOptionPane.showMessageDialog(parentFrame,
                            "No se encontraron vuelos en la base de datos",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(parentFrame,
                        "Error al cargar los vuelos: " + e.getMessage(),
                        "Error de datos",
                        JOptionPane.ERROR_MESSAGE);
            });
        }
    }

    private void PasarVuelos() {
        if (vueloselccionado != null) {
            JFrame quintoFrame = new JFrame("Recibo del vuelo " + vueloselccionado.getIdentificador());
            Interfaz5 quintaVentana = new Interfaz5(quintoFrame);

            quintoFrame.setContentPane(quintaVentana.getrecibo());
            quintoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            quintoFrame.pack();
            quintoFrame.setSize(1000, 550);
            quintoFrame.setLocationRelativeTo(null);
            quintoFrame.setResizable(false);
            quintoFrame.setVisible(true);

            parentFrame.dispose();
        }
    }

    public JPanel getvuelos() {
        return vuelos;
    }
}