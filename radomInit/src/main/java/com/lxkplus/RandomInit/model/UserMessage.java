package com.lxkplus.RandomInit.model;

import lombok.Data;
import org.jboss.logging.MDC;

@Data
public class UserMessage {
    String actionID;
    String activeDatabase;
    Integer level;
    Integer step = 0;

    public void setActionID(String actionID) {
        this.actionID = actionID;
        MDC.put("actionID", actionID);
    }

    public void setStep(Integer step) {
        this.step = step;
        MDC.put("step", step);
    }
}
