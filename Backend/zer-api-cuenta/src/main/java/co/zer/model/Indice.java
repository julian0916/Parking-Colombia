package co.zer.model;

public class Indice {
    private int cont = 0;

    public int iniciar() {
        cont = 0;
        return cont;
    }

    public int siguiente() {
        cont++;
        return cont;
    }
}
