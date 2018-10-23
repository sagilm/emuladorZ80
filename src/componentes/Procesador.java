package componentes;

import java.io.*;
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
    int inicio=0;
    int fin=0;

    public boolean isMreq() {
        return mreq;
    }

    public void setMreq(boolean mreq) {
        this.mreq = mreq;
    }

    public boolean isReqES() {
        return reqES;
    }

    public void setReqES(boolean reqES) {
        this.reqES = reqES;
    }

    public boolean isRD() {
        return RD;
    }

    public void setRD(boolean RD) {
        this.RD = RD;
    }

    public boolean isWR() {
        return WR;
    }

    public void setWR(boolean WR) {
        this.WR = WR;
    }

    public boolean isRefresh() {
        return Refresh;
    }

    public void setRefresh(boolean refresh) {
        Refresh = refresh;
    }

    public boolean isHalt() {
        return Halt;
    }

    public void setHalt(boolean halt) {
        Halt = halt;
    }

    public boolean isWait() {
        return wait;
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }

    public boolean isINT() {
        return INT;
    }

    public void setINT(boolean INT) {
        this.INT = INT;
    }



    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public int getFin() {
        return fin;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }




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
     Utilities.copyData(dataBus,reg.inputD);
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
        Utilities.copyData(dataBus,reg.inputD);
        reg.IN();
        //System.out.println("reg 0 :"+reg.grupo1[0]);
        mreq= false;
        reqES=false;

    }
    public void load_from_memory16(int mempos, String reg1,String reg2){// trae de la memoria uno de 16bits y lo pone en 2 reg
        reg.showMemory(mempos);
        mreq=true;
        reqES=true;
        //System.out.println("reg.inputA: "+ reg.inputA);
        Utilities.copyData(reg.inputA,addressBus);
        Utilities.copyData(addressBus,mem.memoryinput);

        //System.out.println("mem.writeout: "+ mem.writeOUT16());
        reg.LD(reg1,reg2,mem.writeOUT16());

    }
    public void load_from_disp(){// trae de un E/S
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
    public void load_from_disp(String pos){//trae de un e/S y guarda en un reg
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
    public void save_in_memory(String pos, int mempos){//guarda en memoria en x posicion un registro
        mreq= false;
        reqES= false;
        reg.showMemory(mempos);
        WR=false;
        reg.LD("A",pos);
        reg.OUT();
        Utilities.copyData(reg.inputA,addressBus);
        Utilities.copyData(addressBus,mem.memoryinput);
        Utilities.copyData(reg.inputD,mem.Datainput);
        mem.saveDataInPos();


    }
    public void save_in_memory(int dato,int mempos){// guarda en memoria x numero
        mreq= false;
        reqES= false;
        reg.showMemory(mempos);
        //System.out.println("mempos :" + mempos);
        WR=false;
        load_registrer("A",dato);
        reg.OUT();
        Utilities.copyData(reg.inputA,addressBus);
        Utilities.copyData(addressBus,mem.memoryinput);
        Utilities.copyData(reg.inputD,mem.Datainput);
        mem.saveDataInPos();

    }
    public  void save_in_memory16(String rega,String regb,int mempos){// guarda en memoria algo de 16 bits
        mreq= false;
        reqES= false;
        reg.showMemory(mempos);
        WR=false;
        Utilities.copyData(reg.inputA,addressBus);
        Utilities.copyData(addressBus,mem.memoryinput);
        mem.saveDataInPos(reg.LD16(rega,regb));
    }
    public void load_registrer(String pos, int num){
        reg.LD(pos,num);
    }
    public void sum_mem(int mempos){// carga 2 datos
        // System.out.println("sum_mem accum :"+reg.grupo1[0]);

        int accm=reg.getAcumulador();//lo que esta en el acumulador en este momento
        load_from_memory(mempos);//
        //load_from_memory(mempos2);//
        int accm2=reg.getAcumulador();//lo que esta en el acumulador en este momento
        //System.out.println("sum_mem accum :"+reg.grupo1[0]);
        //System.out.println("accm: "+accm);
        //System.out.println("accm2: "+accm2);
        alu.suma(reg.grupo1,accm);
        //System.out.println("sum_mem accum :"+reg.grupo1[0]);

    }

    public void res_mem(int mempos) {
        //System.out.println("sum_mem accum :"+reg.grupo1[0]);
        int accm=reg.getAcumulador();//lo que esta en el acumulador en este momento
        load_from_memory(mempos);//
        int accm2=reg.getAcumulador();//lo que esta en el acumulador en este momento
        alu.resta(reg.grupo1,accm);
        //System.out.println("sum_mem accum :"+reg.grupo1[0]);
    }
    public  void inc16(String a, String b){ // INC BC C=b menos significativo
        int num = reg.LD16(b,a);
        num=num+1;
        reg.LD(b,a,num);
    }

    public void dec16(String a, String b) {// DEC BC C=b menos significativo
        int num = reg.LD16(b,a);
        if(num>0)num=num-1;
        else num=0;
        reg.LD(b,a,num);
    }
    public void and_mem(int mempos){
        int accm=reg.getAcumulador();//lo que esta en el acumulador en este momento
        load_from_memory(mempos);//
        alu.and(reg.grupo1,accm);
    }
    public void or_mem(int mempos){
        int accm=reg.getAcumulador();//lo que esta en el acumulador en este momento
        load_from_memory(mempos);//
        alu.or(reg.grupo1,accm);
    }
    public void xor_mem(int mempos){
        int accm=reg.getAcumulador();//lo que esta en el acumulador en este momento
        load_from_memory(mempos);//
        alu.xor(reg.grupo1,accm);
    }
    public void shiftleft_mem(int mempos){
        int accm=reg.getAcumulador();
        load_from_memory(mempos);
        alu.shiftleft(reg.grupo1,"A");
    }
    public void shiftright_mem(int mempos){
        load_from_memory(mempos);
        alu.shiftright(reg.grupo1,"A");
    }
    public void SLA_mem(int mempos){
        load_from_memory(mempos);
        alu.SLA(reg.grupo1,"A");
    }
    public void SLR_mem(int mempos){
        load_from_memory(mempos);
        alu.SRL(reg.grupo1,"A");
    }
    public void CP_mem(int mempos){
        int accm=reg.getAcumulador();
        alu.compare(reg.grupo1,accm);

    }
    public void BIT_mem(int mempos , int pos){
        load_from_memory(mempos);
        alu.BIT(reg.grupo1[0],pos);
        save_in_memory("A",mempos);
    }
    public void SET_mem(int mempos, int pos){
        load_from_memory(mempos);
        alu.SET(reg.grupo1,pos);
        save_in_memory("A",mempos);
    }
    public void RESET_mem(int mempos, int pos){
        load_from_memory(mempos);
        alu.RESET(reg.grupo1,pos);
        save_in_memory("A",mempos);
    }
    public static void main (String[]args){
        Procesador z80= new Procesador();
        //z80.load_registrer("A",8);
        z80.save_in_memory(2,200);
        //z80.save_in_memory(3,5);
        //System.out.println("reg.grupo1: ");
        //z80.load_from_memory(200);
        System.out.println("saqui inicia la suma");
        z80.shiftleft_mem(200);


    }


}
