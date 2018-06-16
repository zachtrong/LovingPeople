package net.ddns.zimportant.lovingpeople.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.UserViewLoader;
import net.ddns.zimportant.lovingpeople.service.interfaces.OnCreateConversationCounselor;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class CounselorsRecyclerAdapter extends
		RealmRecyclerViewAdapter<UserChat, CounselorsRecyclerAdapter.UserChatViewHolder> {

	public CounselorsRecyclerAdapter(@Nullable OrderedRealmCollection<UserChat> data) {
		super(data, true);
	}

	@NonNull
	@Override
	public UserChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_counselor, parent, false);
		return new UserChatViewHolder(v, parent.getContext());
	}

	@Override
	public void onBindViewHolder(@NonNull UserChatViewHolder holder, int position) {
		holder.setItem(getItem(position));
	}

	class UserChatViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.civ_avatar)
		CircleImageView avatar;
		@BindView(R.id.tv_name)
		TextView textViewName;
		@BindView(R.id.tv_field)
		TextView textViewField;
		@BindView(R.id.onlineIndicator)
		ImageView imageViewOnlineIndicator;
		boolean isSetUpAutoUpdateUser = false;

		UserChat item;
		OnCreateConversationCounselor listener;

		UserChatViewHolder(View itemView, Context context) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			this.listener = (OnCreateConversationCounselor) context;
		}

		void setItem(UserChat item) {
			this.item = item;
			if (!isSetUpAutoUpdateUser) {
				isSetUpAutoUpdateUser = true;
				setUpUser();
				updateOnClick();
			}
		}

		private void setUpUser() {
			RealmResults<UserChat> userRealmResults = item
					.getRealm()
					.where(UserChat.class)
					.equalTo("id", item.getId())
					.findAllAsync();
			UserViewLoader userViewLoader = new UserViewLoader.Builder(userRealmResults)
					.setAvatarView(avatar)
					.setStatusView(imageViewOnlineIndicator)
					.setNameView(textViewName)
					.setFieldsView(textViewField)
					.build();
			userViewLoader.startListening();
		}

		private void updateOnClick() {
			itemView.setOnClickListener(v -> {
				listener.onCreateConversation(item.getId());
			});
		}
	}
}
