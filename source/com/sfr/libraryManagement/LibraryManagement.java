package libraryManagement;

import java.util.Scanner;

class LibraryManagement {
	static {
		private static Connection con = Database.getInstance().makeConnection();
	}
	public static void main(String args[])
	{
		try
		{
		TableCreations tables();
		Admin admin = new Admin();
		UserDetails user = new UserDetails();
		Scanner sc = new Scanner(System.in);
		boolean flag = true;
		while(flag)
		{
			System.out.println("\t\t ** Welcome to Library **");
			System.out.println("press 1 for login as admin  \npress 2 for login as user \npress 3 for exit");
			int opt = sc.nextInt();
			switch(opt)
			{
				case 1: if(admin.adminLogin())
					{
						System.out.println("\t\t Welcome Admin \n\t\t Your Options ");
						admin.adminOptions();
					}
					else
					{
						ExceptionAdmin.exception();
					}
					break;
				case 2:	
					System.out.println("Enter login");
					String login = sc.next();
					System.out.println("Enter password");
					String pass = sc.next();
					user.checkUser(login,pass);
					break;
				case 3: flag = false;
			//		Database.close();
					break;
				default : System.out.println("Enter any above valid option");
			}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				Database.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
