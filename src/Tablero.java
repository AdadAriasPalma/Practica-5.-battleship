//Clase Tablero:
import java.util.*;

public class Tablero {
    private final int TAMANO = 10;
    private Celda[][] celdas;
    private List<Barco> barcos;

    public Tablero() {
        celdas = new Celda[TAMANO][TAMANO];
        barcos = new ArrayList<>();
        for (int i = 0; i < TAMANO; i++) {
            for (int j = 0; j < TAMANO; j++) {
                celdas[i][j] = new Celda(i, j);
            }
        }
    }

    public Celda[][] getCeldas() {
        return celdas;
    }

    public boolean colocarBarco(int x, int y, int tamaño, String orientacion) {
        Barco barco = new Barco(tamaño);
        ArrayList<Celda> posiciones = new ArrayList<>();
        // Verifica si el barco cabe en el tablero en función de su orientación
        if (orientacion.equals("horizontal")) {
            if (y < 0 || y + tamaño > TAMANO) {  // Verifica que el barco no se salga del borde derecho
                System.out.println("El barco no cabe en esta posición horizontalmente.");
                return false;
            }
        } else if (orientacion.equals("vertical")) {
            if (x < 0 || x + tamaño > TAMANO) {  // Verifica que el barco no se salga del borde inferior
                System.out.println("El barco no cabe en esta posición verticalmente.");
                return false;
            }
        } else {
            System.out.println("Orientación no válida. Debe ser 'horizontal' o 'vertical'.");
            return false;
        }

        // Verifica que todas las celdas donde se colocará el barco estén vacías (AGUA)
        for (int i = 0; i < tamaño; i++) {
            if (orientacion.equals("horizontal")) {
                if (celdas[x][y + i].getEstado() != Estado.AGUA) {
                    System.out.println("Ya hay un barco en la posición (" + x + ", " + (y + i) + ").");
                    return false;
                }
            } else { // Orientación vertical
                if (celdas[x + i][y].getEstado() != Estado.AGUA) {
                    System.out.println("Ya hay un barco en la posición (" + (x + i) + ", " + y + ").");
                    return false;
                }
            }
        }

        // Coloca el barco en las posiciones indicadas si pasó todas las verificaciones
        for (int i = 0; i < tamaño; i++) {
            if (orientacion.equals("horizontal")) {
                posiciones.add(celdas[x][y + i]);
                celdas[x][y + i].setBarco(barco);
                celdas[x][y + i].setEstado(Estado.BARCO);
            } else {
                posiciones.add(celdas[x + i][y]);
                celdas[x + i][y].setBarco(barco);
                celdas[x + i][y].setEstado(Estado.BARCO);
            }
        }
        barco.setPosiciones(posiciones);
        barcos.add(barco); // Añadir barco a la lista de barcos del tablero
        return true;// Barco colocado exitosamente
    }

    public boolean recibirDisparo(int x, int y) {
        Celda celda = celdas[x][y];
        if (celda.getEstado() == Estado.BARCO) {
            celda.setEstado(Estado.IMPACTO);
            Barco barco = celda.getBarco(); // Obtener el barco de la celda impactada
            if (barco != null && barco.estaHundido()) {
                for (Celda c : barco.getPosiciones()) {
                    c.setEstado(Estado.HUNDIDO); // Marcar todas las celdas del barco como hundido
                }
            }
            return true;
        } else if (celda.getEstado() == Estado.AGUA) {
            celda.setEstado(Estado.AGUA_DISPARADA);
            return false;
        }
        return false;
    }

    public void verificarHundimiento() {
        for (int i = 0; i < TAMANO; i++) {
            for (int j = 0; j < TAMANO; j++) {
                if (celdas[i][j].getEstado() == Estado.BARCO_IMPACTADO) {
                    // Verificar si todas las celdas del barco están impactadas
                    List<Coordenada> barco = obtenerCeldasDelBarco(i, j);
                    boolean hundido = true;
                    for (Coordenada coord : barco) {
                        if (celdas[coord.getX()][coord.getY()].getEstado() != Estado.IMPACTO) {
                            hundido = false;
                            break;
                        }
                    }
                    // Si el barco está hundido, cambia todas sus celdas a estado HUNDIDO
                    if (hundido) {
                        for (Coordenada coord : barco) {
                            celdas[coord.getX()][coord.getY()].setEstado(Estado.HUNDIDO);
                        }
                    }
                }
            }
        }
    }

    private List<Coordenada> obtenerCeldasDelBarco(int x, int y) {
        List<Coordenada> celdasBarco = new ArrayList<>();
        // Asumimos que la celda (x, y) ya es parte del barco
        celdasBarco.add(new Coordenada(x, y));

        // Verificar horizontalmente hacia la derecha
        int j = y + 1;
        while (j < TAMANO && celdas[x][j].getEstado() == Estado.BARCO) {
            celdasBarco.add(new Coordenada(x, j));
            j++;
        }

        // Verificar horizontalmente hacia la izquierda
        j = y - 1;
        while (j >= 0 && celdas[x][j].getEstado() == Estado.BARCO) {
            celdasBarco.add(new Coordenada(x, j));
            j--;
        }

        // Verificar verticalmente hacia abajo
        int i = x + 1;
        while (i < TAMANO && celdas[i][y].getEstado() == Estado.BARCO) {
            celdasBarco.add(new Coordenada(i, y));
            i++;
        }

        // Verificar verticalmente hacia arriba
        i = x - 1;
        while (i >= 0 && celdas[i][y].getEstado() == Estado.BARCO) {
            celdasBarco.add(new Coordenada(i, y));
            i--;
        }

        return celdasBarco;
    }

    public void barcoImpactado(int x, int y) {
        // Verifica si la celda impactada contiene una parte de barco
        if (celdas[x][y].getEstado() == Estado.BARCO) {
            celdas[x][y].setEstado(Estado.IMPACTO); // Cambia el estado a IMPACTO
            verificarHundimiento(); // Revisa si el barco al que pertenece esta celda está completamente hundido
        }
    }


}