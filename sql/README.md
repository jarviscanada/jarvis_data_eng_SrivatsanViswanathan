# Introduction
Data Centre Hub is a Minimum Viable Product (MVP) that is used to
manage information about a community centre. It serves as a platform
for storing, organizing, and accessing data related to various
community operations like membership information, facility management,
and booking management. The product is built using PostgreSQL to store
the data and docker for hosting the database.

# SQL Queries

### Table Setup (DDL)

###### Members

```sql
CREATE TABLE cd.members (
	memid 		integer NOT NULL, 
	surname 	character varying(200) NOT NULL, 
	firstname 	character varying(200) NOT NULL, 
	address 	character varying(300) NOT NULL, 
	zipcode 	integer NOT NULL, 
	telephone 	character varying(20) NOT NULL, 
	recommendedby 	integer,
	joindate 	timestamp NOT NULL,
	CONSTRAINT 	members_pk PRIMARY KEY (memid),
	CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby)
	REFERENCES cd.members(memid) ON DELETE SET NULL
);
```

###### Facilities

```sql
CREATE TABLE cd.facilities (
	facid 			integer NOT NULL, 
	name 			character varying(100) NOT NULL, 
	membercost 		numeric NOT NULL, 
	guestcost 		numeric NOT NULL, 
	initialoutlay 		numeric NOT NULL, 
	monthlymaintenance	numeric NOT NULL, 
	CONSTRAINT facilities_pk PRIMARY KEY (facid)
);
```

###### Bookings

```sql
CREATE TABLE cd.bookings (
	bookid 		integer NOT NULL, 
	facid 		integer NOT NULL, 
	memid 		integer NOT NULL, 
	starttime 	timestamp NOT NULL,
	slots 		integer NOT NULL,
	CONSTRAINT bookings_pk PRIMARY KEY (bookid),
	CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid),
	CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
);
```

### Queries
###### Insert facility data
```sql
INSERT INTO cd.facilities (
  facid, name, membercost, guestcost, 
  initialoutlay, monthlymaintenance
) 
VALUES
  (9, 'Spa', 20, 30, 100000, 800)
```

###### Insert data
```sql
INSERT INTO cd.facilities (
  facid, name, membercost, guestcost, 
  initialoutlay, monthlymaintenance
) 
VALUES
  (9, 'Spa', 20, 30, 100000, 800)
```

###### Insert calculated data
```sql
INSERT INTO cd.facilities (
  facid, name, membercost, guestcost, 
  initialoutlay, monthlymaintenance
) 
SELECT 
  (
    SELECT 
      max(facid) 
    FROM 
      cd.facilities
  )+ 1, 
  'Spa', 
  20, 
  30, 
  100000, 
  800;
```

###### Update Data
```sql
UPDATE
  cd.facilities 
SET
  initialoutlay = 10000 
WHERE 
  facid = 1
```

###### Update a row based off another row
```sql
UPDATE 
  cd.facilities 
SET 
  membercost = (
    SELECT 
      membercost * 1.1 
    FROM 
      cd.facilities 
    WHERE 
      facid = 0
  ), 
  guestcost = (
    SELECT 
      guestcost * 1.1 
    FROM 
      cd.facilities 
    WHERE 
      facid = 0
  ) 
WHERE 
  facid = 1;
```

###### Delete all bookings
```sql
DELETE FROM 
  cd.bookings;
```

###### Delete a member
```sql
DELETE FROM 
  cd.members 
WHERE 
  memid = 37;
```

###### Produce a list of facilities where the member cost is more than 0 and is less than 1/50th of the monthly maintenance cost
```sql
SELECT 
  facid, 
  name, 
  membercost, 
  monthlymaintenance 
FROM 
  cd.facilities 
WHERE 
  membercost > 0 
  AND membercost < (monthlymaintenance / 50);
```

###### All facilities with Tennis in their name
```sql
SELECT 
  * 
FROM 
  cd.facilities 
WHERE 
  name LIKE '%Tennis%';
```

###### Retrieve details of facilities with ID 1 and 5
```sql
SELECT 
  * 
FROM 
  cd.facilities 
WHERE 
  facid IN (1, 5);
```

###### Produce a list of members who joined after September 2012
```sql
SELECT 
  memid, 
  surname, 
  firstname, 
  joindate 
FROM 
  cd.members 
WHERE 
  joindate >= '2012-09-01';
```

###### Combine surname and facility names
```sql
SELECT 
  surname 
FROM 
  cd.members 
UNION 
SELECT 
  name 
FROM 
  cd.facilities;
```

###### Produce a list of start times for bookings by members named 'David Farrel'
```sql
SELECT 
  starttime 
FROM 
  cd.bookings 
  INNER JOIN cd.members on cd.bookings.memid = cd.members.memid 
WHERE 
  cd.members.firstname = 'David' 
  AND cd.members.surname = 'Farrell';
```

