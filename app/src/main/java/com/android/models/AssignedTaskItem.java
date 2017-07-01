package com.android.models;

import java.io.Serializable;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class AssignedTaskItem implements Serializable {
    private String strName, strDescription;

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }
}
