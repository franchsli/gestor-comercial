# gestor-comercial

Una aplicación en Java que gestiona los productos, ventas y gastos de una empresa.

## Requerimientos

- Java 21

## Como ejecutar la app

- Descargar el archivo GestorComercial.jar y bd_de_prueba.db desde [aquí](https://github.com/franchsli/gestor-comercial/releases/tag/v1.0.0-beta).

- Abrir la carpeta donde estén los archivos descargados.

- Abrir la linea de comandos dentro de la carpeta y ejecutar el comando:
            java -jar GestorComercial.jar

## Diagrama de Datos Actual

<img width="874" height="703" alt="DIAGRAMA DE DATOS" src="https://github.com/user-attachments/assets/b8a8b713-1b30-4f78-8928-be2c23e90042" />

Este diagrama muestra los tipos de datos que se usan para almacenar los datos en la base de datos
y también muestra las siguientes relaciones:

- Un producto puede tener una o varias ventas.
- Una venta debe tener uno o varios productos.
- Un presupuesto puede cubrir uno o varios gastos.
- Un gasto puede ser cubierto por un presupuesto.

Este diagrama fue creado con [esta herramienta](https://dbschema.com/databases/sqlite/).

## Pestañas

La aplicación cuenta con 5 pestañas:

### Productos

Esta ventana muestra el inventario de los productos y permite crear, editar o eliminar productos.
También se puede buscar un producto por su nombre usando la barra de búsqueda en la parte superior de la pestaña.

### Ventas

Esta ventana muestra las ventas registradas y permite crear, editar o anular (eliminar) dichas ventas.

### Gastos

Esta ventana muestra los gastos registrados y permite crear, editar o eliminar gastos.

### Presupuestos

Esta ventana muestra los presupuestos registrados y permite crear, editar o eliminar presupuestos.

## Cierres diarios

Esta ventana muestra los cierres diarios que resultan de todos los demás datos en la base de datos.
La celdas de la columna "cumplimiento" se calculan con esta operación:
            total_ventas / presupuesto * 100
El valor se redondea y se muestra como porcentaje.

En caso de que el presupuesto sea 0, el cumplimiento será del 0%.

Esta pestaña también puede filtrar los datos mostrados para facilitar el análisis usando los botones de abajo.

Esta pestaña es solo lectura ya que todos los datos mostrados aquí vienen de las tablas.
