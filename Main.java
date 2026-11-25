import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class Main {
    private static final int NUM_OBJETIVOS = 3;
    private static final int MAX_ELIMINACIONES = 2;

    private static Grafo grafo;
    private static List<Arista> todasLasAristas;
    private static List<Arista> candidatas; // Aristas no-puente ordenadas de mayor a menor costo
    private static long[] objetivos = new long[NUM_OBJETIVOS];

    private static boolean[] resuelto = new boolean[NUM_OBJETIVOS];
    private static int[] solK = new int[NUM_OBJETIVOS];
    private static List<List<Arista>> solEliminadas = new ArrayList<>();

    private static List<Arista> eliminadasActuales = new ArrayList<>();
    private static Set<Integer> idsEliminadosActuales = new HashSet<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String lineaN = leerSiguienteLineaNoVacia(br);
        if (lineaN == null) {
            return;
        }

        int n = Integer.parseInt(lineaN.trim());
        int m = n + 1; // En la versión modificada hay N ciudades y N+1 autopistas

        todasLasAristas = new ArrayList<>(m);
        long sumaTotal = 0;

        for (int i = 0; i < m; i++) {
            StringTokenizer st = new StringTokenizer(leerSiguienteLineaNoVacia(br));
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            long c = Long.parseLong(st.nextToken());
            Arista arista = new Arista(i, a, b, c);
            todasLasAristas.add(arista);
            sumaTotal += c;
        }

        StringTokenizer stF = new StringTokenizer(leerSiguienteLineaNoVacia(br));
        for (int i = 0; i < NUM_OBJETIVOS; i++) {
            objetivos[i] = Long.parseLong(stF.nextToken());
            solEliminadas.add(new ArrayList<>());
        }

        // Caso K = 0: si algún objetivo coincide con la suma total de todas las autopistas
        for (int i = 0; i < NUM_OBJETIVOS; i++) {
            if (objetivos[i] == sumaTotal) {
                resuelto[i] = true;
                solK[i] = 0;
            }
        }

        if (!todosResueltos()) {
            grafo = new Grafo(n, todasLasAristas);
            grafo.detectarPuentes();

            candidatas = new ArrayList<>();
            for (Arista a : todasLasAristas) {
                if (!a.esPuente) {
                    candidatas.add(a);
                }
            }
            // Ordenar de mayor a menor costo para que las cotas de Branch and Bound sean exactas en O(1)
            candidatas.sort(Comparator.comparingLong((Arista a) -> a.costo).reversed());

            buscar(0, 0, sumaTotal);
        }

        imprimirResultados();
    }

    private static void buscar(int idx, int k, long costoActual) {
        if (todosResueltos()) {
            return;
        }

        // Verificar si el costo actual coincide con algún objetivo pendiente (con K >= 1)
        if (k >= 1) {
            for (int i = 0; i < NUM_OBJETIVOS; i++) {
                if (!resuelto[i] && costoActual == objetivos[i]) {
                    if (grafo.esConexoSinAristas(idsEliminadosActuales)) {
                        resuelto[i] = true;
                        solK[i] = k;
                        solEliminadas.set(i, new ArrayList<>(eliminadasActuales));
                    }
                }
            }
        }

        if (todosResueltos()) {
            return;
        }

        if (k == MAX_ELIMINACIONES || idx == candidatas.size()) {
            return;
        }

        // Cotas de Branch and Bound en O(1)
        long maxCosto = costoActual;
        long minCosto = costoActual;
        int cupoRestante = MAX_ELIMINACIONES - k;

        if (cupoRestante >= 1 && idx < candidatas.size()) {
            minCosto -= candidatas.get(idx).costo;
        }
        if (cupoRestante == 2 && (idx + 1) < candidatas.size()) {
            minCosto -= candidatas.get(idx + 1).costo;
        }

        // Poda: solo continuamos si al menos un objetivo pendiente cae en [minCosto, maxCosto]
        boolean hayObjetivoAlcanzable = false;
        for (int i = 0; i < NUM_OBJETIVOS; i++) {
            if (!resuelto[i] && minCosto <= objetivos[i] && objetivos[i] <= maxCosto) {
                hayObjetivoAlcanzable = true;
                break;
            }
        }

        if (!hayObjetivoAlcanzable) {
            return;
        }

        // Rama 1: Quitar candidatas.get(idx)
        Arista actual = candidatas.get(idx);
        eliminadasActuales.add(actual);
        idsEliminadosActuales.add(actual.id);

        buscar(idx + 1, k + 1, costoActual - actual.costo);

        eliminadasActuales.remove(eliminadasActuales.size() - 1);
        idsEliminadosActuales.remove(actual.id);

        // Rama 2: Conservar candidatas.get(idx)
        buscar(idx + 1, k, costoActual);
    }

    private static boolean todosResueltos() {
        return resuelto[0] && resuelto[1] && resuelto[2];
    }

    private static void imprimirResultados() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < NUM_OBJETIVOS; i++) {
            if (!resuelto[i]) {
                sb.append("NO\n");
            } else {
                sb.append("SI\n");
                sb.append(solK[i]).append('\n');
                for (Arista a : solEliminadas.get(i)) {
                    sb.append(a.u).append(' ').append(a.v).append('\n');
                }
            }
        }
        System.out.print(sb.toString());
    }

    private static String leerSiguienteLineaNoVacia(BufferedReader br) throws IOException {
        String linea;
        while ((linea = br.readLine()) != null) {
            if (!linea.trim().isEmpty()) {
                return linea;
            }
        }
        return null;
    }
}