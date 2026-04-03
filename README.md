# gestor-comercial

Una aplicación en Java que gestiona los productos, ventas y gastos de una empresa.

## Diagrama de Datos Antiguo

![drawSQL-image-export-2026-03-24](https://github.com/user-attachments/assets/ea4c7760-fb42-4ead-a5cd-c3f75fba0303)

Este diagrama se debe cambiar a uno más a corde ya que el de la imagen es de PostgreSQL no SQLite, 
depronto se pueda crear uno con [esta herramienta](https://dbschema.com/databases/sqlite/)

## Diagrama de Datos Actual

<img width="874" height="456" alt="DIAGRAMA DE DATOS PI" src="https://github.com/user-attachments/assets/cc13c486-91d0-4c99-ae74-0390f42898f7" />

Este diagrama fue creado con [esta herramienta](https://dbschema.com/databases/sqlite/), lo malo es que la relacion muchos a uno no se ve bien
en ventas_productos porque las lineas apuntan a la tabla y no a los campos como si lo hacen en el diagrama de PostgreSQL.

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
