package net.ddns.zimportant.lovingpeople.fragment;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.activity.RegisterActivity;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.UserViewLoader;

import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.COUNSELOR;

public class ProfileStorytellerFragment extends ProfileFragment {
	UserChat user;
	@Override
	protected void setUpProfile() {
		user = realm
				.where(UserChat.class)
				.equalTo("id", queryId)
				.findFirst();
		userRealmResults = realm
				.where(UserChat.class)
				.equalTo("id", queryId)
				.findAllAsync();
		UserViewLoader userViewLoader = new UserViewLoader.Builder(userRealmResults)
				.setAvatarView(avatarImageView)
				.setNameView(nameTextView)
				.setStatusView(onlineIndicator)
				.build();
		userViewLoader.startListening();

		buttonRegister = getView().findViewById(R.id.bt_become_counselor);
		buttonRegister.setOnClickListener(v -> {
			if (user.getUserType().equals(COUNSELOR)) {
				realm.executeTransaction(bgRealm -> {
					user.setCurrentUserType(COUNSELOR);
					getMainActivity().restartProfileFragment();
				});
			} else {
				RegisterActivity.open(getContext());
			}
		});
	}

	@Override
	protected UserChat getUser() {
		return user;
	}
}
