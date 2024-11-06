//Clase GUI:
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JFrame frame;
    private JPanel panelTableroPropio;
    private JPanel panelTableroAtaque;
    private JButton[][] botonesPropios;
    private JButton[][] botonesAtaque;
    private final int TAMANO = 10;
    private final int TAMANO_CELDA_GRANDE = 40;
    private final int TAMANO_CELDA_PEQUENO = 30;
    private boolean modoAtaque = false;
    private DisparoListener disparoListener;

    // Constructor que inicializa el frame principal
    public GUI() {
        frame = new JFrame("Batalla Naval - Modo Posicionamiento");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
    }

    // Método para activar el modo de ataque, cambia el título y el tamaño de la ventana
    public void activarModoAtaque() {
        modoAtaque = true;
        frame.setTitle("Batalla Naval - Modo Ataque");
        frame.setSize(1000, 600); // Reducimos el tamaño de la ventana en modo ataque
        frame.revalidate();
        frame.repaint();
    }

    // Método para mostrar el tablero propio del jugador en el modo de colocación
    public void mostrarTableroPropio(Tablero tablero) {
        if (panelTableroPropio == null) {
            panelTableroPropio = crearPanelTablero("Colocación de Barcos", TAMANO_CELDA_GRANDE);
            botonesPropios = inicializarBotones(panelTableroPropio, tablero, false);
        }
        actualizarVistaTablero(botonesPropios, tablero, false); // No es modo ataque
        cambiarPanel(panelTableroPropio);
    }

    // Método para mostrar el tablero de ataque del jugador en el modo de ataque
    public void mostrarTableroAtaque(Tablero tableroOponente, Tablero tableroPropio,String jugadorActual) {
        if (panelTableroAtaque == null || modoAtaque) {
            panelTableroAtaque = new JPanel(new GridLayout(1, 2));
            JPanel panelAtaque = crearPanelTablero("Tablero de Ataque", TAMANO_CELDA_PEQUENO);
            JPanel panelPropio = crearPanelTablero("Flota de "+jugadorActual, TAMANO_CELDA_PEQUENO);
            botonesAtaque = inicializarBotones(panelAtaque, tableroOponente, true);
            botonesPropios = inicializarBotones(panelPropio, tableroPropio, false);

            panelTableroAtaque.add(panelPropio);
            panelTableroAtaque.add(panelAtaque);
        }
        actualizarVistaTablero(botonesAtaque, tableroOponente, true); // Es modo ataque
        actualizarVistaTablero(botonesPropios, tableroPropio, false); // No es modo ataque
        cambiarPanel(panelTableroAtaque);
    }

    private JPanel crearPanelTablero(String titulo, int tamanoCelda) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel labelTitulo = new JLabel(titulo, SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(labelTitulo, BorderLayout.NORTH);

        // Creamos una cuadrícula que incluye las etiquetas de los ejes
        JPanel tableroPanel = new JPanel(new GridLayout(TAMANO + 1, TAMANO + 1));
        tableroPanel.add(new JLabel("")); // Esquina vacía en la intersección de los ejes

        // Etiquetas de la fila superior (eje X) que muestran 1-10
        for (int col = 0; col < TAMANO; col++) {
            tableroPanel.add(new JLabel(String.valueOf(col + 1), SwingConstants.CENTER)); // Mostrar 1-10
        }

        // Filas del tablero con etiquetas en el eje Y que muestran 1-10
        for (int row = 0; row < TAMANO; row++) {
            tableroPanel.add(new JLabel(String.valueOf(row + 1), SwingConstants.CENTER)); // Mostrar 1-10
            for (int col = 0; col < TAMANO; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(tamanoCelda, tamanoCelda));
                button.setBackground(Color.BLUE);
                tableroPanel.add(button);
            }
        }

        panel.add(tableroPanel, BorderLayout.CENTER);
        return panel;
    }


    // Método para inicializar los botones del tablero y añadir ActionListener en el modo de ataque
    private JButton[][] inicializarBotones(JPanel panel, Tablero tablero, boolean esModoAtaque) {
        JButton[][] botones = new JButton[TAMANO][TAMANO];
        JPanel tableroPanel = (JPanel) panel.getComponent(1);
        int index = TAMANO + 1;

        for (int i = 0; i < TAMANO; i++) {
            index++;
            for (int j = 0; j < TAMANO; j++) {
                botones[i][j] = (JButton) tableroPanel.getComponent(index++);

                if (esModoAtaque) {
                    int x = i;
                    int y = j;
                    botones[i][j].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (disparoListener != null) {
                                disparoListener.disparar(x, y); // Llama al método disparar con las coordenadas del botón
                            }
                        }
                    });
                }
            }
        }
        return botones;
    }

    private void actualizarVistaTablero(JButton[][] botones, Tablero tablero, boolean esModoAtaque) {
        for (int i = 0; i < TAMANO; i++) {
            for (int j = 0; j < TAMANO; j++) {
                Estado estado = tablero.getCeldas()[i][j].getEstado();
                if (esModoAtaque) {
                    if (estado == Estado.IMPACTO) {
                        botones[i][j].setBackground(Color.YELLOW);
                    } else if (estado == Estado.AGUA) {
                        botones[i][j].setBackground(Color.BLUE);
                    } else if (estado == Estado.HUNDIDO) {
                        botones[i][j].setBackground(Color.RED); // Pinta de rojo si el barco está hundido
                    } else if (estado == Estado.AGUA_DISPARADA) {
                        botones[i][j].setBackground(Color.BLACK);
                    }else {
                        botones[i][j].setBackground(Color.BLUE);
                    }
                } else {
                    switch (estado) {
                        case AGUA -> botones[i][j].setBackground(Color.BLUE);
                        case BARCO -> botones[i][j].setBackground(Color.GRAY);
                        case IMPACTO -> botones[i][j].setBackground(Color.YELLOW);
                        case AGUA_DISPARADA -> botones[i][j].setBackground(Color.BLACK);
                        case HUNDIDO -> botones[i][j].setBackground(Color.RED); // Pinta de rojo si el barco está hundido
                    }
                }
            }
        }
    }


    // Método para establecer el listener de disparo
    public void setDisparoListener(DisparoListener listener) {
        this.disparoListener = listener;
    }

    // Método para solicitar la orientación del barco
    public String solicitarOrientacion() {
        String[] opciones = {"horizontal", "vertical"};
        int seleccion = JOptionPane.showOptionDialog(null, "Seleccione la orientación del barco:",
                "Orientación", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, opciones, opciones[0]);
        return opciones[seleccion];
    }

    // Método para mostrar mensajes al usuario
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje);
    }

    // Método para cambiar el panel actual en la ventana
    private void cambiarPanel(JPanel nuevoPanel) {
        frame.getContentPane().removeAll();
        frame.add(nuevoPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    // Interfaz para el listener de disparo
    public interface DisparoListener {
        void disparar(int x, int y);
    }

    public Coordenada solicitarCoordenada(String mensaje) {
        try {
            // Ahora pedimos al usuario que ingrese valores entre 0 y 9
            int y = Integer.parseInt(JOptionPane.showInputDialog(mensaje + " - Ingrese la coordenada X (1-10):"));
            int x = Integer.parseInt(JOptionPane.showInputDialog(mensaje + " - Ingrese la coordenada Y (1-10):"));

            // Validamos que las coordenadas estén en el rango esperado de 0 a 9
            if (x < 1 || x > 10 || y < 1 || y > 10) {
                mostrarMensaje("Coordenadas fuera de rango. Intente nuevamente.");
                return solicitarCoordenada(mensaje);
            }

            return new Coordenada(x, y); // Usamos las coordenadas directamente en 0-9
        } catch (NumberFormatException e) {
            mostrarMensaje("Entrada inválida. Intente nuevamente.");
            return solicitarCoordenada(mensaje);
        }
    }

    public void actualizarTableroPropio(Tablero tablero) {
        actualizarVistaTablero(botonesPropios, tablero, false); // No es modo ataque, muestra toda la información
    }

    public void actualizarTableroAtaque(Tablero tablero) {
        actualizarVistaTablero(botonesAtaque, tablero, true); // Es modo ataque, oculta los barcos no impactados
    }

    public void mostrarImpacto(int x, int y) {
        // Convertimos las coordenadas a 1-10 para mostrar al usuario
        mostrarMensaje("¡Impacto en la coordenada (" + (x + 1) + ", " + (y + 1) + ")!");
    }



}