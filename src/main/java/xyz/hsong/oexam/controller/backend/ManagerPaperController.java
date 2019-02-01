package xyz.hsong.oexam.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.hsong.oexam.common.ResponseCode;
import xyz.hsong.oexam.common.ServerResponse;

import xyz.hsong.oexam.pojo.Paper;
import xyz.hsong.oexam.pojo.PaperDetail;
import xyz.hsong.oexam.service.PaperService;
import xyz.hsong.oexam.vo.PaperVo;

@Controller
@RequestMapping("/admin/paper")
public class ManagerPaperController {

    private final PaperService paperService;

    @Autowired
    public ManagerPaperController(PaperService paperService) {
        this.paperService = paperService;
    }

    
    // 添加试卷
    @RequestMapping("/add.do")
    @ResponseBody
    public ServerResponse addPaper(@RequestBody PaperVo paperVo) {
        try {
            return paperService.addPaper(paperVo.getPaper(), paperVo.getQuestions());
        } catch (RuntimeException e) {
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }
    }

    // 更新试卷
    @RequestMapping("/update.do")
    @ResponseBody
    public ServerResponse updatePaper(@RequestBody Paper paper) {
        return paperService.updatePaper(paper);
    }

    // 删除试卷
    @RequestMapping("/delete.do")
    @ResponseBody
    public ServerResponse deletePaper(@RequestParam Long paperId) {
        try {
            return paperService.deletePaper(paperId);
        } catch (RuntimeException e) {
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }
    }

    // 更新试题
    @RequestMapping("/addQuestion.do")
    @ResponseBody
    public ServerResponse addQuestion(@RequestBody PaperDetail paperDetail) {
        return paperService.addQuestion(paperDetail);
    }

    @RequestMapping("/deleteQuestion.do")
    @ResponseBody
    public ServerResponse deleteQuestion(@RequestParam Long paperId, Long questionId) {
        return paperService.deleteQuestion(paperId, questionId);
    }

    // 显示试卷列表
    @RequestMapping("/list.do")
    @ResponseBody
    public ServerResponse paperList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        return paperService.getPaperList(page, pageSize);
    }

    // 显示试卷内容
    @RequestMapping("/questionList.do")
    @ResponseBody
    public ServerResponse paperQuestionList(@RequestParam Long paperId) {
        return paperService.getPaperQuestionList(paperId);
    }

    @RequestMapping("/search.do")
    @ResponseBody
    public ServerResponse searchPaper(@RequestBody Paper paper, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        return paperService.searchPaper(paper, page, pageSize);
    }

    @RequestMapping("/count.do")
    @ResponseBody
    public ServerResponse count(@RequestBody Paper paper) {
        return paperService.getPaperCount(paper);
    }

    @RequestMapping("/detail.do")
    @ResponseBody
    public ServerResponse paperDetail(@RequestParam Long paperId) {
        return paperService.getPaperDetail(paperId);
    }

}
