package com.movie.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.movie.api.mapper.CxFilmMapper;
import com.movie.api.model.entity.Film;
import com.movie.api.service.CxFilmService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@CacheConfig(cacheNames = "film")
public class CxFilmServiceImpl implements CxFilmService {

    @Resource
    private CxFilmMapper cxFilmMapper;

    @Override
    public void save(Film film) {
        film.setHot(0);
        cxFilmMapper.insert(film);
    }

    @CacheEvict
    @Override
    public void deleteById(String id) {
        cxFilmMapper.deleteById(id);
    }

    @Cacheable
    @Override
    public List<Film> findAll() {
        return cxFilmMapper.selectList(null);
    }

    @Override
    public List<Film> findByRegionAndType(String region, String type) {
        QueryWrapper<Film> wrapper = new QueryWrapper<>();
        if (!region.equals("全部")) {
            wrapper.in("region", region);
        }
        if (!type.equals("全部")) {
            wrapper.in("type", type);
        }
        return cxFilmMapper.selectList(wrapper);
    }

    @Override
    public List<Film> findHots(Integer limit) {
        QueryWrapper<Film> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("hot").last("limit " + limit);
        return cxFilmMapper.selectList(wrapper);
    }

    @Cacheable
    @Override
    public List<Film> findLikeName(String name) {
        QueryWrapper<Film> wrapper = new QueryWrapper<>();
        wrapper.like("name", name);
        return cxFilmMapper.selectList(wrapper);
    }

    @Cacheable
    @Override
    public Film findById(String id) {
        return cxFilmMapper.selectById(id);
    }

    @CacheEvict
    @Override
    public Film update(Film film) {
        cxFilmMapper.updateById(film);
        return film;
    }

}
