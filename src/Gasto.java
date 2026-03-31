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
        String condicion = "fecha<" + fecha;
        return todos(condicion);
    }

    /**
     * Devuelve todos los gastos después de la fecha dada.
     * @param fecha La fecha en formato AAAA-MM-DD.
     * @return Todos los gastos posteriores a esa fecha.
     */
    List<Map<String, String>> todosDespues(String fecha){
        String condicion = "fecha>" + fecha;
        return todos(condicion);
    }

    /**
     * Devuelve todos los gastos en la fecha dada.
     * @param fecha La fecha en formato AAAA-MM-DD.
     * @return Todos los gastos previos en esa fecha.
     */
    List<Map<String, String>> todosEn(String fecha){
        String condicion = "fecha=" + fecha;
        return todos(condicion);
    }
}
