package net.ddns.zimportant.lovingpeople.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.stfalcon.frescoimageviewer.ImageViewer;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.activity.RegisterActivity;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.UserViewLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.COUNSELOR;

public class ProfileFragment extends BaseFragment {

	@BindView(R.id.tb_profile)
	Toolbar toolbar;
	@BindView(R.id.iv_avatar_profile)
	ImageView avatarImageView;
	@BindView(R.id.tv_name_profile)
	TextView nameTextView;
	@BindView(R.id.onlineIndicator)
	ImageView onlineIndicator;
	Button buttonRegister;

	Realm realm;
	String queryId;
	RealmResults<UserChat> userRealmResults;

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
		setHasOptionsMenu(true);
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
		setUpProfile();
		setUpAvatar();
	}

	private void setUpView(View view) {
		ButterKnife.bind(this, view);
		Fresco.initialize(getContext());
		super.setUpToolbar(toolbar);
	}

	private void setUpRealm() {
		realm = getMainActivity().getRealm();
	}

	protected void setUpProfile() {
		UserChat user = realm
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

	private void setUpAvatar() {
		avatarImageView.setOnClickListener(v -> {
			if (userRealmResults.isLoaded()) {
				String[] images = {userRealmResults.first().getAvatarUrl()};
				new ImageViewer.Builder(getContext(), images)
						.setStartPosition(0)
						.build()
						.show();
			}
		});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_profile, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_logout) {
			getMainActivity().logOutRealm();
		}
		return true;
	}
}
