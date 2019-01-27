package net.mengkang.manager;

public enum ClientCodeEnum {

    // 别人进入游戏 1
    OtherEnterGame(1),
    // 自己进入游戏 111
    MyselfEnterGame(111),
    // 玩家在移动 2
    Move(2),
    // 玩家在杀人 3
    Kill(3),
    // 玩家在冲刺 4
    Sprint(4);

    private int code;
    private ClientCodeEnum(int key){
        this.code = key;
    }

    public int getCode() {
        return code;
    }
}
