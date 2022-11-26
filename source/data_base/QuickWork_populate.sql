


insert into report


insert into rating



-- 1st job example 
Insert into work (work_loc,  work_pricehr, work_tip, work_starting, work_finished, work_price, work_wt_id) values (POINT(38.7115487417554, -9.159549968757467), 23, null, null, null, null, 2)
insert into usrwork (uw_usr_id, uw_work_id, uw_usrcreate) values (14 ,1,true )
insert into usrwork (uw_usr_id, uw_work_id, uw_usrcreate) values (16 ,1, false )
insert into work_state (ws_work_id, ws_state_id) values (1, 2)




Insert into _state(state_name) values ('Em espera');
Insert into _state(state_name) values ('Em andamento');
Insert into _state(state_name) values ('Completo');
Insert into _state(state_name) values ('Cancelado');



----------------------------------------


Insert into worktype (wt_name, wt_avgprice_hr) values ('Limpeza', 10);
Insert into worktype (wt_name, wt_avgprice_hr) values ('Montagem móveis', 25);
Insert into worktype (wt_name, wt_avgprice_hr) values ('Montagem eletrodomesticos', 5);
Insert into worktype (wt_name, wt_avgprice_hr) values ('Ajuda geral', 15);


Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Luis Jose Maria','ljm@sapo.pt','Minhapass123',84,3); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Florinda Regulo','flr_re@gmail.com','Minhapass123',4,2); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Miguel Andrea','Miguel.Andre@apple.com','Minhapass123',45,1); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Teo Avelino','teoA@iade.pt','Minhapass123',1,4); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Apolonia Casimiro','AC@gmail.com','Minhapass123',6,5); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Ronaldo Walter','WalterRonald@hotmail.com','Minhapass123',41,4); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Luisinho Ines','Luis@meo.pt','Minhapass123',17,1 ); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Rita Celso','Ritacel@microsoft.com','Minhapass123',47,3); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Urbano Tao','UT@outlook.com','Minhapass123',2,4); 
Insert into usr (usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating) values ('Doris Ze Manel','DZM@gmail.com','Minhapass123',10,7); 




commit;