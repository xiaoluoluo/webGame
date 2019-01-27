package net.mengkang.service;


import net.mengkang.entity.Client;
import net.mengkang.entity.RoomInfo;
import net.mengkang.manager.ClassRoomMgr;
import net.mengkang.manager.ClientCodeEnum;
import net.mengkang.manager.MessMgr;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by luoxiaosong on 2018/3/10.
 */
public class HearBeat {

    public  void startBeating() {
        Runnable r = new Runnable() {
            public void run() {
                while(true) {
                    for (RoomInfo roomInfo:  ClassRoomMgr.getAllRoom().values() ) {
                        for (Client c:roomInfo.getClientMap().values()){
                            MessMgr.sendMessageToClient(c.getChannel(), ClientCodeEnum.heartBeat.getCode(),"");
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
}
