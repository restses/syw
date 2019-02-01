package xyz.hsong.oexam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hsong.oexam.common.ResponseCode;
import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.dao.AchievementMapper;
import xyz.hsong.oexam.dao.ExamUserMapper;
import xyz.hsong.oexam.dao.MarkMapper;
import xyz.hsong.oexam.pojo.Achievement;
import xyz.hsong.oexam.pojo.ExamUser;

import java.util.List;

@Service
public class ExamUserServiceImpl implements ExamUserService {

    private static final Logger logger = LoggerFactory.getLogger(ExamServiceImpl.class);

    private final ExamUserMapper examUserMapper;
    private final AchievementMapper achievementMapper;
    private final MarkMapper markMapper;

    @Autowired
    public ExamUserServiceImpl(ExamUserMapper examUserMapper, AchievementMapper achievementMapper, MarkMapper markMapper) {
        this.examUserMapper = examUserMapper;
        this.achievementMapper = achievementMapper;
        this.markMapper = markMapper;
    }

    @Override
    public ServerResponse addExamUser(List<ExamUser> examUsers) {

        try {
            if (examUserMapper.insertExamUsers(examUsers) > 0) {
                return ServerResponse.createSuccess(ResponseCode.EXAM_USER_ADD_SUCCESS.getDesc());
            }
            return ServerResponse.createError(ResponseCode.EXAM_USER_ADD_FAIL.getCode(), ResponseCode.EXAM_USER_ADD_FAIL.getDesc());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }

    }


    @Override
    @Transactional
    public ServerResponse deleteExamUser(Long userId, Long paperId) {

        try {
            examUserMapper.deleteExamUser(userId, paperId);
            achievementMapper.deleteByUserIdWithPaperId(userId, paperId);
            markMapper.deleteByUserIdWithPaperId(userId, paperId);
            return ServerResponse.createSuccess(ResponseCode.EXAM_USER_DELETE_SUCCESS.getDesc());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServerResponse updateExamUser(ExamUser examUser) {

        try {
            if (examUserMapper.updateStatus(examUser) > 0) {
                return ServerResponse.createSuccess(ResponseCode.EXAM_USER_UPDATE_SUCCESS.getDesc());
            }
            return ServerResponse.createError(ResponseCode.EXAM_USER_UPDATE_FAIL.getCode(), ResponseCode.EXAM_USER_UPDATE_FAIL.getDesc());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }

    }


    @Override
    public ServerResponse getExamUserList(Long paperId, Integer page, Integer pageSize) {

        try {
            List<ExamUser> examUsers = examUserMapper.selectExamUserList(paperId, (page - 1) * pageSize, pageSize);
            return ServerResponse.createSuccess(examUsers);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }
    }
}
