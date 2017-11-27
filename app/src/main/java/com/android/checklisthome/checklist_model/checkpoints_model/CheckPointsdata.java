package com.android.checklisthome.checklist_model.checkpoints_model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CheckPointsdata{

	@SerializedName("check_points")
	private List<CheckPointsItem> checkPoints;

	public void setCheckPoints(List<CheckPointsItem> checkPoints){
		this.checkPoints = checkPoints;
	}

	public List<CheckPointsItem> getCheckPoints(){
		return checkPoints;
	}
}