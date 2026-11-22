class Arista():
    def __init__(self, nodos : tuple[int], costo : int):
        self.nodos = nodos
        self.costo = costo
    def __str__(self):
        return f"[{self.nodos}|{self.costo}]"
    def __repr__(self):
        return f"[{self.nodos}|{self.costo}]"
    def __eq__(self, other : Arista) -> bool:
        return self.nodos == other.nodos and self.costo == other.costo
    def __hash__(self):
        return hash((self.nodos, self.costo))
    
class Grafo():
    def __init__(self, nodos : int, aristas : list[Arista]):
        self.lista_adyacencia : list[set[int]] = []
        self.aristas = set(aristas)
        
        for _ in range(nodos + 1):
            self.lista_adyacencia.append(set()) 
        
        for arista in aristas:
            self.lista_adyacencia[arista.nodos[0]].add(arista.nodos[1])
            self.lista_adyacencia[arista.nodos[1]].add(arista.nodos[0])
    
    def insertar_arista(self, arista : Arista):
        u = arista.nodos[0]
        v = arista.nodos[1]
        self.lista_adyacencia[u].add(v)
        self.lista_adyacencia[v].add(u)
    
    def remover_arista(self, arista : Arista):
        if arista not in self.aristas:
            return
        
        u = arista.nodos[0]
        v = arista.nodos[1]
        self.lista_adyacencia[u].remove(v)
        self.lista_adyacencia[v].remove(u)
        
    def adyacentes(self, nodo) -> set[int]: 
        return self.lista_adyacencia[nodo]
    
    def copy(self) -> Grafo:
        return Grafo(len(self.lista_adyacencia) - 1, self.aristas)
    
    def __repr__(self) -> str:
        impresion : str = f""
        for i, vecinos in enumerate(self.lista_adyacencia):
            impresion += f"{i}"
            for vecino in vecinos:
                impresion += " -> "
                impresion += f"{vecino}"
            impresion += f"\n"
        return impresion

# aristas = [Arista((1, 2), 1), Arista((2, 3), 2), Arista((3, 4), 3), Arista((4, 2), 4), Arista((4, 5), 5), Arista((3, 6), 6), Arista((1, 5), 7)]
# grafo = Grafo(6, aristas)
# copia_grafo = grafo.copy()
# print("originales:")
# print(grafo)
# print(copia_grafo)

# print("eliminando arista de copia:")
# copia_grafo.remover_arista(Arista((1, 2), 1))
# print(grafo)
# print(copia_grafo)