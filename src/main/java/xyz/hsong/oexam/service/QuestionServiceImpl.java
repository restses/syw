package xyz.hsong.oexam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hsong.oexam.common.ResponseCode;
import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.dao.AchievementMapper;
import xyz.hsong.oexam.dao.OptionMapper;
import xyz.hsong.oexam.dao.PaperDetailMapper;
import xyz.hsong.oexam.dao.QuestionMapper;
import xyz.hsong.oexam.pojo.Question;
import xyz.hsong.oexam.vo.PaperQuestionVo;

import java.util.List;


@Service
public class QuestionServiceImpl implements QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionMapper questionMapper;
    private final OptionMapper optionMapper;
    private final AchievementMapper achievementMapper;
    private final PaperDetailMapper paperDetailMapper;

    @Autowired
    public QuestionServiceImpl(QuestionMapper questionMapper, OptionMapper optionMapper, AchievementMapper achievementMapper, PaperDetailMapper paperDetailMapper) {
        this.questionMapper = questionMapper;
        this.optionMapper = optionMapper;
        this.achievementMapper = achievementMapper;
        this.paperDetailMapper = paperDetailMapper;
    }

    @Override
    @Transactional
    public ServerResponse addQuestion(Question question) {

        try {
            question.setStatus(1);

            if (questionMapper.insertSelective(question) > 0 && optionMapper.insertOptions(question.getId(), question.getOptions()) > 0) {
                return ServerResponse.createSuccess(ResponseCode.QUESTION_ADD_SUCCESS.getDesc());
            }

            return ServerResponse.createError(ResponseCode.QUESTION_ADD_ERROR.getCode(), ResponseCode.QUESTION_ADD_ERROR.getDesc());

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public ServerResponse getQuestionList(Integer page, Integer pageSize) {

        List<Question> questions;

        try {
            questions = questionMapper.selectQuestionList((page - 1) * pageSize, pageSize);
            return ServerResponse.createSuccess(questions);

        } catch (Exception e) {

            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }

    }

    @Override
    public ServerResponse getQuestion(Long id) {

        Question question;

        try {

            if ((question = questionMapper.selectByPrimaryKey(id)) == null) {
                return ServerResponse.createError(ResponseCode.QUESTION_NOT_FIND.getCode(), ResponseCode.QUESTION_NOT_FIND.getDesc());
            }

            return ServerResponse.createSuccess(question);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }

    }

    @Override
    public ServerResponse searchQuestion(Question question, Integer page, Integer pageSize) {

        try {

            List<Question> questions = questionMapper.selectQuestion(question, (page - 1) * pageSize, pageSize);
            if (questions.isEmpty()) {
                return ServerResponse.createError(ResponseCode.QUESTION_NOT_FIND.getCode(), ResponseCode.QUESTION_NOT_FIND.getDesc());
            }

            return ServerResponse.createSuccess(questions);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }

    }

    @Override
    @Transactional
    public ServerResponse updateQuestion(Question question) {

        try {

            if (questionMapper.updateQuestion(question) > 0 &&
                    optionMapper.updateOptions(question.getOptions()) > 0) {
                return ServerResponse.createSuccess(ResponseCode.QUESTION_UPDATE_SUCCESS.getDesc());
            }

            return ServerResponse.createError(ResponseCode.QUESTION_UPDATE_FAIL.getCode(), ResponseCode.QUESTION_UPDATE_FAIL.getDesc());

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    @Transactional
    public ServerResponse deleteQuestion(Long id) {

        try {

            questionMapper.deleteQuestion(id);
            optionMapper.deleteByQuestionId(id);
            paperDetailMapper.deleteByQuestionId(id);
            achievementMapper.deleteByQuestionId(id);

            return ServerResponse.createSuccess();

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServerResponse getQuestionCount(Question question) {

        try {
            return ServerResponse.createSuccess(questionMapper.selectQuestionCount(question));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }

    }

    @Override
    public ServerResponse getQuestionDetail(Long id) {

        try {
            PaperQuestionVo paperQuestionVo = questionMapper.selectQuestionDetail(id);

            if (paperQuestionVo == null) {
                return ServerResponse.createError(ResponseCode.QUESTION_NOT_FIND.getCode(), ResponseCode.QUESTION_NOT_FIND.getDesc());
            }

            return ServerResponse.createSuccess(paperQuestionVo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }
    }
    
    
}
