# gestor-comercial

Una aplicación en Java que gestiona los productos, ventas y gastos de una empresa.

## Diagrama de Datos Actual

![drawSQL-image-export-2026-03-24](https://github.com/user-attachments/assets/ea4c7760-fb42-4ead-a5cd-c3f75fba0303)

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
