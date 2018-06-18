package net.ddns.zimportant.lovingpeople.service.helper;

import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;

import io.reactivex.disposables.Disposable;
import io.realm.RealmList;
import io.realm.RealmResults;

import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_BUSY;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_OFFLINE;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_ONLINE;

public class UserViewLoader {

	private RealmResults<UserChat> user;
	private TextView name, id, fields;
	private ImageView avatar, status;

	UserViewLoader(Builder builder) {
		this.user = builder.userChats;
		this.name = builder.name;
		this.id = builder.id;
		this.fields = builder.fields;
		this.status = builder.status;
		this.avatar = builder.avatar;
	}

	public void startListening() {
		Disposable checkUserChat = user
				.asFlowable()
				.filter(RealmResults::isLoaded)
				.subscribe(realmResults -> {
					UserChat userChat = realmResults.first();
					if (avatar != null) {
						Picasso.get()
								.load(userChat.getAvatarUrl())
								.into(avatar);
					}
					if (name != null) {
						name.setText(userChat.getName());
					}
					if (id != null) {
						id.setText(userChat.getId());
					}
					if (status != null) {
						status.setImageResource(
								getOnlineIndicatorResource(userChat.getStatus())
						);
					}
					if (fields != null) {
						fields.setText(joinRealmListString(userChat.getFields()));
					}
				});
	}

	public int getOnlineIndicatorResource(String status) {
		switch (status) {
			case USER_ONLINE:
				return R.drawable.shape_bubble_online;
			case USER_OFFLINE:
				return R.drawable.shape_bubble_offline;
			case USER_BUSY:
				return R.drawable.shape_bubble_busy;
		}
		throw new Error("No such status");
	}

	public String joinRealmListString(RealmList<String> fields) {
		String res = "";
		for (String field : fields) {
			if (res.length() != 0) {
				res = res.concat(", ");
			}
			res = res.concat(field);
		}
		return res;
	}

	public static class Builder {
		RealmResults<UserChat> userChats;
		TextView name, id, fields;
		ImageView status, avatar;

		public Builder(RealmResults<UserChat> userChats) {
			this.userChats = userChats;
		}

		public Builder setNameView(TextView v) {
			name = v;
			return this;
		}

		public Builder setIdView(TextView v) {
			id = v;
			return this;
		}

		public Builder setStatusView(ImageView v) {
			status = v;
			return this;
		}

		public Builder setFieldsView(TextView v) {
			fields = v;
			return this;
		}

		public Builder setAvatarView(ImageView v) {
			avatar = v;
			return this;
		}

		public UserViewLoader build() {
			return new UserViewLoader(this);
		}
	}
}
