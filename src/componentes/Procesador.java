package componentes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Procesador {
    ALU alu = new ALU();
    Registros reg= new Registros();
    Memory mem= new Memory();
    OtroDisp disp= new OtroDisp();

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
    // Registros de proposito general
    int[] addressBus = new int[15];
    int[] dataBus= new int[7];

    String[] comandos = {"NEG","CPL","DEC","INC","SUB","ADD","HALT","IN","OUT","LD","ORG","EQU","JP","INC","SET","RESET","CP","RL","RLC","RR","RRC",
    "SLA","SRL","AND","OR","XOR","PUSH","POP", "EXX"};

    public void load_from_memory(String pos,int x){// necesito la direccion de memoria a cargar
     reg.showMemory(x);// carga en pin A la direccion de memoria
     mreq=true;
     reqES=true;
     Utilities.copyData(reg.inputA,addressBus);
     Utilities.copyData(addressBus,mem.memoryinput);
     mem.writeOUT(mem.memoryinput);
     Utilities.copyData(mem.Datainput,dataBus);
     Utilities.copyData(dataBus,addressBus);
     reg.IN();
     reg.LD(pos,"A");
        mreq= false;
        reqES=false;
    }
    public void load_from_memory(int x){// necesito la direccion de memoria a cargar
        reg.showMemory(x);// carga en pin A la direccion de memoria
        mreq=true;
        reqES=true;
        Utilities.copyData(reg.inputA,addressBus);
        Utilities.copyData(addressBus,mem.memoryinput);
        mem.writeOUT(mem.memoryinput);
        Utilities.copyData(mem.Datainput,dataBus);
        Utilities.copyData(dataBus,addressBus);
        reg.IN();
        mreq= false;
        reqES=false;
    }
    public void load_from_disp(){
        RD=true;
        Halt=true;
        disp.writeOUT(disp.Datainput);
        wait=true;
        Utilities.copyData(disp.Datainput,dataBus);
        INT=true;
        Utilities.copyData(dataBus,reg.inputD);
        reg.IN();
        INT=false;
        wait=false;
        Halt=false;
        RD=false;
    }
    public void load_from_disp(String pos){
        RD=true;
        Halt=true;
        disp.writeOUT(disp.Datainput);
        wait=true;
        Utilities.copyData(disp.Datainput,dataBus);
        INT=true;
        Utilities.copyData(dataBus,reg.inputD);
        reg.IN();
        INT=false;
        wait=false;
        Halt=false;
        reg.LD(pos,"A");
        RD=false;
    }
    public void save_in_memory(String pos, int mempos){
        mreq= true;
        reqES= true;
        reg.showMemory(mempos);
        reg.LD("A",pos);
        reg.OUT();
        Utilities.copyData(reg.inputA,addressBus);
        Utilities.copyData(addressBus,mem.memoryinput);
        Utilities.copyData(reg.inputD,mem.Datainput);
        mem.saveDataInPos();
        mreq= false;
        reqES= false;

    }
    public void save_in_memory(int dato,int mempos){
        mreq= true;
        reqES= true;
        reg.showMemory(mempos);
        load_registrer("A",dato);
        reg.OUT();
        Utilities.copyData(reg.inputA,addressBus);
        Utilities.copyData(addressBus,mem.memoryinput);
        Utilities.copyData(reg.inputD,mem.Datainput);
        mem.saveDataInPos();
        mreq= false;
        reqES= false;
    }
    public void load_registrer(String pos, int num){
        reg.LD(pos,num);
    }
    public void sum(){}

    public static void muestraContenido(String archivo) throws FileNotFoundException, IOException {
        String cadena;
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) {
            System.out.println(cadena);
            AnalizarCadena(cadena);
        }
        b.close();
    }

    private static void AnalizarCadena(String cadena) {
        String[] result = cadena.split("\\s");
        for (int x=0; x<result.length; x++)
            System.out.println(result[x]);
        


    }


    public static void main (String[]args){
        Procesador z80= new Procesador();
        //z80.load_registrer("A",8);
        //z80.save_in_memory(21,1);
        try {
            muestraContenido("/Users/alexander/Documents/emuladorZ80/test");
        } catch (IOException e) {
            e.printStackTrace();
        }
//"NEG"
// ,"CPL",
// "DEC",
// "INC",
// "SUB",
// "ADD",
// "HALT",
// "IN",
// "OUT",
// "LD",
// "ORG",
// "EQU",
// "JP",
// "INC",
// "SET",
// "RESET"
// ,"CP",
// "RL",
// "RLC",
// "RR",
// "RRC",
//
// "SLA",
// "SRL",
// "AND",
// "OR",
// "XOR",
// "PUSH",
// "POP",
// "EXX"
    }

}
