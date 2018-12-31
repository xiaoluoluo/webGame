package net.mengkang.dto;

/**
 * Created by luoxiaosong on 2018/3/3.
 */
public enum ClientStatus {

    noStatus(0),
    //玩家已经注册
    regist(1),
    //玩家已经登录
    login(2);
    int Status;
    ClientStatus(int Status){
        this.Status= Status;
    }

    public int getStatus() {
        return Status;
    }
}
