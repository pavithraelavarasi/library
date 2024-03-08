package libraryManagement;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.sql.Timestamp;

class TakenReturnReservation {

	Scanner sc = new Scanner(System.in);
	Display display = new Display();
	TakeBookChecks checks = new TakeBookChecks();
	Reservation reservation = new Reservation();
	SearchBook searchBook = new SearchBook();
	private static Connection con = Database.getInstance().makeConnection();

	public static Date getToday()
	{
		return new Date();
	}
	public void takeBook(String login)throws Exception
	{
		reservDisplay(login);
		System.out.println("Enter options");
		boolean flag = true;
		while(flag)
		{
			System.out.println("1 - Search by title/author \t2 - search by catagory \t3 - take reservation book \t4 - exit");
			int opt = sc.nextInt();
			switch(opt)
			{
			   case 1:
				System.out.println("Enter the book title");
				String title = sc.next();
				if(!checks.isPresentTitle(title))
				{
					System.out.println("------Oops..!.This book is unavailable------");
					return;
				}
				searchBook.searchByTitle(title);
				int us_id = TakeBookChecks.getUserId(login);
				System.out.println("Enter author name of the title");
				String author = sc.next();
				if(!checks.isPresentAuthor(author))
				{
					System.out.println("-------Oops..!.This author is unavailable-----");
					return;
				}
				searchBook.searchAuthor(author);
				System.out.println("Enter the author id");
				int author_id = sc.nextInt();
				searchBook.searchByAuthor(author_id);
				String str = "select book_id,available_stock from books where title = '"+title+"'and author_id ="+author_id;
				ResultSet rs = Database.st.executeQuery(str);
				rs.next();
				int book_id = rs.getInt(1);
				if(checks.isAlreadyTaken(book_id,login) && !checks.isReturned(login,book_id))
				{
					System.out.println("-------You are already taken this book----------");
					break;
				}
				else
				{
					if(rs.getInt(2) == 0)
					{
						reservation(login,book_id);
					}
					else
					{
						int stock = rs.getInt(2);
						System.out.println("\t**You can take this book**");
						checks.stockDecrease(book_id);
						borrowersUpdate(login,book_id);
						changeStatus();
						break;
					}
				}
			break;
			case 2 : 
				System.out.println("Enter the category of the book");
				String category = sc.next();
				if(!checks.isPresentCategory(category))
				{
					System.out.println("----Oops..!..This category is unavailable-----");
					return;
				}
				searchBook.searchByCategory(category);
				System.out.println("Enter the book title");
			        title = sc.next();
				if(!checks.isPresentTitle(title))
                                {
                                        System.out.println("------Oops..This book is unavailable------");
                                        return;
                                }
			        searchBook.searchByTitle(title);
			        System.out.println("Enter author name of the title");
			        author = sc.next();
				if(!checks.isPresentAuthor(author))
                                {
                                        System.out.println("-------Oops..!.This author is unavailable-----");
                                        return;
                                }
			        //SearchBook.searchByAuthor(author,author_id);
				Statement st1 = con.createStatement();
				System.out.println("Enter the author id");
				author_id = sc.nextInt();
			        searchBook.searchByAuthor(author_id);
				str = "select book_id,available_stock from books where title = '"+title+"' and category = '"+category+"' and author_id ="+author_id;
				rs = st1.executeQuery(str);
				while(rs.next()){
				int bid = rs.getInt(1);
				if(rs.getInt("available_Stock") == 0)
					reservation(login,bid);
				else
				{
					checks.stockDecrease(bid);
					borrowersUpdate(login,bid);
					changeStatus();
				}
				}
				break;
			case 3 :
				System.out.println("Enter the book id");
			       	book_id = sc.nextInt();	
				takeReservedBook(login,book_id);
				changeStatus();
			case 4 : flag = false;
				 break;
			default : System.out.println("Enter any valid option");
			}
		}
	}
	public void takeReservedBook(String login,int book_id)throws Exception
	{
		Statement st2 = con.createStatement();
		if(reservation.isBookAvailable(book_id)) {
			reservation.priorityOrder(book_id);
			reservBookTakeProcess(login,book_id);
		}
		else
		{
			System.out.println("The book still unavailable");
			reservation.isTakenBook(book_id);
		}
	}
	public void reservBookTakeProcess(String login,int book_id) throws Exception
	{
                int us_id = checks.getUserId(login);
		String str = "select status from reservation where user_id = "+us_id+" and book_id = "+book_id;
		ResultSet rs = Database.st.executeQuery(str);
			rs.next();
			if(reservation.isBookReserved(login,book_id) && rs.getString(1).equals("reserved"))
			{
				System.out.println("----You are already reserved the book - take this book :) ");
				str = "delete from reservation where user_id ="+us_id + " and book_id = " + book_id;
	                        Database.st1.executeUpdate(str);
				System.out.println("\tDelete the entry from reservation table");
			}
			else
			{
				System.out.println("You didn't reserved the book");
				return;
			}
	}
	public void borrowersUpdate(String login,int bid)throws Exception
	{
		Timestamp take_date = new Timestamp(getToday().getTime());
		int bo_id = TakeBookChecks.getUserId(login);
	        String str ="insert into borrowing_history (borrower_id,book_id,taken_date) values(?,?,?)";
		PreparedStatement pst = con.prepareStatement(str);
		pst.setInt(1,bo_id);
		pst.setInt(2,bid);
		pst.setTimestamp(3,take_date);
		pst.executeUpdate();
		changeStatus();
		System.out.println("History updated");
	}
	public void returnBook(String login)throws Exception
	{
		System.out.println("Enter book id do yo want to return:");
		int id = sc.nextInt();
		int us_id = checks.getUserId(login);
		if(checks.isReturned(login,id))
		{
			System.out.println("You have no books to return");
			return;
		}
		else {
		Timestamp return_date = new Timestamp(getToday().getTime());
		String str = "select taken_date,return_date from borrowing_history where book_id = "+id+" and borrower_id = "+us_id;
		ResultSet rs = Database.st.executeQuery(str);
		while(rs.next()) {
		Timestamp ret_date = rs.getTimestamp(2);
		if(ret_date == null)
		{
			Timestamp taken_date = rs.getTimestamp(1);
			str = "update borrowing_history set return_date = '"+return_date +"' where book_id = "+id + " and borrower_id = "+us_id + " and taken_date = '"+taken_date+"'";
			Database.st.executeUpdate(str);
		TakeBookChecks.stockIncrease(id);
		changeStatus();
		System.out.println("Book returned..stock updated");
		break;
		}
	  }
		}
	}
	public void changeStatus()throws Exception
	{
		Statement st2 = con.createStatement();
		String str = "select available_stock from books";
		ResultSet rs = Database.st.executeQuery(str);
		while(rs.next())
		{
			if(rs.getInt(1) == 0)
			{
				str = "update books set available_status = 'unavailable' where available_stock = 0";
				st2.executeUpdate(str);
			}
			else if(rs.getInt(1) > 0)
			{
				str = "update books set available_status = 'available' where available_stock > 0";
				st2.executeUpdate(str);
			}
		}
	}
	public void reservation(String login,int book_id)throws Exception
	{
		if(reservation.isBookReserved(login,book_id)) {
			System.out.println("\t Already you reserved this book");
			return;
		}
		System.out.println("\tThe book is currently unavailble you reserved the book");
                int us_id = checks.getUserId(login);
		String status = "reserved";
		Timestamp reserv_date = new Timestamp(getToday().getTime());
		String str = "insert into reservation(book_id,user_id,reservation_date,status) values(?,?,?,?)";
		PreparedStatement pst = con.prepareStatement(str);
		pst.setInt(1,book_id);
		pst.setInt(2,us_id);
		pst.setTimestamp(3,reserv_date);
		pst.setString(4,status);
		pst.executeUpdate();
		System.out.println("Reservation table updated");
	}
	public void reservDisplay(String login)throws Exception
	{
		int us_id = checks.getUserId(login);
		String str = "select book_id,reservation_date from reservation where user_id = "+us_id;
		ResultSet rs = Database.st.executeQuery(str);
		while(rs.next())
		{
			System.out.println("You reserved book id : "+ rs.getInt(1) + " On " + rs.getDate(2));
		}
	}
}
