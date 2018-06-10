package net.ddns.zimportant.lovingpeople.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.UserHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncUser;

public class ProfileFragment extends BaseFragment {

	@BindView(R.id.tb_profile)
	Toolbar toolbar;
	@BindView(R.id.iv_avatar_profile)
	ImageView avatarImageView;
	@BindView(R.id.tv_name_profile)
	TextView nameTextView;
	@BindView(R.id.onlineIndicator)
	ImageView onlineIndicator;

	Realm realm;
	RealmResults<UserChat> userChats;
	UserChat queryUser;
	String queryId;

	public static ProfileFragment newInstance(String queryId) {
		ProfileFragment profileFragment = new ProfileFragment();
		Bundle args = new Bundle();
		args.putString("id", queryId);
		profileFragment.setArguments(args);
		return profileFragment;
	}

	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			queryId = getArguments().getString("id");
		}
		if (queryId == null) {
			queryId = SyncUser.current().getIdentity();
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_profile, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setUpView(view);
		setUpRealm();
		setUpProfileAsync();
	}

	private void setUpView(View view) {
		ButterKnife.bind(this, view);
		Fresco.initialize(getContext());
		super.setUpToolbar(toolbar);
	}

	private void setUpRealm() {
		realm = getMainActivity().getRealm();
		userChats = realm
				.where(UserChat.class)
				.findAllAsync();
	}

	private void setUpProfileAsync() {
		userChats.addChangeListener((userChats) -> {
			if (userChats.isLoaded()) {
				userChats.removeAllChangeListeners();
				setUpCurrentUser();
				setUpProfile();
			}
		});
	}

	private void setUpCurrentUser() {
		queryUser = userChats
				.where()
				.equalTo("id", queryId)
				.findFirst();
	}

	private void setUpProfile() {
		setUpAvatar();
		setUpName();
		setUpOnlineIndicator();
	}

	private void setUpAvatar() {
		Picasso.get()
				.load(queryUser.getAvatarUrl())
				.into(avatarImageView);
		avatarImageView.setOnClickListener(v -> {
			String[] images = {queryUser.getAvatarUrl()};
			new ImageViewer.Builder(getContext(), images)
					.setStartPosition(0)
					.build()
					.show();
		});
	}

	private void setUpName() {
		nameTextView.setText(queryUser.getName());
	}

	private void setUpOnlineIndicator() {
		onlineIndicator.setImageResource(
				UserHelper.getOnlineIndicatorResource(queryUser.getStatus())
		);
	}
}
