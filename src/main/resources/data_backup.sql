--rename this file to data.sql (auto loaded by Spring) to load some sample data
-- Spring recommends to use bcrypt over plain-text {noop}
insert into users(username, password, enabled) values ('supriyo','{bcrypt}$2a$10$nFpjDh.LYqR.O4HelAuNLOWaOhkNHPEEYJDuLFVkAlize5efa/BGq',1);
----values ('supriyo','{noop}abc123',1);
insert into users(username, password, enabled) values ('john','{bcrypt}$2a$10$nFpjDh.LYqR.O4HelAuNLOWaOhkNHPEEYJDuLFVkAlize5efa/BGq',1);
--values ('john','{noop}abc123',1);
insert into users(username, password, enabled) values ('marry','{bcrypt}$2a$10$nFpjDh.LYqR.O4HelAuNLOWaOhkNHPEEYJDuLFVkAlize5efa/BGq',1);
--values ('marry','{noop}abc123',1);

insert into authorities(username, authority) values('supriyo','ROLE_EMPLOYEE');
insert into authorities(username, authority) values('supriyo','ROLE_ADMIN');

insert into authorities(username, authority) values('john','ROLE_EMPLOYEE');

insert into authorities(username, authority) values('marry','ROLE_EMPLOYEE');
insert into authorities(username, authority) values('marry','ROLE_MANAGER');

insert into todo (ID, USERNAME, DESCRIPTION, TARGET_DATE, DONE) values(1001, 'Supriyo', 'Learn AWS', CURRENT_DATE(), false);
insert into todo (ID, USERNAME, DESCRIPTION, TARGET_DATE, DONE) values(1002, 'Supriyo', 'Learn Spring', CURRENT_DATE(), false);
insert into todo (ID, USERNAME, DESCRIPTION, TARGET_DATE, DONE) values(1003, 'Supriyo', 'Learn JPA', CURRENT_DATE(), false);
insert into todo (ID, USERNAME, DESCRIPTION, TARGET_DATE, DONE) values(1004, 'Supriyo', 'Learn Cloud', CURRENT_DATE(), false);
insert into todo (ID, USERNAME, DESCRIPTION, TARGET_DATE, DONE) values(1005, 'Supriyo', 'Learn ML', CURRENT_DATE(), false);