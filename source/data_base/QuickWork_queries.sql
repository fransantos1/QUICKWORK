--USED QUERIES SO FAR


-- FIND A USERS WORK HISTORY

select work_id as id, work_pricehr as pricehr, work_tip as tip, work_starting as starting, work_finished as finished, work_wt_id as workstate_id
from usr
inner join usrwork on usr_id = uw_usr_id
inner join work on uw_work_id = work_id  
Where usr_id=:id


--FIND A WORK'S CREATOR ID 

select usr_id as ownerid 
from work 
inner join usrwork on work_id = uw_work_id 
inner join usr on usr_id = uw_usr_id 
where uw_usrcreate = true and work_id =:workid

--GIVE ALL JOBS ID, LOCATION AND TYPE
select work_id as id, work_loc[0] as lat, work_loc[1] as lon, wt_name as type 
from work 
inner join worktype on work_wt_id = wt_id 
where work_starting is null 


-- MODIFY A JOBS STATE 

Update work 
set work_wt_id = :state 
where work_id = :id

-- FIND A SPECEFIC WORK WORKTYPE ID 

Select wt_name as name from worktype where wt_id = :id


--THIS WILL CHANGE FOR SURE IN THE FUTURE