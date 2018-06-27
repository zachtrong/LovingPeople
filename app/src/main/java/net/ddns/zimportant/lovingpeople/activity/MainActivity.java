package net.ddns.zimportant.lovingpeople.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.fragment.MessageStorytellerFragment;
import net.ddns.zimportant.lovingpeople.fragment.HomeFragment;
import net.ddns.zimportant.lovingpeople.fragment.ProfileCounselorFragment;
import net.ddns.zimportant.lovingpeople.fragment.ProfileStorytellerFragment;
import net.ddns.zimportant.lovingpeople.fragment.ResourceFragment;
import net.ddns.zimportant.lovingpeople.fragment.MessageCounselorFragment;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.RequestHelper;
import net.ddns.zimportant.lovingpeople.service.helper.UserViewLoader;
import net.ddns.zimportant.lovingpeople.service.interfaces.OnCreateConversation;
import net.ddns.zimportant.lovingpeople.service.interfaces.OnResponse;
import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.Constant.COUNSELOR_ID;
import static net.ddns.zimportant.lovingpeople.service.Constant.PARTNER;
import static net.ddns.zimportant.lovingpeople.service.Constant.REQUEST;
import static net.ddns.zimportant.lovingpeople.service.Constant.STORYTELLER_ID;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.STORYTELLER;

public class MainActivity extends BaseActivity
		implements NavigationView.OnNavigationItemSelectedListener,
		OnResponse, OnCreateConversation {

	private static final int DELAY_CLOSE_DRAWER_MS = 200;
	private static final int REQUEST_CODE = 1;

	@BindView(R.id.fl_main)
	FrameLayout frameLayout;
	@BindView(R.id.nav_view)
	NavigationView navigationView;
	@BindView(R.id.drawer_layout)
	DrawerLayout drawerLayout;
	View headerView;

	Realm realm;
	UserChat user;

	public static void open(Context context) {
		context.startActivity(new Intent(context, MainActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpRealm();
		setUpView();
		setUpUserRequest();
	}

	private void setUpRealm() {
		realm = Realm.getDefaultInstance();
	}

	private void setUpView() {
		ButterKnife.bind(this);
		navigationView.setNavigationItemSelectedListener(this);
		setUpHeaderView();
		onNavigationItemSelected(navigationView.getMenu().findItem(R.id.navigation_home));
	}

	private void setUpHeaderView() {
		setUpCurrentUserInfo();
		setUpChildHeaderView();
	}

	private void setUpCurrentUserInfo() {
		 user = realm
				.where(UserChat.class)
				.equalTo("id", SyncUser.current().getIdentity())
				.findFirst();
	}

	private void setUpChildHeaderView() {
		headerView = navigationView.getHeaderView(0);
		RealmResults<UserChat> userChats = realm
				.where(UserChat.class)
				.equalTo("id", user.getId())
				.findAllAsync();
		CircleImageView circleImageView = headerView.findViewById(R.id.civ_avatar);
		TextView userName = headerView.findViewById(R.id.tv_name);
		TextView userId = headerView.findViewById(R.id.tv_id);
		UserViewLoader userViewLoader = new UserViewLoader.Builder(userChats)
				.setAvatarView(circleImageView)
				.setNameView(userName)
				.setIdView(userId)
				.build();
		userViewLoader.startListening();
	}

	public Realm getRealm() {
		return realm;
	}

	public void logOutRealm() {
		SyncUser syncUser = SyncUser.current();
		if (syncUser != null) {
			syncUser.logOut();
			closeAllRealm();
			Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		startFragment(item);
		closeDrawerWithDelay();
		return false;
	}

	private void startFragment(@NonNull MenuItem item) {
		Fragment fragment = getFragmentFromId(item.getItemId());

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fl_main, fragment)
				.commit();
	}

	private void closeDrawerWithDelay() {
		final Handler handler = new Handler();
		handler.postDelayed(() -> drawerLayout.closeDrawers(), DELAY_CLOSE_DRAWER_MS);
	}

	Fragment getFragmentFromId(int id) {
		switch (id) {
			case R.id.navigation_home:
				return new HomeFragment();
			case R.id.navigation_message:
				if (user.getCurrentUserType().equals(STORYTELLER)) {
					return new MessageStorytellerFragment();
				} else {
					return new MessageCounselorFragment();
				}
			case R.id.navigation_resource:
				return new ResourceFragment();
			case R.id.navigation_profile:
				if (user.getCurrentUserType().equals(STORYTELLER)) {
					return new ProfileStorytellerFragment();
				} else {
					return new ProfileCounselorFragment();
				}
			default:
				return new HomeFragment();
		}
	}

	public void restartMessageFragment() {
		startFragment(navigationView.getMenu().findItem(R.id.navigation_message));
	}

	public void restartProfileFragment() {
		startFragment(navigationView.getMenu().findItem(R.id.navigation_profile));
	}

	private void setUpUserRequest() {
		RequestHelper.getInstance()
				.register(this, realm);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeAllRealm();
	}

	private void closeAllRealm() {
		realm.close();
	}

	public ActionBarDrawerToggle registerDrawerToggle(Toolbar toolbar) {
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this,
				drawerLayout,
				toolbar,
				R.string.navigation_drawer_open,
				R.string.navigation_drawer_close
		);
		drawerLayout.addDrawerListener(toggle);
		toggle.syncState();
		return toggle;
	}

	public void unRegisterDrawerToggle(ActionBarDrawerToggle drawerToggle) {
		drawerLayout.removeDrawerListener(drawerToggle);
	}

	@Override
	public void onResponse(String requestId) {
		Intent intent = new Intent(this, ResponseActivity.class);
		intent.putExtra(REQUEST, requestId);
		startActivityForResult(intent, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Bundle bundle = data.getExtras();

				String error = bundle.getString("error");
				if (error != null) {
					AppUtils.showToast(this, error, true);
				} else {
					String storytellerId = bundle.getString(STORYTELLER_ID);
					String counselorId = bundle.getString(COUNSELOR_ID);
					ConversationActivity.open(this, storytellerId, counselorId);
				}
			}
		}
	}

	@Override
	public void onOpenConversation(String storytellerId, String counselorId) {
		ConversationActivity.open(this, storytellerId, counselorId);
	}
}
