package packModelo;

import java.util.ArrayList;
import java.util.Random;

public class CatalogoSudokus {

    private int level;
    private String usuario;
    private ArrayList<Sudoku> sudokus = new ArrayList<>();
    private static CatalogoSudokus miCatalogo = null;

    public static synchronized CatalogoSudokus getMiCatalogo(){
        if (miCatalogo == null){
            miCatalogo = new CatalogoSudokus();


        }
        return miCatalogo;
    }

    public void añadirSudoku(Sudoku p){
        sudokus.add(p);
    }

    public void devolverMatriz(){
        boolean fin=false;

        ArrayList<Sudoku> sudokusDeNivel = new ArrayList<>();
        while(!fin){
            for(Sudoku a:sudokus){
                if(a.getNivel()==level){
                    sudokusDeNivel.add(a);
                }
            }
            if(sudokusDeNivel.size()==0){
                level=level+1;
                if(level==4){
                    level=1;
                }
            }
            else{
                fin=true;
            }
        }

        Random rand=new Random();

        int num=rand.nextInt(sudokusDeNivel.size());


        Sudoku introducir=sudokusDeNivel.get(num);
        Tablero.getMiTablero().getMatrizInicialFinal(introducir.getSudoku(),introducir.getSudokuFinal());

    }

    public void recibirDatos(String pUsuario, int nivel){
        this.level=nivel;
        this.usuario=pUsuario;

    }

    public String getUsuario(){
        return usuario;
    }

    public int getLevel(){
        return level;
    }

}
