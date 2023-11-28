/* ENSF 480 F23 Project Database
 
 Each time this file is executed, it will reset the database to the original state defined below.
 
 */

DROP DATABASE IF EXISTS SKYWARD_BOUND;
CREATE DATABASE SKYWARD_BOUND; 
USE SKYWARD_BOUND;


DROP TABLE IF EXISTS FLIGHTS;
CREATE TABLE FLIGHTS (
    FlightID			int not null AUTO_INCREMENT,
	FlightNum		varchar(6),
    FlightDate      varchar(25),    
	Origin			varchar(3),
	Destination		varchar(3),
	Aircraft	    varchar(25),
	DepartureTime	varchar(25),
	ArrivalTime		varchar(25),
	FlightTime		varchar(25),
	primary key (FlightID)
);

INSERT INTO FLIGHTS (FlightNum, FlightDate, Origin, Destination, Aircraft, DepartureTime, ArrivalTime, FlightTime)
VALUES
('SB-617', '2023-12-01','YYC', 'YYZ', 'Boeing 747', '8:32PM', '12:22AM', '3h 50m'),
('SB-233', '2023-12-02','LAX', 'JFK', 'Airbus A380', '6:09AM', '2:35PM', '5h 26m'),
('SB-019', '2023-12-03','YVR', 'LAS', 'Airbus A320', '9:05AM', '11:53AM', '2h 48m'),
('SB-793', '2023-12-04','NYC', 'YYZ', 'Boeing 737', '8:30AM', '10:14AM', '1h 44m'),
('SB-549', '2023-12-05','LAS', 'YYC', 'Airbus A380', '6:45PM', '10:42PM', '2h 57m');



DROP TABLE IF EXISTS AIRCRAFT;
CREATE TABLE AIRCRAFT(
	AircraftID			int not null AUTO_INCREMENT,
	Name			varchar(50),
	Capacity	    int,
	primary key (AircraftID)
);

INSERT INTO AIRCRAFT (Name, Capacity)
VALUES
('Airbus A380', 100),
('Airbus A320', 80),
('Boeing 737', 90),
('Boeing 747', 120);


DROP TABLE IF EXISTS LOCATIONS;
CREATE TABLE LOCATIONS(
    LocationID			int not null AUTO_INCREMENT,
    AirportName         varchar(3),
    City                varchar(25),
    Country             varchar(50),
    primary key(LocationID) 
);

INSERT INTO LOCATIONS(AirportName, City, Country)
VALUES
('YYC', 'Calgary', 'Canada'),
('YYZ', 'Toronto', 'Canada'),
('YVR', 'Vancouver', 'Canada'),
('LAX', 'Los Angeles', 'USA'),
('LAS', 'Las Vegas', 'USA'),
('JFK', 'New York City', 'USA');

DROP TABLE IF EXISTS CREWMEMBER;
CREATE TABLE CREWMEMBER(
    CrewID			int not null AUTO_INCREMENT,
    FName           varchar(25),
    LName           varchar(25),
    Email           varchar(25),
    Job             varchar(25),
    HouseNum        int,
    Street          varchar(25),
    City            varchar(25),
    Country         varchar(50),
    PostalCode      varchar(7),
    primary key(CrewID) 
);


INSERT INTO CREWMEMBER(FName, LName, Email, Job, HouseNum, Street, City, Country, PostalCode)
VALUES
('Chandler', 'Bing','cbing@gmail.com', 'pilot', 22, 'Jump Street', 'New York City', 'USA', 'VPB-176'),
('Rachel', 'Green', 'rachelgreen12@gmail.com','crew', 45, 'Madison Avenue', 'Pittsburgh', 'USA', 'WXS-192'),
('Monica', 'Geller', 'mgeller@hotmail.com','crew', 92, 'Circle Drive', 'Saskatoon', 'Canada', 'SSK-189'),
('Phoebe', 'Buffay', 'phoebe1234@yahoo.ca','pilot', 111, '32nd Avenue', 'Montreal', 'Canada', 'SPM-232');


DROP TABLE IF EXISTS REGISTEREDUSERS;
CREATE TABLE REGISTEREDUSERS(
    UserID			int not null AUTO_INCREMENT,
    FName           varchar(25),
    LName           varchar(25),
    Email           varchar(25),
    Password        varchar(25), 
    HouseNum        int,
    Street          varchar(25),
    City            varchar(25),
    Country         varchar(50),
    PostalCode      varchar(7),
    CreditCardNumber    varchar(50),
    CVV                 int,
    primary key(UserID) 
);
/*
could have email as pk
*/

INSERT INTO REGISTEREDUSERS(FName, LName, Email, Password, HouseNum, Street, City, Country, PostalCode, CreditCardNumber, CVV)
VALUES
('Roy', 'Kent','roykent@gmail.com','soccer123', 819, 'Paved Court', 'London', 'England', 'KLM-352','10987654321', 201),
('Ted', 'Lasso','tedl12@gmail.com','football', 434, 'Wembley Road', 'Kansas', 'USA', 'IHV-164', '02468101214', 176),
('John', 'Smith', 'johnsmith@gmail.com', 'pass', 1, 'Maple Street', 'Calgary', 'Canada', 'TMS-257', '12345678910', 123);


DROP TABLE IF EXISTS TICKETS;
CREATE TABLE TICKETS(
    TicketID			int not null AUTO_INCREMENT,
    seatNum                   int,
    price                     int,
    FlightNumber         varchar(6),
    ClientEmail          varchar(25),
    FName           varchar(25),
    LName           varchar(25),
    primary key(TicketID) 
);

-- DROP TABLE IF EXISTS PAYMENT;
-- CREATE TABLE PAYMENT(
--     PaymentID			int not null AUTO_INCREMENT,
--     CreditCardNumber    varchar(50),
--     CVV                 int,
--     Email               varchar(25),
--     primary key(PaymentID) 
-- ); 

-- INSERT INTO PAYMENT(CreditCardNumber,CVV, Email)
-- VALUES
-- ('10987654321', 201,'roykent@gmail.com'),
-- ('02468101214', 176,'tedl12@gmail.com'),
-- ('12345678910', 123,'johnsmith@gmail.com');


/*
maybe need a crew assignment to flight db?? idk could maybe get around it by just using crew arraylist after it is populated and passing around those objects
*/
