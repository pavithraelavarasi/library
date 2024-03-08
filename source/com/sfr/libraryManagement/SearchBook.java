package libraryManagement;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

class SearchBook {
	Scanner sc = new Scanner(System.in);
	TakeBookChecks checks = new TakeBookChecks();
	private static Connection con = Database.getInstance().makeConnection();
	public void searchByTitle(String title)throws Exception
	{
		ArrayList<Title> al = new ArrayList<>();
		String str = "select book_id,title from books where title = '" + title+"'";
		ResultSet rs = Database.st.executeQuery(str);
		while(rs.next())
		{
			int id = rs.getInt(1);
			title = rs.getString(2);
			Title t = new Title(id,title);
			al.add(t);
		}
		System.out.println("\n------Search by title--------");
		for(Title t : al)
		{
			System.out.println(t);
		}

	}
	public void searchAuthor(String author_name)throws Exception
	{
		Statement st1 = con.createStatement();
		String str = "select * from author where author_name = '"+author_name+"'";
		ResultSet rs = st1.executeQuery(str);
		rs.next();
		System.out.println(rs.getInt(1) +" : "+rs.getString(2)+" - " + rs.getString(3));
	}
	public void searchByAuthor(int author_id)throws Exception
	{
//		ArrayList<Author2> auth2 = new ArrayList<>(); // author id,nickname
		ArrayList<Author2> auth = new ArrayList<>();// book title book id
		Statement st1 = con.createStatement();
		String str = "select author_id,title from books where author_id = "+author_id;
		ResultSet rs = Database.st.executeQuery(str);
		while(rs.next())
		{
			author_id = rs.getInt(1);
			String title = rs.getString(2);
			Author2 author = new Author2(author_id);
			auth.add(author);
		}
		System.out.println("\t\t author id book title");
		for(Author2 a : auth)
		{
			System.out.println(a);
		}
	}
	public void searchByCategory(String category) throws Exception
	{
		ArrayList<Category> al = new ArrayList<>();
		String str = "select title,category,author_id from books where category = '"+category+"'";
		ResultSet rs = Database.st.executeQuery(str);
		while(rs.next())
		{
			String title = rs.getString(1);
			category = rs.getString(2);
			int author_id = rs.getInt(3);
			Category c = new Category(title,category,author_id);
			al.add(c);
		}
		System.out.println("\n----Search by category-------");
		for(Category cat : al)
		{
			System.out.println(cat);
		}
	}
}
class Title {
	int book_id;
	String title;
	Title(int book_id,String title)
	{
		this.book_id = book_id;
		this.title = title;
	}
	public String toString()
	{
		return "Book id :"+book_id +" - Title :"+title;
	}
}
/*class Author {
	int book_id;
        String title;
        Author(int book_id,String title)
        {
                this.book_id = book_id;
                this.title = title;
        }
        public String toString()
        {
                return "Book -id : "+book_id+" - "+title;
        }
}*/
class Author2 {
	int author_id;
	Author2(int author_id)
        {
                this.author_id = author_id;
        }
	public String toString()
	{
		return "Author id : "+ author_id;
	}
}
class Category {
	String title;
	String category;
	int author_id;
	Category(String title,String category,int author_id)
	{
		this.title = title;
		this.category = category;
		this.author_id = author_id;
	}
	public String toString()
	{
		return "Book :"+title+" by : author _ id : "+author_id+")  - " + category;
	}
}


