package packModelo;

import PackVista.TableroVista;

import java.util.ArrayList;
import java.util.Observable;

public class Tablero extends Observable {
    private static Tablero miTablero = null;
    private Casilla[][] matrizActual= new Casilla[9][9];
    private int[][] matrizFinal=new int[9][9];
    private int ayudas=0;
    private Tablero() {

    }
    public static synchronized Tablero getMiTablero() {
        if(miTablero == null) {
            miTablero = new Tablero();
            CargadorSudoku.getMiCargadorSudoku().cargarFichero("sudoku.txt");
            CatalogoSudokus.getMiCatalogo().devolverMatriz();
            miTablero.calcularCandidatos(miTablero.matrizActual);
            miTablero.inicarVista();
        }
        return miTablero;
    }
    private void inicarVista() {
        TableroVista tab = new TableroVista();
        tab.setVisible(true);
        this.addObserver(tab);
        setChanged();
        notifyObservers();
    }
    public void getMatrizInicialFinal(int[][] n, int[][] f) {
        for(int i=0; i<9; i++){
            matrizActual[i] = new Casilla[9];
            for(int j=0; j<9; j++){
                if(n[i][j]==0){
                    matrizActual[i][j]=new Casilla(n[i][j],true);
                }
                else{
                    matrizActual[i][j]=new Casilla(n[i][j],false);
                }

            }
        }
        matrizFinal = f;
    }
    public Casilla[][] getMatrizActual() {
        return matrizActual;
    }

    public boolean victoria(){
        boolean victoria =true;
        int i=0;
        int j;
        while(victoria && i<9){
            j=0;
            while(victoria && j<9){
                if(matrizActual[i][j].getValor() != matrizFinal[i][j]){
                   victoria = false;
                }j++;
            }i++;
        }
        return victoria;
    }

    public void modificar(int pi,int pj,String pValor, String pCandidato) {
        int valor;
        int candidato;
        if (matrizActual[pi][pj].esModificable()) {
            if (pValor.equals("") || pValor.equals("0")){
                valor=0;//No hay nada en los valores
                if(!(pCandidato.equals("") || pCandidato.equals("0")) && matrizActual[pi][pj].getValor()==0){ //No hay nada en los candidatos
                    candidato = Integer.parseInt(pCandidato);
                    matrizActual[pi][pj].actualizarDescartados(candidato);
                }
                else if(pCandidato.equals("") || pCandidato.equals("0")) {
                    matrizActual[pi][pj].setValor(valor);
                    calcularCandidatos(matrizActual);
                }
            }
            else {//Se pone el valor
                valor = Integer.parseInt(pValor);
                matrizActual[pi][pj].setValor(valor);
                calcularCandidatos(matrizActual);
                if (victoria()) {
                    Cronometro.getMiCronometro().pararCronometro();
                    gestionarRanking();
                }
            }
            boolean[] arg = new boolean[1];
            arg[0] = victoria();
            setChanged();
            notifyObservers(arg);
        }
    }
    public void gestionarRanking(){
        String usr=CatalogoSudokus.getMiCatalogo().getUsuario();
        int level=CatalogoSudokus.getMiCatalogo().getLevel();
        int tiempo=Cronometro.getMiCronometro().getTiempo();
        int puntuacion=(30000*level/(tiempo + (30*ayudas)));

        Ranking.getMiRanking().añadirPartida(usr,level,puntuacion);
        Ranking.getMiRanking().cargarRanking();
        Ranking.getMiRanking().getTop10();
        //Ranking.getMiRanking().getTopPorNivel(1);
    }

    public void setAyudas(int p){
        ayudas=p;

    }

