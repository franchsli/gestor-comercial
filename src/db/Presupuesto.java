package db;

public class Presupuesto extends Modelo {
    public Presupuesto() {
        this.nombreTabla = "PRESUPUESTOS";
        this.columnas = "(fecha, valor)";
    }

    /**
     * Crea un presupuesto en la base de datos.
     * @param fecha La fecha para la que se destina el presupuesto.
     * @param valor El mnto del presupuesto.
     */
    public void crear(String fecha, String valor) {
        String datos[] = {fecha, valor};
        insertar(datos);
    }
}