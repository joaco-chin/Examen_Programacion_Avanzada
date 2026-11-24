import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Grafo {
    int cantidadNodo;
    List<List<Arista>> adyacencia;
    List<Arista> todasAristas;
    int pesoTotal = 0;

    public Grafo(int cantidadNodo) {
        this.cantidadNodo = cantidadNodo;
        this.adyacencia = new ArrayList<>();
        this.todasAristas = new ArrayList<>();
        for (int i = 1; i <= cantidadNodo + 1; i++) {
            this.adyacencia.add(new ArrayList<>());
        }
    }

    public void agregarArista(int nodo1, int nodo2, int peso) {
        Arista a = new Arista(nodo1, nodo2, peso);
        todasAristas.add(a);
        adyacencia.get(nodo1).add(a);
        pesoTotal += peso;
        if (nodo1 != nodo2) {
            adyacencia.get(nodo2).add(a);
        }
    }

    boolean sigueConexoSin(Set<Arista> eliminadas) {
        boolean[] visitado = new boolean[cantidadNodo + 1];
        int nodosAlcanzados = dfsContar(1, visitado, eliminadas);
        return nodosAlcanzados == cantidadNodo;
    }

    private int dfsContar(int actual, boolean[] visitado, Set<Arista> eliminadas) {
        visitado[actual] = true;
        int cuenta = 1;

        for (Arista a : adyacencia.get(actual)) {
            if (!eliminadas.contains(a)) {
                int vecino = a.otroExtremo(actual);
                if (!visitado[vecino]) {
                    cuenta += dfsContar(vecino, visitado, eliminadas);
                }
            }
        }
        return cuenta;
    }
}
