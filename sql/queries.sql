-- Modifying Data

-- https://pgexercises.com/questions/updates/insert.html
insert into cd.facilities (
  facid, name, membercost, guestcost, 
  initialoutlay, monthlymaintenance
) 
values 
  (9, 'Spa', 20, 30, 100000, 800)

-- https://pgexercises.com/questions/updates/insert3.html
insert into cd.facilities (
  facid, name, membercost, guestcost, 
  initialoutlay, monthlymaintenance
) 
select 
  (
    select 
      max(facid) 
    from 
      cd.facilities
  )+ 1, 
  'Spa', 
  20, 
  30, 
  100000, 
  800;

-- https://pgexercises.com/questions/updates/update.html
update 
  cd.facilities 
set 
  initialoutlay = 10000 
where 
  facid = 1

-- https://pgexercises.com/questions/updates/updatecalculated.html
update 
  cd.facilities 
set 
  membercost = (
    select 
      membercost * 1.1 
    from 
      cd.facilities 
    where 
      facid = 0
  ), 
  guestcost = (
    select 
      guestcost * 1.1 
    from 
      cd.facilities 
    where 
      facid = 0
  ) 
where 
  facid = 1;

-- https://pgexercises.com/questions/updates/delete.html 
delete from 
  cd.bookings;

-- https://pgexercises.com/questions/updates/deletewh.html
delete from 
  cd.members 
where 
  memid = 37;

-- Basics

-- https://pgexercises.com/questions/basic/where2.html
SELECT 
  facid, 
  name, 
  membercost, 
  monthlymaintenance 
FROM 
  cd.facilities 
WHERE 
  membercost > 0 
  AND membercost < monthlymaintenance / 50;

-- https://pgexercises.com/questions/basic/where3.html
SELECT 
  * 
FROM 
  cd.facilities 
WHERE 
  name like '%Tennis%';

-- https://pgexercises.com/questions/basic/where4.html
SELECT 
  * 
FROM 
  cd.facilities 
WHERE 
  facid IN (1, 5);

-- https://pgexercises.com/questions/basic/date.html
SELECT 
  memid, 
  surname, 
  firstname, 
  joindate 
FROM 
  cd.members 
WHERE 
  joindate >= '2012-09-1';

-- https://pgexercises.com/questions/basic/union.html
SELECT 
  surname 
FROM 
  cd.members 
UNION 
SELECT 
  name 
FROM 
  cd.facilities;

-- Join

-- https://pgexercises.com/questions/joins/simplejoin.html
SELECT 
  starttime 
FROM 
  cd.bookings 
  INNER JOIN cd.members on cd.bookings.memid = cd.members.memid 
WHERE 
  cd.members.firstname = 'David' 
  AND cd.members.surname = 'Farrell';
 
 -- https://pgexercises.com/questions/joins/simplejoin2.html
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

 -- https://pgexercises.com/questions/joins/self2.html
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

-- https://pgexercises.com/questions/joins/self.html
SELECT 
  DISTINCT recs.firstname, 
  recs.surname 
FROM 
  cd.members 
  INNER JOIN cd.members recs ON recs.memid = cd.members.recommendedBy 
ORDER BY 
  recs.surname, 
  recs.firstname;

-- https://pgexercises.com/questions/joins/sub.html
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

-- Aggregation

-- https://pgexercises.com/questions/aggregates/count3.html
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

-- https://pgexercises.com/questions/aggregates/fachours.html
SELECT 
  facid, 
  SUM(slots) as "Total Slots" 
FROM 
  cd.bookings 
GROUP BY 
  facid 
ORDER BY 
  facid;

-- https://pgexercises.com/questions/aggregates/fachoursbymonth.html
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

-- https://pgexercises.com/questions/aggregates/fachoursbymonth2.html
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

-- https://pgexercises.com/questions/aggregates/members1.html
SELECT 
  COUNT(DISTINCT memid) 
FROM 
  cd.bookings

-- https://pgexercises.com/questions/aggregates/nbooking.html
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

-- https://pgexercises.com/questions/aggregates/countmembers.html
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

-- https://pgexercises.com/questions/aggregates/nummembers.html
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

-- https://pgexercises.com/questions/aggregates/fachours4.html
select 
  facid, 
  total 
from 
  (
    select 
      facid, 
      sum(slots) total, 
      rank() over (
        order by 
          sum(slots) desc
      ) rank 
    from 
      cd.bookings 
    group by 
      facid
  ) as ranked 
where 
  rank = 1;

-- https://pgexercises.com/questions/string/concat.html
SELECT 
  surname || ', ' || firstname as name 
from 
  cd.members;

-- https://pgexercises.com/questions/string/reg.html
SELECT 
  memid, 
  telephone 
FROM 
  cd.members 
WHERE 
  telephone ~ '[()]';

-- https://pgexercises.com/questions/string/substr.html
SELECT 
  substr (cd.members.surname, 1, 1) as letter, 
  count(*) as count 
from 
  cd.members 
GROUP BY 
  letter 
ORDER BY 
  letter;










 
 


 
 