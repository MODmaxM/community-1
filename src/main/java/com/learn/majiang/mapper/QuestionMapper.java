package com.learn.majiang.mapper;

import com.learn.majiang.dto.QuestionDto;
import com.learn.majiang.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into question(title,description,gmtCreate,gmtModified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param("userId") Integer userId, @Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(1) from question where creator= #{id}")
    Integer countByUserId(@Param("id") Integer id);

    @Select("select * from question where id=#{id}")
    Question getById(@Param("id") Integer id);

    @Update("update question set title=#{title},description=#{description},gmtModified=#{gmtModified},tag=#{tag} where id=#{id}")
    void update(Question question);
}
