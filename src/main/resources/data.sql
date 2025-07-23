INSERT INTO roles (name)
VALUES ('admin'),
       ('operator'),
       ('analyst'),
       ('guest');

INSERT INTO users (username, name, password)
VALUES ('ivan_admin', 'Ivan Ivanov', 'AdminPass123'),
       ('maria_operator', 'Maria Petrova', 'OperatorPass456'),
       ('sergey_analyst', 'Sergey Sidorov', 'AnalystPass789'),
       ('anna_guest', 'Anna Smirnova', 'GuestPass000');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (3, 3),
       (3, 2),
       (4, 4);