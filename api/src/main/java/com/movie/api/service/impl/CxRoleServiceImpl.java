package com.movie.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.movie.api.constant.CxRoles;
import com.movie.api.mapper.CxRoleMapper;
import com.movie.api.model.entity.Role;
import com.movie.api.service.RoleService;
import com.movie.api.utils.DataTimeUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = "role")
public class CxRoleServiceImpl implements RoleService {

    @Resource
    private CxRoleMapper cxRoleMapper;

    @Override
    public Role create(Role role) throws Exception {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.in("wid", role.getWid());
        wrapper.in("value", role.getValue());
        if (cxRoleMapper.selectOne(wrapper) != null) {
            throw new Exception("该员工已拥有该权限, 请不要重复添加");
        }
        role.setId(UUID.randomUUID().toString());
        role.setCreateAt(DataTimeUtil.getNowTimeString());
        cxRoleMapper.insert(role);
        return role;
    }

    @Override
    public void deleteById(String id) throws Exception {
        Role role = cxRoleMapper.selectById(id);
        if (role.getValue().equals(CxRoles.ROLE_WORKER)) throw new Exception("员工的基本权限不能删除");
        cxRoleMapper.deleteById(id);
    }

    @Override
    public List<Role> listRolesByWorkerId(String wid) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.in("wid", wid);
        return cxRoleMapper.selectList(wrapper);
    }

    @Override
    public void deleteWorkerAllRoles(String wid) {
        cxRoleMapper.delete(new QueryWrapper<Role>().in("wid", wid));
    }

}
