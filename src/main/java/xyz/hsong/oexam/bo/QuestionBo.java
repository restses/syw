package xyz.hsong.oexam.bo;

import java.util.List;
/**
 * 试题与选项
 */
public class QuestionBo {

    private Long id;
    private List<Long> answers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<Long> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Long> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "QuestionBo{" +
                "id=" + id +
                ", answers=" + answers +
                '}';
    }
}
