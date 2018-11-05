package com.android.drawings_module.drawing_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class CommentsData extends RealmObject{

	@SerializedName("comments")
	private RealmList<CommentsListItem> commentsList;

	public void setCommentsList(RealmList<CommentsListItem> commentsList){
		this.commentsList = commentsList;
	}

	public RealmList<CommentsListItem> getCommentsList(){
		return commentsList;
	}
}