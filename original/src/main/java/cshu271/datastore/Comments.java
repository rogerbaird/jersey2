package cshu271.datastore;

import cshu271.value.Comment;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Comments
{
	private static Long nextCommentId = 1L;
	private static Map<Long,Comment> comments = new TreeMap<>();
	
	
	public static Collection<Comment> getCommentsByArticleId(Long articleId)
	{
		return comments.values().stream()
			.filter(c->c.getArticleId() == articleId)
			.collect(Collectors.toList());
	}
	
	public static Comment add(Comment comment)
	{
		comment.setCommentId(nextCommentId++);
		comments.put(comment.getCommentId(), comment);
		return comment;
	}
	
}
