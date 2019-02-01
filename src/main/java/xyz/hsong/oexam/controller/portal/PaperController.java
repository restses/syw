package xyz.hsong.oexam.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.hsong.oexam.common.Const;
import xyz.hsong.oexam.common.ResponseCode;
import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.pojo.Paper;
import xyz.hsong.oexam.pojo.User;
import xyz.hsong.oexam.service.ExamService;
import xyz.hsong.oexam.service.PaperService;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RequestMapping("/paper")
@Controller
public class PaperController {

    private final PaperService paperService;
    private final ExamService examService;

    @Autowired
    public PaperController(PaperService paperService, ExamService examService) {
        this.paperService = paperService;
        this.examService = examService;
    }

    //获取当前考生所有考试列表
    @RequestMapping("/list.do")
    @ResponseBody
    public ServerResponse getPaperList(HttpSession session, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return examService.getUserPaperList(user.getId(), page, pageSize);
    }

    // 
    @RequestMapping("/count.do")
    @ResponseBody
    public ServerResponse getPaperListCount(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return examService.getUserPaperListCount(user.getId());
    }

    // 
    @RequestMapping("/content.do")
    @ResponseBody
    public ServerResponse getPaperContent(@RequestParam Long paperId) {
        return paperService.getPaperQuestionList(paperId);
    }

    // 进入考试
    @RequestMapping("/info.do")
    @ResponseBody
    public ServerResponse getPaperDetail(@RequestParam Long paperId) {

        ServerResponse paperDetail = paperService.getPaperDetail(paperId);

        if (!paperDetail.isSuccess()) {
            return paperDetail;
        }

        Paper paper = (Paper) paperDetail.getData();

        //判断考试时间是否已经开始
        Date now = new Date();

        if (now.getTime() > paper.getStartTime().getTime() && now.getTime() < paper.getEndTime().getTime()) {
            return paperDetail;
        }

        return ServerResponse.createError(ResponseCode.TIME_ERROR.getCode(), ResponseCode.TIME_ERROR.getDesc());
    }

}
