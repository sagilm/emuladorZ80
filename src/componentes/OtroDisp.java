package componentes;

public class OtroDisp implements Dispositivo {
    int[] Datainput = new int[7];
    int[] memory = new int[2];
    int datain;

    @Override
    public int readIN(int[] bus) {
        StringBuilder aux = new StringBuilder();
        for (int r : Datainput) {
            aux.append(r);
        }
        String aux2 = aux.reverse().toString();
        //System.out.println(aux2);
        datain = Integer.parseInt(aux2, 2);
        memory[0] = datain;
        return datain;
    }

    @Override
    public void writeOUT(int[] bus) {
        int pos = (memory[1]);
        int bin = Utilities.decimalToBinary(pos);
        String x = new StringBuilder((Integer.toString(bin))).reverse().toString();
        for (int i = 0; i < x.length(); i++) {
            Datainput[i] = Character.getNumericValue(x.charAt(i));
        }
    }

    @Override
    public void pointTo0(int x) {

    }

    @Override
    public void saveData(int x) {

    }
}
