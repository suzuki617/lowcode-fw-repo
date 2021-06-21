CREATE TABLE Employee
(id    INTEGER    NOT NULL,
name   TEXT       NOT NULL,
email  TEXT       NOT NULL,
PRIMARY KEY (id));

truncate table Employee;
INSERT INTO Employee VALUES (1, 'suzuki', 'suzuki@test.co.jp');
