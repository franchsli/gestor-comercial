# gestor-comercial

Una aplicación en Java que gestiona los productos, ventas y gastos de una empresa.

## Diagrama de Datos Actual

<img width="874" height="703" alt="DIAGRAMA DE DATOS" src="https://github.com/user-attachments/assets/b8a8b713-1b30-4f78-8928-be2c23e90042" />

Este diagrama fue creado con [esta herramienta](https://dbschema.com/databases/sqlite/).

## Ejecución

1. El usuario abre la aplicacion y ve 3 pestañas: Productos, Ventas y Gastos.
2. Cliquear cada pestaña muestra la tabla correspondiente (filas y columnas).
3. En cada pestaña se puede hacer las operaciones CRUD:
    - Create: Se puede crear un objeto de la tabla correspondiente, proporcionando valores para ello.
    - Read: Se puede filtrar los datos que se ven en la pestaña.
    - Update: Se puede seleccionar una fila y actualizarla o actualizar varias filas con una condición.
    - Delete: Se puede seleccionar una fila y borrarla o borrar varias filas con una condición.
4. Cada pestaña tiene sus operaciones propias de ser necesario:
    - La pestaña de Ventas debe actualizar los Productos cada vez que se venda algo.
5. Cada acción va a darle un feedback/retroalimentación al usuario para informarle que
la acción fue realizada con éxito o si falló.
