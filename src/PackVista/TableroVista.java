package PackVista;
import packModelo.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.text.NumberFormatter;

public class TableroVista extends JFrame implements Observer {
    public TableroVista(){
        initComponents();
        Tablero.getMiTablero().addObserver(this);
        modificarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(casillaSeleccionada!=null){
                    int [] lista = buscar();
                    if(lista[0] == 1){ //encontrado
                        Tablero.getMiTablero().modificar(lista[1],lista[2],((txValores.getText())),(txCandidatos.getText()));
                    }
                    txValores.setText(null);
                    txCandidatos.setText(null);
                }
            }

        });
        ayudabtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contayuda++;
                ArrayList<Integer> l = Tablero.getMiTablero().ayuda();
                for(int i=0;i<9 ;i++){
                    for(int j=0;j<9;j++){
                     listaPanel[i][j].setBorder(BorderFactory.createLineBorder(Color.black,1));
                    }
                }
                if(l.isEmpty()){
                    ayudas.setText(" No se ha encontrado\n ninguna ayuda valida\n");
                    ayudas.setFont(new Font("Sherif",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/50));
                }
                else{
                    int fila = l.get(0) + 1;
                    int columna = l.get(1) + 1;
                    String tipo;
                    if(l.get(3) == 0){
                        tipo = "Sole";
                        listaPanel[l.get(0)][l.get(1)].setBorder(BorderFactory.createLineBorder(Color.red,3));
                    }
                    else
                        {tipo = "Unique";
                        listaPanel[l.get(0)][l.get(1)].setBorder(BorderFactory.createLineBorder(Color.yellow,3));
                        }
                    ayudas.setText("Tipo: " + tipo + "\n" + "Casilla: " + "(" + fila + "," + columna + ")" + "\n" + "Valor : " + l.get(2) + "\n");
                    ayudas.setFont(new Font("Serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().width/50));
                    casillaSeleccionada=listaPanel[l.get(0)][l.get(1)];
                }
            }
        });
    }
    public void getContayuda(){
        Tablero.getMiTablero().setAyudas(contayuda);
    }
    private void initComponents() {
        getContentPane().setLayout(new BorderLayout(0,0));
        sudoku = getSudoku();
        getContentPane().add(sudoku, BorderLayout.CENTER);
        getContentPane().add(getTextos(),BorderLayout.EAST);
        getContentPane().add(getVacio(),BorderLayout.WEST);
        getContentPane().add(getVacio(),BorderLayout.SOUTH);
        getContentPane().add(getCrono(),BorderLayout.NORTH);
        setExtendedState(MAXIMIZED_BOTH);
        setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/2,Toolkit.getDefaultToolkit().getScreenSize().height/2));
        setLocationRelativeTo(null);
    }

    public JPanel getVacio(){
        JPanel vacio = new JPanel();
        JLabel espacios = new JLabel("             ");
        vacio.setLayout(new FlowLayout());
        vacio.add(espacios);
        return vacio;
    }

    public JPanel getSudoku(){
        if (sudoku == null) {
            sudoku = new JPanel();
            sudoku.setBorder(BorderFactory.createLineBorder(Color.black,2));
            sudoku.setLayout(new GridLayout(3, 3, 0, 0));
        }
        int cont=0;
        int casillaXY=0;
        for (int i = 0; i < 9; i++) {
            if(i<3){cont=0;}
            else if (i<6){cont=3;}
            else{cont=6;}
            JPanel casillaGrande = new JPanel();
            casillaGrande.setLayout(new GridLayout(3,3,0,0));
            casillaGrande.setBorder(BorderFactory.createLineBorder(Color.black,2));

            for(int j = 0; j < 9;j++){
                JPanel casillaActual= new JPanel();
                casillaActual.setLayout(new FlowLayout());

                if(j<3){
                    casillaActual=crearCasilla(cont,3*(i%3)+j);
                    if(casillaXY % 2 == 0){
                        casillaActual.setBackground(new Color(218,215,215));
                    }
                    casillaGrande.add(casillaActual);
                }
                else if(j<6){
                    casillaActual=crearCasilla(cont+1,(3*(i%3)+j-3));
                    if(casillaXY % 2 == 0){
                        casillaActual.setBackground(new Color(218,215,215));
                    }
                    casillaGrande.add(casillaActual);
                }
                else{
                    casillaActual=crearCasilla(cont+2,3*(i%3)+j-6);
                    if(casillaXY % 2 == 0){
                        casillaActual.setBackground(new Color(218,215,215));
                    }
                    casillaGrande.add(casillaActual);
                }
                JPanel finalCasillaActual = casillaActual;
                casillaActual.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        setCasillaSeleccionada(finalCasillaActual);
                    }
                });
            casillaXY++;
            }
            sudoku.add(casillaGrande);
        }
        actualizarCandidatos();
        return sudoku;
    }
    public JPanel crearCasilla(int i,int j){

        casilla = new JPanel();
        casilla.setLayout(new BorderLayout());
        casilla.setBorder(BorderFactory.createLineBorder(Color.black,1));
        casilla.add(new JPanel());
        listaPanel[i][j]=casilla;
        return casilla;
    }

    public void rellenarSudoku(){
        for(int n: completos){
            n=0;
        }
        for(int i=0;i<9 ;i++){
            for(int j=0;j<9;j++){
                Casilla[][] matrizCasilla= Tablero.getMiTablero().getMatrizActual();
                int a=matrizCasilla[i][j].getValor();
                JLabel label;
                if(a!=0) {
                    actualizarCompletos(a,true);
                    listaPanel[i][j].remove(0);
                    label = new JLabel();
                    label.setText("" + a);
                    label.setForeground(Color.RED);
                    label.setFont(new Font("Sherif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().width/30));
                    label.setHorizontalAlignment(JLabel.CENTER);
                    listaPanel[i][j].add(label, BorderLayout.CENTER);
                }
            }
        }
        actualizarCandidatos();

    }

    public JPanel getTextos(){
        textos = new JPanel();
        textos.setLayout(new GridLayout(6,1,0,0));
        textos.add(getCandidatos());
        textos.add(getValores());
        textos.add(getBoton("Modificar"));
        textos.add(getBoton("Ayuda"));
        textos.add(getNumeros());
        textos.add(getAyuda());
        return textos;
    }

    public JPanel getNumeros(){
        numeros = new JPanel();
        numeros.setLayout(new GridLayout(1,9,0,0));
        JLabel lb;
        for(int i = 1; i <= 9; i++){
            lb = new JLabel(i + "");
            lb.setFont(new Font("Sherif",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/60));
            lb.setForeground(Color.BLACK);
            numeros.add(lb);
        }
        return numeros;
    }

    public JTextArea getAyuda(){
        ayudas = new JTextArea();
        ayudas.setLayout(new BorderLayout());
        ayudas.setBorder(BorderFactory.createLineBorder(Color.black));
        ayudas.setEditable(false);
        return ayudas;
    }

    public JPanel getBoton(String info){
        JPanel boton = new JPanel();
        boton.setLayout(new FlowLayout());
        if(info.equals("Modificar")){
            modificarbtn = new JButton("Modificar");
            modificarbtn.setFont(new Font("Sherif",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/70));
            boton.add(modificarbtn);
        }
        else{
            ayudabtn = new JButton("Ayuda");
            ayudabtn.setFont(new Font("Sherif",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/70));
            boton.add(ayudabtn);
        }
        return boton;
    }

    public JPanel getCandidatos(){
        candidatos = new JPanel();
        candidatos.setLayout(new FlowLayout());
        JLabel lb = new JLabel("Candidatos");
        lb.setFont(new Font("Sherif",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/70));
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(1);
        formatter.setMaximum(9);
        formatter.setCommitsOnValidEdit(true);
        txCandidatos = new JFormattedTextField(formatter);
        txCandidatos.setColumns(5);
        txCandidatos.setFont(new Font("Sherif",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/70));
        candidatos.add(lb);
        candidatos.add(txCandidatos);
        return candidatos;
    }

    public JPanel getValores(){
        valores = new JPanel();
        valores.setLayout(new FlowLayout());
        JLabel lb = new JLabel("Valores");
        lb.setFont(new Font("Sherif",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/70));
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(1);
        formatter.setMaximum(9);
        formatter.setCommitsOnValidEdit(true);
        txValores = new JFormattedTextField(formatter);
        txValores.setColumns(5);
        txValores.setFont(new Font("Sherif",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/70));
        valores.add(lb);
        valores.add(txValores);
        return valores;
    }

    private void setCasillaSeleccionada(JPanel p) {
        if (casillaSeleccionada != null) {
            if (p == casillaSeleccionada) {
                casillaSeleccionada.setBorder(BorderFactory.createLineBorder(Color.black));
                casillaSeleccionada = null;
            }
            else {
                casillaSeleccionada.setBorder(BorderFactory.createLineBorder(Color.black));
                casillaSeleccionada = p;
                casillaSeleccionada.setBorder(BorderFactory.createLineBorder(Color.blue,3));
            }
        }
        else {
            casillaSeleccionada = p;
            casillaSeleccionada.setBorder(BorderFactory.createLineBorder(Color.blue,3));
        }
    }

    private JPanel getCrono(){
        JPanel crono = Cronometro.getMiCronometro();
        return crono;
    }

    public int[] buscar(){  //Si no lo encuentra w = 0
        int[] lista = new int[3];
        int i=0;
        int j = 0;
        int w = 0;
        boolean enc =false;
        while (i<9 && !enc) {
            j=0;
            while(j<9 && !enc){
                if(casillaSeleccionada.equals(listaPanel[i][j])){
                    enc=true;
                    w = 1;
                }j++;
            }i++;
        }
        lista[0] = w;
        lista[1] = i -1;
        lista[2] = j -1;
        return lista;
    }

    public JPanel getPanelCandidatos(int i, int j, Color pColor){
        JPanel panel = new JPanel();
        panel.setBackground(pColor);
        panel.setLayout(new GridLayout(3,3,0,0));
        JLabel lb;
        boolean [] lista = Tablero.getMiTablero().getCandidatos(i,j);
        for(int w = 0; w < lista.length; w++){
            if(lista[w]){
                int a = w + 1;
                lb = new JLabel(a +"");
                lb.setFont(new Font("Sherif",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/50));
                lb.setHorizontalAlignment(JLabel.CENTER);
            }
            else{
                lb = new JLabel();
            }
            panel.add(lb);
        }
        return panel;
    }

    public void actualizarCandidatos(){
        for (int i = 0; i < listaPanel.length; i++){
            for(int j = 0; j < listaPanel[i].length; j++){
                if (listaPanel[i][j].getComponent(0) instanceof JPanel && !(listaPanel[i][j].getComponentCount()==0)){
                    Color color = listaPanel[i][j].getBackground();
                    listaPanel[i][j].remove(0);
                    listaPanel[i][j].add(getPanelCandidatos(i, j, color));
            }
        }
    }}

    private void actualizarCompletos(int pN, boolean sumar){
        if (sumar){
            completos[pN-1]++;
        }else {completos[pN-1]--;}
        if(completos[pN-1] > 8){
            numeros.getComponent(pN-1).setVisible(false);
        }else{numeros.getComponent(pN-1).setVisible(true);}

    };
    //Variables
    private int[] completos = new int[9];
    private JPanel[][] listaPanel = new JPanel[9][9];
    private JFormattedTextField txValores;
    private JFormattedTextField txCandidatos;
    private JPanel sudoku;
    private JPanel casilla;
    private JPanel casillaSeleccionada;
    private JPanel textos;
    private JPanel candidatos;
    private JPanel valores;
    private JButton modificarbtn;
    private JButton ayudabtn;
    private JTextArea ayudas;
    private JPanel numeros;
    private int contayuda = 0;

    //OBSERVADOR
    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Tablero){
            if(arg instanceof boolean[]){
                boolean[] a=(boolean[]) arg;
                if(txValores.getText().equals("") || txValores.getText().equals("0")){
                    if(txCandidatos.getText().equals("") || txCandidatos.getText().equals("0")){
                        JPanel act = new JPanel();
                        if(casillaSeleccionada.getComponent(0) instanceof JLabel) {
                            JLabel casilla = (JLabel) casillaSeleccionada.getComponent(0);
                            actualizarCompletos(Integer.parseInt(casilla.getText()), false);
                        }
                        casillaSeleccionada.remove(0);
                        casillaSeleccionada.add(act);
                        actualizarCandidatos();
                    }
                    else{//Se añade el candidato
                        if(casillaSeleccionada.getComponent(0) instanceof JLabel){ //Ya hay un valor
                            System.out.println("No puedes poner candidatos sobre un valor \n");
                        }
                        else {
                            casillaSeleccionada.remove(0);
                            int[] lista = buscar();
                            if (lista[0] == 1) {
                                casillaSeleccionada.add(getPanelCandidatos(lista[1], lista[2], casillaSeleccionada.getBackground()));
                            }
                        }
                    }
                }
                else{ //Se añade el valor
                    casillaSeleccionada.remove(0);
                    JLabel act = new JLabel();
                    act.setFont(new Font("Sherif",Font.PLAIN,Toolkit.getDefaultToolkit().getScreenSize().width/30));
                    act.setHorizontalAlignment(JLabel.CENTER);
                    casillaSeleccionada.add(act);
                    act.setText(txValores.getText());
                    actualizarCompletos(Integer.parseInt(txValores.getText()),true);
                    actualizarCandidatos();
                }
                if(a[0]){
                    TableroVista.this.dispose();
                    this.getContayuda();
                    PackVista.PantallaFinal frame = new PackVista.PantallaFinal();
                    frame.setVisible(true);
                }
            }else{
                rellenarSudoku();
            }
        }
    }
}




