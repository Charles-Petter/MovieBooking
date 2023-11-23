package com.movie.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.movie.api.mapper.CxPosterMapper;
import com.movie.api.model.entity.Poster;
import com.movie.api.service.PosterService;
import com.movie.api.utils.DataTimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class CxPosterServiceImpl implements PosterService {

    @Resource
    private CxPosterMapper cxPosterMapper;

    @Override
    public void save(Poster poster) {
        poster.setId(UUID.randomUUID().toString());
        poster.setCreateAt(DataTimeUtil.getNowTimeString());
        cxPosterMapper.insert(poster);
    }

    @Override
    public void update(Poster poster) {
        cxPosterMapper.updateById(poster);
    }

    @Override
    public List<Poster> findAll() {
        return cxPosterMapper.selectList(null);
    }

    @Override
    public List<Poster> findByStatus(boolean status) {
        QueryWrapper<Poster> wrapper = new QueryWrapper<>();
        wrapper.in("status", status);
        return cxPosterMapper.selectList(wrapper);
    }

    @Override
    public void deleteById(String id) {
        cxPosterMapper.deleteById(id);
    }

    @Override
    public void deleteAll() {
        cxPosterMapper.delete(null);
    }

}
