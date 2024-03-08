Table overview :
-------------------

String admin : 
		String str = "create table if not exists admin(login varchar,password varchar)";


String Books : 
		String str = "create table if not exists books(book_id serial primary key,title varchar,author_id int,constraint fk_at_bt foreign key(author_id) references author(author_id) on delete cascade on update cascade,available_stock int,available_status varchar,category varchar,published_date date)";	
		

String users :
		String str = "create table if not exists users(user_id serial primary key,user_name varchar,login varchar,pwd varchar,email varchar,phone bigint)";
		
String author :
			String str = "create table if not exists author(author_id serial primary key,author_name varchar,nick_name varchar unique)";
			
String reservation :

			String str = "Create table if not exists reservation(book_id int,constraint fk_bookid_rid foreign key(book_id) references books(book_id) on delete cascade on update cascade,user_id int, constraint fk_bid_rid foreign key(user_id) references users(user_id) on delete cascade on update cascade,reservation_date Timestamp,status varchar)";
			
String borrowing_history :
			String str = "create table if not exists borrowing_history(borrower_id int,constraint fk_bid_uid foreign key(borrower_id) references users(user_id) on delete cascade on update cascade,book_id int,constraint fk_bid_bbid foreign key(book_id) references books(book_id) on delete cascade on update cascade,taken_date Timestamp,return_date Timestamp)";

