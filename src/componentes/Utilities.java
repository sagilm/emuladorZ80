package componentes;

import java.util.Arrays;

public class Utilities {
    public static boolean useLoop(String[] arr, String targetValue) {
        for(String s: arr){
            if(s.equals(targetValue))
                return true;
        }
        return false;
    }

    public static <T> int indexOf(T needle, T[] haystack)
    {
        for (int i=0; i<haystack.length; i++)
        {
            if (haystack[i] != null && haystack[i].equals(needle)
                    || needle == null && haystack[i] == null) return i;
        }

        return -1;
    }
    public static String AdjustLength(String s){

        String repeated = new String(new char[8-s.length()]).replace("\0","0");
        s= repeated+s;
        //System.out.println(s);
        return repeated+ s;

    }
    public static int decimalToBinary(int N) {
        if(N==0)return 0;
        System.out.println("utilities. dec to bin :" + N);
        StringBuilder builder = new StringBuilder();
        int base = 2;
        while (N != 0) {
            int reminder = N % base;
            builder.append(reminder);
            N = N / base;
        }
        String temp = builder.reverse().toString();
        if(temp.length()<8) AdjustLength(temp);
        int aux=Integer.parseInt(temp);

        System.out.println("utilities. dec to bin .temp:" + aux);
        return aux;
    }


    public static String getLetraG1(int x){
        String[] traduccion ={"A","B","C","D","E","H","L"};
        return traduccion[x];
    }

    public static int  getIntG1(String x){
        String[] traduccion ={"A","B","C","D","E","H","L"};
        int i =0;
        for (String y:traduccion){
            if (x==y){
                i= indexOf(y,traduccion);
            }
        }
        return i;
    }
    public  static String remove(String s){
        String prosc=s.substring( s.indexOf("(")+1,s.indexOf(")"));
        //System.out.println( prosc);
        return prosc;
    }
    public static void copyData(int[] origen, int[] destino){
        System.arraycopy(origen,0,destino,0,origen.length);
    }

}
