package net.mengkang.dto;

/**
 * Created by luoxiaosong on 2018/3/3.
 */
public enum UserStatus {

    student(1),
    teacher(2);

    int status;
    UserStatus(int status){
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
