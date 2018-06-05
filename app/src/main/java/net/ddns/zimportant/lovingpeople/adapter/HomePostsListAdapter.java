package net.ddns.zimportant.lovingpeople.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.HomeItem;
import net.ddns.zimportant.lovingpeople.service.persistence.HomePostsData;

import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class HomePostsListAdapter
		extends RealmRecyclerViewAdapter<HomeItem, HomePostsListAdapter.ItemViewHolder> {

	public HomePostsListAdapter(OrderedRealmCollection<HomeItem> data) {
		super(data, true);
	}

	@NonNull
	@Override
	public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_post, parent, false);
		return new ItemViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
		holder.setItem(getItem(position));
		holder.setColor(position);
	}

	static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private View backgroundLayout;
		private TextView contentTextView;
		private TextView likeTextView;
		private ImageView likeButton;
		private HomeItem item;
		private AlphaAnimation alphaAnimationShowIcon;

		ItemViewHolder(View itemView) {
			super(itemView);
			backgroundLayout = itemView.findViewById(R.id.background_layout);
			contentTextView = itemView.findViewById(R.id.tv_content);
			likeTextView = itemView.findViewById(R.id.tv_like);
			likeButton = itemView.findViewById(R.id.ib_item_post);

			likeButton.setOnClickListener(this);

			alphaAnimationShowIcon = new AlphaAnimation(0.2f, 1.0f);
			alphaAnimationShowIcon.setDuration(500);
		}

		void setItem(HomeItem item) {
			this.item = item;
			contentTextView.setText(item.getBody());
			int likeCount = item.getLikeCount();
			if (likeCount <= 1) {
				likeTextView.setText(String.valueOf(likeCount));
			} else {
				likeTextView.setText(String.valueOf(likeCount));
			}
			if (item.getIsLiked()) {
				likeButton.setImageResource(R.drawable.heart);
			} else {
				likeButton.setImageResource(R.drawable.heart_outline);
			}
			likeButton.startAnimation(alphaAnimationShowIcon);
			likeButton.startAnimation(alphaAnimationShowIcon);
		}

		void setColor(int position) {
			backgroundLayout.setBackgroundColor(HomePostsData.getColor(position));
		}

		@Override
		public void onClick(View v) {
			String itemId = this.item.getItemId();
			boolean isLiked = this.item.getIsLiked();

			this.item.getRealm().executeTransactionAsync(realm -> {
				HomeItem item = realm
						.where(HomeItem.class)
						.equalTo("itemId", itemId)
						.findFirst();
				if (item != null) {
					item.setIsLiked(!isLiked);
					item.setLikeCount(item.getLikeCount() + (isLiked ? -1 : 1));
				}
			});
		}
	}
}
