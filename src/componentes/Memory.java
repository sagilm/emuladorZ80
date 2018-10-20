package componentes;

import java.util.Arrays;

public class Memory implements Dispositivo {
    int[] Datainput= new int[7];
    int[] memoryinput= new int [15];
    int[] memory = new int [500];
    int memoryPos=0;
    public Memory(){
    for(int i=0; i<this.memory.length;i++){
        this.memory[i]=0;
    }
    this.memoryPos=0;
    }

    @Override
    public void pointTo0(int x){// limpia datos de una posicion
        memory[x]=0;
    }

    @Override
    public int readIN(int[] bus) {// lee un nuevo dato a guardar
        StringBuilder aux= new StringBuilder();
        for(int r: Datainput){
            aux.append(r);
        }
        String aux2= aux.reverse().toString();
        //System.out.println(aux2);
        memoryPos=Integer.parseInt(aux2,2);
        return memoryPos;
    }
    public void readPos(int[] bus){// lee la posicion de memoria que piden desde registros
        StringBuilder aux= new StringBuilder();
        for(int r: memoryinput){
            aux.append(r);
        }
        String aux2= aux.reverse().toString();
        System.out.println("se trae de memoria :"+ aux2);
        memoryPos=Integer.parseInt(aux2,2);

    }
    public int returnPos(int[] bus){// dada una posicion devuelve el dato que encontro ahi
        readPos(bus);
        int aux=memory[memoryPos];

        return aux;
    }

    @Override
    public void saveData(int x){//guarda un dato que viene desde otro lado
        int data= memoryPos;
        int i =0;
        while (memory[i]!=0){
            i++;
        }
        memory[i]=data;
        System.out.println("guardado en " + i);
    }
    public void saveDataInPos(){//guarda un dato que viene desde otro lado en una posicion de memoria especifica
        int data= readIN(Datainput);
        System.out.println("datoleida :" + data);
        readPos(memoryinput);
        int pos= memoryPos;
        memory[pos]=data;
        System.out.println("guardado en " + pos);
    }
    public void saveDataInPos(int x){
        readPos(memoryinput);
        int pos = memoryPos;
        memory[pos]=x;
    }

    @Override
    public void writeOUT(int[] bus) {//lee de la direccion y pone en el pueto lo que el dato de esa pos de memoria
        int pos= returnPos(bus);
        System.out.println("mem.memoryinput:"+ returnPos(bus));
        int bin= Utilities.decimalToBinary(pos);
        //System.out.println("writeout :" + bin);
        String x = new StringBuilder((Integer.toString(bin))).reverse().toString();
        for(int i =0; i < x.length();i++){
            Datainput[i]=Character.getNumericValue(x.charAt(i)) ;

        }
        //System.out.println(Arrays.toString(Datainput));
    }

    public int writeOUT16(){
        //System.out.println("memoryinput:"+ returnPos(memoryinput));
        return returnPos(memoryinput);
    }



    public static void main (String[]args){
        Memory m = new Memory();

        m.memoryinput[0]=1;
        m.memoryinput[1]=0;
        m.memoryinput[2]=0;
        m.memoryinput[3]=0;
        m.memoryinput[4]=0;
        m.memoryinput[5]=0;
        m.memoryinput[6]=0;
        m.memoryinput[7]=0;
        m.memoryinput[8]=0;
        m.Datainput[0]=1;
        m.Datainput[1]=1;
        m.Datainput[2]=1;
        m.Datainput[3]=0;
        m.Datainput[4]=0;
        m.Datainput[5]=0;
        m.Datainput[6]=0;
        int c =m.readIN(m.Datainput);
        m.readPos(m.memoryinput);
        System.out.println(c);
        //m.saveData(c);
        m.saveDataInPos();
        m.writeOUT(m.memoryinput);
    }
}
