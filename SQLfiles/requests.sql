CREATE TABLE Requests (
	user_id	bigint not NULL,
	requestText varchar(1024) not NULL,
	requestDate date not NULL,
	requestTime time not NULL
) PARTITION BY RANGE (requestDate);

CREATE TABLE Requests_y2020m1 PARTITION OF Requests FOR VALUES FROM ('2020-01-01') TO ('2020-02-01');
CREATE TABLE Requests_y2020m2 PARTITION OF Requests FOR VALUES FROM ('2020-02-01') TO ('2020-03-01');
CREATE TABLE Requests_y2020m3 PARTITION OF Requests FOR VALUES FROM ('2020-03-01') TO ('2020-04-01');
CREATE TABLE Requests_y2020m4 PARTITION OF Requests FOR VALUES FROM ('2020-04-01') TO ('2020-05-01');
CREATE TABLE Requests_y2020m5 PARTITION OF Requests FOR VALUES FROM ('2020-05-01') TO ('2020-06-01');

CREATE INDEX ON Requests_y2020m1 (requestDate);
CREATE INDEX ON Requests_y2020m2 (requestDate);
CREATE INDEX ON Requests_y2020m3 (requestDate);
CREATE INDEX ON Requests_y2020m4 (requestDate);
CREATE INDEX ON Requests_y2020m5 (requestDate);

CREATE INDEX ON Requests (user_id);
CREATE INDEX ON Requests (requestDate);

INSERT INTO Requests VALUES(1, 'ololo!', '2020-01-01', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-01-02', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-01-03', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-01-04', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-01-05', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-02-01', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-02-02', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-02-03', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-02-04', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-02-05', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-03-01', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-03-02', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-03-03', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-03-04', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-03-05', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-04-01', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-04-02', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-04-03', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-04-04', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-04-05', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-05-01', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-05-02', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-05-03', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-05-04', '18:31:11');
INSERT INTO Requests VALUES(1, 'ololo!', '2020-05-05', '18:31:11');

SELECT * FROM Requests

SELECT * FROM Requests_y2020m3

ALTER TABLE Requests DETACH PARTITION Requests_y2020m1;

CREATE TABLE Requests_y2020m6 (LIKE Requests INCLUDING DEFAULTS INCLUDING CONSTRAINTS);
ALTER TABLE Requests_y2020m6 ADD CONSTRAINT y2020m6
   CHECK ( requestDate >= DATE '2020-06-01' AND requestDate < DATE '2020-07-01' );
ALTER TABLE Requests ATTACH PARTITION Requests_y2020m6
    FOR VALUES FROM ('2020-06-01') TO ('2020-07-01');
CREATE INDEX ON Requests_y2020m6 (requestDate);

