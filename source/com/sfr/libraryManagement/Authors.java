package libraryManagement;

import java.sql.Statement;
import java.sql.PreparedStatement;

class Authors {
	private int author_id;
	private String author_name;
	private String nick_name;

	Authors(int author_id,String author_name,String nick_name)
	{
		this.author_id = author_id;
		this.author_name = author_name;
		this.nick_name = nick_name;
	}
	Authors()
	{
	}

	public void setAuthorId(int author_id)
	{
		this.author_id = author_id;
	}
	public int getAuthorId()
	{
		return author_id;
	}
	public void setAuthorName(String author_name)
	{
		this.author_name = author_name;
	}
	public String getAuthorName()
	{
		return author_name;
	}
	public void setNick(String nick_name)
	{
		this.nick_name = nick_name;
	}
	public String getNick()
	{
		return nick_name;
	}
	public String toString()
	{
		return "Author id : "+author_id +"\nAuthor_name :" +author_name+"\nNick name :"+nick_name;
	}
	public void insertAuthor(String author,String nick_name)throws Exception
	{
		setAuthorName(author);
		setNick(nick_name);
		System.out.println("Get name:"+ getAuthorName() +" _ " + getNick());
		String str = "insert into author(author_name,nick_name) values(?,?)";
		PreparedStatement pst = Database.con.prepareStatement(str);
		pst.setString(1,getAuthorName());
		pst.setString(2,getNick());
		pst.executeUpdate();
		System.out.println("\tAuthor inserted");
	}
}
