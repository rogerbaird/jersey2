package cshu271.value;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Article
{
	public Long id = -1L;
	public String title = "";
	public String link = "";
	public String username;
	public Long total;
	private Map<String,Long> votes = new HashMap<>();
	
	public Article()
	{
	}

	public Article(String user, String link, String title)
	{
		this.username = user;
		this.link = link;
		this.title = title;
		vote(user, 1L);
	}
	
    public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id=id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public Long getTotal()
	{
		return votes.values().stream().reduce(0L, (a,b)->a+b);
	}

	public void setTotal(Long total)
	{
		this.total = total;
	}
	
	public void vote(String username, Long vote)
	{
		if ( Math.abs(vote) <= 1L)
		{
			votes.put(username, vote);
			votes.put(this.username, 1L);
		}
		
		setTotal(votes.values().stream().reduce(0L,(a,b)->a+b));
	}
	
	public long getVote(String username)
	{
		if ( votes.containsKey(username))
			return votes.get(username);
		return 0L;
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final Article other = (Article) obj;
		if (!Objects.equals(this.title, other.title))
		{
			return false;
		}
		if (!Objects.equals(this.link, other.link))
		{
			return false;
		}
		if (!Objects.equals(this.username, other.username))
		{
			return false;
		}
		if (!Objects.equals(this.id, other.id))
		{
			return false;
		}
		if (!Objects.equals(this.total, other.total))
		{
			return false;
		}
		if (!Objects.equals(this.votes, other.votes))
		{
			return false;
		}
		return true;
	}
		
}
