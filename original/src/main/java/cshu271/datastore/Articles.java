package cshu271.datastore;

import cshu271.value.Article;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Articles
{
	private static final Map<Long, Article> articles = new TreeMap<>();
	
	private static Long nextId = 1L;
	
	public static void clear()
	{
		articles.clear();
		nextId = 1L;
	}
	
	public static Collection<Article> getAll()
	{
		return articles.values();
	}
	
	public static Article getById(Long articleId)
		throws NotFoundException
	{
		if (!articles.containsKey(articleId))
		{
			throw new NotFoundException( "article not found" );
		}
		return articles.get(articleId);
	}
	
	public static Collection<Article> search(String text)
	{
		return articles.values()
			.stream()
			.filter(a->a.title.toLowerCase().contains(text.toLowerCase()))
			.collect(Collectors.toList());
	}
	
	public static Article add(Article article)
	{
		article.setId(nextId++);
		article.vote(article.username, 1L);
		articles.put(article.getId(), article);
		return article;
	}
	
	public static void update(Article article)
		throws NotFoundException
	{
		if ( articles.containsKey(article.getId()))
		{
			articles.put(article.getId(), article);
		}
		else
		{
			throw new NotFoundException(String.format("Article %d not found", article.getId()));
		}
	}
	
	public static void delete(Long id)
		throws NotFoundException
	{
		if ( articles.containsKey(id))
		{
			articles.remove(id);
		}
		else
		{
			throw new NotFoundException(String.format("Article %d not found", id));
		}
	}
}
