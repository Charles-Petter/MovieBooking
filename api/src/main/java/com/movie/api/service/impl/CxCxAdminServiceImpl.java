package com.movie.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.movie.api.constant.CxRoles;
import com.movie.api.mapper.CxAdminMapper;
import com.movie.api.model.dto.CxLoginDto;
import com.movie.api.model.entity.Admin;
import com.movie.api.service.CxAdminService;
import com.movie.api.utils.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CxCxAdminServiceImpl implements CxAdminService {

    @Resource
    private CxAdminMapper adminMapper;

    @Override
    public String login(CxLoginDto cxLoginDto) throws Exception {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.in("username", cxLoginDto.getUsername());
        wrapper.in("password", cxLoginDto.getPassword());
        Admin admin = adminMapper.selectOne(wrapper);
        if (admin == null) throw new Exception("用户名密码错误");
        //是否选择记住我
        long exp = cxLoginDto.isRemember() ? JwtTokenUtil.REMEMBER_EXPIRATION_TIME : JwtTokenUtil.EXPIRATION_TIME;
        List<String> roles = new ArrayList<>();
        roles.add(CxRoles.ROLE_ADMIN);
        return JwtTokenUtil.createToken(cxLoginDto.getUsername(), roles, exp);
    }

}
