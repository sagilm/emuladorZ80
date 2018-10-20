package componentes;


public class Registros {
    int[] inputA=new int[15];// direcciones A0- A15, intercambian entre memori cpu y perifericos
    int[] inputD=new int[7];// direcciones D0-D7, intercambio perifericos- memoria

    int[] direccionadores = new int[2];// 0= IX, 1 = IY
    int[] grupo1= new int[7];//
    int[] grupo2= new int[7];// grupo stack


    public  int getAcumulador(){
        return grupo1[0];
    }


    public void LD( String destino, String origen){// transfiere datos entre 2 Registros
        int y = Utilities.getIntG1(destino);
        int z = Utilities.getIntG1(origen);
        grupo1[y]=grupo1[z];
    }
    public void LD(String a, String b, int x){// se divide el numero en los 2 Registros a tiene menor preponderacia que b
        String xp= String.format("%16s",Integer.toString(x)).replace(' ', '0') ;
        //System.out.print("LD int :" + xp+ "\n");
        int a1 = Utilities.getIntG1(a);
        int b1 = Utilities.getIntG1(b);
        grupo1[a1]= Integer.parseInt(xp.substring(xp.length()/2,xp.length()));
        grupo1[b1]= Integer.parseInt(xp.substring(0,xp.length()/2));
        //System.out.print(grupo1[a1]+ "\n");
        //System.out.print(grupo1[b1]+ "\n");


    }
    public int LD16(String a, String b){// a tiene menor preponderancia que b
        int a1 = Utilities.getIntG1(a);
        int b1 = Utilities.getIntG1(b);
        String c =Integer.toString(grupo1[b1])+Integer.toString(grupo1[a1]);
        //System.out.print("c : " + c);
        return Integer.parseInt(c);
    }

    public void LD(String direc, int x){// lee de una direccion de memoria
        if (direc == "IX"){
        direccionadores[0]=x;
        }
        else if (direc== "IY"){
            direccionadores[1]=x;
        }
        else{
           int pos= Utilities.getIntG1(direc);
           grupo1[pos]=x;
        }
    }


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
        int bin= Utilities.decimalToBinary(grupo1[0]);
        String x = new StringBuilder((Integer.toString(bin))).reverse().toString();
        for(int i =0; i < x.length();i++){
            inputD[i]=Character.getNumericValue(x.charAt(i)) ;
            //System.out.println(D[i]);
        }
    }
    public void showMemory(int mempos){
        int bin= Utilities.decimalToBinary(mempos);
        System.out.println("bin :" + bin);
        String x = new StringBuilder((Integer.toString(bin))).reverse().toString();
        for(int i =0; i < x.length();i++) {
            inputA[i] = Character.getNumericValue(x.charAt(i));
        }
    }
    public  void EXX(String x){// transfiere al grupo de respaldo A->A'
        int i = Utilities.getIntG1(x);
        grupo2[i]=grupo1[i];
    }


    public static void main(String[]args){
        Registros m = new Registros();
        int x = 1;
        String y = "IX";
        String z = "C";
        m.inputD[0]= 0;
        m.inputD[1]= 1;
        m.inputD[2]= 1;
        m.inputD[3]= 1;
        m.inputD[4]= 1;
        m.inputD[5]= 1;
        m.inputD[6]= 1;

       // System.out.println(m.direccionadores[1]);


        for (int i =0; i<m.inputD.length;i++) {
            System.out.print(m.inputD[i] + " ");
        }
        System.out.println(" ");
        m.IN();
        System.out.println(m.grupo1[0]);
        m.LD("A","B",2112);
        m.LD16("A","B");

    }

}
