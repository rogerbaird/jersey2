package cshu271.value;

public class User
{

	private Long userId = -1L;
	private String userName;
	private String password;

	public User(String userName, String password)
	{
		this.userName = userName;
		this.password = password;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

}
