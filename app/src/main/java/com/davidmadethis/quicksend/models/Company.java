package com.davidmadethis.quicksend.models;

/**
 * Created by root on 5/7/17.
 */

public class Company {

    private String emailAddress;
    private boolean editAble;
    private String companyName;
    private boolean shouldSend = false;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isEditAble() {
        return editAble;
    }

    public void setEditAble(boolean editAble) {
        this.editAble = editAble;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isShouldSend() {
        return shouldSend;
    }

    public void setShouldSend(boolean shouldSend) {
        this.shouldSend = shouldSend;
    }
}
