package libraryManagement;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;

class TakeBookChecks {
       private static Connection con = Database.getInstance().makeConnection();
	static int getUserId(String login)throws Exception
	{
		Statement state = con.createStatement();
		String str = "select user_id from users where login = '"+login+"';";
                ResultSet rs = state.executeQuery(str);
		rs.next();
		int us_id = rs.getInt(1);
		return us_id;
	}
	static boolean isAlreadyTaken(int book_id,String login)throws Exception
        {
                int us_id = getUserId(login);
                String str = "select book_id from borrowing_history where borrower_id ="+us_id;
		Statement st1 = con.createStatement();
//                ResultSet rs = Database.st.executeQuery(str);
                int count = 0;
		ResultSet rs = st1.executeQuery(str);
                while(rs.next())
                {
                        if(book_id == rs.getInt(1))
                        count++;
                }
                if(count > 0)
                {
                        return true;
                }
                return false;
        }
        static boolean isPresentTitle(String title)throws Exception
        {
		Statement st1 = con.createStatement();
                String str = "select title from books where title = '"+title+"'";
                ResultSet rs = st1.executeQuery(str);
                while(rs.next()) {
                if(rs.getString(1).equals(title))
                {
                        return true;
                }
                }
                return false;
        }
        static boolean isPresentAuthor(String author)throws Exception
        {
                Statement st1 = con.createStatement();
                String str = "select author_name from author where author_name = '"+author+"'";
                ResultSet rs = st1.executeQuery(str);
                while(rs.next()) {
                if(rs.getString(1).equals(author))
                {
                        return true;
                }
                }
		return false;
	}
	static boolean isPresentCategory(String category)throws Exception
        {
                Statement st1 = con.createStatement();
                String str = "select category from books where category = '"+category+"'";
                ResultSet rs = st1.executeQuery(str);
                while(rs.next()) {
                if(rs.getString(1).equals(category))
                {
                        return true;
                }
                }
                return false;
        }
	static boolean isReturned(String login,int book_id)throws Exception
        {
                Statement st1 = con.createStatement();
		int us_id = getUserId(login);
                String str = "select return_date from borrowing_history where book_id = "+book_id + " and borrower_id = " + us_id;
                ResultSet rs = st1.executeQuery(str);
                while(rs.next()) {
                if(rs.getTimestamp(1) == null)
                {
                        return false;
                }
                }
                return true;
        }
	static void stockIncrease(int id)throws Exception
        {
		Display d = new Display();
                String str = "select available_stock from books where book_id = "+id;
                ResultSet rs = Database.st.executeQuery(str);
                rs.next();
                int stock = rs.getInt(1);
                stock++;
                str = "update books set available_stock = "+stock+"where book_id = "+id;
                Database.st.executeUpdate(str);
                System.out.println("Stock increased");
                d.displayBooks();
        }
        static void stockDecrease(int id)throws Exception
        {
		Display d = new Display();
                String str = "select available_stock from books where book_id = "+id;
                ResultSet rs = Database.st.executeQuery(str);
                rs.next();
                int stock = rs.getInt(1);
                stock--;
                str = "update books set available_stock = "+stock+"where book_id = "+id;
                Database.st.executeUpdate(str);
                System.out.println("Stock decreased");
                d.displayBooks();
        }
}
