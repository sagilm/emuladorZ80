package componentes;

public class ALU {
    boolean paridad;// true cuando el complemento a 2 produce un carry
    boolean sign;// true cuando el bil mas significativo == 1

    public boolean isParidad() {
        return paridad;
    }

    public boolean isSign() {
        return sign;
    }

    public boolean isZero() {
        return zero;
    }

    public boolean isCarry() {
        return carry;
    }

    public boolean isZ() {
        return Z;
    }

    boolean zero;// cuando el resultadod e la operacion es 0
    boolean carry;// carry en suma o resta
    boolean Z;// resultado verificaicon bit
    String[] traduccion ={"A","B","C","D","E","H","L"};
    public ALU() {
       paridad= false;
       sign= false;
       zero= false;
       carry= false;

    }
    private String[] transform(int x){

        String y = new StringBuilder((Integer.toString(x))).toString();
        String[] out= new String[y.length()];
        for(int i =0; i < y.length();i++){
           // out[i]=y.charAt(i);
        }
        return out;
    }

    //suma binaria
    // cualquier registro ya esta como int normal

    public void suma(int[] registros, int x){// opera lo que hay en el acumulador y un numero
        int a1= registros[0];
       // System.out.println("alu.a1: "+ registros[0]);
        int sum= registros[0]+x;
        //System.out.println("alu.x: "+ x);
        registros[0]=sum;
        if(registros[0]>255)carry=true;
        if(registros[0]==0)zero=true;
        System.out.println("suma: "+ registros[0]);


    }
    public void suma (int[] registros,String x){// opera lo que hay en el acumulador y un registro / memoria
        boolean register= Utilities.useLoop(traduccion,x);
        if(register){// es un registro
            int pos= Utilities.getIntG1(x);
            //System.out.println("alu.suma.pos: "+ pos);
            int y = registros[pos];
           // System.out.println("alu.suma.y: "+ registros[pos]);
            suma(registros,y);
        }

    }
    public void resta(int[] registros, int x){// opera lo que hay en el acumulador y un numero
        int a1= registros[0];
        registros[0]=a1-x;
        if(registros[0]>255)carry=true;
        if(registros[0]==0)zero=true;
        System.out.println("resta: "+ registros[0]);

    }
    public void resta (int[] registros,String x){// opera lo que hay en el acumulador y un registro / memoria
        boolean register= Utilities.useLoop(traduccion,x);
        if(register){// es un registro
            int pos= Utilities.getIntG1(x);
            int y = registros[pos];
            resta(registros,y);
        }


    }

    public void decremento(int[] registros,String pos){
        int id = Utilities.getIntG1(pos);
        if(registros[id]>0)registros[id]--;
        else registros[id]=0;
        System.out.println("valor tras DEC: "+ registros[id]);
    }
    public void  incremento(int[] registros,String pos){
        int id = Utilities.getIntG1(pos);
        registros[id]++;
        System.out.println("valor tras INC: "+ registros[id]);
    }
    public int onesComplement(int n)
    {
        // Find number of bits in the
        // given integer
        int number_of_bits =
                (int)(Math.floor(Math.log(n) /
                        Math.log(2))) + 1;

        // XOR the given integer with poe(2,
        // number_of_bits-1 and print the result
        return ((1 << number_of_bits) - 1) ^ n;
    }
    public int twocomplement(int n){
        return (int) (Math.pow(2,8) - n);
    }
    public void and(int[]registros,int x){
        registros[0]= registros[0]&x;
        System.out.println("valor tras AND: "+ registros[0]);
    }
    public void and(int[] registros, String x){
        boolean register= Utilities.useLoop(traduccion,x);
        if(register) {// es un registro
            int pos= Utilities.getIntG1(x);
            int y = registros[pos];
            and(registros,y);
        }
    }

