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
  id_producto INTEGER NOT NULL REFERENCES PRODUCTOS (id) ON DELETE CASCADE,
  cantidad_producto INTEGER NOT NULL
);

CREATE VIEW CIERRES_DIARIOS AS
SELECT
  v.fecha,
  SUM(
    CASE
      WHEN v.tipo = 'EFECTIVO' THEN v.valor
      ELSE 0
    END
  ) AS ventas_efectivo,
  SUM(
    CASE
      WHEN v.tipo = 'CREDITO' THEN v.valor
      ELSE 0
    END
  ) AS ventas_credito,
  SUM(v.valor) AS total_ventas,
  COALESCE(p.valor, 0) AS presupuesto,
  CASE
    WHEN COALESCE(p.valor, 0) = 0 THEN 0 || '%'
    ELSE ROUND((SUM(v.valor) * 100.0) / p.valor, 0) || '%'
  END AS cumplimiento,
  COALESCE(SUM(g.valor), 0) AS gastos
FROM
  VENTAS v
  LEFT JOIN GASTOS g ON g.fecha_gasto = v.fecha
  LEFT JOIN PRESUPUESTOS p ON p.fecha = v.fecha
GROUP BY
  v.fecha,
  p.valor
ORDER BY
  v.fecha;