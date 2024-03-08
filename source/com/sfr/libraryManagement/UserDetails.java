package libraryManagement;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.Console;

class UserDetails {
		private String name;
        private String email;
        private String login;
        private String pass;
        private long phNo;

        Scanner sc = new Scanner(System.in);
	TakenReturnReservation takeReturn = new TakenReturnReservation();
	private static Connection con = Database.getInstance().makeConnection();

	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return name;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getEmail()
	{
		return email;
	}
	public void setPass(String pass)
	{
		this.pass = pass;
	}
	public String getPass()
	{
		return pass;
	}
	public void setLogin(String login)
	{
		this.login = login;
	}
	public String getLogin()
	{
		return login;
	}
	public void setPhNo(long phNo)
	{
		this.phNo = phNo;
	}
	public long getPhNo()
	{
		return phNo;
	}

	UserDetails(String name,String email,long phNo)
	{
		this.name = name;
		this.email = email;
		this.phNo = phNo;
	}
	UserDetails()
	{
	}
	public void addUser()
        {
                try
                {
			System.out.println("Enter new user  name");
			String name = sc.next();
			setName(name);
                        System.out.println("Enter user login name:");
                        String login = sc.next();
			setLogin(login);
                        System.out.println("Enter  password:");
                        Console c = System.console();
                        char[]ch = c.readPassword();
                        String pass = String.valueOf(ch);
			setPass(pass);
			System.out.println("Enter email id");
			String email = sc.next();
			setEmail(email);
			System.out.println("Enter  phone number");
			long phone = sc.nextLong();
			if(phNumCheck(phone))
				setPhNo(phone);
			else
				System.out.println("invalid phone number (must give 10 numbers) ! pls enter once again");
				phone = sc.nextLong();
			String query = "insert into users (user_name,login,pwd,email,phone) values (?,?,?,?,?)";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1,getName());
			pst.setString(2,getLogin());
			pst.setString(3,getPass());
			pst.setString(4,getEmail());
			pst.setLong(5,getPhNo());
			pst.executeUpdate();
			System.out.println("\t\tNew user inserted");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public boolean phNumCheck(long phNo)
        {
                String s = String.valueOf(phNo);
                if(s.length() == 10)
                {
                        return true;
                }
                return false;
        }
	public boolean checkUser(String login,String pass) throws Exception
        {
                String query = "select login,pwd from users";
                ResultSet rs = Database.st.executeQuery(query);
		Statement st2 = con.createStatement();
                while(rs.next())
                {
                        if(login.equals(rs.getString("login")) && pass.equals(rs.getString("pwd")))
                        {
				setLogin(login);
				query = " select user_name from users where login = ' "+login+"'";
/*				rs = st2.executeQuery(query);
				rs.next();
				String name = rs.getString(1);
				System.out.println("\t\t Welcome " + name);*/
				userOptions();
                                return true;
                        }
                }
                return false;
        }
	public void borrowingHistory(String login) throws Exception
	{
		String str = "select user_id from users where login = '"+login+"'";
		ResultSet rs = Database.st.executeQuery(str);
		rs.next();
		int us_id = rs.getInt(1);
		str = "select book_id,taken_date,return_date from borrowing_history where borrower_id = "+us_id;
		rs = Database.st.executeQuery(str);
		System.out.println("book id \t taken date \t return date");
		System.out.println("---------------------------------------");
		while(rs.next())
		{
			System.out.println(rs.getInt(1) +"\t"+rs.getDate(2)+"\t"+rs.getDate(3));
		}
	}
	public void userOptions() throws Exception
        {
		takeReturn.reservDisplay(getLogin());
                System.out.println("\t\tUser options");
                boolean flag = true;
                while(flag)
                {
                        System.out.println("1 - view books \t2 -view your Borrowed history \t3 - take book \t4 - return book \t5 - exit");
                        int opt = sc.nextInt();
			Display d = new Display();
                        switch(opt)
                        {
                                case 1 : d.displayBooks();
                                         break;
                                case 2 : borrowingHistory(getLogin());
                                         break;
                                case 3 : takeReturn.takeBook(getLogin());
                                         break;
                                case 4 : takeReturn.returnBook(getLogin());
                                         break;
                                case 5 : flag = false;
                                         break;
                                default : System.out.println("Enter any valid options");
                        }
                }
        }

}
