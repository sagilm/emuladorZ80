package componentes;


import java.util.Arrays;
import java.util.Stack;

public class Registros {
    int[] inputA=new int[15];// direcciones A0- A15, intercambian entre memori cpu y perifericos
    int[] inputD=new int[7];// direcciones D0-D7, intercambio perifericos- memoria
    Stack<Integer> stack= new Stack<Integer>();
    int[] direccionadores = new int[2];// 0= IX, 1 = IY
    int[] grupo1= new int[7];//
    int[] grupo2= new int[7];// grupo stack
    int SP=0;

    public  int getAcumulador(){
        return grupo1[0];
    }


    public void LD( String destino, String origen){// transfiere datos entre 2 Registros
        int y = Utilities.getIntG1(destino);
        int z = Utilities.getIntG1(origen);
        grupo1[y]=grupo1[z];
        System.out.println("se movio a : "+ Utilities.getLetraG1(y) +" valor :" +grupo1[z]);
    }
    public void LD2( String destino, String origen){// transfiere datos entre 2 Registros
        int y = Utilities.getIntG1(destino);
        int z = Utilities.getIntG1(origen);
        grupo2[y]=grupo2[z];
        System.out.println("se movio a : "+ Utilities.getLetraG1(y) +" valor :" +grupo2[z]);
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
        System.out.println("se movio a : "+ Utilities.getLetraG1(a1) +" valor :" +grupo1[a1] +" y "+ Utilities.getLetraG1(b1) +" valor :" +grupo1[b1]);
    }
    public int LD16(String a, String b){// a tiene menor preponderancia que b
        int a1 = Utilities.getIntG1(a);
        int b1 = Utilities.getIntG1(b);
        String c =Integer.toString(grupo1[b1])+Integer.toString(grupo1[a1]);
        //System.out.print("c : " + c);
        return Integer.parseInt(c);
    }

    public void LD(String direc, int x){// carga una direccion de memoria
        if (direc == "IX"){
        direccionadores[0]=x;
        }
        else if (direc== "IY"){
            direccionadores[1]=x;
        }
        else{
           int pos= Utilities.getIntG1(direc);
           grupo1[pos]=x;
            System.out.println("se movio : "+ Utilities.getLetraG1(pos) +" valor :" +grupo1[pos]);
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
        System.out.println("ingreso desde D0-D6 :"+ grupo1[0]);
    }
    public void OUT(){// desde el acumulador hacia la memoria
        int bin= Utilities.decimalToBinary(grupo1[0]);
        String x = new StringBuilder((Integer.toString(bin))).reverse().toString();
        System.out.println("salio desde el acumulador: "+ grupo1[0]);
        for(int i =0; i < x.length();i++){
            inputD[i]=Character.getNumericValue(x.charAt(i)) ;
            //System.out.println(D[i]);
        }
    }
    public void showMemory(int mempos){// despliega contenido en A0-15
        for(int pata=0;pata<inputA.length;pata ++){
            inputA[pata]=0;
        }
        //System.out.println("reg.mempos :" + mempos);
        int bin= Utilities.decimalToBinary(mempos);
        //System.out.println("reg.showmemory :" + bin);
        String x = new StringBuilder((Integer.toString(bin))).reverse().toString();
        System.out.println("salio  desde A0-15 :"+ x);
        for(int i =0; i < x.length();i++) {
            inputA[i] = Character.getNumericValue(x.charAt(i));
        }
    }
    public  void EXX(String x){// transfiere al grupo de respaldo A->A'
        int i = Utilities.getIntG1(x);
        grupo2[i]=grupo1[i];
        System.out.println("transferido :"+ x +" al grupo de respaldo");
    }
    public  void EXX2(String x){// transfiere al grupo de respaldo A->A'
        int i = Utilities.getIntG1(x);
        grupo1[i]=grupo2[i];
    }
    public void push(int x){
    SP++;
    stack.push(x);
        System.out.println("se agrego al stack nuevo elemento");
    }
    public int  pop(){
    SP--;
        System.out.println("se elimino del stack un elemento");
    return stack.pop();
    }
    public String getgrupo1(){
        String s= "Registros: "+"A: "+ Integer.toString(grupo1[0]) + "B: "+ grupo1[1]+"C: "+ grupo1[2]+"D: "+ grupo1[3]+"E: "+ grupo1[4]+"H: "+ grupo1[5]+"L: "+ grupo1[6];
        return s;
    }
    public String getstack(){
        String s = "Stack :"+Arrays.toString(stack.toArray())+ " SP: "+ SP;
        return s;
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
        System.out.println(m.getgrupo1());

    }

}
