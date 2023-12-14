-- liquibase formatted sql

-- changeset kimlevchenko:1
create table notification_task
(
    task_id   serial primary key,
    chat_id bigint not null,
    text      text      not null,
    date_time timestamp not null
);