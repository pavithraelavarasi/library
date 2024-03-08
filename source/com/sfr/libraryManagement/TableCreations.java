package libraryManagement;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

class TableCreations {

	public static void tables()
	{
		try
		{
		//	createRole();
			createInsertAdmin();
			createAuthor();
			createBooks();
			createUsers();
			createReservation();
			createBorrowHistory();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void createRole()
	{
		try
		{
			String query = "create table if not exists role(id serial primary key,role varchar(20) not null)";
			Database.st.executeUpdate(query);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void createInsertAdmin()
	{
		try
		{
			Admin admin = new Admin();
			String str = "create table if not exists admin(role_id int,constraint fk_id_admin foreign key(role_id) references role(id)on delete cascade on update cascade,login varchar(20)unique,password varchar(20) not null)";
			Database.st.executeUpdate(str);
			System.out.println("Admin table created");
			admin.insertAdmin();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void createBooks()
	{
		try
		{
			String str = "create table if not exists books(book_id serial primary key,title varchar,author_id int,constraint fk_at_bt foreign key(author_id) references author(author_id) on delete cascade on update cascade,available_stock int,available_status varchar,category varchar,published_date date)";
			Database.st.executeUpdate(str);
			System.out.println("Books table created");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void createUsers()
	{
		try
		{
			String str = "create table if not exists users(role_id int ,constraint fk_role_users foreign key (role_id) references role(id) on delete cascade on update cascade,user_id serial primary key,user_name varchar,login varchar,pwd varchar,email varchar,phone bigint)";
			Database.st.executeUpdate(str);
			System.out.println("Users table created");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void createAuthor()
	{
		try
		{
			String str = "create table if not exists author(author_id serial primary key,author_name varchar,nick_name varchar unique)";
			Database.st.executeUpdate(str);
			System.out.println("Author table created");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void createReservation()
	{
		try
		{
			String str = "Create table if not exists reservation(book_id int,constraint fk_bookid_rid foreign key(book_id) references books(book_id) on delete cascade on update cascade,user_id int, constraint fk_bid_rid foreign key(user_id) references users(user_id) on delete cascade on update cascade,reservation_date Timestamp,status varchar)";
			Database.st.executeUpdate(str);
			System.out.println("Reservation table created");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void createBorrowHistory()
	{
		try 
		{
			String str = "create table if not exists borrowing_history(borrower_id int,constraint fk_bid_uid foreign key(borrower_id) references users(user_id) on delete cascade on update cascade,book_id int,constraint fk_bid_bbid foreign key(book_id) references books(book_id) on delete cascade on update cascade,taken_date Timestamp,return_date Timestamp)";
			Database.st.executeUpdate(str);
			System.out.println("Borrowing history table created");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
