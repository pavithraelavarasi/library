package libraryManagement;

import java.sql.ResultSet;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.sql.Timestamp;


class Reservation {
        
        Scanner sc = new Scanner(System.in);
	TakeBookChecks check = new TakeBookChecks();

	public boolean isBookReserved(String login,int book_id)throws Exception
	{
		int us_id = check.getUserId(login);
		String str = "select status from reservation where book_id = "+book_id+" and user_id ="+us_id;
		ResultSet rs = Database.st.executeQuery(str);
		while(rs.next())
		{
			if(rs.getString(1).equals("reserved"))
			{
				return true;
			}
		}
		return false;
	}
	public boolean isTaken(int book_id) throws Exception
	{
                String str = "select taken_date,return_date from borrowing_history where book_id ="+book_id;
                ResultSet rs = Database.st.executeQuery(str);
                while(rs.next())
               	{
			if(rs.getTimestamp(2) == null)
			{
				return true;
			}
		}
		return false;
	}
	public void isTakenBook(int book_id)throws Exception
	{
		if(isTaken(book_id))
		{	
			System.out.println("\t\t* The book is already taken by other user * \n Wait ..for some more time to available"); 
		}
		else
		{
			System.out.println("Stock unavailable");
		}
	}
	public void priorityOrder(int book_id) throws Exception
	{
		String str = "select user_id,reservation_date from reservation where book_id = "+book_id;
		ResultSet rs = Database.st.executeQuery(str);
		while(rs.next()) {
		int us_id = rs.getInt(1);
		Timestamp rsd = rs.getTimestamp(2);
		System.out.println("\t The user who have id - " + us_id+ " has the first priority to take the book - > (book reserved on _ "+ rsd + ")");
		return;
		}
	}
	public boolean isBookAvailable(int book_id) throws Exception
	{
		String str = "select available_stock,available_status from books where book_id = "+book_id;
		ResultSet rs = Database.st.executeQuery(str);
		while(rs.next())
		{
			if((rs.getInt(1) > 0) && (rs.getString(2).equals("available")))
			{
				return true;
			}
		}
		return false;
	}
}
