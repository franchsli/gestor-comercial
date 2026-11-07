# gestor-comercial

Una aplicación en Java que gestiona los productos, ventas y gastos de una empresa.

## Requerimientos

- Java 21

## Como ejecutar la app

- Descargar el archivo GestorComercial.jar y bd_de_prueba.db desde [aquí](https://github.com/franchsli/gestor-comercial/releases/tag/v2.0.0-beta).

- Abrir la carpeta donde estén los archivos descargados.

- Abrir la linea de comandos dentro de la carpeta y ejecutar el comando:
            java -jar GestorComercial.jar

## Diagrama de Datos Actual

<img width="912" height="684" alt="DIAGRAMA_DE_DATOS" src="https://github.com/user-attachments/assets/4a92af21-b38f-47c1-8d47-ac4604ea3484" />

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

<img width="1600" height="869" alt="PRODUCTOS (R)" src="https://github.com/user-attachments/assets/03f93574-580b-4ae5-9714-2fd1e274ac94" />

### Ventas

Esta ventana muestra las ventas registradas y permite crear, editar o anular (eliminar) dichas ventas.

<img width="1600" height="855" alt="VENTAS (D)" src="https://github.com/user-attachments/assets/22114107-3b53-40c8-81e1-6b7b7ae4ebf9" />

### Gastos

Esta ventana muestra los gastos registrados y permite crear, editar o eliminar gastos.

<img width="1600" height="860" alt="GASTOS (D)" src="https://github.com/user-attachments/assets/8d697b42-afcf-4ef2-a51e-5bebda5e2f17" />

### Presupuestos

Esta ventana muestra los presupuestos registrados y permite crear, editar o eliminar presupuestos.

<img width="1600" height="856" alt="PRESUPUESTOS (D)" src="https://github.com/user-attachments/assets/d0d1fe4b-bf70-43e6-b9dc-2db3ba6b7d9e" />

### Cierres diarios

Esta ventana muestra los cierres diarios que resultan de todos los demás datos en la base de datos.
La celdas de la columna "cumplimiento" se calculan con esta operación:
            total_ventas / presupuesto * 100
El valor se redondea y se muestra como porcentaje.

En caso de que el presupuesto sea 0, el cumplimiento será del 0%.

<img width="1600" height="858" alt="CIERRES (R)" src="https://github.com/user-attachments/assets/98737686-7fd8-44bc-b9c1-93422058a1e8" />

Esta pestaña también puede filtrar los datos mostrados para facilitar el análisis usando los botones de abajo.

Esta pestaña es solo lectura ya que todos los datos mostrados aquí vienen de las tablas.
