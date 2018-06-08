package net.ddns.zimportant.lovingpeople.adapter;

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
import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.OrderedRealmCollection;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;

import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_BUSY;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_OFFLINE;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_ONLINE;

public class UserChatsListRecyclerAdapter extends
		RealmRecyclerViewAdapter<UserChat, UserChatsListRecyclerAdapter.UserChatViewHolder> {

	public UserChatsListRecyclerAdapter(@Nullable OrderedRealmCollection<UserChat> data) {
		super(data, true);
	}

	@NonNull
	@Override
	public UserChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_counselor, parent, false);
		return new UserChatViewHolder(v);
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

		UserChat item;

		UserChatViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		void setItem(UserChat item) {
			this.item = item;
			updateAvatar();
			updateStatus();
			updateName();
			updateField();
		}

		private void updateAvatar() {
			Picasso.get()
					.load(item.getAvatarUrl())
					.into(avatar);
		}

		private void updateStatus() {
			AppUtils.d(item.getStatus());
			switch (item.getStatus()) {
				case USER_ONLINE:
					imageViewOnlineIndicator.setImageResource(R.drawable.shape_bubble_online);
					break;
				case USER_OFFLINE:
					imageViewOnlineIndicator.setImageResource(R.drawable.shape_bubble_offline);
					break;
				case USER_BUSY:
					imageViewOnlineIndicator.setImageResource(R.drawable.shape_bubble_busy);
					break;
			}
		}

		private void updateName() {
			textViewName.setText(item.getName());
		}

		private void updateField() {
			textViewField.setText(joinRealmListString(item.getFields()));
		}

		private String joinRealmListString(RealmList<String> fields) {
			String res = "";
			for (String field : fields) {
				if (res.length() != 0) {
					res = res.concat(", ");
				}
				res = res.concat(field);
			}
			return res;
		}
	}
}
