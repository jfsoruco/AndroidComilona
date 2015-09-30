# AndroidComilona
## Proyecto Comilona para el curso de Android. V 1.0.0

### Integrantes.
- Milton Torrico
- Yecid Vega
- Juan Soruco

### Descripción.

La solución propuesta en el proyecto, tiene como objetivo principal, facilitar la organización de grupos de compañeros para realizar la elección y solicitud de pedidos de refrigerio.
Para ello, el diseño contempla 3 tipos de usuarios:
- <b>Encargado del grupo.</b> Quien se encargó de crear al grupo y es el único que puede agregar participantes o cambiar la descripción del grupo.
- <b>Responsable.</b> Es un participante del grupo que por turnos semanales, se convierte en el responsable de la Orden Semanal de la comilona, debiendo llenar una lista de 4 menús para que los demás participantes realicen la elección de uno de ellos. Una vez que la votación termina, debe cerrar la votación y anunciar el menú ganador para recibir los pedidos. Cuando el pedido está completo, cerrará la Orden Semanal y con ello abrirá la orden de la semana siguiente, de la cual un nuevo participante se hará responsable.
- <b>Participante.</b> Es un participante que cuando no es responsable, solo podrá ver la lista de menues propuesta y realizar su votación.
La implementación de estos tres tipos de usuarios sin embargo, está todavia en desarrollo.

### Casos de uso.
- [ ] Creación de un grupo de participantes. *[No implementado]*
- [ ] Edición de un grupo de participantes. *[No implementado]*
- [x] Selección del grupo activo.
- [x] Programación de las 5 ordenes semanales siguientes.
- [x] Abrir orden semanal.
- [x] Llenar lista de menues para habilitar votacion.
- [ ] Cambiar de responsable. *[No implementado]*
- [x] Abrir votación con lista de menues completo.
- [x] Votar.
- [x] Cerrar votación y elección de la opción ganadora.
- [ ] Recepción de pedidos. *[No implementado]*
- [x] Cerrar la orden semanal/Apertura de la siguiente orden semanal.
- [ ] Quitar participantes del grupo. *[No implementado]*
- [ ] Integración *[No implementado]*
