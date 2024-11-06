//Clase Barco:
import java.util.ArrayList;
public class Barco {
    private int tamano;
    private ArrayList<Celda> posiciones;

    public Barco(int tamano) {
        this.tamano = tamano;
        this.posiciones = new ArrayList<>();
    }

    public boolean estaHundido() {
        for (Celda celda : posiciones) {
            if (celda.getEstado() != Estado.IMPACTO) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Celda> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(ArrayList<Celda> posiciones) {
        this.posiciones = posiciones;
    }
}