package cshu271.datastore;

import cshu271.value.User;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class Users
{

	private static Long nextId = 1L;
	private static Map<Long, User> users = new TreeMap<>();

	public static void clear()
	{
		users.clear();
		nextId = 1L;
	}

	public static User add(User user)
		throws UnavailableException
	{
		Optional<User> existingUser = users.values()
			.stream()
			.filter(u -> u.getUserName().equals(user.getUserName()))
			.findAny();

		if (existingUser.isPresent())
		{
			throw new UnavailableException("Username is not available");
		}

		user.setUserId(nextId++);
		users.put(user.getUserId(), user);

		return user;
	}

	public static User getUserById(Long userId)
		throws NotFoundException
	{
		if (!users.containsKey(userId))
		{
			throw new NotFoundException("User not found.");
		}

		return users.get(userId);
	}

	public static User getUserByName(String username)
		throws NotFoundException
	{
		Optional<User> user = users.values().stream()
			.filter(u -> u.getUserName().equals(username))
			.findAny();

		if (!user.isPresent())
		{
			throw new NotFoundException("User not found");
		}
		return user.get();
	}
}
