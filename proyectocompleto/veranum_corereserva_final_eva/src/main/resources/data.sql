INSERT IGNORE INTO habitaciones (numero, tipo, precio_por_noche, estado)
VALUES ('101', 'SIMPLE', 45000.0, 'DISPONIBLE');
INSERT IGNORE INTO habitaciones (numero, tipo, precio_por_noche, estado)
VALUES ('205', 'DOBLE', 70000.0, 'DISPONIBLE');
INSERT IGNORE INTO habitaciones (numero, tipo, precio_por_noche, estado)
VALUES ('301', 'SUITE', 120000.0, 'DISPONIBLE');



INSERT IGNORE INTO reservas (rut_usuario, habitacion_id, fecha_ingreso, fecha_salida, estado, costo_total, cantidad_huespedes)
VALUES ('11111111-1', 1, '2026-06-01', '2026-06-03', 'CONFIRMADA', 90000.0, 2);
