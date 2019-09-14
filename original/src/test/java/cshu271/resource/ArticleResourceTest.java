package cshu271.resource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cshu271.datastore.DefaultData;
import cshu271.datastore.NotFoundException;
import cshu271.datastore.Users;
import cshu271.value.Article;
import cshu271.value.User;
import cshu271.value.UserToken;
import java.util.Collection;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rbaird
 */


public class ArticleResourceTest
{
	private ArticleResource articleResource;
	private User user;
	private UserToken userToken;
	private String userTokenStr;
	private String validUserTokenString;
	private String invalidUserTokenString;
	private Gson gson = new Gson();
	
	@Before
	public void setUp()
		throws NotFoundException
	{
		DefaultData.populateArticles();	
		DefaultData.populateUsers();
		DefaultData.populateComments();
		DefaultData.populateVotes();
		
		articleResource = new ArticleResource();
		user = Users.getUserByName("abigail");
		userToken = new UserToken(user);
		validUserTokenString = gson.toJson(userToken);
		invalidUserTokenString = "gobbledygook";
		
	}
	
	@Test
	public void testGetAll()
	{		
		Response response = articleResource.getAll();
		
		//check the response status
		assertEquals(response.getStatus(), Status.OK.getStatusCode());
		
		//convert the JSON reponse to Collection<Article>
		Collection<Article> list = gson.fromJson(response.getEntity().toString(), new TypeToken<Collection<Article>>(){}.getType());
		
		//validate that at least one article was returned
		assertTrue(list.size() > 0);
		
	}
}
