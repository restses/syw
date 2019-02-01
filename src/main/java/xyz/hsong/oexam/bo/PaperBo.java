package xyz.hsong.oexam.bo;

import java.util.Map;

//学生答卷
public class PaperBo {

    //答题用户的id
    private Long userId;
    //用户答题的题号，和答题的答案
    private Map<Long, QuestionBo> questionBoMap;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Map<Long, QuestionBo> getQuestionBoMap() {
        return questionBoMap;
    }

    public void setQuestionBoMap(Map<Long, QuestionBo> questionBoMap) {
        this.questionBoMap = questionBoMap;
    }
}
