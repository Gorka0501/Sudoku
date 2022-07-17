package packModelo;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CargadorSudoku {
    private static CargadorSudoku miCargadorSudoku = null;


    private ArrayList<Sudoku> sudokus = new ArrayList<>();



    public static synchronized CargadorSudoku getMiCargadorSudoku(){
        if (miCargadorSudoku == null){
            miCargadorSudoku = new CargadorSudoku();
            miCargadorSudoku.cargarFichero("sudoku.txt");

        }
        return miCargadorSudoku;
    }



    public void cargarFichero(String nomF){

        try{
            Scanner entrada = new Scanner(new FileReader(nomF));
            int f;
            String codigo;
            String fila;

            while(entrada.hasNext()){
                int[][] matrizAct=new int[9][9];
                int[][] matrizR=new int[9][9];
                codigo = entrada.nextLine();

                String dificultad=entrada.nextLine();

                for (int i=1;i<=2;i++){

                    f=0;
                    while (f<9) {
                        fila=entrada.nextLine();
                        int cont=0;
                        while(cont<9){
                            char l=fila.charAt(cont);

                            if(i==1){
                                if (Character.getNumericValue(l) == 0){
                                    matrizAct[f][cont] = Character.getNumericValue(l);
                                }
                                else {
                                    matrizAct[f][cont] = Character.getNumericValue(l);
                                }

                            }else{
                                matrizR[f][cont]=Character.getNumericValue(l);
                            }
                            cont++;
                        }
                        f++;}}

                    Sudoku nuevo=new Sudoku(matrizAct,matrizR,Integer.parseInt(dificultad));
                    CatalogoSudokus.getMiCatalogo().añadirSudoku(nuevo);
            }


            entrada.close();


       }
        catch(IOException e) {e.printStackTrace();}


    }





}
