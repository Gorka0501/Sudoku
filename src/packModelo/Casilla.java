package packModelo;

public class Casilla {
    private boolean[] candidatos = new boolean[9]; // Todos los posibles
    private boolean[] delusuario = new boolean[9]; // Los descartados por el usuario
    private boolean modificable;
    private int valor;

    public Casilla(int pValor, boolean pModificable){
        inicializar();
        this.valor = pValor;
        this.modificable = pModificable;
    }

    private void inicializar(){
        for (int i = 0; i < 9; i++){
            candidatos[i] = false;
            delusuario[i] = false;
        }
    }

    public void setValor(int pValor){
        this.valor = pValor;
    }

    public boolean esModificable(){
        return modificable;
    }

    public int getValor(){
        return valor;
    }

    public void actualizarDescartados(int candidato) {
        delusuario[candidato - 1] = true;
        if (candidatos[candidato - 1] == true) {
            candidatos[candidato - 1] = false;
        } else {
            candidatos[candidato - 1] = true;

        }

    }

    public void actualizarCandidatos(int[] pCandidatos) {
        for (int i=0; i<pCandidatos.length; i++) {
            if (delusuario[i] == false) {       //es candidato
                if (pCandidatos[i] != 0) { //no ha sido descartado
                    candidatos[i]=true;
                }else{
                    candidatos[i]=false;
            }

            }
        }
    }

    public boolean[] getCandidatos(){return candidatos;}
}
