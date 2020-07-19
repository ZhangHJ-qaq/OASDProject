package com.haojie.others;


/**
 * 封装了各种操作（如：登录，注册，收藏图片）的结果对象
 */
public class ActionResult {
    private boolean success;
    private String info;

    public ActionResult(boolean success, String info) {
        this.success = success;
        this.info = info;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
