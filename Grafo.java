import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Grafo {
    private final int numCiudades;
    private final List<Arista> aristas;
    private final List<List<Integer>> adyacenciaPorArista;

    private int[] tin;
    private int[] low;
    private boolean[] visitado;
    private int timer;

    public Grafo(int numCiudades, List<Arista> aristas) {
        this.numCiudades = numCiudades;
        this.aristas = aristas;
        this.adyacenciaPorArista = new ArrayList<>(numCiudades + 1);
        for (int i = 0; i <= numCiudades; i++) {
            adyacenciaPorArista.add(new ArrayList<>());
        }
        for (Arista a : aristas) {
            adyacenciaPorArista.get(a.u).add(a.id);
            adyacenciaPorArista.get(a.v).add(a.id);
        }
    }

    /**
     * Ejecuta el algoritmo de Tarjan sobre el grafo completo para marcar
     * las aristas que son puentes (que no pueden eliminarse).
     */
    public void detectarPuentes() {
        tin = new int[numCiudades + 1];
        low = new int[numCiudades + 1];
        visitado = new boolean[numCiudades + 1];
        timer = 0;

        for (Arista a : aristas) {
            a.esPuente = false;
        }

        // Por enunciado, el grafo inicial es conexo; iniciamos desde la ciudad 1.
        if (numCiudades >= 1) {
            dfsTarjan(1, -1);
        }
    }

    private void dfsTarjan(int u, int aristaPadreId) {
        visitado[u] = true;
        timer++;
        tin[u] = timer;
        low[u] = timer;

        for (int idArista : adyacenciaPorArista.get(u)) {
            if (idArista == aristaPadreId) {
                continue;
            }

            Arista arista = aristas.get(idArista);
            int v = (arista.u == u) ? arista.v : arista.u;

            if (visitado[v]) {
                if (tin[v] < low[u]) {
                    low[u] = tin[v];
                }
            } else {
                dfsTarjan(v, idArista);
                if (low[v] < low[u]) {
                    low[u] = low[v];
                }
                if (low[v] > tin[u]) {
                    arista.esPuente = true;
                }
            }
        }
    }

    /**
     * Verifica mediante BFS iterativo si las ciudades siguen conectadas
     * ignorando las aristas cuyos IDs están en 'eliminadasIds'.
     */
    public boolean esConexoSinAristas(Set<Integer> eliminadasIds) {
        if (numCiudades <= 1) {
            return true;
        }

        boolean[] vis = new boolean[numCiudades + 1];
        Queue<Integer> cola = new ArrayDeque<>();

        vis[1] = true;
        cola.add(1);
        int visitadas = 1;

        while (!cola.isEmpty()) {
            int actual = cola.poll();
            for (int idArista : adyacenciaPorArista.get(actual)) {
                if (eliminadasIds.contains(idArista)) {
                    continue;
                }
                Arista arista = aristas.get(idArista);
                int siguiente = (arista.u == actual) ? arista.v : arista.u;
                if (!vis[siguiente]) {
                    vis[siguiente] = true;
                    visitadas++;
                    cola.add(siguiente);
                }
            }
        }

        return visitadas == numCiudades;
    }
}