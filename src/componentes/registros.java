package componentes;


public class registros {
    int[] inputA=new int[15];// direcciones A0- A15, intercambian entre memori cpu y perifericos
    int[] inputD=new int[7];// direcciones D0-D7, intercambio perifericos- memoria
    boolean mreq= true;// activa en bajo,vse enciende cuando hay una peticion que relaciona memoria cpu
    boolean reqES= true; // activa en bajo indica que A0-A7 van a tener una direccion valida de lectura escritura
    boolean RD= true;// activa en bajo indica que la cpu desea leer datos de un disp externo E/S
    boolean WR = true;// activa en bajo, indica que se va a escribir un dato en memoria
    boolean Refresh=true;// activa en bajo, indica que A0-A7 contienen una direccion valida de refresco de memoria
    boolean Halt = true;// se realizo parada por software a espera de NMI o INT para continuar, se ejecutan operaciones NOP mientras
    boolean wait = true; // activa en falso, detiene el proceso para esperar sincronia con los E/S
    boolean INT = true; // si el enable de flip flop IFF! esta habilitado, reconoce la peticion del E/S
    boolean nmi = true; // posiciona al contador de programa (PC) en la dirección 0066h desde donde continua el proceso
    boolean reset= true; // obliga a la CPU a reiniciar  actividad, coloca al contador de programa (PC) en la localidad de inicio de memoria 0000h, desde donde empieza el proceso
    // registros de proposito general
    int[] direccionadores = new int[2];// 0= IX, 1 = IY
    int[] grupo1= new int[7];//
    int[] grupo2= new int[7];// grupo stack


    public  int getAcumulador(){
        return grupo1[0];
    }

    public void LD( String a, String b){// transfiere datos entre 2 registros
        int y = utilities.getIntG1(a);
        int z = utilities.getIntG1(b);
        grupo1[y]=grupo1[z];
    }
    public void LD(String a, String b, int x){// se divide el numero en los 2 registros a tiene menor preponderacia que b
        String xp = Integer.toString(x);
        System.out.print(xp+ "\n");
        int y = utilities.getIntG1(a);
        int z = utilities.getIntG1(b);
        grupo1[y]= Integer.parseInt(xp.substring(xp.length()/2,xp.length()),16);
        grupo1[z]= Integer.parseInt(xp.substring(0,xp.length()/2),16);
        //System.out.print(aux+ "\n");


    }
    public void LD(String direc, int x){// lee de una direccion de memoria
        if (direc == "IX"){
        direccionadores[0]=Integer.parseInt(Integer.toHexString(x));
        }
        else if (direc== "IY"){
            direccionadores[1]=Integer.parseInt(Integer.toHexString(x));
        }
    }
    public void LD_mem(String a,int x){}// transfiere datos de registros a memoria
    public void mem_LD(String a,int x){}// transfiere datos de memoria a registros

    public void IN(){// parar el dato del bus de entrada a en acumulador
        StringBuilder aux= new StringBuilder();
        for(int r: inputD){
            aux.append(r);
        }
        String aux2= aux.reverse().toString();
        //System.out.println(aux2);
        grupo1[0]=Integer.parseInt(aux2,2);
    }
    public void OUT(){// desde el acumulador hacia la memoria
        int bin=utilities.decimalToBinary(grupo1[0]);
        String x = new StringBuilder((Integer.toString(bin))).reverse().toString();
        for(int i =0; i < x.length();i++){
            inputD[i]=Character.getNumericValue(x.charAt(i)) ;
            //System.out.println(D[i]);
        }
    }
    public  void EXX(String x){// transfiere al grupo de respaldo A->A'
        int i = utilities.getIntG1(x);
        grupo1[i]=grupo2[i];
    }


    public static void main(String[]args){
        registros m = new registros();
        int x = 1;
        String y = "IX";
        String z = "C";
        m.inputA[0]= 0;
        m.inputA[1]= 1;
        m.inputA[2]= 1;
        m.inputA[3]= 1;
        m.inputA[4]= 1;
        m.inputA[5]= 1;
        m.inputA[6]= 1;
        m.inputA[7]= 1;
       // System.out.println(m.direccionadores[1]);


        for (int i =0; i<m.inputA.length;i++) {
            System.out.print(m.inputA[i] + " ");
        }
        System.out.println(" ");
        m.IN();
        System.out.println(m.grupo1[0]);

    }

}
