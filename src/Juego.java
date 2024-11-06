//Clase Juego:
import java.util.Arrays;
import java.util.List;

public class Juego {
    private Jugador jugador1;
    private Jugador jugador2;
    private GUI gui;
    private Jugador jugadorActual;
    private List<Integer> tamanosBarcos;

    public Juego(String nombreJugador1, String nombreJugador2) {
        jugador1 = new Jugador(nombreJugador1);
        jugador2 = new Jugador(nombreJugador2);
        tamanosBarcos = Arrays.asList(2, 2, 3, 3, 3, 4, 1, 1);
        gui = new GUI();
        jugadorActual = jugador1;
    }

    public void iniciar() {
        // Fase de posicionamiento de barcos
        colocarBarcos(jugador1);
        colocarBarcos(jugador2);

        // Cambiar al modo de ataque
        gui.activarModoAtaque();

        // Fase de ataque
        while (!haTerminadoElJuego()) {
            turnoDisparo();
            if (haTerminadoElJuego()) {
                break;
            }
            cambiarTurno();
        }
    }

    private void colocarBarcos(Jugador jugador) {
        gui.mostrarMensaje("Turno de " + jugador.getNombre() + " para colocar sus barcos.");
        gui.mostrarTableroPropio(jugador.getTablero());

        for (int tamaño : tamanosBarcos) {
            boolean colocado = false;
            while (!colocado) {
                Coordenada coord = gui.solicitarCoordenada("Coloca tu barco de tamaño " + tamaño);
                String orientacion = gui.solicitarOrientacion();
                colocado = jugador.colocarBarco(coord.getX() - 1, coord.getY() - 1, tamaño, orientacion);

                if (!colocado) {
                    gui.mostrarMensaje("No se pudo colocar el barco en esta posición. Intente otra vez.");
                }
            }
            gui.actualizarTableroPropio(jugador.getTablero());
        }
    }

    private void turnoDisparo() {
        Jugador oponente = (jugadorActual == jugador1) ? jugador2 : jugador1;
        gui.mostrarMensaje("Turno de " + jugadorActual.getNombre() + " para atacar.");
        gui.mostrarTableroAtaque(oponente.getTablero(), jugadorActual.getTablero(), jugadorActual.getNombre());

        boolean disparoAcertado = true;
        while (disparoAcertado) {
            Coordenada coord = gui.solicitarCoordenada("Ingresa la coordenada para disparar");
            disparoAcertado = jugadorActual.disparar(oponente, coord.getX() - 1, coord.getY() - 1);
            gui.actualizarTableroAtaque(oponente.getTablero());

            if (disparoAcertado) {
                gui.mostrarMensaje("¡Impacto! Dispara de nuevo.");
                if (haTerminadoElJuego()) {
                    mostrarGanador();
                    return;
                }
            } else {
                gui.mostrarMensaje("¡Agua!");
            }
        }
    }

    private void cambiarTurno() {
        jugadorActual = (jugadorActual == jugador1) ? jugador2 : jugador1;
    }

    private boolean haTerminadoElJuego() {
        return jugador1.todosLosBarcosHundidos() || jugador2.todosLosBarcosHundidos();
    }

    private void mostrarGanador() {
        String ganador = jugador1.todosLosBarcosHundidos() ? jugador2.getNombre() : jugador1.getNombre();
        gui.mostrarMensaje("¡El juego ha terminado! El ganador es: " + ganador);
    }
}