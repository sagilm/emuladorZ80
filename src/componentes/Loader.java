package componentes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.*;

public class Loader {
    Procesador z80 = new Procesador();
    String traduccion ="ABCDEHL";
    Pattern numHex=Pattern.compile("^[A-Z0-9]*H(?!\\()$");
    Pattern memorypos= Pattern.compile("\\([A-Z0-9]*H\\)$");
    Pattern regPos= Pattern.compile("^[ABCDEHL](?<!H)");

    public  void muestraContenido(String archivo) throws FileNotFoundException, IOException {
        String cadena;
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) {
            System.out.println("<<CADENA A ANALIZAR : " + cadena+">>");
            AnalizarCadena(cadena);
        }
        b.close();
    }

    public void getEstado(){

        System.out.println("<<<<ESTADO DEL DISPOSITIVO>>>>");
        System.out.println(z80.reg.getgrupo1());
        System.out.println(z80.reg.getstack());
        System.out.println("Banderas dispositivo: " );
        System.out.println("halt:"+z80.isHalt()+" INT:"+z80.isINT()+" Mreq:"+z80.isMreq()+" RD:"+z80.isRD());
        System.out.println("Refresh:"+z80.isRefresh()+" ReqES:"+z80.isReqES()+" Wait:"+z80.isWait()+" WR:"+z80.isWR());
        System.out.println("Banderas alu: ");
        System.out.println(" Carry:"+z80.alu.isCarry()+" Paridad:"+z80.alu.isParidad()+" Sign:"+z80.alu.isSign()+" Z:"+z80.alu.isZ()+" Zero:"+z80.alu.isZero());
        System.out.println("<<<<<<<<<<<<>>>>>>>>>>>>");
    }
    public void setori(){
       z80.setHalt(true);
        z80.setINT(true);
        z80.setMreq(true);
        z80.setRD(true);
        z80.setRefresh(true);
        z80.setReqES(true);
        z80.setWait(true);
        z80.setWR(true);

    }
    private  void AnalizarCadena(String cadena) {
        String[] line = cadena.trim().split("\\s");
        // "LD"
        if(line[0].trim().equals("LD")){
            System.out.println("comando encontrado LD");
            int mempos=0;
            int value=0;
            String[] data= line[1].trim().split(",");
            if(data[0].contains("(")){// LD (algo) el destino es posicion de memoria
                System.out.println(" posicion de memoria encontrada");
                String aux= data[0].substring(1,data[0].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                //System.out.println(mempos);
                if (data[1].length()==2){// el dato es de 16 bits ya que necesita 2 registros LD (mempos) BC
                    System.out.println("operacion de 16 bits");
                    z80.load_from_memory16(mempos, Character.toString(data[1].charAt(0)).toString().trim() ,Character.toString(data[1].charAt(1)).toString().trim());
                }
                else {// dato es de 8 bits LD(mempos) B
                    System.out.println("registro encontrado");
                    z80.save_in_memory(data[1],mempos);
                }
            }
            else{// el destino es un registro  LD "[A~L]"
                if(data[1].contains("(")){// segundo dato es una posicion de memoria LD "[A~L]" (mempos)
                    System.out.println("posicion de memoria encontrada");
                    String aux= data[1].substring(1,data[1].indexOf("H"));
                    //System.out.println(aux);
                    mempos= Integer.parseInt(aux,16);
                    String d= data[0].trim();
                    //System.out.println("mempos: "+ mempos);
                    z80.load_from_memory(d,mempos);
                }
                Matcher num = numHex.matcher(data[1]);
                if(num.find()){// el segundo dato es un numero LD "[A~L]" xxxH
                    System.out.println("numero encontrado");
                    String aux= data[1].substring(0,data[1].indexOf("H"));
                    value= Integer.parseInt(aux,16);
                    z80.reg.LD(data[0],value);
                    //System.out.println(value);
                }
                Matcher m1 = regPos.matcher(data[1]);// segundo dato es un registro
                System.out.println("registro encontrado");
                if(num.find()){
                    z80.reg.LD(data[0],data[1]);
                }
            }
        }
        // "ADD"
        if(line[0].trim().equals("ADD")){
            System.out.println("comando encontrado ADD");
            int mempos=0;
            int value=0;
            String[] data= line[1].trim().split(",");
            Matcher m1= numHex.matcher(data[1]);
            Matcher m2 = memorypos.matcher(data[1]);
            if(m1.find()){//es un numero         ADD A,xxH
                String aux= data[0].substring(0,data[0].indexOf("H"));
                value= Integer.parseInt(aux,16);
                z80.alu.suma(z80.reg.grupo1,value);
            }
            if(m2.find()){// es una posicion de memoria     ADD A,(xxH)
                System.out.println("posicion de memoria encontrada");
                //System.out.println(data[1]);
                String aux= data[1].substring(1,data[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.sum_mem(mempos);
            }
            else{// es un registro     ADD A,[A~L]
                System.out.println("registro encontrado");
                z80.alu.suma(z80.reg.grupo1,data[1]);
            }

        }
        // "DEC"
        if(line[0].trim().equals("DEC")){
            System.out.println("comando encontrado DEC");
            if(line[1].length()==1){// es un numero de 8 bits
                z80.alu.decremento(z80.reg.grupo1,line[1]);
            }
            if(line[1].length()==2){// es un numero de 16bits
                System.out.println(Character.toString(line[1].charAt(1)).toString().trim());
                z80.dec16( Character.toString(line[1].charAt(1)).toString().trim(), Character.toString(line[1].charAt(0)).toString().trim());
            }
        }
        // "INC"
        if(line[0].trim().equals("INC")){
            if(line[1].length()==1){// es un numero de 8 bits
                z80.alu.incremento(z80.reg.grupo1,line[1]);
            }
            if(line[1].length()==2){// es un numero de 16bits
                z80.inc16( Character.toString(line[1].charAt(1)).toString().trim(), Character.toString(line[1].charAt(0)).toString().trim());
            }
        }
        //"NEG"
        if(line[0].trim().equals("NEG")){
            System.out.println("comando encontrado NEG");
            System.out.println("registro encontrado");
            z80.alu.twocomplement(z80.reg.grupo1[0]);
        }
        // ,"CPL",
        if(line[0].trim().equals("CPL")){
            System.out.println("comando encontrado CPL");
            System.out.println("registro encontrado");
            z80.alu.onesComplement(z80.reg.grupo1[0]);
        }

        // "SUB"
        if(line[0].trim().equals("SUB")){
            System.out.println("comando encontrado SUB");
            int mempos=0;
            int value=0;
            String[] data= line[1].trim().split(",");
            Matcher m1= numHex.matcher(data[1]);
            Matcher m2 = memorypos.matcher(data[1]);
            if(m1.find()){//es un numero         ADD A,xxH
                String aux= data[0].substring(0,data[0].indexOf("H"));
                value= Integer.parseInt(aux,16);
                z80.alu.resta(z80.reg.grupo1,value);
            }
            if(m2.find()){// es una posicion de memoria     ADD A,(xxH)
                System.out.println("posicion de memoria encontrada");
                String aux= data[0].substring(1,data[0].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.res_mem(mempos);
            }
            else{// es un registro     ADD A,[A~L]
                System.out.println("registro encontrado");
                z80.alu.resta(z80.reg.grupo1,data[1]);
            }

        }
        // "IN"
        if(line[0].trim().equals("IN")){
            System.out.println("comando encontrado IN");
            z80.reg.IN();
        }
        // "OUT"
        if(line[0].trim().equals("OUT")){
            System.out.println("comando encontrado OUT");
            z80.reg.OUT();
        }
        // "AND"
        if(line[0].trim().equals("AND")){
            System.out.println("comando encontrado AND");
            int mempos=0;
            int value=0;
            Matcher m1= numHex.matcher(line[1].trim());
            Matcher m2= memorypos.matcher(line[1].trim());
            Matcher m3= regPos.matcher((line[1].trim()));
            if(m1.find()){// es un numero
                String aux= line[1].substring(0,line[1].indexOf("H"));
                value= Integer.parseInt(aux,16);
                z80.alu.and(z80.reg.grupo1,value);
            }
            if(m2.find()){// es posicion de memoria
                System.out.println("posicion de memoria encontrada");
                String aux= line[1].substring(1,line[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.and_mem(mempos);
            }
            if(m3.find()){
                System.out.println("registro encontrado");
                z80.alu.and(z80.reg.grupo1,line[1]);
            }
        }
        // "OR"
        if(line[0].trim().equals("OR")){
            System.out.println("comando encontrado OR");
            int mempos=0;
            int value=0;
            Matcher m1= numHex.matcher(line[1].trim());
            Matcher m2= memorypos.matcher(line[1].trim());
            Matcher m3= regPos.matcher((line[1].trim()));
            if(m1.find()){// es un numero
                String aux= line[1].substring(0,line[1].indexOf("H"));
                value= Integer.parseInt(aux,16);
                z80.alu.or(z80.reg.grupo1,value);
            }
            if(m2.find()){// es posicion de memoria
                System.out.println("posicion de memoria encontrada");
                String aux= line[1].substring(1,line[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.or_mem(mempos);
            }
            if(m3.find()){
                System.out.println("registro encontrado");
                z80.alu.or(z80.reg.grupo1,line[1]);
            }
        }
        // "XOR"
        if(line[0].trim().equals("XOR")){
            System.out.println("comando encontrado XOR");
            int mempos=0;
            int value=0;
            Matcher m1= numHex.matcher(line[1].trim());
            Matcher m2= memorypos.matcher(line[1].trim());
            Matcher m3= regPos.matcher((line[1].trim()));
            if(m1.find()){// es un numero
                String aux= line[1].substring(0,line[1].indexOf("H"));
                value= Integer.parseInt(aux,16);
                z80.alu.xor(z80.reg.grupo1,value);
            }
            if(m2.find()){// es posicion de memoria
                System.out.println("posicion de memoria encontrada");
                String aux= line[1].substring(1,line[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.xor_mem(mempos);
            }
            if(m3.find()){
                System.out.println("registro encontrado");
                z80.alu.xor(z80.reg.grupo1,line[1]);
            }
        }
        // "RL"
        if(line[0].trim().equals("RL")){
            System.out.println("comando encontrado RL");
            int mempos=0;
            Matcher m2= memorypos.matcher(line[1].trim());
            Matcher m3= regPos.matcher((line[1].trim()));
            if(m2.find()){// es una posicion de memoria
                System.out.println("posicion de memoria encontrada");
                String aux= line[1].substring(1,line[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.shiftleft_mem(mempos);
            }
            if(m3.find()){
                System.out.println("registro encontrado");
                z80.alu.shiftleft(z80.reg.grupo1,line[1]);
            }
        }
        // "RLC"
        if(line[0].trim().equals("RLC")){
            System.out.println("comando encontrado RLC");
            int mempos=0;
            Matcher m2= memorypos.matcher(line[1].trim());
            Matcher m3= regPos.matcher((line[1].trim()));
            if(m2.find()){// es una posicion de memoria
                System.out.println("posicion de memoria encontrada");
                String aux= line[1].substring(1,line[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.shiftleft_mem(mempos);
            }
            if(m3.find()){
                System.out.println("registro encontrado");
                z80.alu.shiftleft(z80.reg.grupo1,line[1]);
            }
        }
        // "RR"
        if(line[0].trim().equals("RR")){
            System.out.println("comando encontrado RR");
            int mempos=0;
            Matcher m2= memorypos.matcher(line[1].trim());
            Matcher m3= regPos.matcher((line[1].trim()));
            if(m2.find()){// es una posicion de memoria
                System.out.println("posicion de memoria encontrada");
                String aux= line[1].substring(1,line[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.shiftright_mem(mempos);
            }
            if(m3.find()){
                System.out.println("registro encontrado");
                z80.alu.shiftright(z80.reg.grupo1,line[1]);
            }
        }
        // "RRC"
        if(line[0].trim().equals("RRC")){
            System.out.println("comando encontrado RRC");
            int mempos=0;
            Matcher m2= memorypos.matcher(line[1].trim());
            Matcher m3= regPos.matcher((line[1].trim()));
            if(m2.find()){// es una posicion de memoria
                System.out.println("posicion de memoria encontrada");
                String aux= line[1].substring(1,line[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.shiftright_mem(mempos);
            }
            if(m3.find()){
                System.out.println("registro encontrado");
                z80.alu.shiftright(z80.reg.grupo1,line[1]);
            }
        }
        // "SLA"
        if(line[0].trim().equals("SLA")){
            System.out.println("comando encontrado SLA");
            int mempos=0;
            Matcher m2= memorypos.matcher(line[1].trim());
            Matcher m3= regPos.matcher((line[1].trim()));
            if(m2.find()){// es una posicion de memoria
                System.out.println("posicion de memoria encontrada");
                String aux= line[1].substring(1,line[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.SLA_mem(mempos);
            }
            if(m3.find()){
                System.out.println("registro encontrado");
                z80.alu.SLA(z80.reg.grupo1,line[1]);
            }
        }
        // "SRL"
        if(line[0].trim().equals("SLR")){
            System.out.println("comando encontrado SLR");
            int mempos=0;
            Matcher m2= memorypos.matcher(line[1].trim());
            Matcher m3= regPos.matcher((line[1].trim()));
            if(m2.find()){// es una posicion de memoria
                System.out.println("posicion de memoria encontrada");
                String aux= line[1].substring(1,line[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.SLR_mem(mempos);
            }
            if(m3.find()){
                System.out.println("registro encontrado");
                z80.alu.SRL(z80.reg.grupo1,line[1]);
            }
        }
        // "SET"
        if(line[0].trim().equals("SET")){
            System.out.println("comando encontrado SET");
            int mempos=0;
            String[] data= line[1].trim().split(",");
            Matcher m2= memorypos.matcher(data[1].trim());
            Matcher m3= regPos.matcher((data[1].trim()));
            if(m2.find()){// mempos
                System.out.println("posicion de memoria encontrada");
                String aux= data[1].substring(1,data[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.SET_mem(mempos,Integer.parseInt(data[0]));
            }
            if(m3.find()){// registro
                System.out.println("registro encontrado");
                z80.alu.SET(z80.reg.grupo1,data[1],Integer.parseInt(data[0]));
            }

        }
        // "RESET"
        if(line[0].trim().equals("RESET")){
            System.out.println("comando encontrado RESET");
            int mempos=0;
            String[] data= line[1].trim().split(",");
            Matcher m2= memorypos.matcher(data[1].trim());
            Matcher m3= regPos.matcher((data[1].trim()));
            if(m2.find()){// mempos
                System.out.println("posicion de memoria encontrada");
                String aux= data[1].substring(1,data[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.RESET_mem(mempos,Integer.parseInt(data[0]));
            }
            if(m3.find()){// registro
                System.out.println("registro encontrado");
                z80.alu.RESET(z80.reg.grupo1,data[1],Integer.parseInt(data[0]));
            }
        }
        // "CP"
        if(line[0].trim().equals("CP")){
            System.out.println("comando encontrado CP");
            Matcher m1= numHex.matcher(line[1].trim());
            Matcher m2= memorypos.matcher(line[1].trim());
            Matcher m3= regPos.matcher((line[1].trim()));
            int mempos=0;
            int value=0;
            if(m1.find()){//numero
                String aux= line[1].substring(0,line[1].indexOf("H"));
                value= Integer.parseInt(aux,16);
                z80.alu.compare(z80.reg.grupo1,value);
            }
            if(m2.find()){// mempos
                System.out.println("posicion de memoria encontrada");
                String aux= line[1].substring(1,line[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.CP_mem(mempos);
            }
            if(m3.find()){// registro
                System.out.println("registro encontrado");
                z80.alu.compare(z80.reg.grupo1,line[1]);
            }

        }
        // "BIT"
        if(line[0].trim().equals("BIT")){
            System.out.println("comando encontrado BIT");
            int mempos=0;
            String[] data= line[1].trim().split(",");
            Matcher m2= memorypos.matcher(data[1].trim());
            Matcher m3= regPos.matcher((data[1].trim()));
            if(m2.find()){// mempos
                System.out.println("posicion de memoria encontrada");
                String aux= data[1].substring(1,data[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.BIT_mem(mempos,Integer.parseInt(data[0]));
            }
            if(m3.find()){// registro
                System.out.println("registro encontrado");
                z80.alu.BIT(z80.reg.grupo1,data[1],Integer.parseInt(data[0]));
            }
        }


        // "PUSH"
        if(line[0].trim().equals("PUSH")){
            System.out.println("comando encontrado PUSH");
            int id = Utilities.getIntG1(line[1]);
            z80.reg.push(z80.reg.grupo1[id]);
        }
        // "POP"
        if(line[0].trim().equals("POP")){
            System.out.println("comando encontrado POP");
            z80.reg.pop();
        }
        // "EXX"
        if(line[0].trim().equals("EXX")){
            System.out.println("comando encontrado EXX");
            System.out.println("registro encontrado");
            z80.reg.EXX(line[1]);
        }
        //org
        if(line[0].trim().equals("ORG")){
            System.out.println("comando encontrado ORG");
            String aux= line[1].substring(1,line[1].indexOf("H"));
            int mempos= Integer.parseInt(aux,16);
            z80.load_from_memory(mempos);
        }
        //equ
        if(line[0].trim().equals("EQU")){
            System.out.println("comando encontrado EQU");
            String aux= line[1].substring(1,line[1].indexOf("H"));
            int mempos= Integer.parseInt(aux,16);
            z80.save_in_memory("A", mempos);
        }
        // "HALT"
        if(line[0].trim().equals("HALT")){
            System.out.println("comando encontrado HALT, inica detencion del programa");
        }
        /*for (int x=0; x<line.length; x++)
            System.out.println(line[x]+ " " +x);

       */
        getEstado();
        setori();
        }

    public static void main (String[]args){
        Loader readin= new Loader();
        try {
            readin.muestraContenido("/Users/alexander/Documents/emuladorZ80/test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }



