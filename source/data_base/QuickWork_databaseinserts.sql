    create table User (
    user_id not null;
    user_name VARCHAR(255) not null;
    user_email VARCHAR(255) not null;
    user_password VARCHAR(255) not null;
    user_njobs INT;
    user_avg_rating INT(10);

    user_rating_id INT not null;
    user_lj_id INT not null;
    user_cw_id INT;
    user_loc_id INT;


    primary key (user_id)	
);

create table ratings (
    rating_id not null;
    rating_comment VARCHAR(255);
    rating_rat INT(10) not null;

    rating_user1_id INT not null; -- rating User Id
    rating_user2_id INT not null; -- Rated User Id

    primary key (rating_id)
);

create table Last_Jobs (
    lj _id not null;
    lj_price DECIMAL(10,2) not null;
    lj_time TIME not null;


    primary key (lj_id)
);

create table Current_work (
    cw_id not null;
    cw_price DECIMAL(10,2) not null;
    cw_tip INT;

    cw_loc_id INT not null;
    cw_user1_id INT not null; -- client
    cw_user2_id INT not null; -- worker
    primary key (cw_id)
	
);
create table Work_type (
    wt_name VARCHAR(250) not null;
    wt_avgprice_hr DECIMAL(10,2) not null;    

    primary key (wt_id)
);  

create table Localization(
    loc_id not null;
    loc_lag DECIMAL(12,9) not null;
    loc_lat DECIMAL(12,9) not null;
    primary key (loc_id);
);



--Foreign Keys


            
alter table rating 
add constraint rating_fk_usercontratador
foreign key (rating_user1_id) references user(user_id) 
ON DELETE NO ACTION ON UPDATE NO ACTION;
            
alter table Last_Jobs
add constraint studyplan_fk_course
foreign key (plan_cour_id) references course(cour_id) 
ON DELETE NO ACTION ON UPDATE NO ACTION;            
            
alter table Current_work
add constraint studyplan_fk_class
foreign key (plan_cla_id) references class(cla_id) 
ON DELETE NO ACTION ON UPDATE NO ACTION;            

alter table Work_type 
add constraint enrollment_fk_student
foreign key (enr_stud_id) references student(stu_id) 
ON DELETE NO ACTION ON UPDATE NO ACTION; 
                        
alter table Localization
add constraint enrollment_fk_studyplan
foreign key (enr_plan_id) references studyplan(plan_id) 
ON DELETE NO ACTION ON UPDATE NO ACTION; 

