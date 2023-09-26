package com.lxkplus.RandomInit.controller;

import com.lxkplus.RandomInit.commons.BodyResponse;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.mapper.UserMapper;
import com.lxkplus.RandomInit.model.DO.UserLogin;
import com.lxkplus.RandomInit.model.VO.UserInfo;
import com.lxkplus.RandomInit.service.SchemaService;
import com.lxkplus.RandomInit.utils.MD5Utils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class LoginController {

    static final int MIN_PASS_WORD_LEN = 8;

    @Resource
    SchemaService schemaService;

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
            throw new NormalErrorException(ErrorEnum.PARAM_NOT_SUPPORT, "对应用户存在或者密码错误！");
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
            throw new NormalErrorException(ErrorEnum.PASS_WORD_ERROR);
        }

        // 注册session
        httpSession.setAttribute("actionID", userLogins.get(0).getActionID());

        // 给这个用户加一个默认的数据库
        schemaService.createSchema(userLogins.get(0).getActionID());

        return new BodyResponse<>(null, "成功登录");
    }

    private void checkVoidBodyResponse(UserInfo userInfo) throws NormalErrorException {
        ThrowUtils.throwIf(StringUtils.isBlank(userInfo.getUserName()), ErrorEnum.EMPTY, "用户名为空！");
        ThrowUtils.throwIf(StringUtils.isBlank(userInfo.getPassword()), ErrorEnum.EMPTY, "密码为空！");
        ThrowUtils.throwIf(userInfo.getUserName().length() > 100, ErrorEnum.PARAM_NOT_SUPPORT, "用户名过长！");
        ThrowUtils.throwIf(userInfo.getPassword().length() > 100, ErrorEnum.PARAM_NOT_SUPPORT, "密码过长！");
        ThrowUtils.throwIf(userInfo.getPassword().length() < MIN_PASS_WORD_LEN, ErrorEnum.EMPTY, "密码小于" + MIN_PASS_WORD_LEN + "位数！");
    }


    @GetMapping("/loginCheck")
    public BodyResponse<Map<String, Boolean>> checkLogin() {
        if (httpSession.getAttribute("actionID") == null) {
            return new BodyResponse<>(Map.of("login", false));
        }
        return new BodyResponse<>(Map.of("login", true));
    }


}
