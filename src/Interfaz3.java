import Clases.BD;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Interfaz3 {
    private JList<Compania> list1;
    private JPanel compa;
    private JButton cbt;
    private JButton vbt;
    private JFrame parentFrame;
    static Connection connection;
    private DefaultListModel<Compania> listModel;
    private static Compania compaSeleccionado;

    public Interfaz3(JFrame frame) {
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
                    compaSeleccionado = list1.getSelectedValue();
                    PasarCompañia();
                }
            }
        });

        initialize();

        cbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Compania selectedCompania = list1.getSelectedValue();
                if (selectedCompania != null) {
                    System.out.println("Compañía seleccionada: " + selectedCompania);
                    PasarCompañia();
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Por favor, seleccione una compañía",
                            "Selección requerida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        vbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame segundoFrame = new JFrame("Aeropuertos");
                Interfaz2 segundaVentana = new Interfaz2(segundoFrame);

                segundoFrame.setContentPane(segundaVentana.getAero());
                segundoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                segundoFrame.pack();
                segundoFrame.setSize(650, 500);
                segundoFrame.setLocationRelativeTo(null);
                segundoFrame.setResizable(false);
                segundoFrame.setVisible(true);

                parentFrame.dispose();
            }
        });
    }

    public static class Compania {
        private int idCompania;
        private String nombre;

        public Compania(int idCompania, String nombre) {
            this.idCompania = idCompania;
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }

        public int getIdCompania() {
            return idCompania;
        }

        public String getNombre() {
            return nombre;
        }
    }

    private void initialize() {
        connectToDatabase();
        if (connection != null) {
            cargarCompaniasAsync();
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

    private void cargarCompaniasAsync() {
        JDialog loadingDialog = createLoadingDialog();

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                System.out.println("Iniciando carga de compañías...");
                cargarCompanias();
                return null;
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    get();
                    System.out.println("Carga de compañías completada");
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(parentFrame,
                            "Error durante la carga: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
        loadingDialog.setVisible(true);
    }

    private JDialog createLoadingDialog() {
        JDialog loadingDialog = new JDialog(parentFrame, "Cargando compañías...", false);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        loadingDialog.add(progressBar);
        loadingDialog.setSize(200, 60);
        loadingDialog.setLocationRelativeTo(parentFrame);
        return loadingDialog;
    }

    private void cargarCompanias() {
        String query = "SELECT idCompania, nombre FROM u984447967_op2024b.companias";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            SwingUtilities.invokeLater(() -> listModel.clear());

            int count = 0;
            while (rs.next()) {
                count++;
                final String nombre = rs.getString("nombre");
                final int id = rs.getInt("idCompania");

                System.out.println("Cargando compañía: " + nombre);

                final Compania compania = new Compania(id, nombre);

                SwingUtilities.invokeLater(() -> {
                    listModel.addElement(compania);
                    System.out.println("Compañía agregada al modelo: " + compania);
                });
            }

            final int finalCount = count;
            SwingUtilities.invokeLater(() -> {
                System.out.println("Total de compañías cargadas: " + finalCount);
                if (finalCount == 0) {
                    JOptionPane.showMessageDialog(parentFrame,
                            "No se encontraron compañías en la base de datos",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(parentFrame,
                        "Error al cargar las compañías: " + e.getMessage(),
                        "Error de datos",
                        JOptionPane.ERROR_MESSAGE);
            });
        }
    }

    private void PasarCompañia() {
        if (compaSeleccionado != null) {
            JFrame cuartoFrame = new JFrame("Vuelos de la Compañía " + compaSeleccionado.getNombre());
            Interfaz4 cuartaVentana = new Interfaz4(cuartoFrame);

            cuartoFrame.setContentPane(cuartaVentana.getvuelos());
            cuartoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            cuartoFrame.pack();
            cuartoFrame.setSize(500, 650);
            cuartoFrame.setLocationRelativeTo(null);
            cuartoFrame.setResizable(false);
            cuartoFrame.setVisible(true);

            parentFrame.dispose();
        }
    }

    public JPanel getcompa() {
        return compa;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Compañías");
                Interfaz3 ventana = new Interfaz3(frame);
                frame.setContentPane(ventana.getcompa());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setSize(650, 1000);
                frame.setVisible(true);
            }
        });
    }
}