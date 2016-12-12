package com.wzgiceman.rxretrofitlibrary.retrofit_rx.download;

/**
 * 下载状态
 * Created by WZG on 2016/10/21.
 */

public enum  DownState {
    START(0),
    DOWN(1),
    PAUSE(2),
    STOP(3),
    ERROR(4),
    FINISH(5);
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    DownState(int state) {
        this.state = state;
    }
}
