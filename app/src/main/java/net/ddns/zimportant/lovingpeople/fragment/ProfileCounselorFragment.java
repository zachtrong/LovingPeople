package net.ddns.zimportant.lovingpeople.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.activity.ConversationActivity;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.UserViewLoader;
import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;

import io.realm.RealmResults;
import io.realm.SyncUser;

public class ProfileCounselorFragment extends ProfileFragment {

	TextView fieldsTextView;
	TextView introduceTextView;
	Button messageButton;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_profile_counselor, container, false);
	}

	@Override
	protected void setUpProfile() {
		super.userRealmResults = realm
				.where(UserChat.class)
				.equalTo("id", queryId)
				.findAllAsync();
		UserViewLoader userViewLoader = new UserViewLoader.Builder(userRealmResults)
				.setAvatarView(avatarImageView)
				.setNameView(nameTextView)
				.setStatusView(onlineIndicator)
				.build();
		userViewLoader.startListening();

		messageButton = getView().findViewById(R.id.bt_message);
		if (SyncUser.current().getIdentity().equals(queryId)) {
			messageButton.setVisibility(View.GONE);
		} else {
			messageButton.setOnClickListener(v -> {
				ConversationActivity.open(
						this.getContext(),
						SyncUser.current().getIdentity(),
						queryId
				);
			});
		}
	}
}
