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

import butterknife.BindView;
import io.realm.RealmResults;
import io.realm.SyncUser;

public class ProfileCounselorFragment extends ProfileFragment {

	@BindView(R.id.tv_introduce)
	TextView introduce;
	@BindView(R.id.tv_birth)
	TextView birth;
	@BindView(R.id.tv_address)
	TextView address;
	@BindView(R.id.tv_experience)
	TextView experience;

	Button messageButton;
	Button becomeStoryteller;

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
				.setIntroduceView(introduce)
				.setBirthView(birth)
				.setAddressView(address)
				.setExperienceView(experience)
				.build();
		userViewLoader.startListening();

		messageButton = getView().findViewById(R.id.bt_message);
		becomeStoryteller = getView().findViewById(R.id.bt_become_storyteller);
		if (SyncUser.current().getIdentity().equals(queryId)) {
			messageButton.setVisibility(View.GONE);
			becomeStoryteller.setVisibility(View.VISIBLE);

			UserChat user = realm
					.where(UserChat.class)
					.equalTo("id", queryId)
					.findFirst();
			becomeStoryteller.setOnClickListener(v -> {
				realm.executeTransaction(bgRealm -> {
					user.setCurrentUserType(UserChat.STORYTELLER);
				});
				getMainActivity().restartProfileFragment();
			});
		} else {
			messageButton.setVisibility(View.VISIBLE);
			becomeStoryteller.setVisibility(View.GONE);

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
