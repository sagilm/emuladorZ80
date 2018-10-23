package componentes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class simuladorZ80 extends JFrame {
    private JPanel rootpanel;
    private JTextArea INArea;
    private JTextArea OUTArea;
    private JTextField direccion;
    private JButton Turn_on;


    public simuladorZ80(){
        Loader input = new Loader();
        add(rootpanel);
        setTitle("simulador z80");
        setSize(800,700);
        //ins.setEnabled(true);
        INArea.setEditable(false);
        Turn_on.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileC = new JFileChooser();
                fileC.showOpenDialog(null);
                File f = fileC.getSelectedFile();
                String filename = f.getAbsolutePath();
                direccion.setText(filename);
                try {
                    FileReader reader = new FileReader(filename);
                    BufferedReader br = new BufferedReader(reader);
                    INArea.read(br,null);
                    br.close();
                    INArea.requestFocus();
                    input.muestraContenido(filename);

                    FileReader reader2 = new FileReader("fichero.txt");
                    br = new BufferedReader(reader2);
                    OUTArea.read(br,null);
                    br.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            }
        });
    }


}
