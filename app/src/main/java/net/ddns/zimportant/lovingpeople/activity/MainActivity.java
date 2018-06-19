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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.fragment.HomeFragment;
import net.ddns.zimportant.lovingpeople.fragment.MessageFragment;
import net.ddns.zimportant.lovingpeople.fragment.ProfileCounselorFragment;
import net.ddns.zimportant.lovingpeople.fragment.ProfileFragment;
import net.ddns.zimportant.lovingpeople.fragment.ResourceFragment;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.ResponseHelper;
import net.ddns.zimportant.lovingpeople.service.interfaces.OnCreateConversation;
import net.ddns.zimportant.lovingpeople.service.interfaces.OnResponse;
import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.Constant.COUNSELOR_ID;
import static net.ddns.zimportant.lovingpeople.service.Constant.PARTNER;
import static net.ddns.zimportant.lovingpeople.service.Constant.STORYTELLER_ID;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.COUNSELOR;

public class MainActivity extends BaseActivity
		implements NavigationView.OnNavigationItemSelectedListener,
		OnResponse, OnCreateConversation {

	private static final int DELAY_CLOSE_DRAWER_MS = 100;
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
		setUpAvatar();
		setUpUserName();
		setUpUserId();
	}

	private void setUpAvatar() {
		CircleImageView circleImageView = headerView.findViewById(R.id.civ_avatar);
		Picasso.get().load(user.getAvatarUrl()).into(circleImageView);
	}

	private void setUpUserName() {
		TextView userName = headerView.findViewById(R.id.tv_name);
		userName.setText(user.getName());
	}

	private void setUpUserId() {
		TextView userId = headerView.findViewById(R.id.tv_id);
		userId.setText(user.getId());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_logout) {
			logOutRealm();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public Realm getRealm() {
		return realm;
	}

	public void setRealm(Realm realm) {
		this.realm = realm;
	}

	public void logOutRealm() {
		SyncUser syncUser = SyncUser.current();
		if (syncUser != null) {
			syncUser.logOut();
			closeAllRealm();
			Realm.deleteRealm(Realm.getDefaultConfiguration());
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
				return new MessageFragment();
			case R.id.navigation_resource:
				return new ResourceFragment();
			case R.id.navigation_profile:
				if (user.getUserType().equals(COUNSELOR)) {
					return new ProfileCounselorFragment();
				} else {
					return new ProfileFragment();
				}
			default:
				return new HomeFragment();
		}
	}

	public void restartMessageFragment() {
		startFragment(navigationView.getMenu().findItem(R.id.navigation_message));
	}

	private void setUpUserRequest() {
		ResponseHelper.getInstance()
				.register(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeAllRealm();
	}

	private void closeAllRealm() {
		realm.close();
		ResponseHelper.getInstance()
				.unregister();
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
	public void onResponse(String partner) {
		Intent intent = new Intent(this, ResponseActivity.class);
		intent.putExtra(PARTNER, partner);
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
