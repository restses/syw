package xyz.hsong.oexam.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import xyz.hsong.oexam.pojo.Paper;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamVo {

    private Paper paper;
    private List<PaperQuestionVo> paperQuestionVos;
    private List<Long> checkedId;
    private Integer score;


    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public List<PaperQuestionVo> getPaperQuestionVos() {
        return paperQuestionVos;
    }

    public void setPaperQuestionVos(List<PaperQuestionVo> paperQuestionVos) {
        this.paperQuestionVos = paperQuestionVos;
    }

    public List<Long> getCheckedId() {
        return checkedId;
    }

    public void setCheckedId(List<Long> checkedId) {
        this.checkedId = checkedId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}


