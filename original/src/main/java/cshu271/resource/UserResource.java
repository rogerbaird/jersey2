package cshu271.resource;

import cshu271.datastore.Articles;
import cshu271.datastore.NotFoundException;
import cshu271.datastore.UnavailableException;
import cshu271.datastore.Users;
import cshu271.value.User;
import cshu271.value.UserToken;
import cshu271.main.ErrorResponseBuilder;
import cshu271.main.JsonResponseBuilder;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
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

@Path("/user")
public class UserResource
{
	/**
	 * Get the user by Id
	 * @param userId 
	 * @return Error Status.NOT_FOUND if userId isn't valid
	 *          User (json) if user is valid
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") Long userId)
	{
		try
		{
			User user = Users.getUserById(userId);
			return JsonResponseBuilder.build(user);			
		} catch (NotFoundException notFE)
		{
			return ErrorResponseBuilder.build(Status.NOT_FOUND);
		}
	}

	/**
	 * Gets the list of article ids the user liked
	 * @param username
	 * @return Collection<Long> (json), or else empty list if no likes or username is invalid
	 */
	@GET
	@Path("/{username}/likes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLikes(@PathParam("username") String username)
	{
		Collection<Long> likes = Articles.getAll().stream()
			.filter(a -> a.getVote(username) == 1L)
			.map(a -> a.getId())
			.collect(Collectors.toList());
		
		return JsonResponseBuilder.build(likes);		
	}
	
	/**
	 * Gets the list of article ids the user disliked
	 * @param username
	 * @return Collection<Long> (json), or else empty list if no dislikes or username is invalid
	 */
	@GET
	@Path("/{username}/dislikes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDislikes(@PathParam("username") String username)
	{
		Collection<Long> dislikes = Articles.getAll().stream()
			.filter(a -> a.getVote(username) == -1L)
			.map(a -> a.getId())
			.collect(Collectors.toList());
		
		return JsonResponseBuilder.build(dislikes);
	
	}

	/**
	 * Indicates whether or not a username already exists
	 * @param username
	 * @return Boolean (json) indicating the existence of the username
	 */
	@GET
	@Path("/exists/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUserExistence(@PathParam("username") String username)
	{
		try
		{
			User user = Users.getUserByName(username);
			return JsonResponseBuilder.build(true);
		} catch (NotFoundException notFE)
		{
			return JsonResponseBuilder.build(false);
		}
	}
	 
	/**
	 * Registers a user
	 * @param username
	 * @param password1
	 * @param password2
	 * @return Error Status.FORBIDDEN if the username isn't available (already exists)
	 *          Error Status.BAD_REQUEST if the passwords don't match or don't conform to password rules, optional String message 
	 *					to display to user if the username isn't available, the passwords don't match or don't conform to rules
	 *					(example: ErrorResponseBuilder.build(Status.BAD_REQUEST, "password too short"); )
	 *          User (json) the newly registered user
	 */
	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response register(@FormParam("username") String username, @FormParam("password1") String password1, @FormParam("password2") String password2)
	{
		try
		{
			if (password1.equals(password2))
			{
				if (password1.length() <= 2)
				{
					return ErrorResponseBuilder.build(Status.BAD_REQUEST, "password too short");
				}
				User user = new User(username, password1);
				user = Users.add(user);
				
				return JsonResponseBuilder.build(user);				
			}
			return ErrorResponseBuilder.build(Status.BAD_REQUEST, "password mismatch");			
		} catch (UnavailableException ue)
		{
			return ErrorResponseBuilder.build(Status.FORBIDDEN, "The user name is unavailable");
		}
	}

	/**
	 * Log the user in
	 * @param username
	 * @param password
	 * @return Error Status.UNAUTHORIZED if password is incorrect or username is not valid
	 *          UserToken (json) user's token for this session
	 */
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response login(@FormParam("username") String username, @FormParam("password") String password)
	{
		try
		{
			User user = Users.getUserByName(username);
			if (user.getPassword().equals(password))
			{
				return JsonResponseBuilder.build(new UserToken(user));				
			}
			return ErrorResponseBuilder.build(Status.UNAUTHORIZED);
		} catch (NotFoundException notFE)
		{
			return ErrorResponseBuilder.build(Status.UNAUTHORIZED);
		}
	}

}
