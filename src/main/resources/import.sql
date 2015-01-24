insert into UserAuthority (authority) values ('USER');

insert into User (id, name, password, enabled, authority) values (10,'Vasya', '123', true, 'USER');
insert into User (id, name, password, enabled, authority) values (11,'Vova', '123', true, 'USER');
insert into User (id, name, password, enabled, authority) values (12,'Kolya', '321', true, 'USER');

insert into PaymentEvent (id, eventType, title, creationtimestamp, totalValue) values (10, 'type A', 'Event First', CURRENT_TIMESTAMP, 0);
insert into PaymentEvent (id, eventType, title, creationtimestamp, totalValue) values (11, 'type A', 'Event Second', CURRENT_TIMESTAMP, 0);
insert into PaymentEvent (id, eventType, title, creationtimestamp, totalValue) values (12, 'type B', 'Event Third', CURRENT_TIMESTAMP, 0);
insert into PaymentEvent (id, eventType, title, creationtimestamp, totalValue) values (13, 'type C', 'Event Fourth', CURRENT_TIMESTAMP, 0);

insert into Payment(user_id, paymentEvent_id, paymentValue, debt) values (10, 10, 5.4, 0)

-- select count(payment0_.id) as col_0_0_ from Payment payment0_ group by payment0_.user_id
-- select * from PaymentEvent
-- select sum(paymentValue) from Payment