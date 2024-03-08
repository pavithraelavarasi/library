package libraryManagement;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.Console;

class Admin {

        Scanner sc = new Scanner(System.in);
        private static Connection con = Database.getInstance().makeConnection();
	public void insertAdmin()throws Exception
	{
		String str = "select * from admin";
		ResultSet rs = Database.st.executeQuery(str);
		int count = 0;
		while(rs.next())
		{
			count++;
		}
		if(count == 0)
		{
			String login = "pavi01";
                        String pwd = "pavi";
                        String query = "insert into admin values('"+login+"',"+"'"+pwd+"')";
                        Database.st.executeUpdate(query);
                        System.out.println("Admin inserted!");
		}
	}		
	public void boolean adminLogin()
	{
		try
		{
			System.out.println("Enter login");
			String login = sc.next();
			System.out.println("Enter password");
			Console c = System.console();
			char[]ch = c.readPassword();
			String pwd = String.valueOf(ch);
//			String pwd = sc.next();
			String str = "select login,password from admin";
			ResultSet rs = Database.st.executeQuery(str);
			while(rs.next())
			{
				if(login.equals(rs.getString("login")) && pwd.equals(rs.getString("password")))
				{
					return true;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	public void adminOptions() throws Exception
	{
		System.out.println("Enter any options");
		boolean flag = true;
		while(flag)
		{
			System.out.println("1 - add user \t2 - add book \t3 - add stock to available book \t4 - view books \t5 - view borrowers list \t6 - view borrowers history \t7-exit");
			int opt = sc.nextInt();
			Display d = new Display();
			switch(opt)
			{
				case 1 : UserDetails.addUser();
					 break;
				case 2 : addBook();
					 break;
				case 3 : addAvailableBookStock();
					 break;
				case 4 : d.displayBooks();
					 break;
				case 5 : d.displayBorrowers();
					 break;
				case 6 : d.displayBorrowingHistory();
					 break;
				case 7 : flag = false;
					 break;
				default : System.out.println("Enter any valid options");
			}
		}
	}

	public void addBook()throws Exception
	{
		System.out.println("Enter the book title");
		String title = sc.next();
		System.out.println("Enter author name");
		String author = sc.next();
		Author a = new Author();
		a.setAuthorName(author);
		System.out.println("Enter nick name of the author");
		String nick = sc.next();
		a.setNick(nick);
		//Author.insertAuthor(a.getAuthorName(),a.getNick());
		if(isPresentAuthor(nick))
		{
			System.out.println("\t\tThe author (with same nick name) is already here");
			return;
		}
		Author.insertAuthor(a.getAuthorName(),a.getNick());
//		if(isAlreadyPresent(title,a.getName(author))== false)
//		{\
		
			System.out.println("Enter the category of the book");
			String type = sc.next();
			System.out.println("Enter the stock of the book");
			int stock = sc.nextInt();
			System.out.println("Enter the published date");
			String sd = sc.next();
			try {
			Date dd = new SimpleDateFormat("dd/MM/yyyy").parse(sd);
			java.sql.Date sqlDate = new java.sql.Date(dd.getTime());
			int author_id = getAuthorId(nick);
			String str = "insert into books (title,author_id,available_stock,category,published_date)values(?,?,?,?,?)";
			PreparedStatement pst = con.prepareStatement(str);
			pst.setString(1,title);
			pst.setInt(2,author_id);
			pst.setInt(3,stock);
			pst.setString(4,type);
			pst.setDate(5,sqlDate);
			pst.executeUpdate();
			System.out.println("Books details inserted");
			TakenReturnReservation.changeStatus();
			}
			catch(Exception e){
				e.printStackTrace();
			}
//		}
/*		else
		{
			System.out.println("The book is alreay present");
			System.out.println("You can increse the stock of the book");
			increaseStock(title,author);
		}*/
	}
	public int getAuthorId(String nick)throws Exception
	{
		String str = "select author_id from author where nick_name = '"+nick+"'";
		ResultSet rs = Database.st.executeQuery(str);
		rs.next();
		int id = rs.getInt(1);
		return id;
	}
	public boolean isPresentAuthor(String nick_name) throws Exception
	{
		String str = "select nick_name from author where nick_name = '"+nick_name+"'";
		ResultSet rs = Database.st.executeQuery(str);
		while(rs.next())
		{
			if(rs.getString(1).equals(nick_name))
			{
				return true;
			}
		}
		return false;
	}
	public void addAvailableBookStock() throws Exception
	{
		Display d = new Display();
		d.displayBooks();
		System.out.println("Enter title");
		String title = sc.next();
		//SearchBook.searchByAuthor(author);
		System.out.println("Enter author id");
		int author_id = sc.nextInt();
//		SearchBook.searchByAuthor(author,author_id);
		if(isAlreadyPresent(title,author_id))
		{
			increaseStock(title,author_id);
		}
		else
		{
			System.out.println("Ther is no author with this id");
		}
	}
	public boolean isAlreadyPresent(String title,int author_id)throws Exception
	{
		String str = "select title,author_id from books";
		ResultSet rs = Database.st.executeQuery(str);
		while(rs.next())
		{
			if(rs.getString(1).equals(title) && rs.getInt(2) == author_id)
			{
				return true;
			}
		}
		return false;
	}
	public void increaseStock(String title,int author_id)throws Exception
	{
		Statement st2 = con.createStatement();
		String str = "select title,author_id from books";
                ResultSet rs = Database.st.executeQuery(str);
                while(rs.next())
                {
                        if(rs.getString(1).equals(title) && (rs.getInt(2)==author_id))
                        {
                                str = "select available_stock from books";
				rs = Database.st.executeQuery(str);
				rs.next();
				int stock = rs.getInt(1);
				System.out.println("Stock :" + stock);
				System.out.println("Enter the number to increase");
				int s = sc.nextInt();
				stock = stock + s;
				System.out.println("Increased stock : "+ stock);
				str = "update books set available_stock = " + stock +" where title = '"+title+"' and author_id=" +author_id;
				st2.executeUpdate(str);
				System.out.println("Stock updated");
                        }
		}
	}
}
