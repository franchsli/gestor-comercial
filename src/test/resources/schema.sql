CREATE TABLE PRESUPUESTOS (
  fecha DATE PRIMARY KEY,
  valor INTEGER NOT NULL
);

CREATE TABLE GASTOS (
  id INTEGER PRIMARY KEY,
  fecha_gasto DATE NOT NULL,
  estado VARCHAR NOT NULL CHECK (estado in ('PAGADO', 'PENDIENTE')),
  valor INTEGER NOT NULL,
  fecha_pago DATE,
  descripcion VARCHAR NOT NULL,
  fecha_presupuesto DATE REFERENCES PRESUPUESTOS (fecha)
);

CREATE TABLE PRODUCTOS (
  id INTEGER PRIMARY KEY,
  nombre VARCHAR UNIQUE NOT NULL,
  precio_unitario INTEGER NOT NULL,
  cantidad INTEGER NOT NULL
);

CREATE TABLE VENTAS (
  id INTEGER PRIMARY KEY,
  fecha DATE NOT NULL,
  valor INTEGER NOT NULL,
  tipo TEXT NOT NULL CHECK (tipo IN ('EFECTIVO', 'CREDITO'))
);

CREATE TABLE VENTAS_PRODUCTOS (
  id_venta INTEGER NOT NULL REFERENCES VENTAS (id) ON DELETE CASCADE,
  id_producto INTEGER NOT NULL REFERENCES PRODUCTOS (id) ON DELETE RESTRICT,
  cantidad_producto INTEGER NOT NULL
);

CREATE VIEW CIERRES_DIARIOS AS
WITH ventas_dia AS (
  SELECT
    fecha,
    SUM(CASE WHEN tipo = 'EFECTIVO' THEN valor ELSE 0 END) AS ventas_efectivo,
    SUM(CASE WHEN tipo = 'CREDITO' THEN valor ELSE 0 END) AS ventas_credito,
    SUM(valor) AS total_ventas
  FROM VENTAS
  GROUP BY fecha
),
gastos_dia AS (
  SELECT fecha_gasto AS fecha, SUM(valor) AS gastos
  FROM GASTOS
  GROUP BY fecha_gasto
),
fechas AS (
  SELECT fecha FROM ventas_dia
  UNION
  SELECT fecha FROM gastos_dia
  UNION
  SELECT fecha FROM PRESUPUESTOS
)
SELECT
  f.fecha,
  COALESCE(vd.ventas_efectivo, 0) AS ventas_efectivo,
  COALESCE(vd.ventas_credito, 0)  AS ventas_credito,
  COALESCE(vd.total_ventas, 0)    AS total_ventas,
  COALESCE(p.valor, 0)            AS presupuesto,
  CASE
    WHEN COALESCE(p.valor, 0) = 0 THEN '0%'
    ELSE ROUND(COALESCE(vd.total_ventas, 0) * 100.0 / p.valor, 0) || '%'
  END AS cumplimiento,
  COALESCE(gd.gastos, 0) AS gastos
FROM fechas f
LEFT JOIN ventas_dia  vd ON vd.fecha = f.fecha
LEFT JOIN gastos_dia  gd ON gd.fecha = f.fecha
LEFT JOIN PRESUPUESTOS p ON p.fecha  = f.fecha
ORDER BY f.fecha;