###### Produce a list of start times for bookings of tennis courts for 2012-09-21
```sql
SELECT 
  starttime, 
  name 
FROM 
  cd.bookings 
  INNER JOIN cd.facilities ON cd.bookings.facid = cd.facilities.facid 
WHERE 
  cd.facilities.name LIKE '%Tennis Court%' 
  AND cd.bookings.starttime >= '2012-09-21' 
  AND cd.bookings.starttime < '2012-09-22' 
ORDER BY 
  starttime;
```

###### Produce a list of all members along with their recommender
```sql
 SELECT 
  cd.members.firstname as memfname, 
  cd.members.surname as memsname, 
  recs.firstname as recfname, 
  recs.surname as recsname 
FROM 
  cd.members 
  LEFT OUTER JOIN cd.members recs ON recs.memid = cd.members.recommendedBy 
ORDER BY 
  cd.members.surname, 
  cd.members.firstname;
```

###### Produce a list of all members who have recommended another member
```sql
SELECT 
  DISTINCT recs.firstname, 
  recs.surname 
FROM 
  cd.members 
  INNER JOIN cd.members recs ON recs.memid = cd.members.recommendedBy 
ORDER BY 
  recs.surname, 
  recs.firstname;
```

###### Produce a list of all members along with their recommender
```sql
SELECT 
  distinct cd.members.firstname || ' ' || cd.members.surname as member, 
  (
    SELECT 
      recs.firstname || ' ' || recs.surname as recommender 
    FROM 
      cd.members recs 
    WHERE 
      recs.memid = cd.members.recommendedBy
  ) 
FROM 
  cd.members 
ORDER BY 
  member;
```

###### Count the number of recommendations each member makes
```sql
SELECT 
  recommendedby, 
  COUNT(*) as count 
FROM 
  cd.members 
WHERE 
  recommendedBy IS NOT NULL 
GROUP BY 
  recommendedBy 
ORDER BY 
  recommendedBy;
```

###### Produce a list of the total slots booked per facility
```sql
SELECT 
  facid, 
  SUM(slots) as "Total Slots" 
FROM 
  cd.bookings 
GROUP BY 
  facid 
ORDER BY 
  facid;
```

###### Produce a list of the total slots booked per facility on September 2012
```sql
SELECT 
  facid, 
  SUM(slots) 
FROM 
  cd.bookings 
WHERE 
  starttime > '2012-09-01' 
  AND starttime < '2012-10-01' 
GROUP BY 
  facid 
ORDER BY 
  sum(slots);
```

###### Produce a list of the total slots booked per facility per month
```sql
SELECT 
  facid, 
  extract(
    month 
    from 
      starttime
  ) as month, 
  sum(slots) as "Total Slots" 
from 
  cd.bookings 
WHERE 
  extract(
    year 
    from 
      starttime
  ) = '2012' 
GROUP BY 
  facid, 
  month 
ORDER BY 
  facid, 
  month;
```

###### Find the total number of members who have made at least one booking
```sql
SELECT 
  COUNT(DISTINCT memid) 
FROM 
  cd.bookings
```

###### List each member's first booking after September 1st 2012
```sql
SELECT 
  cd.members.surname, 
  cd.members.firstname, 
  cd.members.memid, 
  min(cd.bookings.starttime) as starttime 
from 
  cd.members 
  INNER JOIN cd.bookings ON cd.members.memid = cd.bookings.memid 
WHERE 
  starttime > '2012-09-01' 
GROUP BY 
  cd.members.surname, 
  cd.members.firstname, 
  cd.members.memid 
ORDER BY 
  memid;
```

###### Produce a list of members with each row containing the total member count
```sql
SELECT 
  (
    SELECT 
      COUNT(*) 
    FROM 
      cd.members
  ) as count, 
  firstname, 
  surname 
FROM 
  cd.members 
ORDER BY 
  joindate;
```

###### Produce a numbered list of members
```sql
SELECT 
  (
    SELECT 
      COUNT(*) 
    FROM 
      cd.members
  ) as count, 
  firstname, 
  surname 
FROM 
  cd.members 
ORDER BY 
  joindate;
```

###### Output the facility ID that has the highest number of slots booked
```sql
SELECT
  facid, 
  total 
FROM 
  (
    SELECT 
      facid, 
      sum(slots) total, 
      rank() over (
        order by 
          sum(slots) desc
      ) rank 
    FROM 
      cd.bookings 
    GROUP BY 
      facid
  ) AS ranked 
WHERE 
  rank = 1;
```

###### Output the names of all members formatted as 'Surname, Firstname'
```sql
SELECT 
  surname || ', ' || firstname as name 
from 
  cd.members;
```

###### Produce a list of telephone numbers with parentheses
```sql
SELECT 
  memid, 
  telephone 
FROM 
  cd.members 
WHERE 
  telephone ~ '[()]';
```

###### Count the number of members whose surname starts with each letter of the alphabet
```sql
SELECT 
  substr (cd.members.surname, 1, 1) as letter, 
  count(*) as count 
FROM 
  cd.members 
GROUP BY 
  letter 
ORDER BY 
  letter;
```