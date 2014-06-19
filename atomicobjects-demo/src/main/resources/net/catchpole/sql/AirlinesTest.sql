
create table aircraft (
    aircraft        VARCHAR(8) NOT NULL,
    cruise_speed    INTEGER NOT NULL,
    cruise_altitude INTEGER NOT NULL,
    range           INTEGER NOT NULL,
    PRIMARY KEY ( aircraft )
);

create table airline (
    code            CHAR(2) NOT NULL,
    name            VARCHAR(32) NOT NULL,
    PRIMARY KEY ( code )
);

create table fleet (
    airline         CHAR(2) NOT NULL,
    call_sign       VARCHAR(8) NOT NULL,
    aircraft        VARCHAR(8) NOT NULL,
    tacometer       INTEGER NOT NULL,
    PRIMARY KEY ( call_sign ),
    FOREIGN KEY ( airline ) REFERENCES airline ( code ),
    FOREIGN KEY ( aircraft ) REFERENCES aircraft ( aircraft )
);

create table classes (
    class           CHAR(1) NOT NULL,
    name            VARCHAR(16) NOT NULL,
    PRIMARY KEY (class)
);

create table seating (
    call_sign       VARCHAR(8) NOT NULL,
    class           CHAR(1) NOT NULL,
    rows            INTEGER NOT NULL,
    isles           INTEGER NOT NULL,
    PRIMARY KEY ( call_sign, class ),
    FOREIGN KEY ( call_sign ) REFERENCES fleet ( call_sign ),
    FOREIGN KEY ( class ) REFERENCES classes ( class )
);

create table airport (
    airport         CHAR(3) NOT NULL,
    name            VARCHAR(32) NOT NULL,
    latitude        FLOAT NOT NULL,
    longitude       FLOAT NOT NULL,
    gates           INTEGER NOT NULL,
    PRIMARY KEY ( airport )
);

create table schedule (
    airline         CHAR(2) NOT NULL,
    flight          INTEGER NOT NULL,
    call_sign       VARCHAR(8) NOT NULL,
    depart_airport  CHAR(3) NOT NULL,
    arrive_airport  CHAR(3) NOT NULL,
    depart_time     TIMESTAMP NOT NULL,
    arrive_time     TIMESTAMP NOT NULL,
    status          VARCHAR(12),
    PRIMARY KEY ( airline, flight ),
    FOREIGN KEY ( call_sign ) REFERENCES fleet ( call_sign ),
    FOREIGN KEY ( depart_airport ) REFERENCES airport ( airport ),
    FOREIGN KEY ( arrive_airport ) REFERENCES airport ( airport ),
    FOREIGN KEY ( airline ) REFERENCES airline ( code )
);

create table reservation (
    booking         INTEGER NOT NULL,
    airline         CHAR(2) NOT NULL,
    flight          INTEGER NOT NULL,
    class           CHAR(1) NOT NULL,
    row             INTEGER NOT NULL,
    isle            CHAR(1) NOT NULL,
    title           VARCHAR(12),
    passenger       VARCHAR(32) NOT NULL,
    PRIMARY KEY ( booking ),
    FOREIGN KEY ( airline, flight ) REFERENCES schedule( airline, flight ),
    FOREIGN KEY ( class ) REFERENCES classes( class ),
    FOREIGN KEY ( airline ) REFERENCES airline( code )
);

insert into aircraft (aircraft, cruise_speed, cruise_altitude, range) values ('B777', 905, 11000, 12200);
insert into aircraft (aircraft, cruise_speed, cruise_altitude, range) values ('B737', 780, 11000, 4200);
insert into aircraft (aircraft, cruise_speed, cruise_altitude, range) values ('B767', 851, 10600, 7300);
insert into aircraft (aircraft, cruise_speed, cruise_altitude, range) values ('A300', 829, 10600, 7540);

insert into airline (code, name) values ('CA', 'Catchpole Airlines');
insert into airline (code, name) values ('JV', 'Javajet');
insert into airline (code, name) values ('SW', 'Swampwreck Airlines');
insert into airline (code, name) values ('FA', 'Fullov Air');

insert into classes (class, name) values ('E', 'Economy Class');
insert into classes (class, name) values ('B', 'Business Class');
insert into classes (class, name) values ('F', 'First Class');

insert into fleet (airline, call_sign, aircraft, tacometer ) values ('CA', 'VH123', 'B737', 4725432);
insert into seating (call_sign, class, rows, isles) values ('VH123', 'E', 23, 6);
insert into seating (call_sign, class, rows, isles) values ('VH123', 'B', 8, 4);
insert into seating (call_sign, class, rows, isles) values ('VH123', 'F', 2, 2);

insert into fleet (airline, call_sign, aircraft, tacometer ) values ('CA', 'VH663', 'B767', 2843987);
insert into seating (call_sign, class, rows, isles) values ('VH663', 'E', 30, 6);
insert into seating (call_sign, class, rows, isles) values ('VH663', 'B', 8, 4);

insert into fleet (airline, call_sign, aircraft, tacometer ) values ('JV', 'VH444', 'B737', 7425432);
insert into seating (call_sign, class, rows, isles) values ('VH444', 'E', 34, 6);

insert into airport (airport, name, latitude, longitude, gates) values ('BNE', 'Brisbane', -25.335448, 135.745076, 30);
insert into airport (airport, name, latitude, longitude, gates) values ('SYD', 'Sydney', -27.46758, 153.027892, 60);
insert into airport (airport, name, latitude, longitude, gates) values ('MEL', 'Melbourne', -31.9554, 115.85859, 50);
insert into airport (airport, name, latitude, longitude, gates) values ('PER', 'Perth', -33.867139, 151.207114, 32);
insert into airport (airport, name, latitude, longitude, gates) values ('HBA', 'Hobart', -37.814251, 144.963169, 15);
insert into airport (airport, name, latitude, longitude, gates) values ('DRW', 'Darwin', -42.882743, 147.330234, 19);
insert into airport (airport, name, latitude, longitude, gates) values ('ADL', 'Adelaide', -12.461334,130.841904, 32);

insert into schedule (airline, flight, call_sign, depart_airport, arrive_airport, depart_time, arrive_time, status) values
    ('CA', 101, 'VH123', 'BNE', 'SYD', '2007-01-01 8:00', '2007-01-01 9:30', 'ON TIME');
insert into schedule (airline, flight, call_sign, depart_airport, arrive_airport, depart_time, arrive_time, status) values
    ('CA', 102, 'VH663', 'BNE', 'MEL', '2007-01-01 9:10', '2007-01-01 11:20', 'BOARDING');
insert into schedule (airline, flight, call_sign, depart_airport, arrive_airport, depart_time, arrive_time, status) values
    ('JV', 900, 'VH444', 'MEL', 'SYD', '2007-01-01 9:00', '2007-01-01 10:30', 'LANDED');

insert into reservation (booking, airline, flight, class, row, isle, title, passenger) values
    (1000, 'CA', 101, 'F', 4, 'B', 'Mrs', 'Minnie Banister');
insert into reservation (booking, airline, flight, class, row, isle, title, passenger) values
    (1001, 'CA', 102, 'E', 12, 'A', 'Mr', 'Henry Crun');
insert into reservation (booking, airline, flight, class, row, isle, title, passenger) values
    (1002, 'JV', 900, 'E', 1, 'D', 'Doctor', 'Love');
