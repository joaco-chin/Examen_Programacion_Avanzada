public class Arista {
    public final int id;
    public final int u;
    public final int v;
    public final long costo;
    public boolean esPuente;

    public Arista(int id, int u, int v, long costo) {
        this.id = id;
        this.u = u;
        this.v = v;
        this.costo = costo;
        this.esPuente = false;
    }
}