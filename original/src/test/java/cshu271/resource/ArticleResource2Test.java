package cshu271.resource;

import cshu271.resource.ArticleResource;
import cshu271.datastore.Articles;
import cshu271.datastore.NotFoundException;
import cshu271.datastore.UnavailableException;
import cshu271.datastore.Users;
import cshu271.value.Article;
import cshu271.value.User;
import cshu271.value.UserToken;
import com.google.gson.Gson;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArticleResource2Test
{

	private final static String USERNAME_1 = "user1";
	private final static String PASSWORD_1 = "password1";
	private final static String USERNAME_2 = "user2";
	private final static String PASSWORD_2 = "password2";

	private final static String LINK_1 = "http://somewhere.com/article_1.html";
	private final static String TITLE_1 = "Article 1 Title";

	private final static String LINK_2 = "http://somewhere.com/article_2.html";
	private final static String TITLE_2 = "Article 2 Title";

	private ArticleResource instance;
	private Gson gson;
	private User user1, user2;
	private Article article1, article2;

	public ArticleResource2Test()
	{
	}

	@Before
	public void setUp()
	{
		Articles.clear();
		Users.clear();

		instance = new ArticleResource();
		gson = new Gson();

		user1 = new User(USERNAME_1, PASSWORD_1);
		user2 = new User(USERNAME_2, PASSWORD_2);

		article1 = new Article(user1.getUserName(), LINK_1, TITLE_1);
		article2 = new Article(user1.getUserName(), LINK_2, TITLE_2);
	}

	@After
	public void tearDown()
	{
	}

	/**
	 * Test of getAll method, of class ArticleResource.
	 */
	@Test
	public void testGetAll_Empty()
	{
		Response result = instance.getAll();
		assertEquals(Status.OK.getStatusCode(), result.getStatus());
		assertEquals(gson.toJson(new Article[0]), result.getEntity());
	}

	/**
	 * Test of getAll method, of class ArticleResource.
	 */
	@Test
	public void testGetAll_Populated()
	{
		populateArticles(article1, article2);

		Response result = instance.getAll();
		assertEquals(Status.OK.getStatusCode(), result.getStatus());
		Article[] articles = gson.fromJson(result.getEntity().toString(), Article[].class);

		assertEquals(2, articles.length);

		for (Article article : articles)
		{
			assertTrue(LINK_1.equals(article.getLink()) || LINK_2.equals(article.getLink()));
			assertTrue(TITLE_1.equals(article.getTitle()) || TITLE_2.equals(article.getTitle()));
		}
	}

	/**
	 * Test of search method, of class ArticleResource.
	 */
	@Test
	public void testSearch_match()
	{
		populateArticles(article1, article2);

		Response result = instance.search(TITLE_1.substring(1));
		assertEquals(Status.OK.getStatusCode(), result.getStatus());
		Article[] articles = gson.fromJson(result.getEntity().toString(), Article[].class);

		assertEquals(1, articles.length);

		assertEquals(TITLE_1, articles[0].getTitle());
	}

	/**
	 * Test of search method, of class ArticleResource.
	 */
	@Test
	public void testSearch_noneFound()
	{
		populateArticles(article1, article2);

		Response result = instance.search("doesn't match");
		assertEquals(Status.OK.getStatusCode(), result.getStatus());
		Article[] articles = gson.fromJson(result.getEntity().toString(), Article[].class);

		assertEquals(0, articles.length);
	}

	/**
	 * Test of getById method, of class ArticleResource.
	 */
	@Test
	public void testGetById_notFound()
	{
		populateArticles(article1, article2);

		Response result = instance.getById(-1L);

		assertEquals(Status.NOT_FOUND.getStatusCode(), result.getStatus());
	}

	/**
	 * Test of getById method, of class ArticleResource.
	 */
	@Test
	public void testGetById_found()
	{
		populateArticles(article1, article2);
		Article article = Articles.getAll().iterator().next();

		Response result = instance.getById(article.getId());

		assertEquals(Status.OK.getStatusCode(), result.getStatus());
		Article foundArticle = gson.fromJson(result.getEntity().toString(), Article.class);

		assertEquals(article, foundArticle);
	}

	/**
	 * Test of vote method, of class ArticleResource.
	 */
	@Test
	public void testVote()
	{
		populateArticles(article1, article2);
		populateUsers(user1, user2);
		UserToken userToken1 = new UserToken(user2);

		Article art1 = Articles.getAll().iterator().next();
		try
		{
			Article a = Articles.getById(art1.getId());
			Long tot = a.getTotal();
			assertEquals((Long) 1L, Articles.getById(art1.getId()).getTotal());
		} catch (NotFoundException ex)
		{
			Assert.fail(ex.getMessage());
		}
		Response result = instance.vote(art1.getId(),
			gson.toJson(userToken1),
			1L);

		assertEquals(Status.OK.getStatusCode(), result.getStatus());

		try
		{
			assertEquals((Long) 2L, Articles.getById(art1.getId()).getTotal());
		} catch (NotFoundException ex)
		{
			Assert.fail(ex.getMessage());
		}
	}

	/**
	 * Test of create method, of class ArticleResource.
	 */
	@Test
	public void testCreate()
	{
		populateUsers(user1, user2);
		UserToken token = new UserToken(user1);
		Response result = instance.create(gson.toJson(token), LINK_1, TITLE_1);
		assertEquals(Status.OK.getStatusCode(), result.getStatus());
		assertEquals(1, Articles.getAll().size());
	}

	/**
	 * Test of delete method, of class ArticleResource.
	 */
	@Test
	public void testDelete()
	{
		populateUsers(user1, user2);
		populateArticles(article1, article2);

		UserToken token = new UserToken(user1);

		try
		{
			Article art1 = Articles.getById(1L);

		} catch (NotFoundException ex)
		{
			Assert.fail(ex.getMessage());
		}

		Response result = instance.delete(1L, gson.toJson(token));
		assertEquals(Status.OK.getStatusCode(), result.getStatus());
		assertEquals(1, Articles.getAll().size());
	}

	private void populateArticles(Article... articles)
	{
		for (Article a : articles)
		{
			Articles.add(a);
		}
	}

	private void populateUsers(User... users)
	{
		try
		{
			for (User u : users)
			{
				Users.add(u);
			}
		} catch (UnavailableException e)
		{
			Assert.fail(e.getMessage());
		}
	}
}
