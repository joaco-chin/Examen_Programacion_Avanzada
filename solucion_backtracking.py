from grafos import Arista, Grafo

# Utilizamos DFS para verificar si una rama es podable o no.
# De no serlo, quiere decir que al removerla, el grafo
# dejaria de ser convexo, incumpliendo lo pedido por el 
# enunciado 

def es_podable_dfs(grafo : Grafo, arista : Arista) -> bool:
    grafo.remover_arista(arista)
    u = arista.nodos[0]
    v = arista.nodos[1]
    
    pila = []
    visitados = set()
    
    pila.append(u)
    visitados.add(u)
    
    while len(pila) > 0:
        u = pila.pop()
        # procesar(v) 
        if u == v:
            grafo.insertar_arista(arista)
            return True
        
        for vecino in grafo.adyacentes(u):
            if vecino not in visitados:
                pila.append(vecino)
                visitados.add(vecino)
                
    grafo.insertar_arista(arista)
    return False

# Utilizamos backtracking para calcular que el costo de construir el grafo
# sea igual al valor solicitada por el ministro. Si el valor es distinto, 
# vemos si podemos eliminar una o varias aristas (serian maximo 2 por propiedad.
# De no ser posible, retornamos falso. En caso contrario, retornamos verdadero y
# una lista de aristas cortadas para llegar al costo (puede estar vacia)

def calcular_costos(aristas : list, costo : int, valor : int, cortadas : list, grafo : Grafo) -> tuple[bool, list]:
    if not aristas and costo == valor:
        return (True, cortadas)
    
    if not aristas and costo != valor:
        return (False, [])
    
    if costo == valor:
        return (True, cortadas + aristas)
    
    arista_actual = aristas[0]
    nuevas_aristas = aristas.copy()
    nuevas_aristas.pop(0)
    nuevo_costo = arista_actual.costo + costo
    
    if nuevo_costo > valor:
        nuevo_grafo = grafo.copy()
        if not es_podable_dfs(nuevo_grafo, arista_actual):
            return (False, [])
        nuevo_grafo.remover_arista(arista_actual) 
        return calcular_costos(nuevas_aristas, costo, valor, cortadas + [arista_actual], nuevo_grafo)
    
    resultado = calcular_costos(nuevas_aristas, nuevo_costo, valor, cortadas, grafo)
    
    if not resultado[0]:
        nuevo_grafo = grafo.copy()
        if not es_podable_dfs(nuevo_grafo, arista_actual):
            return (False, [])
        nuevo_grafo.remover_arista(arista_actual) 
        return calcular_costos(nuevas_aristas, costo, valor, cortadas + [arista_actual], nuevo_grafo)
    
    return resultado

def crear_ruta(num_ciudades : int, autopistas : list[Arista], valores : tuple[int]) -> list[list]:
    grafo_ciudad = Grafo(num_ciudades, autopistas)
    salida : list = []
    salidas : list[list] = []
    
    for valor in valores:
        resultado = calcular_costos(autopistas, 0, valor, [], grafo_ciudad)
        if not resultado[0]:
            salida.append("NO")
            continue
        
        salida.append("SI")
        salida.append(len(resultado[1]))
        salida.append(resultado[1])
        salidas.append(salida)
    
    return salida

cant_ciudades = 6
aristas = [Arista((1, 2), 1), Arista((2, 3), 2), Arista((3, 4), 3), Arista((4, 2), 4), Arista((4, 5), 5), Arista((3, 6), 6), Arista((1, 5), 7)]
f = (21, 22, 30)
print(crear_ruta(cant_ciudades, aristas, f))




