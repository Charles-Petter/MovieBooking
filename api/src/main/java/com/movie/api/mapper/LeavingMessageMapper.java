package com.movie.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.api.model.entity.LeavingMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LeavingMessageMapper extends BaseMapper<LeavingMessage> {
}