    public void calcularCandidatos(Casilla[][] matrix){
        int[] descartes = new int[8];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j].getValor() == 0) {
                    int[] candidatos = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}; //Se resetea en cada posicion
                    descartes = calcularFilas(candidatos, i, j, matrix);
                    descartes = calcularColumnas(descartes, i, j, matrix);
                    descartes = calcularZona(descartes, i, j, matrix);
                    matrix[i][j].actualizarCandidatos(descartes);
                }
            }
        }
    }


    public int[] calcularFilas(int [] candidatos, int i , int j, Casilla[][] matrix){
        for (int c = 0; c < matrix[i].length; c ++){
            if (matrix[i][c].getValor() != 0 && c != j) {
                int descarte = matrix[i][c].getValor();
                candidatos[descarte - 1] = 0; // Elemento borrado;
            }
        }
        return candidatos;
    }

    public int[] calcularColumnas(int [] candidatos, int i, int j, Casilla[][] matrix){
        for (int f = 0; f < matrix.length; f++){
            if (matrix[f][j].getValor() != 0 && f != i){
                int descarte = matrix[f][j].getValor();
                candidatos[descarte - 1] = 0; //Elemento borrado
            }
        }
        return candidatos;
    }

    public int[] calcularZona(int [] candidatos, int i, int j, Casilla[][] matrix){
        int fila = i/3;
        int columna = j/3;
        for(int c = fila*3; c <(fila*3)+3; c++){
            for(int k = columna*3; k <(columna*3)+3; k++){
                if (matrix[c][k].getValor() != 0) {
                    int descarte = matrix[c][k].getValor();
                    candidatos[descarte - 1] = 0; //Elemento borrado
                }
            }
        }

        return candidatos;
    }

    public boolean[] getCandidatos(int i, int j){
        return matrizActual[i][j].getCandidatos();
    }

    public ArrayList<Integer> ayuda(){
        ArrayList<Integer> l = soleCandidate(matrizActual);
        if(l.isEmpty()){ //No hay solucion con el sole
            l = uniqueCandidate(matrizActual);

        }
        return l;
    }
    public ArrayList<Integer> soleCandidate(Casilla[][] matrix){ //fila, columna y numero y tipo
        ArrayList<Integer> l = new ArrayList<Integer>();
        int i = 0;
        boolean salir = false;
        while (i < matrix.length && !salir) {
            int j = 0;
            while (j < matrix[i].length && !salir) {
                if (matrix[i][j].getValor() == 0) {
                    boolean[] candidatos = matrix[i][j].getCandidatos();
                    int cont = 0;
                    int num = 0;
                    for(int w = 0; w < candidatos.length; w++){
                        if(candidatos[w]){cont++; num = w + 1;}
                    }
                    if(cont == 1){
                        l.add(i);
                        l.add(j);
                        l.add(num);
                        l.add(0);
                        salir = true;
                    }
                }
                j++;
            }
            i++;
        }
        return l;
    }


    public ArrayList<Integer> uniqueCandidate(Casilla[][] matrix){ //fila, columna y numero y tipo
        ArrayList<Integer> l = new ArrayList<Integer>();
        int i = 0;
        boolean salir = false;
        while (i < matrix.length && !salir) {
            int j = 0;
            while (j < matrix[i].length && !salir) {
                if (matrix[i][j].getValor() == 0) {
                    boolean[] candidatos = matrix[i][j].getCandidatos();
                    int w = 0;
                    while(w < candidatos.length && !salir){
                        if(candidatos[w]){ //Comprobamos fila y columna y cuadrado a ver si no esta el candidato
                            int num = w + 1;
                            if(filaUnique(matrix,i,j,num) || columnaUnique(matrix,i,j,num) || zonaUnique(matrix,i,j,num)){
                                salir = true;
                                l.add(i);
                                l.add(j);
                                l.add(num);
                                l.add(1);
                            }
                        }
                        w++;
                    }
                }
                j++;
            }
            i++;
        }
        return l;
    }

    public boolean filaUnique(Casilla[][] matrix, int i, int j, int num){
        boolean salir = false;
        int c = 0;
        boolean[] candidatos;
        while (c < matrix[i].length && !salir){
            if (matrix[i][c].getValor() == 0 && c != j) {
                candidatos = matrix[i][c].getCandidatos();
                if(candidatos[num - 1]){ //No seguimos buscando
                    salir = true;
                }
                //Seguimos mirando
            }
            c++;
        }
        return !salir;
    }

    public boolean columnaUnique(Casilla[][] matrix, int i, int j, int num){
        boolean salir = false;
        int f = 0;
        boolean[] candidatos;
        while (f < matrix.length && !salir){
            if (matrix[f][j].getValor() == 0 && f != i) {
                candidatos = matrix[f][j].getCandidatos();
                if(candidatos[num - 1]){ //No seguimos buscando
                    salir = true;
                }
                //Seguimos mirando
            }
            f++;
        }
        return !salir;
    }

    public int[] eliminarNums(Casilla[][] matrix){
        int[] numerosCompletos = new int[9];
        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[i].length; j++){
                comprobarNum(matrix[i][j].getValor(), numerosCompletos);
            }
        }
        return numerosCompletos;
    }

    public void comprobarNum(int num, int[] numerosTab){
        for (int i = 1; i < numerosTab.length; i++){
            if (i == num){
                numerosTab[i] = numerosTab[i] + 1;
            }
        }
    }

    public boolean zonaUnique(Casilla[][] matrix, int i, int j, int num){
        int fila = i/3;
        int columna = j/3;
        boolean salir = false;
        boolean[] candidatos;
        int c = fila * 3;
        while(c <(fila*3)+3 && !salir){
            int k = columna * 3;
            while(k <(columna*3)+3 && !salir){
                if (matrix[c][k].getValor() == 0) {
                    candidatos = matrix[c][k].getCandidatos();
                    if(candidatos[num-1]){ //No seguimos buscando
                        salir = true;
                    }
                    //Seguimos buscando
                }
                k++;
            }
            c++;
        }

        return !salir;
    }
    public void restart(){
        miTablero=null;
        Ranking.getMiRanking().setVacio();
        Cronometro.getMiCronometro().restart();
    }
}
