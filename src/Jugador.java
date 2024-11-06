//Clase Jugador:
public class Jugador {
    private String nombre;
    private Tablero tablero;

    // Constructor que inicializa el nombre del jugador y su tablero
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.tablero = new Tablero();
    }

    // Devuelve el nombre del jugador
    public String getNombre() {
        return nombre;
    }

    // Devuelve el tablero del jugador
    public Tablero getTablero() {
        return tablero;
    }

    // Coloca un barco en el tablero del jugador
    // Devuelve true si la colocación es exitosa, false si hay problemas (como un choque o fuera de límites)
    public boolean colocarBarco(int x, int y, int tamaño, String orientacion) {
        return tablero.colocarBarco(x, y, tamaño, orientacion);
    }

    // Realiza un disparo al tablero del oponente en las coordenadas dadas
    // Devuelve true si el disparo impacta un barco, false si cae en agua o en una posición ya disparada
    public boolean disparar(Jugador oponente, int x, int y) {
        return oponente.getTablero().recibirDisparo(x, y);
    }

    // Devuelve true si todos los barcos del jugador han sido hundidos
    public boolean todosLosBarcosHundidos() {
        for (int i = 0; i < tablero.getCeldas().length; i++) {
            for (int j = 0; j < tablero.getCeldas()[i].length; j++) {
                if (tablero.getCeldas()[i][j].getEstado() == Estado.BARCO) {
                    return false; // Si queda algún barco sin hundir, devuelve false
                }
            }
        }
        return true; // Todos los barcos han sido hundidos
    }
}