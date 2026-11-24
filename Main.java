import static java.lang.IO.println;

int cantNodos, cantAristas;
Grafo grafo;
int[] objetivos = new int[3];


void main() {
    //cargarGrafo();
    grafoCargado();
    mostrarGrafo();
    resolver(grafo, objetivos);
}

public void resolver(Grafo grafo, int[] objetivos) {
    Map<Integer, List<List<Arista>>> solucionesPorObjetivo = new HashMap<>();
    for (int i : objetivos) {
        solucionesPorObjetivo.put(i, new ArrayList<>());
    }

    int m = grafo.todasAristas.size();
    int totalCombinaciones = 1 << m;

    for (int i = 0; i < totalCombinaciones; i++) {
        List<Arista> eliminadasLista = new ArrayList<>();
        Set<Arista> aristasEliminadas = new HashSet<>();
        int pesoElim = 0;
        for (int j = 0; j < m; j++) {
            if ((i & (1 << j)) != 0) {
                Arista a = grafo.todasAristas.get(j);
                aristasEliminadas.add(a);
                eliminadasLista.add(a);
                pesoElim += a.peso;
            }
        }
        int pesoRestante = grafo.pesoTotal - pesoElim;
        if (solucionesPorObjetivo.containsKey(pesoRestante) && grafo.sigueConexoSin(aristasEliminadas)) {
            solucionesPorObjetivo.get(pesoRestante).add(eliminadasLista);
        }
    }
    for (int objetivo : objetivos) {
        List<List<Arista>> soluciones = solucionesPorObjetivo.get(objetivo);
        if (soluciones.isEmpty()) {
            println("NO");
        } else {
            println("SI\n" + soluciones.size());
            for (List<Arista> combinacionElim : soluciones) {
                List<String> eliminadas = new ArrayList<>();
                for (Arista a : combinacionElim) {
                    eliminadas.add(a.toString());
                }
                System.out.println(", " + eliminadas);
            }
        }

    }
}

public void mostrarGrafo() {
    println("\n--- Grafo Cargado ---");
    for (int i = 1; i <= cantNodos; i++) {
        System.out.println("Nodo " + i + ": " + grafo.adyacencia.get(i));
    }
}

public void grafoCargado() {
    cantNodos = 6;
    cantAristas = cantNodos + 1;
    grafo = new Grafo(cantNodos);
    grafo.agregarArista(1, 2, 1);
    grafo.agregarArista(2, 3, 2);
    grafo.agregarArista(3, 4, 3);
    grafo.agregarArista(4, 2, 4);
    grafo.agregarArista(4, 5, 5);
    grafo.agregarArista(3, 6, 6);
    grafo.agregarArista(1, 5, 7);
    objetivos[0] = 21;
    objetivos[1] = 22;
    objetivos[2] = 30;
}

public void cargarGrafo() {
    Scanner sc = new Scanner(System.in);
    println("Ingrese cantidad de Nodos: ");
    cantNodos = sc.nextInt();
    cantAristas = cantNodos + 1;
    Grafo grafo = new Grafo(cantNodos);

    println("Ingrese cada arista en formato [Nodo1 Nodo2 Peso]:");
    for (int i = 0; i < cantAristas; i++) {
        int nodo1 = sc.nextInt();
        int nodo2 = sc.nextInt();
        int peso = sc.nextInt();
        grafo.agregarArista(nodo1, nodo2, peso);
    }
    IO.print("Ingrese los 3 valores objetivo (T1 T2 T3): ");
    objetivos[0] = sc.nextInt();
    objetivos[1] = sc.nextInt();
    objetivos[2] = sc.nextInt();
    sc.close();
}
