package xyz.hsong.oexam.pojo;

/**
 * 用户答卷表
 */
public class Achievement {

	private Long id;

	private Long userId;

	private Long paperId;

	private Long questionId;

	private Long optionId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getPaperId() {
		return paperId;
	}

	public void setPaperId(Long paperId) {
		this.paperId = paperId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getOptionId() {
		return optionId;
	}

	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}

	@Override
	public String toString() {
		return "Achievement{" + "id=" + id + ", userId=" + userId + ", paperId=" + paperId + ", questionId="
				+ questionId + ", optionId=" + optionId + '}';
	}
}