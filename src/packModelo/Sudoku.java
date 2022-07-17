package packModelo;

import java.util.HashMap;

public class Sudoku {
    private int[][] sudoku = new int[9][9];
    private int[][] sudokuFinal=new int[9][9];
    private int level;

    public Sudoku(int[][] pInicial,int[][] pFinal, int pLevel){

        this.sudoku = pInicial;
        this.sudokuFinal = pFinal;
        this.level = pLevel;

    }

    public int[][] getSudoku(){
        return this.sudoku;
    }

    public int[][] getSudokuFinal(){
        return this.sudokuFinal;
    }

    public int getNivel(){
        return this.level;
    }



}
