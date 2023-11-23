package com.movie.api.controller;

import com.movie.api.model.dto.CxLoginDto;
import com.movie.api.service.CxAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "管理员接口")
@RequestMapping("/api/admin")
public class CxAdminController {

    @Resource
    private CxAdminService cxAdminService;

    @PostMapping("/login")
    @ApiOperation("管理员登陆")
    public Map<String, String> login(@RequestBody CxLoginDto cxLoginDto) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("token", cxAdminService.login(cxLoginDto));
        return map;
    }

}
