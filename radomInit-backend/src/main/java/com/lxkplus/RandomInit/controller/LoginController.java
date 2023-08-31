package com.lxkplus.RandomInit.controller;

import com.lxkplus.RandomInit.commons.BodyResponse;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.mapper.UserMapper;
import com.lxkplus.RandomInit.model.UserInfo;
import com.lxkplus.RandomInit.model.DBModel.UserLogin;
import com.lxkplus.RandomInit.utils.MD5Utils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@Slf4j
public class LoginController {

    final Integer minPassWordLen = 8;

    @Resource
    UserMapper userMapper;


    @PostMapping("/log")
    @Transactional
    public BodyResponse<Void> login(@RequestBody UserInfo userInfo) throws NormalErrorException {
        ThrowUtils.throwIf(StringUtils.isBlank(userInfo.getUserName()), ErrorEnum.Empty, "用户名为空！");
        ThrowUtils.throwIf(StringUtils.isBlank(userInfo.getPassword()), ErrorEnum.Empty, "密码为空！");
        ThrowUtils.throwIf(userInfo.getUserName().length() > 100, ErrorEnum.paramNotSupport, "用户名过长！");
        ThrowUtils.throwIf(userInfo.getPassword().length() > 100, ErrorEnum.paramNotSupport, "密码过长！");
        ThrowUtils.throwIf(userInfo.getPassword().length() < minPassWordLen, ErrorEnum.Empty, "密码小于" + minPassWordLen + "位数！");

        UserLogin userLogin = new UserLogin();
        userLogin.setCreateTime(new Date());
        userLogin.setLevel(10);
        userLogin.setUserName(userInfo.getUserName());
        userLogin.setComment(userInfo.getComment());
        String passwordSalt = MD5Utils.SaltStr(userInfo.getPassword());
        userLogin.setPassword(passwordSalt);

        String timeStamp = Long.toString(userLogin.getCreateTime().getTime() % Integer.MAX_VALUE);
        userLogin.setActionID(timeStamp);

        int userCount = userMapper.selectByMap(Map.of("user_name", userLogin.getUserName())).size();
        if (userCount > 0) {
            throw new NormalErrorException(ErrorEnum.paramNotSupport, "对应用户存在或者密码错误！");
        }
        userMapper.insert(userLogin);

        return new BodyResponse<>(null, "成功注册");
    }
}
