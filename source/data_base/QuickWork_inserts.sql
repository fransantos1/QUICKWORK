--report category

insert into report_category (rc_name) values ('bug');
insert into report_category (rc_name) values ('user misbehaviour');
insert into report_category (rc_name) values ('feature request');
--estados 

Insert into _state(state_name) values ('Em espera');
Insert into _state(state_name) values ('Em andamento');
Insert into _state(state_name) values ('Completo');
Insert into _state(state_name) values ('Cancelado');

-- tipos de trabalho


Insert into worktype (wt_name, wt_avgprice_hr) values ('Limpeza', 10);
Insert into worktype (wt_name, wt_avgprice_hr) values ('Montagem móveis', 25);
Insert into worktype (wt_name, wt_avgprice_hr) values ('Montagem eletrodomesticos', 5);
Insert into worktype (wt_name, wt_avgprice_hr) values ('Ajuda geral', 15);



-- users

Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Luis Jose Maria','ljm@sapo.pt','Minhapass123',84,3); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Florinda Regulo','flr_re@gmail.com','Minhapass123',4,2); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Miguel Andrea','Miguel.Andre@apple.com','Minhapass123',45,1); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Teo Avelino','teoA@iade.pt','Minhapass123',1,4); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Apolonia Casimiro','AC@gmail.com','Minhapass123',6,5); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Ronaldo Walter','WalterRonald@hotmail.com','Minhapass123',41,4); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Luisinho Ines','Luis@meo.pt','Minhapass123',17,1 ); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Rita Celso','Ritacel@microsoft.com','Minhapass123',47,3); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Urbano Tao','UT@outlook.com','Minhapass123',2,4); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Doris Ze Manel','DZM@gmail.com','Minhapass123',10,1); 




-- availabel jobs example (will show up on jobs list view) 
Insert into work (work_loc,  work_pricehr, work_tip, work_starting, work_finished, work_price, work_wt_id) values (POINT(38.7115487417554, -9.159549968757467), 23, null, null, null, null, 2);
Insert into work (work_loc,  work_pricehr, work_tip, work_starting, work_finished, work_price, work_wt_id) values (POINT(38.63038872078478, -8.932668044543384), 15, null, null, null, null, 4);
Insert into work (work_loc,  work_pricehr, work_tip, work_starting, work_finished, work_price, work_wt_id) values (POINT(38.81601036752521, -9.357701600445369), 4, null, null, null, null, 4);
Insert into work (work_loc,  work_pricehr, work_tip, work_starting, work_finished, work_price, work_wt_id) values (POINT(38.834198338681375, -8.407384246660511), 7, null, null, null, null, 3);
Insert into work (work_loc,  work_pricehr, work_tip, work_starting, work_finished, work_price, work_wt_id) values (POINT(40.40706494789366, -8.599644983780125), 8, null, null, null, null, 3);

insert into usrwork (uw_usr_id, uw_work_id, uw_usrcreate) values (1 ,1,true );
insert into work_state (ws_work_id, ws_state_id) values (1, 1);
insert into usrwork (uw_usr_id, uw_work_id, uw_usrcreate) values (10 ,2,true );
insert into work_state (ws_work_id, ws_state_id) values (2, 1);
insert into usrwork (uw_usr_id, uw_work_id, uw_usrcreate) values (8 ,3,true );
insert into work_state (ws_work_id, ws_state_id) values (3, 1);
insert into usrwork (uw_usr_id, uw_work_id, uw_usrcreate) values (2 ,4,true );
insert into work_state (ws_work_id, ws_state_id) values (4, 1);
insert into usrwork (uw_usr_id, uw_work_id, uw_usrcreate) values (6 ,5,true );
insert into work_state (ws_work_id, ws_state_id) values (5, 1);


-- non available jobs example (will NOT show up on jobs list view) 
Insert into work (work_loc,  work_pricehr, work_tip, work_starting, work_finished, work_price, work_wt_id) values (POINT(38.891941069284734, -9.096776322628058), 7, null, '2022-08-14', null, null, 3);
insert into usrwork (uw_usr_id, uw_work_id, uw_usrcreate) values (5 ,6,true );
insert into usrwork (uw_usr_id, uw_work_id, uw_usrcreate) values (9 ,6,false );
insert into usrwork (uw_usr_id, uw_work_id, uw_usrcreate) values (7 ,6,false );
insert into work_state (ws_work_id, ws_state_id) values (6, 2);


-- reports 

insert into report (report_comment, report_usr_id, report_rc_id) values ('example bug', 4, 1);



--- coments
--cant have comments because there is no finished work
-- we didnt test comments feature yet so we havent done this 
 