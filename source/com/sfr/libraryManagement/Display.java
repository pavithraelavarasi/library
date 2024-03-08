package libraryManagement;

import java.sql.ResultSet;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Comparator;
import java.util.TreeSet;

class Display implements AdminUser {
	
       public void displayBooks() throws Exception
        {
                String str = "select book_id,title,author_id,available_status from books";
                ResultSet rs = Database.st.executeQuery(str);
                System.out.println("\nBook id \t title \t\t author_id \t\t status");
                System.out.println("---------------------------------------------------------------");
                while(rs.next())
                {
                        System.out.println("  " +rs.getInt(1) +"\t\t"+ rs.getString(2)+"\t\t"+rs.getInt(3)+"\t\t"+rs.getString(4));
                }
        }
        public void displayBorrowingHistory()throws Exception
        {
                String str = "Select * from borrowing_history";
                ResultSet rs = Database.st.executeQuery(str);
                System.out.println("borrower_id \t book_id \t taken date \t return date");
                System.out.println("------------------------------------------------------");
                while(rs.next())
                {
                        System.out.println("\t"+rs.getInt(1)+"\t"+rs.getInt(2)+"\t"+rs.getDate(3)+"\t"+rs.getDate(4));
                }
        }
	public void displayBorrowers() throws Exception
	{
		BorrowerOrder.borrowers();
	}
}
class BorrowerOrder {
	static void borrowers() throws Exception
	{
		TreeSet<UserDetails> t = new TreeSet<>(new NameComparator());
		String str = "select user_name,email,phone from users";
		ResultSet rs = Database.st.executeQuery(str);
		while(rs.next())
		{
			String name = rs.getString(1);
			String email = rs.getString(2);
			long phNo = rs.getLong(3);
			UserDetails ud = new UserDetails(name,email,phNo);
			t.add(ud);
		}
		System.out.println("-----Borrowers List-----");
		System.out.println("name \t mail id \t phone");
		System.out.println("-----------------------------");
		for(UserDetails u : t)
		{
			System.out.println(u.getName() +"	|	"+u.getEmail()+"	|	"+u.getPhNo());
		}
	}
}
class NameComparator implements Comparator<UserDetails> {
	public int compare(UserDetails ud1,UserDetails ud2)
	{
		return ud1.getName().compareTo(ud2.getName());
	}
}
