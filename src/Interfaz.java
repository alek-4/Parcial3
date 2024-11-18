import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interfaz {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton sbtn;
    private JButton cbtn;
    private JPanel usuario;
    JFrame frame;

    public Interfaz() {
        frame = new JFrame();


        cbtn.addActionListener(new ActionListener() {
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