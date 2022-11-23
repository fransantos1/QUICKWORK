create table report_category (
    rc_id SERIAL not null,
    rc_name VARCHAR(255) not null,

    primary key (rc_id)

);

create table report(

    report_id SERIAL not null,
    reported_usr_id INT,
    report_comment VARCHAR(255),


    report_usr_id INT not null,
    report_rc_id INT not null,

    primary key (report_id)

);



create table usr(

    usr_id SERIAL not null,

    usr_name VARCHAR(255) not null,
    usr_email VARCHAR(255) not null,
    usr_password VARCHAR(255) not null,
    usr_njobs INT,
    usr_avg_rating smallINT not null check (usr_avg_rating between 1 and 5),
    usr_loc POINT,

    primary key (usr_id)	
);



create table usrwork ( 
    uw_id SERIAL not null,

    uw_usr_id INT not null,
    uw_work_id INT not null,

    primary key (uw_id)

);






create table work ( 
    work_id SERIAL not null,

    work_loc POINT not null,
    work_pricehr MONEY not null,
    work_tip MONEY,
    work_starting date,
    work_finished date,
    
    work_wt_id INT not null,
    primary key (work_id)

);




create table work_state ( 
    ws_id SERIAL not null,

    ws_work_id INT not null,
    ws_state_id INT not null,


    primary key (ws_id)
);





create table _state (

    state_id SERIAL not null,
    state_name VARCHAR(45) not null,
    primary key (state_id)
);


create table rating (
    rating_id SERIAL not null,
    rating_comment VARCHAR(255),
    rating_rat smallINT not null check (rating_rat between 1 and 5),

    rating_usr1_id INT not null, -- rating usr Id
    rating_usr2_id INT not null, -- Rated usr Id

    primary key (rating_id)
);



create table worktype (
    wt_id SERIAL not null,

    wt_name VARCHAR(250) not null,
    wt_avgprice_hr MONEY not null,    

    primary key (wt_id)
);




--Foreign Keys
alter table rating 
add constraINT rt_fk_usr1
foreign key (rating_usr1_id) references usr(usr_id)  
ON DELETE NO ACTION ON UPDATE NO ACTION;

alter table rating 
add constraINT rt_fk_usr2
foreign key (rating_usr2_id) references usr(usr_id)  
ON DELETE NO ACTION ON UPDATE NO ACTION;


alter table work_state
add constraINT ws_fk_work
foreign key (ws_work_id) references work(work_id)  
ON DELETE NO ACTION ON UPDATE NO ACTION;

alter table work_state
add constraINT ws_fk_state
foreign key (ws_state_id) references _state(state_id)  
ON DELETE NO ACTION ON UPDATE NO ACTION;


alter table work
add constraINT work_fk_worktype 
foreign key (work_wt_id) references worktype(wt_id)  
ON DELETE NO ACTION ON UPDATE NO ACTION;



alter table report 
add constraINT report_fk_rc
foreign key (report_rc_id) references report_category(rc_id) 
ON DELETE NO ACTION ON UPDATE NO ACTION;

alter table report 
add constraINT report_fk_reportedusr
foreign key (reported_usr_id) references usr(usr_id) 
ON DELETE NO ACTION ON UPDATE NO ACTION;

alter table usrwork
add constraINT uw_fk_usr 
foreign key (uw_usr_id) references usr(usr_id)  
ON DELETE NO ACTION ON UPDATE NO ACTION;

alter table usrwork
add constraINT uw_fk_work 
foreign key (uw_work_id) references work(work_id)  
ON DELETE NO ACTION ON UPDATE NO ACTION;

alter table report
add constraINT report_fk_usr 
foreign key (report_usr_id) references usr(usr_id)  
ON DELETE NO ACTION ON UPDATE NO ACTION;
