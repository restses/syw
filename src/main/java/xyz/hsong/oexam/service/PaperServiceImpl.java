package xyz.hsong.oexam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hsong.oexam.common.ResponseCode;
import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.dao.*;
import xyz.hsong.oexam.pojo.Paper;
import xyz.hsong.oexam.pojo.PaperDetail;
import xyz.hsong.oexam.vo.PaperQuestionVo;
import xyz.hsong.oexam.vo.QuestionWithScoreVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PaperServiceImpl implements PaperService {

    private static final Logger logger = LoggerFactory.getLogger(PaperServiceImpl.class);

    private final PaperMapper paperMapper;
    private final PaperDetailMapper paperDetailMapper;
    private final ExamUserMapper examUserMapper;
    private final AchievementMapper achievementMapper;
    private final MarkMapper markMapper;

    @Autowired
    public PaperServiceImpl(PaperMapper paperMapper, PaperDetailMapper paperDetailMapper, ExamUserMapper examUserMapper, AchievementMapper achievementMapper, MarkMapper markMapper) {
        this.paperMapper = paperMapper;
        this.paperDetailMapper = paperDetailMapper;
        this.examUserMapper = examUserMapper;
        this.achievementMapper = achievementMapper;
        this.markMapper = markMapper;
    }

    @Override
    @Transactional
    public ServerResponse addPaper(Paper paper, List<QuestionWithScoreVo> questions) {

        try {

            if (paperMapper.insertSelective(paper) > 0 &&
                    paperDetailMapper.insertQuestions(paper.getId(), questions) > 0) {
                return ServerResponse.createSuccess(ResponseCode.PAPER_ADD_SUCCESS.getDesc());
            }
            return ServerResponse.createSuccess(ResponseCode.PAPER_ADD_FAIL.getDesc(), ResponseCode.PAPER_ADD_FAIL.getDesc());

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public ServerResponse updatePaper(Paper paper) {
        try {
            if (paperMapper.updateByPrimaryKeySelective(paper) > 0) {
                return ServerResponse.createSuccess(ResponseCode.PAPER_UPDATE_SUCCESS.getDesc());
            }
            return ServerResponse.createError(ResponseCode.PAPER_UPDATE_FAIL.getCode(), ResponseCode.PAPER_UPDATE_FAIL.getDesc());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }

    }

    @Override
    public ServerResponse addQuestion(PaperDetail paperDetail) {

        try {

            if (paperDetailMapper.selectDetail(paperDetail) > 0) {
                return ServerResponse.createError(ResponseCode.QUESTION_ADD_REPEATED.getCode(), ResponseCode.QUESTION_ADD_REPEATED.getDesc());
            }


            if (paperDetailMapper.insertSelective(paperDetail) > 0) {
                return ServerResponse.createSuccess(ResponseCode.PAPER_UPDATE_SUCCESS.getDesc());
            }

            return ServerResponse.createError(ResponseCode.PAPER_UPDATE_FAIL.getCode(), ResponseCode.PAPER_UPDATE_FAIL.getDesc());

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }

    }

    @Override
    public ServerResponse deleteQuestion(Long paperId, Long questionId) {

        try {
            if (paperDetailMapper.deleteQuestion(paperId, questionId) > 0) {
                return ServerResponse.createSuccess(ResponseCode.PAPER_UPDATE_SUCCESS.getDesc());
            }
            return ServerResponse.createError(ResponseCode.PAPER_UPDATE_FAIL.getCode(), ResponseCode.PAPER_UPDATE_FAIL.getDesc());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }
    }

    @Override
    @Transactional
    public ServerResponse deletePaper(Long paperId) {

        try {

            paperMapper.deleteByPrimaryKey(paperId);
            paperDetailMapper.deleteByPaperId(paperId);
            achievementMapper.deleteByPaperId(paperId);
            examUserMapper.deleteByPaperId(paperId);
            markMapper.deleteByPaperId(paperId);

            return ServerResponse.createSuccess(ResponseCode.PAPER_DELETE_SUCCESS.getDesc());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServerResponse searchPaper(Paper paper, Integer page, Integer pageSize) {

        try {

            List<Paper> papers = paperMapper.selectPapers(paper, (page - 1) * pageSize, pageSize);
            if (papers.isEmpty()) {
                return ServerResponse.createError(ResponseCode.PAPER_LIST_EMPTY.getCode(), ResponseCode.PAPER_LIST_EMPTY.getDesc());
            }
            return ServerResponse.createSuccess(papers);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }
    }

    @Override
    public ServerResponse getPaperList(Integer page, Integer pageSize) {

        try {

            List<Paper> papers = paperMapper.selectPaperList((page - 1) * pageSize, pageSize);

            if (papers.isEmpty()) {
                return ServerResponse.createError(ResponseCode.PAPER_NOT_FIND.getCode(), ResponseCode.PAPER_NOT_FIND.getDesc());
            }

            return ServerResponse.createSuccess(papers);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }
    }

    @Override
    public ServerResponse getPaperQuestionList(Long paperId) {

        try {
            List<PaperQuestionVo> paperQuestionList = paperMapper.selectPaperQuestionList(paperId);
            if (paperQuestionList.isEmpty()) {
                return ServerResponse.createError(ResponseCode.PAPER_QUESTION_NOT_FIND.getCode(), ResponseCode.PAPER_QUESTION_NOT_FIND.getDesc());
            }
            return ServerResponse.createSuccess(paperQuestionList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }
    }

    @Override
    public ServerResponse getPaperDetail(Long paperId) {

        try {

            Paper paper = paperMapper.selectByPrimaryKey(paperId);

            if (paper == null) {
                return ServerResponse.createError(ResponseCode.PAPER_NOT_FIND.getCode(), ResponseCode.PAPER_NOT_FIND.getDesc());
            }

            return ServerResponse.createSuccess(paper);


        } catch (Exception e) {
            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }
    }

    @Override
    public ServerResponse getPaperCount(Paper paper) {

        try {
            return ServerResponse.createSuccess(paperMapper.selectPaperCount(paper));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }
    }


}
