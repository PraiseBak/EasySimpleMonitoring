package com.monitoring.easysimplemonitering.utility;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class WarnSend {

    private LocalDateTime lastWarnSendTime = null;

    private final long sendDuration = 3600000;


    public void update(){
        this.lastWarnSendTime = LocalDateTime.now();
    }

    public boolean isSendable(){
        boolean sendable = false;
        if(lastWarnSendTime == null){
            sendable = true;
            update();
            return true;
        }

        long curSec = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        long lastWarnSendSec = lastWarnSendTime.atZone(ZoneId.systemDefault()).toEpochSecond();

        sendable = (curSec >= lastWarnSendSec + sendDuration);
        if(sendable){
            update();
        }
        return sendable;
    }

}
