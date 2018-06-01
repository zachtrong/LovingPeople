package com.example.ztrong.lovingpeople.adapter;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.ztrong.lovingpeople.R;
import com.example.ztrong.lovingpeople.service.persistence.HomePostsData;

import java.util.ArrayList;

public class HomePostsListAdapter extends RecyclerView.Adapter<HomePostsListAdapter.ViewHolder> {

	ArrayList<String> contentData;
	ArrayList<String> backgroundColors;

	public static HomePostsListAdapter getAdapter() {
		return new HomePostsListAdapter(HomePostsData.getHomePosts(), HomePostsData.getColors());
	}

	HomePostsListAdapter() {

	}

	HomePostsListAdapter(ArrayList<String> data, ArrayList<String> colors) {
		contentData = data;
		backgroundColors = colors;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_post, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.contentTextView.setText(contentData.get(position));
		holder.backgroundLayout
				.setBackgroundColor(Color.parseColor(backgroundColors.get(position)));
	}

	@Override
	public int getItemCount() {
		return contentData.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		private FrameLayout backgroundLayout;
		private TextView contentTextView;
		private TextView likeTextView;

		ViewHolder(View itemView) {
			super(itemView);
			backgroundLayout = itemView.findViewById(R.id.fl_background);
			contentTextView = itemView.findViewById(R.id.tv_content);
			likeTextView = itemView.findViewById(R.id.tv_like);
		}
	}
}
