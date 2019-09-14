package cshu271.main;

import com.google.gson.Gson;
import cshu271.datastore.AccessDeniedException;
import cshu271.value.UserToken;

public class Authenticator
{
	private static Gson gson = new Gson();
	
	public static UserToken checkUserToken(String userTokenStr)
		throws AccessDeniedException
	{
		if (userTokenStr == null || userTokenStr.isEmpty())
			throw new AccessDeniedException("User token value is invalid");
			
		try
		{
			UserToken userToken = gson.fromJson(userTokenStr, UserToken.class);
			userToken.validate();
			return userToken;
		}
		catch(Exception e)
		{
			throw new AccessDeniedException("User token value is invalid");
		}
	}
}
