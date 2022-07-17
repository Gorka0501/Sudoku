/*
 * Created by JFormDesigner on Sat Apr 10 12:13:35 CEST 2021
 */

package PackVista;

import packModelo.CargadorSudoku;
import packModelo.CatalogoSudokus;
import packModelo.Tablero;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.*;

public class InicioSesion extends JFrame {
    public InicioSesion() {
        initComponents();
    }

    private void initComponents() {

        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        textField1 = new JTextField();
        comboBox1 = getNiveles();
        button1 = new JButton();


        getContentPane().setLayout(new GridLayout(4, 1, 0, 0));
        setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/3,Toolkit.getDefaultToolkit().getScreenSize().height/3));
        setResizable(false);


        getContentPane().add(panel1);
        label3.setText("Usuario incorrecto vuelva a introducir su nombre de usuario");
        label3.setVisible(false);
        label3.setFont(new Font("Segoe IU",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/90));
        panel1.add(label3);

        getContentPane().add(panel2);
        panel2.setLayout(new GridLayout(1, 2, 0, 0));
        label1.setText("Usuario : ");
        label1.setFont(new Font("Segoe IU",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/50));
        panel2.add(label1);
        textField1.setFont(new Font("Segoe IU",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/40));
        panel2.add(textField1);

        getContentPane().add(panel3);
        panel3.setLayout(new GridLayout(1, 2, 0, 0));
        label2.setText("Seleccione un nivel :");
        label2.setFont(new Font("Segoe IU",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/60));
        panel3.add(label2);
        comboBox1.setFont(new Font("Segoe IU",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/50));
        panel3.add(comboBox1);


        getContentPane().add(button1);
        button1.setText("Continuar");
        button1.setFont(new Font("Segoe IU",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/50));
        button1.addActionListener(new BtnContinuar());
    }


    public JComboBox getNiveles(){
        JComboBox boxNiveles = new JComboBox();
        boxNiveles.addItem("1");
        boxNiveles.addItem("2");
        boxNiveles.addItem("3");

        return boxNiveles;
    }

    private void guardarDatos(String usr, Integer lvl){
        HashMap<String, Integer> datos = new HashMap<String, Integer>();
        datos.put(usr, lvl);
        System.out.println("Usuario : " + usr + " " + "\n" + "Nivel Seleccionado : " + datos.get(usr));
        CatalogoSudokus.getMiCatalogo().recibirDatos(usr, lvl);
    }

    public class BtnContinuar implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            String nivel = (String) comboBox1.getSelectedItem();
            String user = textField1.getText();
            Integer level;
            if (btn.equals(button1)) {
                if (nivel == "1") {
                    level = 1;
                }else if (nivel == "2") {
                    level = 2;
                }else {level = 3;}
                if (user.isEmpty()) {
                    label3.setVisible(true);}
                else {
                    guardarDatos(user, level);
                    Tablero.getMiTablero();
                    InicioSesion.this.dispose();
                }
            }
        }
    }

    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JTextField textField1;
    private JComboBox comboBox1;
    private JButton button1;
}
