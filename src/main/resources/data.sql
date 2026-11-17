insert into policies (type, status, canon, premium) values ('INDIVIDUAL', 'ACTIVA', 1000000.00, 150000.00);
insert into policies (type, status, canon, premium) values ('COLECTIVA', 'ACTIVA', 5000000.00, 800000.00);
insert into policies (type, status, canon, premium) values ('COLECTIVA', 'CANCELADA', 3000000.00, 450000.00);
insert into policies (type, status, canon, premium) values ('INDIVIDUAL', 'RENOVADA', 2000000.00, 300000.00);
insert into policies (type, status, canon, premium) values ('INDIVIDUAL', 'CANCELADA', 1500000.00, 220000.00);
insert into policies (type, status, canon, premium) values ('COLECTIVA', 'RENOVADA', 8000000.00, 1200000.00);

insert into risks (description, status, policy_id) values ('Apartamento 301', 'ACTIVO', 1);
insert into risks (description, status, policy_id) values ('Local comercial 12', 'ACTIVO', 2);
insert into risks (description, status, policy_id) values ('Bodega 4', 'ACTIVO', 2);
insert into risks (description, status, policy_id) values ('Oficina 502', 'CANCELADO', 3);
insert into risks (description, status, policy_id) values ('Casa campestre', 'ACTIVO', 4);
insert into risks (description, status, policy_id) values ('Apartamento 804', 'CANCELADO', 5);
insert into risks (description, status, policy_id) values ('Sede norte', 'ACTIVO', 6);
insert into risks (description, status, policy_id) values ('Sede sur', 'ACTIVO', 6);
insert into risks (description, status, policy_id) values ('Centro de distribución', 'ACTIVO', 6);
