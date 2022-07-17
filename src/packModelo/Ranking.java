package packModelo;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Ranking {
    private ArrayList<Posicion> ranking=new ArrayList<>();
    private static Ranking miRanking = null;
    private Ranking(){

    }

    public static Ranking getMiRanking(){
        if(miRanking==null){
            miRanking=new Ranking();

        }

        return  miRanking;

    }

    public void setVacio(){
        ranking.clear();
    }

    public ArrayList<Posicion> getRanking(){
        return ranking;
    }
    public void cargarRanking(){
        try{

            Scanner entrada = new Scanner(new FileReader("ranking.txt"));
            String linea;
            String[] nuevaPosi=new String[3];
            while(entrada.hasNext()){

                linea = entrada.nextLine();
                nuevaPosi=linea.split(" ");
                Posicion act=new Posicion(nuevaPosi[0],Integer.parseInt(nuevaPosi[1]) ,Integer.parseInt(nuevaPosi[2])) ;
                ranking.add(act);

            }
            entrada.close();


        }
        catch(IOException e) {e.printStackTrace();}
    }

    public void añadirPartida(String pUsuario, int pNivel, int pPuntacion){


        try{
            FileWriter fw = new FileWriter("ranking.txt", true);
            fw.write("\n" + pUsuario + " " + pNivel + " " + pPuntacion);
            fw.close();
        }
        catch(IOException e) {e.printStackTrace();}


    }

    public ArrayList<String[]> getTop10(){

        List<String[]> a= ranking.stream().sorted(Comparator.comparing(Posicion::getPuntuacion).reversed()).limit(10).map(Posicion::getDatos).collect(Collectors.toList());
        ArrayList<String[]> top = new ArrayList<>(a);
        return top;
    }



    public  ArrayList<String[]>  getTopPorNivel(int pNivel){
        ArrayList<Posicion> top10b=new ArrayList<>();


        List<String[]> a= ranking.stream().filter(posicion -> posicion.getNivel()==pNivel).sorted(Comparator.comparing(Posicion::getPuntuacion).reversed()).limit(10).map(Posicion::getDatos).collect(Collectors.toList());
        ArrayList<String[]> top10 = new ArrayList<>(a);
        return top10;



    }




}
