package PackVista;

import packModelo.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PantallaFinal extends JFrame  {


    public PantallaFinal(){
        initComponents();
        this.setLocationRelativeTo(null);

        bRanking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!tipoRanking){
                    tipoRanking = true;
                    rankings.setViewportView(getTop10());
                }
            }
        });

        bRankingNivel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tipoRanking){ tipoRanking = false;}
                rankings.setViewportView(getTop10());
            }
        });

        bRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); //Cierra la pantalla
                String[] l = new String[]{"restart"};
                Tablero.getMiTablero().restart();
                main.main(l);  //Reinicio el main
            }
        });
    }

    private void initComponents(){
        setSize(getToolkit().getScreenSize());
        setExtendedState(MAXIMIZED_BOTH);
        setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/2,Toolkit.getDefaultToolkit().getScreenSize().height/2));
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getBotones(),BorderLayout.NORTH);
        getContentPane().add(getRankings(),BorderLayout.CENTER);
    }

    public JPanel getBotones(){
        botones=new JPanel();
        botones.setLayout(new GridLayout(1,4,0,0));
        bRanking=new JButton();
        bRanking.setText("Ranking");
        bRanking.setFont(new Font("Sherif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().width/30));
        botones.add(bRanking);
        bRankingNivel=new JButton();
        bRankingNivel.setText("Ranking Nivel");
        bRankingNivel.setFont(new Font("Sherif",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/30));
        botones.add(bRankingNivel);
        elegirNivel=new JComboBox();
        elegirNivel.addItem("1");
        elegirNivel.addItem("2");
        elegirNivel.addItem("3");
        elegirNivel.setFont(new Font("Sherif",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/30));
        botones.add(elegirNivel);
        bRestart=new JButton();
        bRestart.setText("Restart");
        bRestart.setFont(new Font("Sherif",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/30));
        botones.add(bRestart);
        return botones;
    }

    public JScrollPane getRankings(){
        rankings=new JScrollPane();
        rankings.setViewportView(getTop10());
        return rankings;
    }

    private JPanel getTop10(){
        if (top10==null){
            top10 = new JPanel();
            top10.setLayout(new GridLayout(0,1,0,0));
        }
        while(top10.getComponentCount()>0){
            top10.remove(0);
        }
        ArrayList<String[]> top;
        if(tipoRanking) {
            top = Ranking.getMiRanking().getTop10();
        }else{
            String nivel = "1";
            if(elegirNivel.getSelectedItem() != null){nivel = (String) elegirNivel.getSelectedItem();}
            top = Ranking.getMiRanking().getTopPorNivel(Integer.parseInt(nivel));
        }
        int cont = 1;
        for (String[] s : top){
            JLabel posicion = new JLabel();
            posicion.setText(cont+".    Nombre:" + s[0] +"   Puntuacion:" + s[2] + "    Nivel:"+ s[1]);
            posicion.setHorizontalAlignment(SwingConstants.LEFT);
            posicion.setFont(new Font("Sherif",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/30));
            top10.add(posicion);
            cont++;
        }
        return top10;
    }

    //Atributos
    private JButton bRanking;
    private JButton bRankingNivel;
    private JButton bRestart;
    private JPanel botones;
    private JScrollPane rankings;
    private JPanel top10;
    boolean tipoRanking;
    private JComboBox elegirNivel;

}