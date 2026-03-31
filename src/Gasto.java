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

    List<Map<String, String>> pendientes(){
        String condicion = "estado='PENDIENTE'";
        return todos(condicion);
    }

    List<Map<String, String>> pagados(){
        String condicion = "estado='PAGADO'";
        return todos(condicion);
    }

    List<Map<String, String>> todosAntes(String fecha){
        String condicion = "fecha<" + fecha;
        return todos(condicion);
    }
    List<Map<String, String>> todosDespues(String fecha){
        String condicion = "fecha>" + fecha;
        return todos(condicion);
    }
    List<Map<String, String>> todosEn(String fecha){
        String condicion = "fecha=" + fecha;
        return todos(condicion);
    }
}