    public void or(int[]registros,int x){
        registros[0]= registros[0]|x;
        System.out.println("valor tras OR: "+ registros[0]);
    }
    public void or(int[] registros, String x){
        boolean register= Utilities.useLoop(traduccion,x);
        if(register) {// es un registro
            int pos= Utilities.getIntG1(x);
            int y = registros[pos];
            or(registros,y);
        }
    }
    public void xor(int[]registros,int x){
        registros[0]= registros[0]^x;
        System.out.println("valor tras XOR: "+ registros[0]);
    }
    public void xor(int[] registros, String x){
        boolean register= Utilities.useLoop(traduccion,x);
        if(register) {// es un registro
            int pos= Utilities.getIntG1(x);
            int y = registros[pos];
            xor(registros,y);
        }
    }
    public void shiftleft(int[] registros, int x){
        String aux= String.format("%8s",Integer.toBinaryString(x)).replace(' ', '0') ;
        char c = aux.charAt(0);
        //System.out.println(aux);
        String rotado= aux.substring(1)+c;
        System.out.println(rotado);
        if(Integer.parseInt(String.valueOf(c))==1) carry=true;
        else carry=false;
        System.out.println("valor tras shift: "+ rotado);
        registros[0]=Integer.parseInt(rotado.toString(),2) ;
        //System.out.println(registros[0]);
    }
    public void shiftleft(int[] registros, String pos){
        int id = Utilities.getIntG1(pos);
        String aux= String.format("%8s",Integer.toBinaryString(registros[id])).replace(' ', '0') ;
        char c = aux.charAt(0);
        //System.out.println(aux);
        String rotado= aux.substring(1)+c;
        System.out.println("valor tras shift: "+ rotado);
        System.out.println(rotado);
        if(Integer.parseInt(String.valueOf(c))==1) carry=true;
        else carry=false;
        registros[id]=Integer.parseInt(rotado.toString(),2) ;
       // System.out.println(registros[id]);
    }
    public void shiftright(int[] registros ,int value){
        String aux = String.format("%8s",Integer.toBinaryString(value)).replace(' ', '0') ;
        char c= aux.charAt(aux.length()-1);
       // System.out.println(c);
        //System.out.println(aux);
        String rotado= c+aux.substring(0,aux.length()-1);
        System.out.println("valor tras shift: "+ rotado);
        if(Integer.parseInt(String.valueOf(c))==1) carry=true;
        else carry=false;
        registros[0]=Integer.parseInt(rotado.toString(),2) ;
        //System.out.println(registros[0]);
    }
    public void shiftright(int[] registros ,String pos){
        int id = Utilities.getIntG1(pos);
        String aux = String.format("%8s",Integer.toBinaryString(registros[id])).replace(' ', '0') ;
        char c= aux.charAt(aux.length()-1);
        //System.out.println(c);
        //System.out.println(aux);
        String rotado= c+aux.substring(0,aux.length()-1);
        System.out.println("valor tras shift: "+ rotado);
        if(Integer.parseInt(String.valueOf(c))==1) carry=true;
        else carry=false;
        registros[id]=Integer.parseInt(rotado.toString(),2) ;
        //System.out.println(registros[id]);
    }
    public void SLA(int[] registros, int value){
        String aux= String.format("%8s",Integer.toBinaryString(value)).replace(' ', '0') ;
        char c = aux.charAt(0);
        //System.out.println(aux);
        String rotado= aux.substring(1)+'0';
        System.out.println("valor tras shift: "+ rotado);
        if(Integer.parseInt(String.valueOf(c))==1) carry=true;
        else carry=false;
        registros[0]=Integer.parseInt(rotado.toString(),2) ;
        //System.out.println(registros[0]);
    }
    public void SLA(int[] registros, String pos){
        int id = Utilities.getIntG1(pos);
        String aux= String.format("%8s",Integer.toBinaryString(registros[id])).replace(' ', '0') ;
        char c = aux.charAt(0);
        //System.out.println(aux);
        String rotado= aux.substring(1)+'0';
        System.out.println("valor tras shift: "+ rotado);
        if(Integer.parseInt(String.valueOf(c))==1) carry=true;
        else carry=false;
        registros[id]=Integer.parseInt(rotado.toString(),2) ;
        //System.out.println(registros[id]);
    }
    public void SRL(int[] registros, int x){
        String aux = String.format("%8s",Integer.toBinaryString(x)).replace(' ', '0') ;
        char c= aux.charAt(aux.length()-1);
        //System.out.println(aux);
        String rotado= '0'+aux.substring(0,aux.length()-1);
        System.out.println("valor tras shift: "+ rotado);
        if(Integer.parseInt(String.valueOf(c))==1) carry=true;
        else carry=false;
        registros[0]=Integer.parseInt(rotado.toString(),2) ;
        //System.out.println(registros[0]);
    }
    public void SRL(int[] registros, String pos){
        int id = Utilities.getIntG1(pos);
        String aux = String.format("%8s",Integer.toBinaryString(registros[id])).replace(' ', '0') ;
        char c= aux.charAt(aux.length()-1);
        //System.out.println(aux);
        String rotado= '0'+aux.substring(0,aux.length()-1);
        System.out.println("valor tras shift: "+ rotado);
        if(Integer.parseInt(String.valueOf(c))==1) carry=true;
        else carry=false;
        registros[id]=Integer.parseInt(rotado.toString(),2) ;
       // System.out.println(registros[id]);
    }

