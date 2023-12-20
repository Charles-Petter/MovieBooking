package com.movie.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.movie.api.constant.Roles;
import com.movie.api.model.dto.LoginDto;
import com.movie.api.model.entity.User;
import com.movie.api.service.UserService;
import com.movie.api.utils.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "用户接口")
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Map<String, Object> login(@RequestBody LoginDto dto) throws Exception {
        User user = userService.login(dto);
        Map<String, Object> map = new HashMap<>();
        // 是否选择记住我
        long exp = dto.isRemember() ? JwtTokenUtil.REMEMBER_EXPIRATION_TIME : JwtTokenUtil.EXPIRATION_TIME;
        List<String> roles = new ArrayList<>();
        roles.add(Roles.ROLE_USER);
        map.put("token", JwtTokenUtil.createToken(dto.getUsername(), roles, exp));
        map.put("user", user);
        return map;
    }

    @GetMapping("")
    @ApiOperation(value = "查找所有用户")
    public List<User> findAll() {
        return userService.findAll();
    }

    @PutMapping("")
    @ApiOperation(value = "更新用户")
    public User updateUser(HttpServletRequest request, @RequestBody User user) {
        String token = JwtTokenUtil.getTokenFromRequest(request);
        String username = JwtTokenUtil.getUsername(token);
        // 根据需要处理用户信息
        return userService.update(user);
    }


//    @ApiOperation("根据id删除用户")
//    @ResponseBody
//    @PostMapping("/deleteUser")
//    public int deleteUser(Integer id) {
//        return userService.deleteById(String.valueOf(id));
//    }

    @ApiOperation("根据用户名删除用户")
    @ResponseBody
    @DeleteMapping("/deleteUserByUsername")
    public void deleteUserByUsername(@RequestParam String username) {
        userService.deleteByUsername(username);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查找用户")
    public User findById(@PathVariable String id) {
        return userService.findById(id);
    }

    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    public User save(@RequestBody User user) throws Exception {
        if (validateVerificationCode(user.getEmail(), user.getVerificationCode())) {
            return userService.save(user);
        } else {
            throw new Exception("验证码错误");
        }
    }

    @PostMapping("/sendVerificationCode")
    @ApiOperation(value = "发送验证码")
    public JSONObject sendVerificationCode(@RequestBody Map<String, String> params) {
        String email = params.get("email");
        if (userService.findByUsername(email) != null) {
            JSONObject result = new JSONObject();
            result.put("success", false);
            result.put("message", "该邮箱已经注册，请直接登录");
            return result;
        }
        String verificationCode = generateVerificationCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Xin电影 <1748521208@qq.com>");
        message.setTo(email);
        message.setSubject("Xin电影 - 验证码");
        message.setText("您的验证码是：" + verificationCode + "，请勿泄露给他人。");
        mailSender.send(message);
        redisTemplate.opsForValue().set(email, verificationCode, 5, TimeUnit.MINUTES);
        JSONObject result = new JSONObject();
        result.put("success", true);
        result.put("message", "验证码已发送，请留意邮箱");
        return result;
    }

    private String generateVerificationCode() {
        StringBuilder verificationCode = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            verificationCode.append(random.nextInt(10));
        }
        return verificationCode.toString();
    }


    private boolean validateVerificationCode(String email, String code) {
        String savedCode = redisTemplate.opsForValue().get(email);
        return code.equals(savedCode);
    }





}