package db;

public class Gasto extends Modelo {
    
    public Gasto(){
        this.columnas = "(fecha_gasto, estado, valor, fecha_pago, descripcion, fecha_presupuesto)";
    }

    /**
     * Crea un nuevo gasto en la base de datos.
     * @param fecha_gasto La fecha en la que se contrajo el gasto.
     * @param estado El estado del gasto
     * debe ser "PAGADO" o "PENDIENTE".
     * @param valor El valor del gasto.
     * @param fecha_pago La fecha en la que se pagó el gasto.
     * @param descripcion Explicación del gasto.
     * @param fecha_presupuesto La fecha del presupuesto
     */
    public void crear(String fecha_gasto, String estado, String valor, String fecha_pago, String descripcion, String fecha_presupuesto){
        if (!fecha_pago.isEmpty() && fecha_pago != "null") {
            estado = "PAGADO";
        }
        else {
            estado = "PENDIENTE";
        }
        String[] datos = {fecha_gasto, estado, valor, fecha_pago, descripcion, fecha_presupuesto};
        insertar(datos);
    }
}