    public void compare(int[] registros, int x){
        if (registros[0]>x){carry = false; zero= false;}
        else if (registros[0]==x){carry= false; zero= true;}
        else{carry=true ;zero= false;}
    }
    public void compare(int[] registros, String pos){
        int id = Utilities.getIntG1(pos);
        if (registros[0]>registros[id]){carry = false; zero= false;}
        else if (registros[0]==registros[id]){carry= false; zero= true;}
        else{carry=true ;zero= false;}
    }

    public void BIT(int x, int pos){
        String aux= String.format("%8s",Integer.toBinaryString(x)).replace(' ', '0') ;
        char c= aux.charAt(pos);
        if(Integer.parseInt(String.valueOf(c))==1) Z=true;
        else Z=false;
    }
    public void BIT(int[] registro, String reg,int bit){
        int id = Utilities.getIntG1(reg);
        String aux= String.format("%8s",Integer.toBinaryString(registro[id])).replace(' ', '0') ;
        char c= aux.charAt(bit);
        if(Integer.parseInt(String.valueOf(c))==1) Z=true;
        else Z=false;
    }
    public void SET(int[] registros, int pos){
        String aux= String.format("%8s",Integer.toBinaryString(registros[0]).replace(' ', '0')) ;
        StringBuilder set = new StringBuilder(aux);
        set.setCharAt(pos, '1');
        registros[0]=Integer.parseInt(set.toString(),2);
    }

    public void SET(int[] registros, String pos, int num){
        int id = Utilities.getIntG1(pos);
        String aux= String.format("%8s",Integer.toBinaryString(registros[id])).replace(' ', '0') ;
        StringBuilder set = new StringBuilder(aux);
        set.setCharAt(num, '1');
        registros[id]=Integer.parseInt(set.toString(),2);
    }

    public void RESET(int[] registros, int pos){
        String aux= String.format("%8s",Integer.toBinaryString(registros[0])).replace(' ', '0') ;
        StringBuilder set = new StringBuilder(aux);
        set.setCharAt(pos, '0');
        registros[0]=Integer.parseInt(set.toString(),2);
    }
    public void RESET(int[] registros, String pos,int num){
        int id = Utilities.getIntG1(pos);
        String aux= String.format("%8s",Integer.toBinaryString(registros[id])).replace(' ', '0') ;
        StringBuilder set = new StringBuilder(aux);
        set.setCharAt(num, '0');
        registros[id]=Integer.parseInt(set.toString(),2);
    }
public static void main (String[]args){
        ALU a=new ALU();
        int a1= 5;
        int b =7;
        int[] z={1,2,3,4,5,6,250};
        a.SRL(z,"L");
   //System.out.print(a);
}

}
