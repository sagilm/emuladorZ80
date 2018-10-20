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
            System.out.println(cadena);
            AnalizarCadena(cadena);
        }
        b.close();
    }

    private  void AnalizarCadena(String cadena) {
        String[] line = cadena.trim().split("\\s");
        // "LD"
        if(line[0].trim().equals("LD")){
            int mempos=0;
            int regpos=0;
            int value=0;
            String[] data= line[1].trim().split(",");
            if(data[0].contains("(")){// LD (algo) el destino es posicion de memoria
                //System.out.println("( encontrado");
                String aux= data[0].substring(1,data[0].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                //System.out.println(mempos);
                if (data[1].length()==2){// el dato es de 16 bits ya que necesita 2 registros LD (mempos) BC
                    System.out.println("mempos :" + mempos);
                    z80.load_from_memory16(mempos, Character.toString(data[1].charAt(0)).toString().trim() ,Character.toString(data[1].charAt(1)).toString().trim());
                }
                else {// dato es de 8 bits LD(mempos) B
                    z80.save_in_memory(data[1],mempos);
                }
            }
            else{// el destino es un registro  LD "[A~L]"
                if(data[1].contains("(")){// segundo dato es una posicion de memoria LD "[A~L]" (mempos)
                    String aux= data[1].substring(1,data[1].indexOf("H"));
                    //System.out.println(aux);
                    mempos= Integer.parseInt(aux,16);
                    String d= data[0].trim();
                    System.out.println("mempos: "+ mempos);
                    z80.load_from_memory(d,mempos);
                }
                Matcher num = numHex.matcher(data[1]);
                if(num.find()){// el segundo dato es un numero LD "[A~L]" xxxH
                    String aux= data[1].substring(0,data[1].indexOf("H"));
                    value= Integer.parseInt(aux,16);
                    z80.reg.LD(data[0],value);
                    System.out.println(value);
                }
            }
        }
        // "ADD"
        if(line[0].trim().equals("ADD")){
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
                System.out.println(data[1]);
                String aux= data[1].substring(1,data[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.sum_mem(mempos);
            }
            else{// es un registro     ADD A,[A~L]
                z80.alu.suma(z80.reg.grupo1,data[1]);
            }

        }
        // "DEC"
        if(line[0].trim().equals("DEC")){
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
            z80.alu.twocomplement(z80.reg.grupo1[0]);
        }
        // ,"CPL",
        if(line[0].trim().equals("CPL")){
            z80.alu.onesComplement(z80.reg.grupo1[0]);
        }

        // "SUB"
        if(line[0].trim().equals("SUB")){
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
                String aux= data[0].substring(1,data[0].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.res_mem(mempos);
            }
            else{// es un registro     ADD A,[A~L]
                z80.alu.resta(z80.reg.grupo1,data[1]);
            }

        }
        // "IN"
        if(line[0].trim().equals("IN")){
            z80.reg.IN();
        }
        // "OUT"
        if(line[0].trim().equals("OUT")){
            z80.reg.OUT();
        }
        // "AND"
        if(line[0].trim().equals("AND")){
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
                String aux= line[1].substring(1,line[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.and_mem(mempos);
            }
            if(m3.find()){
                z80.alu.and(z80.reg.grupo1,line[1]);
            }
        }
        // "OR"
        if(line[0].trim().equals("OR")){
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
                String aux= line[1].substring(1,line[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.or_mem(mempos);
            }
            if(m3.find()){
                z80.alu.or(z80.reg.grupo1,line[1]);
            }
        }
        // "XOR"
        if(line[0].trim().equals("XOR")){
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
                String aux= line[1].substring(1,line[1].indexOf("H"));
                mempos= Integer.parseInt(aux,16);
                z80.xor_mem(mempos);
            }
            if(m3.find()){
                z80.alu.xor(z80.reg.grupo1,line[1]);
            }
        }
        // "JP"
        if(line[0].trim().equals("JP")){}
        // "SET"
        if(line[0].trim().equals("SET")){}
        // "RESET"
        if(line[0].trim().equals("RESET")){}
        // "CP"
        if(line[0].trim().equals("CP")){}
        // "RL"
        if(line[0].trim().equals("RL")){}
        // "RLC"
        if(line[0].trim().equals("RLC")){}
        // "RR"
        if(line[0].trim().equals("RR")){}
        // "RRC"
        if(line[0].trim().equals("RRC")){}
        // "SLA"
        if(line[0].trim().equals("SLA")){}
        // "SRL"
        if(line[0].trim().equals("SLR")){}
        // "PUSH"
        if(line[0].trim().equals("PUSH")){}
        // "POP"
        if(line[0].trim().equals("POP")){}
        // "EXX"
        if(line[0].trim().equals("EXX")){}
        //org
        if(line[0].trim().equals("ORG")){
            System.out.println("ENCONTRE ORG");
        }
        //equ
        if(line[0].trim().equals("EQU")){

        }
        // "HALT"
        if(line[0].trim().equals("HALT")){}
        /*for (int x=0; x<line.length; x++)
            System.out.println(line[x]+ " " +x);

       */
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



