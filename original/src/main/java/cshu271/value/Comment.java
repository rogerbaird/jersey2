package cshu271.value;

public class Comment
{

	private Long commentId;
	private Long articleId;
	private String text;
	private Long userId;

	public Comment(Long commentId, Long articleId, String text, Long userId)
	{
		this.commentId = commentId;
		this.articleId = articleId;
		this.text = text;
		this.userId = userId;
	}

	public Long getCommentId()
	{
		return commentId;
	}

	public void setCommentId(Long commentId)
	{
		this.commentId = commentId;
	}

	public Long getArticleId()
	{
		return articleId;
	}

	public void setArticleId(Long articleId)
	{
		this.articleId = articleId;
	}

	public String getComment()
	{
		return text;
	}

	public void setComment(String comment)
	{
		this.text = comment;
	}

}
