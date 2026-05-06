package db;

public class DiaNoLaborable extends Modelo {
    public DiaNoLaborable(){
        this.nombreTabla = "DIAS_NO_LABORABLES";
        this.columnas = "(fecha, motivo)";
    }
}
