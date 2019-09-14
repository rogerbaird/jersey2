package cshu271.resource;

import cshu271.datastore.AccessDeniedException;
import cshu271.datastore.Articles;
import cshu271.datastore.NotFoundException;
import cshu271.value.Article;
import cshu271.value.UserToken;
import com.google.gson.Gson;
import cshu271.main.Authenticator;
import cshu271.main.ErrorResponseBuilder;
import cshu271.main.JsonResponseBuilder;
import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/article")
public class ArticleResource
{

	private Gson gson = new Gson();

	/**
	 * Get all articles
	 * @return Collection<Article> (json)	
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll()
	{
		Collection<Article> articles = Articles.getAll();
		
		return JsonResponseBuilder.build(articles);		
	}
	
	/**
	 * Get all articles whose title contains the search text
	 * @param text - the search text
	 * @return Collection<Article> (json)
	 */
	@GET
	@Path("/search/{text}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@PathParam("text") String text)
	{
		Collection<Article> articles = Articles.search(text);
		
		return JsonResponseBuilder.build(articles);
	}

	/**
	 * Get article given the id
	 * @param articleId
	 * @return Error Status.NOT_FOUND if not found
	 *          Article (json) if found
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") Long articleId)
	{
		try
		{
			Article article = Articles.getById(articleId);
			
			return JsonResponseBuilder.build(article);
			
		} 
		catch (NotFoundException ex)
		{
			return ErrorResponseBuilder.build(Status.NOT_FOUND);
		}
	}

	/**
	 * Validate the user token and record a vote from the given user on the given article
	 * @param articleId id of article to vote for
	 * @param userTokenStr string version of user token
	 * @param vote should be 1L, -1L, or 0L
	 * @return Error Status.BAD_REQUEST if article not found
	 *          Error Status.UNAUTHORIZED if user token is not valid
	 *          Long (json) if successful, return the new vote total
	 */
	@POST
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response vote(@PathParam("id") Long articleId,
		@FormParam("userToken") String userTokenStr,
		@FormParam("vote") Long vote)
	{
		try
		{
			UserToken userToken = Authenticator.checkUserToken(userTokenStr);
			
			Article article = Articles.getById(articleId);
			article.vote(userToken.getUsername(), vote);
			Long total = article.total;
			
			return JsonResponseBuilder.build(total);			
		} catch (NotFoundException nfe)
		{
			return ErrorResponseBuilder.build(Status.BAD_REQUEST);
		} catch (AccessDeniedException ex)
		{
			return ErrorResponseBuilder.build(Status.UNAUTHORIZED);
		}
	}

	/**
	 * Validate the user token and create a new article
	 * @param userTokenStr string version of user token	
	 * @param link web address of the article
	 * @param title title to display for the article
	 * @return Error Status.UNAUTHORIZED if user token not valid
	 *          Article (json) if successful
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response create(@FormParam("userToken") String userTokenStr,
		@FormParam("link") String link,
		@FormParam("title") String title)
	{
		try
		{
			UserToken userToken = Authenticator.checkUserToken(userTokenStr);
			String username = userToken.getUsername();

			
			Article article = new Article();
			article.title = title;
			article.link = link;
			article.username = username;
			article = Articles.add(article);
			
			return JsonResponseBuilder.build(article);
			
		} catch (AccessDeniedException ex)
		{
			return ErrorResponseBuilder.build(Status.UNAUTHORIZED);
		}
	}
	
	/** 
	 * Validate the user token and delete an article
	 * @param articleId the article to delete
	 * @param userTokenStr string version of user token	
	 * @return Error Status.UNAUTHORIZED if user token not valid
	 *          Error Status.NOT_FOUND if article is not found
	 *          Empty json if successful
	 */
	@POST
	@Path("delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response delete(@PathParam("id") Long articleId, @FormParam("userToken") String userTokenStr)
	{

		try
		{
			Authenticator.checkUserToken(userTokenStr);

			try
			{
				Articles.delete(articleId);
			} catch (NotFoundException nfe)
			{
				return ErrorResponseBuilder.build(Status.NOT_FOUND);
			}
			return JsonResponseBuilder.build();
			
		} catch (AccessDeniedException ex)
		{
			return ErrorResponseBuilder.build(Status.UNAUTHORIZED);
		}
	}
}
