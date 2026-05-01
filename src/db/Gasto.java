package db;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

public class Gasto extends Modelo {
    
    public Gasto(){
        this.columnas = "(fecha_gasto, estado, valor, fecha_pago, descripcion)";
    }

    /**
     * Paga el gasto con el id dado.
     * @param id El id del gasto a pagar
     */
    public void pagar(String id){
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
    public List<Map<String, String>> pendientes(){
        String condicion = "estado='PENDIENTE'";
        return todos(condicion);
    }

    /**
     * Devuelve todos los gastos pagados en
     * la base de datos.
     * @return Los gastos pagados en la base de datos.
     */
    public List<Map<String, String>> pagados(){
        String condicion = "estado='PAGADO'";
        return todos(condicion);
    }

    /**
     * Devuelve todos los gastos antes de la fecha dada.
     * @param fecha La fecha en formato AAAA-MM-DD.
     * @param pagados Si se debe devolver los gastos pagados o pendientes.
     * @return Todos los gastos previos a esa fecha.
     */
    public List<Map<String, String>> todosAntes(String fecha, boolean pagados){
        String condicion;
        if (pagados) {
            condicion = "fecha_pago<" + formatearValor(fecha);            
        }
        else{
            condicion = "fecha_gasto<" + formatearValor(fecha);
        }
        return todos(condicion);
    }

    /**
     * Devuelve todos los gastos pendientes antes de la fecha dada.
     * @param fecha La fecha en formato AAAA-MM-DD.
     * @return Todos los gastos previos a esa fecha.
     */
    public List<Map<String, String>> todosAntes(String fecha){
        return todosAntes(fecha, false);
    }

    /**
     * Devuelve todos los gastos después de la fecha dada.
     * @param fecha La fecha en formato AAAA-MM-DD.
     * @param pagados Si se debe devolver los gastos pagados o pendientes.
     * @return Todos los gastos posteriores a esa fecha.
     */
    public List<Map<String, String>> todosDespues(String fecha, boolean pagados){
        String condicion;
        if (pagados) {
            condicion = "fecha_pago>" + formatearValor(fecha);            
        }
        else{
            condicion = "fecha_gasto>" + formatearValor(fecha);
        }
        return todos(condicion);
    }

    /**
     * Devuelve todos los gastos pendientes después de la fecha dada.
     * @param fecha La fecha en formato AAAA-MM-DD.
     * @return Todos los gastos posteriores a esa fecha.
     */
    public List<Map<String, String>> todosDespues(String fecha){
        return todosDespues(fecha, false);
    }

    /**
     * Devuelve todos los gastos en la fecha dada.
     * @param fecha La fecha en formato AAAA-MM-DD.
     * @param pagados Si se debe devolver los gastos pagados o pendientes.
     * @return Todos los gastos previos en esa fecha.
     */
    public List<Map<String, String>> todosEn(String fecha, boolean pagados){
        String condicion;
        if (pagados) {
            condicion = "fecha_pago=" + formatearValor(fecha);            
        }
        else{
            condicion = "fecha_gasto=" + formatearValor(fecha);
        }
        return todos(condicion);
    }

    /**
     * Devuelve todos los gastos pendientes en la fecha dada.
     * @param fecha La fecha en formato AAAA-MM-DD.
     * @return Todos los gastos previos en esa fecha.
     */
    public List<Map<String, String>> todosEn(String fecha){
        return todosEn(fecha, false);
    }


    /**
     * Crea un nuevo gasto en la base de datos.
     * @param fecha_gasto La fecha en la que se contrajo el gasto.
     * @param estado El estado del gasto
     * debe ser "PAGADO" o "PENDIENTE".
     * @param valor El valor del gasto.
     * @param fecha_pago La fecha en la que se pagó el gasto.
     * @param descripcion Explicación del gasto.
     */
    public void crear(String fecha_gasto, String estado, String valor, String fecha_pago, String descripcion){
        if (!fecha_pago.isEmpty() && fecha_pago != "null") {
            estado = "PAGADO";
        }
        else {
            estado = "PENDIENTE";
        }
        String[] datos = {fecha_gasto, estado, valor, fecha_pago, descripcion};
        insertar(datos);
    }

    /**
     * Crea un nuevo gasto en la base de datos.
     * @param fecha_gasto La fecha en la que se contrajo el gasto.
     * @param estado El estado del gasto
     * debe ser "PAGADO" o "PENDIENTE".
     * @param valor El valor del gasto.
     * @param descripcion Explicación del gasto.
     */
    public void crear(String fecha_gasto, String estado, String valor, String descripcion){
        crear(fecha_gasto, estado, valor, "null", descripcion);
    }
    /**
     * Crea un nuevo gasto en la base de datos.
     * @param fecha La fecha en la que se contrajo el gasto.
     * @param valor El valor del gasto.
     * @param descripcion Explicación del gasto.
     */
    public void crear(String fecha_gasto, String valor, String descripcion){
        crear(fecha_gasto, "PENDIENTE", valor, "null", descripcion);
    }
    /**
     * Crea un nuevo gasto en la base de datos.
     * @param valor El valor del gasto.
     * @param descripcion Explicación del gasto.
     */
    public void crear(String valor, String descripcion){
        LocalDate fechahoy = LocalDate.now();
        String fechaHoyTexto = fechahoy.toString();
        crear(fechaHoyTexto, "PENDIENTE", valor, "null", descripcion);
    }

    public static void main(String[] args) {
        Gasto gasto = new Gasto();
        // pagar un gasto
        gasto.pagar("10");
        // verificar que se pagó
        System.out.println(gasto.todos("id=10"));
        System.out.println("------");
        // muestra todos los gastos pendientes
        List<Map<String, String>> resultados = gasto.pendientes();
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
        System.out.println("------");
        // muestra todos los gastos pagados
        resultados = gasto.pagados();
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
        System.out.println("------");
        // muestra todos los gastos antes de '2025-01-15'
        resultados = gasto.todosAntes("2025-01-15");
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
        System.out.println("------");
        // muestra todos los gastos depues de '2025-01-15'
        resultados = gasto.todosDespues("2025-02-01");
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
        System.out.println("------");
        // muestra todos los gastos en '2025-01-15'
        resultados = gasto.todosDespues("2025-03-25");
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
        System.out.println("------");
        // crea un gasto
        gasto.crear("100000", "no sé");
        System.out.println(gasto.todos("id=11"));
        System.out.println("------");
        // muestra todos los gastos pagados antes de '2025-01-15'
        resultados = gasto.todosAntes("2025-01-15", true);
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
        System.out.println("------");
        // muestra todos los gastos pagados depues de '2025-01-15'
        resultados = gasto.todosDespues("2025-02-01", true);
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
        System.out.println("------");
        // muestra todos los gastos pagados en '2025-01-15'
        resultados = gasto.todosDespues("2025-03-25", true);
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
        System.out.println("------");
        // crea un gasto con todos los datos
        gasto.crear("2026-03-01","PENDIENTE",  "100000", "2026-04-01", "algo");
        System.out.println("------");
        // muestra todos los gastos pagados antes de '2025-01-15'
        resultados = gasto.todosAntes("2025-01-15", true);
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
        System.out.println("------");
        // muestra todos los gastos pagados depues de '2025-01-15'
        resultados = gasto.todosDespues("2025-02-01", true);
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
        System.out.println("------");
        // muestra todos los gastos pagados en '2025-01-15'
        resultados = gasto.todosDespues("2025-03-25", true);
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
        System.out.println("------");
        DBConnection.cerrar();
    }
}
