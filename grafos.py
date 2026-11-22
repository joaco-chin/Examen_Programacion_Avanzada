class Arista():
    def __init__(self, nodos : tuple[int], costo : int):
        self.nodos = nodos
        self.costo = costo
    def __str__(self):
        return f"[{self.nodos}|{self.costo}]"
    def __repr__(self):
        return f"[{self.nodos}|{self.costo}]"
    
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
    
    def __repr__(self) -> str:
        return f"{self.lista_adyacencia}"

