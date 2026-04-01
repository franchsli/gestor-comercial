import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

public class Gasto extends Modelo {
    /**
     * Paga el gasto con el id dado.
     * @param id El id del gasto a pagar
     */
    void pagar(String id){
        LinkedHashMap<String, String> gastoPagado = new LinkedHashMap<>();
        gastoPagado.put("estado", "PAGADO");
        String condicion = "id=" + id;
        actualizar(gastoPagado, condicion);
    }

    /**
     * Devuelve todos los gastos pendientes en
     * la base de datos.
     * @return Los gastos pendientes en la base de datos.
     */
    List<Map<String, String>> pendientes(){
        String condicion = "estado='PENDIENTE'";
        return todos(condicion);
    }

    /**
     * Devuelve todos los gastos pagados en
     * la base de datos.
     * @return Los gastos pagados en la base de datos.
     */
    List<Map<String, String>> pagados(){
        String condicion = "estado='PAGADO'";
        return todos(condicion);
    }

    /**
     * Devuelve todos los gastos antes de la fecha dada.
     * @param fecha La fecha en formato AAAA-MM-DD.
     * @return Todos los gastos previos a esa fecha.
     */
    List<Map<String, String>> todosAntes(String fecha){
        String condicion = "fecha<" + formatearValor(fecha);
        return todos(condicion);
    }

    /**
     * Devuelve todos los gastos después de la fecha dada.
     * @param fecha La fecha en formato AAAA-MM-DD.
     * @return Todos los gastos posteriores a esa fecha.
     */
    List<Map<String, String>> todosDespues(String fecha){
        String condicion = "fecha>" + formatearValor(fecha);
        return todos(condicion);
    }

    /**
     * Devuelve todos los gastos en la fecha dada.
     * @param fecha La fecha en formato AAAA-MM-DD.
     * @return Todos los gastos previos en esa fecha.
     */
    List<Map<String, String>> todosEn(String fecha){
        String condicion = "fecha=" + formatearValor(fecha);
        return todos(condicion);
    }


    /**
     * Crea un nuevo gasto en la base de datos.
     * @param fecha La fecha en la que se pagó el gasto.
     * @param estado El estado del gasto
     * debe ser "PAGADO" o "PENDIENTE".
     * @param valor El valor del gasto.
     */
    void crear(String fecha, String estado, String valor){
        String[] datos = {fecha, estado, valor};
        insertar(datos);
    }
    /**
     * Crea un nuevo gasto en la base de datos.
     * @param fecha La fecha en la que se pagó el gasto.
     * @param valor El valor del gasto.
     */
    void crear(String fecha, String valor){
        String[] datos = {fecha, "PENDIENTE", valor};
        insertar(datos);
    }
    /**
     * Crea un nuevo gasto en la base de datos.
     * @param valor El valor del gasto.
     */
    void crear(String valor){
        LocalDate fechahoy = LocalDate.now();
        String fechaHoyTexto = fechahoy.toString();
        String[] datos = {fechaHoyTexto, "PENDIENTE", valor};
        insertar(datos);
    }
}
