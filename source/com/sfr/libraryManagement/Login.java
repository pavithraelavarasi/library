package libraryManagement;

class Login {
	public void login()
	{
		try
		{
			System.out.println("Enter the login");
			String login = console.readLine();
			System.out.println("Enter the password");
			String password = console.readLine();
			int flag = valid.getRole(login,password);
			if(flag != 0)
			{
				String admin = "admin";
				String user = "user";
				String role = valid.getRole(login,password);
				if(user.equals(role))
				{
					userName = users.getName(
