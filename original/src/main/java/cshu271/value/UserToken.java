package cshu271.value;

import cshu271.datastore.AccessDeniedException;
import cshu271.datastore.NotFoundException;
import cshu271.datastore.Users;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UserToken
{

	private static String salt = new Double(Math.random()).toString();

	private final Long userId;
	private final String username;
	private String value;

	public UserToken(Long userId, String username)
	{
		this.userId = userId;

		this.username = username;
		this.value = getHashValue();
	}

	public UserToken(User user)
	{
		this.userId = user.getUserId();
		this.username = user.getUserName();
		this.value = getHashValue();
	}

	public Long getUserId()
	{
		return userId;
	}

	public String getUsername()
	{
		return username;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public void validate()
		throws AccessDeniedException
	{
		try
		{
			User realUser = Users.getUserById(userId);
			if (!(Objects.equals(realUser.getUserName(), this.username)
				&& Objects.equals(getHashValue(), this.value)))
			{
				throw new AccessDeniedException("Invalid user token");
			}
		} catch (NotFoundException ex)
		{
			throw new AccessDeniedException("Invalid user token");
		}
	}

	private String getHashValue()
	{
		try
		{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedHash = digest.digest(
				(username + salt).getBytes(StandardCharsets.UTF_8));
			return toHex(encodedHash);
		} catch (NoSuchAlgorithmException ex)
		{
			Logger.getLogger(UserToken.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	private String toHex(byte[] b)
	{
		List<String> arr = new ArrayList<>();
		for (int i = 0; i < b.length; i++)
		{
			arr.add(String.format("%02x", b[i]));
		}
		return arr.stream().collect(Collectors.joining());
	}

	private byte[] fromHex(String hex)
	{
		byte[] b = new byte[hex.length() / 2];
		for (int i = 0; i < b.length; i++)
		{
			int index = i * 2;
			int v = Integer.parseInt(hex.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}

}
