create table timestamps
(
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    check (false) no inherit --prohibits insertion into the table
);

create table authority
(
    id    serial primary key,
    title varchar(25) unique
) inherits (timestamps);

create table _role
(
    id    serial primary key,
    title varchar(25) unique
) inherits (timestamps);

create table role_authority
(
    auth_id int references authority (id),
    role_id int references _role (id)
) inherits (timestamps);

create table person
(
    id       bigserial primary key,
    email    varchar(50) unique not null,
    password varchar(100)        not null
) inherits (timestamps);

create table person_role
(
    person_id bigint references person (id),
    role_id   int references _role (id)
) inherits (timestamps);


insert into authority (title)
values ('READ'),
       ('EDIT_PRODUCT_DESCRIPTION'),
       ('ADD_DISCOUNT'),
       ('EDIT_ROLE');

insert into _role(title)
values ('CLIENT'),
       ('EMPLOYEE'),
       ('MANAGER'),
       ('ADMIN');

insert into person (email, password)
values ('admin@mail.com', '$2a$12$cTDNtlUJwrus14UUey65W.f92vcVAA4wI0rt/WZWs7dCu/4UZ70WG'),
       ('employee@mail.com', '$2a$12$cTDNtlUJwrus14UUey65W.f92vcVAA4wI0rt/WZWs7dCu/4UZ70WG'),
       ('manager@mail.com', '$2a$12$cTDNtlUJwrus14UUey65W.f92vcVAA4wI0rt/WZWs7dCu/4UZ70WG');

insert into role_authority (role_id, auth_id)
values (4, 1),
       (4, 4),
       (2, 1),
       (2, 2),
       (3, 1),
       (3, 2),
       (3, 3);

insert into person_role(person_id, role_id)
values (1, 4),
       (2, 1);