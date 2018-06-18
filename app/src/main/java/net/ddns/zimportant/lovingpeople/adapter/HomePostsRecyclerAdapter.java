package net.ddns.zimportant.lovingpeople.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.HomeItem;
import net.ddns.zimportant.lovingpeople.service.persistence.HomePostsData;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.SyncUser;

public class HomePostsRecyclerAdapter
		extends RealmRecyclerViewAdapter<HomeItem, HomePostsRecyclerAdapter.HomeItemViewHolder> {

	public HomePostsRecyclerAdapter(OrderedRealmCollection<HomeItem> data) {
		super(data, true);
	}

	@NonNull
	@Override
	public HomeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_home, parent, false);
		return new HomeItemViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull HomeItemViewHolder holder, int position) {
		holder.setItem(getItem(position));
		holder.setColor(position);
	}

	static class HomeItemViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.background_layout)
		View backgroundLayout;
		@BindView(R.id.tv_content)
		TextView contentTextView;
		@BindView(R.id.tv_like)
		TextView likeTextView;
		@BindView(R.id.ib_item_post)
		ImageView likeButton;
		@BindView(R.id.iv_share_item_post)
		ImageView shareButton;
		private HomeItem item;
		private AlphaAnimation alphaAnimationShowIcon;

		HomeItemViewHolder(View itemView) {
			super(itemView);
			setUpLayout(itemView);
		}

		private void setUpLayout(View itemView) {
			ButterKnife.bind(this, itemView);
			likeButton.setOnClickListener(onLikeClick);
			shareButton.setOnClickListener(onShareClick);
			alphaAnimationShowIcon = new AlphaAnimation(0.2f, 1.0f);
			alphaAnimationShowIcon.setDuration(500);
		}

		View.OnClickListener onLikeClick = (View v) -> {
			String itemId = this.item.getItemId();

			this.item.getRealm().executeTransactionAsync(realm -> {
				HomeItem item = realm
						.where(HomeItem.class)
						.equalTo("itemId", itemId)
						.findFirst();
				if (item != null) {
					if (isCurrentUserLiked(item)) {
						item.getListLikeIdentity().remove(userIdentity());
					} else {
						item.getListLikeIdentity().add(userIdentity());
					}
				}
			});
		};

		private boolean isCurrentUserLiked(HomeItem item) {
			return item.getListLikeIdentity().contains(userIdentity());
		}

		private String userIdentity() {
			return SyncUser.current().getIdentity();
		}

		View.OnClickListener onShareClick = (View v) -> {
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			String shareBody = item.getBody();
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Loving People");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));
		};

		void setItem(HomeItem item) {
			this.item = item;
			contentTextView.setText(item.getBody());
			updateLike();
		}

		private void updateLike()  {
			updateLikeCount();
			updateLikeImage();
		}

		private void updateLikeCount() {
			int likeCount = item.getLikeCount();
			if (likeCount <= 1) {
				likeTextView.setText(String.valueOf(likeCount));
			} else {
				likeTextView.setText(String.valueOf(likeCount));
			}
		}

		private void updateLikeImage() {
			if (isCurrentUserLiked(item)) {
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
	}
}
