INSERT IGNORE INTO roles (nombre) VALUES ('CLIENTE');
INSERT IGNORE INTO roles (nombre) VALUES ('RECEPCIONISTA');
INSERT IGNORE INTO roles (nombre) VALUES ('ADMINISTRADOR');

INSERT IGNORE INTO usuarios (rut, nombre, email, password, rol_id, bloqueado, intentos_fallidos)
VALUES ('19123456-7', 'Juan Perez', 'juan@correo.com', '123456', 1, false, 0);

INSERT IGNORE INTO usuarios (rut, nombre, email, password, rol_id, bloqueado, intentos_fallidos)
VALUES ('9876543-2', 'Ana Gomez', 'ana@correo.com', 'admin123', 2, false, 0);

INSERT IGNORE INTO usuarios (rut, nombre, email, password, rol_id, bloqueado, intentos_fallidos)
VALUES ('11223344-5', 'Jefe Hotel', 'jefe@veranum.com', 'superclave', 3, false, 0);