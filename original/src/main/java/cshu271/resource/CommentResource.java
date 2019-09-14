package cshu271.resource;

import cshu271.datastore.AccessDeniedException;
import cshu271.datastore.Comments;
import cshu271.value.Comment;
import cshu271.value.UserToken;
import cshu271.datastore.Articles;
import cshu271.datastore.NotFoundException;
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

@Path("/comment")
public class CommentResource
{
	/**
	 * Retrieve the comments for the given article Id
	 *
	 * @param articleId
	 * @return Collection<Comment> (json), possibly empty
	 */
	@GET
	@Path("/{articleId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByArticleId(@PathParam("articleId") Long articleId)
	{
		Collection<Comment> comments = Comments.getCommentsByArticleId(articleId);
		
		return JsonResponseBuilder.build(comments);
	}

	/**
	 * Add a comment to an article
	 * @param articleId 
	 * @param userTokenStr token of logged-in user
	 * @param commentStr text of comment
	 * @return Error Status.UNAUTHORIZED if user token validation fails
	 *          Error Status.NOT_FOUND if articleId is not valid
	 *          Comment (json) if successful
	 */
	@POST
	@Path("/{articleId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response postComment(@PathParam("articleId") Long articleId,
		@FormParam("userToken") String userTokenStr,
		@FormParam("comment") String commentStr)
	{
		try
		{
			UserToken userToken = Authenticator.checkUserToken(userTokenStr);
			Long userId = userToken.getUserId();
			
			Articles.getById(articleId);
			Comment comment = new Comment(0L, articleId, commentStr, userId);
			Comments.add(comment);
			
			return JsonResponseBuilder.build(comment);			
		}
		catch (NotFoundException nfe)
		{
			return ErrorResponseBuilder.build(Status.NOT_FOUND);
		}
		catch (AccessDeniedException ade)
		{
			return ErrorResponseBuilder.build(Status.UNAUTHORIZED);
		}
	}
	
    /**
	 * Get the number of comments for an article
	 * @param articleId
	 * @return Long (json)
	 */
	@GET
	@Path("/count/{articleId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCountByArticleId(@PathParam("articleId") Long articleId)
	{
		Long count = new Long(Comments.getCommentsByArticleId(articleId).size());
		return JsonResponseBuilder.build(count);		
	}

}
