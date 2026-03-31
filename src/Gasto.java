import java.util.LinkedHashMap;

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
}
