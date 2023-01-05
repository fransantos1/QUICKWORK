--USED QUERIES SO FAR

--GIVE ALL JOBS ID, LOCATION AND TYPE
select work_id as id, work_loc[0] as lat, work_loc[1] as lon, wt_name as type
from work
inner join worktype on wt_id = work_wt_id
inner join work_state on work_id = ws_work_id
inner join _state on state_id = ws_state_id
where state_name = 'Em espera'

-- FIND A SPECEFIC WORKTYPE from work

select work_id as id, work_pricehr as pricehr, work_tip as tip, work_starting as started_time, work_finished as finished_time, work_loc[0] as lat, work_loc[1] as lon, wt_name as type
from work
inner join worktype on wt_id = work_wt_id
where work_id = :id

-- GET WORK FROM A WORKER ID THAT HE IS WORKING AT THE MOMENT

select work_id as id, work_pricehr as pricehr, work_tip as tip, work_starting as started_time, work_finished as finished_time, work_loc[0] as lat, work_loc[1] as lon, wt_name as type 
from work 
inner join usrwork on work_id = uw_work_id 
inner join work_state on ws_work_id = work_id 
inner join worktype on wt_id = work_wt_id 
where uw_usr_id = :user_id and ws_state_id != 4 and ws_state_id != 3 
order by work_id desc  
limit 1

--GET WORK FROM OWNER 

select work_id as id, work_pricehr as pricehr, work_tip as tip, work_starting as started_time, work_finished as finished_time, work_loc[0] as lat, work_loc[1] as lon, wt_name as type 
from work 
inner join usrwork on work_id = uw_work_id 
inner join work_state on ws_work_id = work_id 
inner join worktype on wt_id = work_wt_id 
where uw_usr_id = :user_id and ws_state_id != 4 and ws_state_id != 3 and uw_usrcreate is true 
order by work_id desc  
limit 1

--SAVE WORK
Insert into work (work_loc,  work_pricehr, work_tip, work_starting, work_finished, work_price, work_wt_id) values (POINT(:lat, :lon), :pricehr, null, null, null, null, :worktype_id);
insert into usrwork (uw_usr_id, uw_work_id, uw_usrcreate) values (:usrid ,:workid, true);
insert into work_state (ws_work_id, ws_state_id) values (:workid, 1);

--GET ALL USERS EXCEPT OWNER

select usr_id as id, usr_name as name, usr_email as email, usr_njobs as jobnumber, usr_avg_rating as rating 
from usr 
inner join usrwork on uw_usr_id = usr_id  
where uw_work_id = :workid and uw_usrcreate is not true

--modify state

insert into work_state (ws_work_id, ws_state_id) values (:workid, :work_state_id);

--get current state of work

select state_name as state
from work_state
inner join _state on ws_state_id = state_id
where ws_work_id = :workid
Order by ws_id DESC
limit 1

--get all states

select state_name
from _state

--accepting work

update work
set work_starting = :time
where work_id = :id;

insert into usrwork (uw_usr_id, uw_work_id, uw_usrcreate) values (:usrid ,:workid, false);

--cancel job 

delete from usrwork where uw_work_id = :workid ;
delete from work_state where ws_work_id = :workid ;
Delete from work where work_id = :workid ;

--finish Job

UPDATE work
SET work_finished = :time
WHERE work_id = :workid ;
insert into work_state (ws_work_id, ws_state_id) values ( :workid , 3);

--Users Jobs history

select work_id as id, work_pricehr as pricehr, work_tip as tip, work_starting as starting, work_finished as finished, work_wt_id as worktype_id 
from usr 
inner join usrwork on usr_id = uw_usr_id 
inner join work on uw_work_id = work_id 
Where usr_id=:id 

--get owner id

select usr_id as ownerid
from work
inner join usrwork on work_id = uw_work_id 
inner join usr on usr_id = uw_usr_id 
where uw_usrcreate = true and work_id =:workid

--verify is user is occupied

select exists (select state
from (select state_id as state 
from work_state 
inner join _state on ws_state_id = state_id inner join work on ws_work_id = work_id inner join usrwork on uw_work_id = work_id
where uw_usr_id = :id 
Order by ws_id DESC limit 1) subquerie
where state != 4 and state != 3)

--verify if the user is the owner (This function is only called if the "verify is user is occupied" function, returns true)

select uw_usrcreate
from usrwork
where uw_usr_id = :id order by uw_id desc limit 1

--get user location
select usr_loc[0] as lat, usr_loc[1] as lon from usr where usr_id = :usrid


--set user location
UPDATE usr
SET usr_loc = POINT(:lat, :lon)
where usr_id = :usrid

--add one jobto jobn
UPDATE usr
SET usr_njobs = :njob
where usr_id = :usrid




















