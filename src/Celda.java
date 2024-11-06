//Clase Celda:
public class Celda {
    private Coordenada coordenada;
    private Estado estado;
    private Barco barco;

    public Celda(int x, int y) {
        this.coordenada = new Coordenada(x, y);
        this.estado = Estado.AGUA; // Estado inicial
        this.barco = null;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    public Barco getBarco() {
        return barco;
    }

    public void setBarco(Barco barco) {
        this.barco = barco;
    }
}