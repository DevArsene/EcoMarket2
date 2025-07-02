-- 1) Limpiar datos sin tocar la estructura
DELETE FROM usuarios;
DELETE FROM productos;
DELETE FROM roles;

-- 2) Insertar roles (ID autogenerado)
INSERT INTO roles (tipo) VALUES
  ('ADMINISTRADOR'),
  ('GERENTE'),
  ('EMPLEADO'),
  ('LOGISTICA');

-- 3) Insertar usuarios usando subconsulta para id_rol
INSERT INTO usuarios (run, nombre, apellido, correo, password, id_rol) VALUES
  ('12345678-9','María','González','maria@example.com','clave123',
     (SELECT id FROM roles WHERE tipo='GERENTE')
  ),
  ('98765432-1','Carlos','Rojas','carlos@example.com','pass456',
     (SELECT id FROM roles WHERE tipo='ADMINISTRADOR')
  ),
  ('45678912-3','Laura','Díaz','laura@example.com','segura789',
     (SELECT id FROM roles WHERE tipo='LOGISTICA')
  ),
  ('15975328-4','Andrés','Morales','andres@example.com','miClave321',
     (SELECT id FROM roles WHERE tipo='EMPLEADO')
  ),
  ('74185296-7','Sofía','Muñoz','sofia@example.com','admin2024',
     (SELECT id FROM roles WHERE tipo='GERENTE')
  );


-- Paso 6: Insertar productos
INSERT INTO productos (ean, nombre, descripcion, precio, fecha_vencimiento) VALUES
  ('5482739106247', 'Jabón artesanal', 'Aroma lavanda, hecho con aceites vegetales y sin químicos', 2990, '2026-08-15'),
  ('1035827491653', 'Cepillo de dientes de bambú', 'Mango biodegradable y cerdas suaves sin BPA', 3500, '2029-12-31'),
  ('9821374650912', 'Shampo sólido', 'Con ingredientes naturales, sin sulfatos ni envase plástico', 5490, '2026-05-10'),
  ('7364918203579', 'Bolsa reutilizable de algodón', 'Bolsa ecológica lavable, ideal para compras a granel', 4200, '2030-01-01'),
  ('6142098371546', 'Cera vegetal para velas', 'Cera de soja sin parafina, ideal para velas artesanales', 6700, '2027-11-20');