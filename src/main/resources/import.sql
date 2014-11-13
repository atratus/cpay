insert into User (id, name) values (10,'Vasya');
insert into User (id, name) values (11,'Vova');
insert into User (id, name) values (12,'Kolya');

insert into PaymentEvent (id,title, creationtimestamp) values (10, 'Event First', CURRENT_TIMESTAMP);
insert into PaymentEvent (id,title, creationtimestamp) values (11, 'Event Second', CURRENT_TIMESTAMP);
insert into PaymentEvent (id,title, creationtimestamp) values (12, 'Event Third', CURRENT_TIMESTAMP);
insert into PaymentEvent (id,title, creationtimestamp) values (13, 'Event Fourth', CURRENT_TIMESTAMP);

-- select * from PaymentEvent