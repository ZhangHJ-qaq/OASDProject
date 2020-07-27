package com.haojie.others;


/**
 * This can encapsulate the result of some actions.
 */
public class ActionResult {

    //Whether the action has succeeded
    private boolean success;

    //The info
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
