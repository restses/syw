package xyz.hsong.oexam.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import xyz.hsong.oexam.pojo.Paper;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaperVo {

    private Paper paper;
    private List<QuestionWithScoreVo> questions;

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public List<QuestionWithScoreVo> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionWithScoreVo> questions) {
        this.questions = questions;
    }
}
