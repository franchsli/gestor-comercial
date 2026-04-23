package gui;
import javax.swing.JFrame;

public class Ventana extends JFrame {
    public Ventana(){
        setTitle("Gestor Comercial");
        setSize(800, 600);
        // cierra el programa al cerrar la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // centra la ventana
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
