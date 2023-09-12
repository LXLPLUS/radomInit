package com.lxkplus.RandomInit.controller;

import com.lxkplus.RandomInit.commons.BodyResponse;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.mapper.UserMapper;
import com.lxkplus.RandomInit.model.DO.UserLogin;
import com.lxkplus.RandomInit.model.VO.UserInfo;
import com.lxkplus.RandomInit.utils.MD5Utils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class LoginController {

    final Integer minPassWordLen = 8;

    @Resource
    UserMapper userMapper;

    @Resource
    private HttpSession httpSession;

    @PostMapping("/createUser")
    @Transactional
    public BodyResponse<Void> createUser(@RequestBody UserInfo userInfo) throws NormalErrorException {
        checkVoidBodyResponse(userInfo);

        UserLogin userLogin = new UserLogin();
        userLogin.setCreateTime(new Date());
        userLogin.setLevel(10);
        userLogin.setUserName(userInfo.getUserName());
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

    @PostMapping("/login")
    public BodyResponse<Void> login(@RequestBody UserInfo userInfo) throws NormalErrorException {
        checkVoidBodyResponse(userInfo);

        // 密码是单向加盐的
        String salt = MD5Utils.SaltStr(userInfo.getPassword());

        assert salt != null;
        List<UserLogin> userLogins = userMapper.selectByMap(Map.of("user_name", userInfo.getUserName(), "password", salt));

        // 如果没有用户信息，则抛出异常
        if (userLogins.isEmpty()) {
            throw new NormalErrorException(ErrorEnum.passWordError);
        }

        // 注册session
        httpSession.setAttribute("actionID", userLogins.get(0).getActionID());

        return new BodyResponse<>(null, "成功登录");

    }

    private void checkVoidBodyResponse(@RequestBody UserInfo userInfo) throws NormalErrorException {
        ThrowUtils.throwIf(StringUtils.isBlank(userInfo.getUserName()), ErrorEnum.Empty, "用户名为空！");
        ThrowUtils.throwIf(StringUtils.isBlank(userInfo.getPassword()), ErrorEnum.Empty, "密码为空！");
        ThrowUtils.throwIf(userInfo.getUserName().length() > 100, ErrorEnum.paramNotSupport, "用户名过长！");
        ThrowUtils.throwIf(userInfo.getPassword().length() > 100, ErrorEnum.paramNotSupport, "密码过长！");
        ThrowUtils.throwIf(userInfo.getPassword().length() < minPassWordLen, ErrorEnum.Empty, "密码小于" + minPassWordLen + "位数！");
    }
}
