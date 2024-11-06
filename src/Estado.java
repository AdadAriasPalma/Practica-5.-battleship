//Enum Estado:
public enum Estado {
    AGUA,       // Celda sin barco
    BARCO,      // Parte de un barco
    IMPACTO,    // Parte de un barco alcanzada
    HUNDIDO,    // Barco completamente destruido
    BARCO_IMPACTADO, AGUA_DISPARADA // Agua disparada sin impacto
}