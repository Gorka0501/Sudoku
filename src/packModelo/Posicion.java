package packModelo;

public class Posicion {
    private String usuario;
    private int nivel;
    private int puntuacion;

    public Posicion(String pUsuario, int pNivel, int pPuntuacion){
        usuario = pUsuario;
        nivel = pNivel;
        puntuacion = pPuntuacion;
    }

    public String getUsuario(){
        return usuario;
    }

    public int getNivel(){
        return nivel;
    }

    public int getPuntuacion(){
        return puntuacion;
    }

    public void setPuntuacion(int pPuntuacion){
        puntuacion=pPuntuacion;
    }

    public boolean esTop10(){
        return Ranking.getMiRanking().getRanking().indexOf(this)<10;

    }
    public String[] getDatos(){
        String[] datos=new String[3];
        datos[0]=usuario;
        datos[1]=Integer.toString(nivel);
        datos[2]=Integer.toString(puntuacion);
        return datos;
    }


}
