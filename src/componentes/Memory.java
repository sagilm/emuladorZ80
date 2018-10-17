package componentes;

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
    public int readIN(int[] bus) {
        StringBuilder aux= new StringBuilder();
        for(int r: memoryinput){
            aux.append(r);
        }
        String aux2= aux.reverse().toString();
        //System.out.println(aux2);
        memoryPos=Integer.parseInt(aux2,2);
        return memoryPos;
    }

    @Override
    public void writeOUT(int[] bus) {


    }
    @Override
    public void pointTo0(int x){// limpia datos de una posicion
        memory[x]=0;
    }

    public int returnPos(int[] bus){// dada una posicion devuelve el dato que encontro ahi
        readPos(bus);
        int aux=memory[memoryPos];
        return aux;
    }
    @Override
    public void saveData(int x){//guarda un dato que viene desde otro lado

    }
    public void readPos(int[] bus){// lee la posicion de memoria que piden desde registros
        StringBuilder aux= new StringBuilder();
        for(int r: memoryinput){
            aux.append(r);
        }
        String aux2= aux.reverse().toString();
        //System.out.println(aux2);
        memoryPos=Integer.parseInt(aux2,2);

    }
    public static void main (String[]args){
        Memory m = new Memory();

    }
}
