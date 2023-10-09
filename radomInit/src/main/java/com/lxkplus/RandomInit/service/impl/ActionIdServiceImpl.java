package com.lxkplus.RandomInit.service.impl;

import com.lxkplus.RandomInit.service.ActionIdService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActionIdServiceImpl implements ActionIdService {

    @Resource
    HttpSession httpSession;

    @Override
    public Integer getActionID() {
        Object actionID = httpSession.getAttribute("actionID");
        if (actionID == null) {
            return null;
        }
        return Integer.parseInt(actionID.toString());
    }

}
