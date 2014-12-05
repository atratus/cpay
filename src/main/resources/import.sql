insert into User (id, name) values (10,'Vasya');
insert into User (id, name) values (11,'Vova');
insert into User (id, name) values (12,'Kolya');

insert into PaymentEvent (id, title, creationtimestamp, totalValue) values (10, 'Event First', CURRENT_TIMESTAMP, 0);
insert into PaymentEvent (id, title, creationtimestamp, totalValue) values (11, 'Event Second', CURRENT_TIMESTAMP, 0);
insert into PaymentEvent (id, title, creationtimestamp, totalValue) values (12, 'Event Third', CURRENT_TIMESTAMP, 0);
insert into PaymentEvent (id, title, creationtimestamp, totalValue) values (13, 'Event Fourth', CURRENT_TIMESTAMP, 0);

insert into Payment(user_id, paymentEvent_id, paymentValue) values (10, 10, 5.4)

-- select * from PaymentEvent
-- select * from Payment