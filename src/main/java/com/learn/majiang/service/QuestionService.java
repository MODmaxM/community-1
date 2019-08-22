package com.learn.majiang.service;

import com.learn.majiang.dto.PageDto;
import com.learn.majiang.dto.QuestionDto;
import com.learn.majiang.mapper.QuestionMapper;
import com.learn.majiang.mapper.UserMapper;
import com.learn.majiang.model.Question;
import com.learn.majiang.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public PageDto list(Integer page, Integer size) {
        //在service中通过questionMapper userMapper 组合生成一个QuestionDto
        //QuestionDto包含了Question和User
        PageDto pageDto = new PageDto();
        //查记录总数
        Integer totalCount = questionMapper.count();

        Integer totalPage;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }

        if (page > totalPage) {
            page = totalPage;
        }

        if (page == 0) {
            page = 1;
        }
        Integer offset = size * (page - 1);

        pageDto.setPageInfo(totalPage, page, size);
        //通过page计算offset

        //offset和size查数据库拿到question
        System.out.println(offset);
        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDto> questionDtoList = new ArrayList<>();

        //questionlist-->加入user
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }

        //questionDto变为pageDto
        pageDto.setQuestions(questionDtoList);
        return pageDto;
    }

    public PageDto list(Integer userID, Integer page, Integer size) {
        //在service中通过questionMapper userMapper 组合生成一个QuestionDto
        //QuestionDto包含了Question和User
        PageDto pageDto = new PageDto();
        //查记录总数
        System.out.println(userID);
        Integer totalCount = questionMapper.countByUserId(userID);
        System.out.println("totalCount" + totalCount);

        Integer totalPage;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }

        if (page > totalPage) {
            page = totalPage;
        }

        if (page == 0) {
            page = 1;
        }
        Integer offset = size * (page - 1);

        pageDto.setPageInfo(totalPage, page, size);

        //offset和size查数据库拿到question
        List<Question> questions = questionMapper.listByUserId(userID, offset, size);

        System.out.println("length" + questions.size());
        List<QuestionDto> questionDtoList = new ArrayList<>();

        //questionlist-->加入user
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }

        //questionDto变为pageDto
        pageDto.setQuestions(questionDtoList);
        return pageDto;
    }

    public QuestionDto getById(Integer id) {

        Question question = questionMapper.getById(id);
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question, questionDto);

        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDto.setUser(user);

        return questionDto;
    }

    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }else{
            //更新
            question.setGmtModified(System.currentTimeMillis());
            questionMapper. update(question);
        }
    }
}
