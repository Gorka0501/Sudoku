package packModelo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

public class Cronometro extends JPanel implements Runnable {
    private static Cronometro miCronometro = null;
    private JLabel tiempo;
    private Thread hilo;
    private boolean cronometroActivo;
    private int tiempoTotal;

    public Cronometro()
    {
        tiempo = new JLabel( "00:00:000" );
        tiempo.setFont( new Font( Font.SERIF, Font.BOLD, 25 ) );
        tiempo.setHorizontalAlignment( JLabel.CENTER );
        tiempo.setForeground( Color.BLACK );

        add( tiempo, BorderLayout.CENTER );
        iniciarCronometro();
        setVisible( true );
    }
    public static synchronized Cronometro getMiCronometro(){
        if(miCronometro==null){miCronometro =new Cronometro();}
        return miCronometro;
    }

    public void run(){
        Integer horas = 0 , minutos = 0 , segundos = 0, milesimas = 0;
        String hor="", min="", seg="", mil="";
        try{
            //Mientras cronometroActivo sea verdadero entonces seguira
            //aumentando el tiempo
            while(cronometroActivo){
                Thread.sleep( 4 );
                //Incrementamos 4 milesimas de segundo
                milesimas = milesimas + 4;
                if(milesimas == 1000){
                    milesimas = 0;
                    segundos++;
                    if(segundos == 60){
                        segundos = 0;
                        minutos++;
                        if(minutos == 60){
                            minutos = 0;
                            horas++;
                        }
                    }
                }
                tiempoTotal=horas*3600+minutos*60+segundos;
                //siempre este en formato 00:00:00:000
                if (horas < 10){hor = "0" + horas;}
                else{hor = horas.toString();}

                if(minutos < 10){min = "0" + minutos;}
                else{min = minutos.toString();}

                if(segundos < 10){seg = "0" + segundos;}
                else{seg = segundos.toString();}

                //Colocamos en la etiqueta la informacion
                tiempo.setText(hor + ":" + min + ":" + seg);
            }
        }catch(Exception e){}
        //Cuando se reincie se coloca nuevamente en 00:00:00:000
        tiempo.setText(hor + ":" + min + ":" + seg);
        //tiempo.setText( "00:00:00:000" );
    }

    public void iniciarCronometro(){
        cronometroActivo = true;
        hilo = new Thread( this );
        hilo.start();
    }
    public int getTiempo(){
        return this.tiempoTotal;
    }

    public void pararCronometro(){cronometroActivo = false;}
    public void restart(){miCronometro = null;}




}