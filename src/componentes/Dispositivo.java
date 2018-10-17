package componentes;

public interface Dispositivo {

    int readIN(int[] bus);
    void writeOUT(int[] bus);

    void pointTo0(int x);

    void saveData(int x);
}
