package libraryManagement;

class NoSuchAdminException extends Exception {
	public NoSuchAdminException(String s)
	{
		super(s);
	}
}
public class ExceptionAdmin {
//	public static void main(String args[])
	public static void exception()
	{
		try
		{
			throw new NoSuchAdminException("\t\t Oooppsss..You are not an admin to this this library");
		}
		catch(NoSuchAdminException e)
		{
			System.out.println("\t......No such admin exception caught");
			System.out.println(e.getMessage());
		}
	}
}
