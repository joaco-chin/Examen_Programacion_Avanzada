public class Arista {
    int nodo1, nodo2, peso;

    public Arista(int nodo1, int nodo2, int peso) {
        this.nodo1 = nodo1;
        this.nodo2 = nodo2;
        this.peso = peso;
    }

    int otroExtremo(int actual) {
        return (actual == nodo1) ? nodo2 : nodo1;
    }

    public String toString() {
        return nodo1 + " - " + nodo2;
    }

}
