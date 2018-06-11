package net.ddns.zimportant.lovingpeople.activity;

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
import net.ddns.zimportant.lovingpeople.fragment.ProfileFragment;
import net.ddns.zimportant.lovingpeople.fragment.ResourceFragment;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.RealmHelper;
import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.ObjectChangeSet;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObjectChangeListener;
import io.realm.RealmResults;
import io.realm.SyncUser;

public class MainActivity extends BaseActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	private static final int DELAY_CLOSE_DRAWER_MS = 100;

	@BindView(R.id.fl_main)
	FrameLayout frameLayout;
	@BindView(R.id.nav_view)
	NavigationView navigationView;
	@BindView(R.id.drawer_layout)
	DrawerLayout drawerLayout;
	View headerView;

	Realm realm;
	UserChat currentUser;

	public static void open(Context context) {
		context.startActivity(new Intent(context, MainActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpRealm();
		setUpView();
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
		 currentUser = realm
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
		Picasso.get().load(currentUser.getAvatarUrl()).into(circleImageView);

	}

	private void setUpUserName() {
		TextView userName = headerView.findViewById(R.id.tv_name);
		userName.setText(currentUser.getName());
	}

	private void setUpUserId() {
		TextView userId = headerView.findViewById(R.id.tv_id);
		userId.setText(currentUser.getId());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
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
			Intent intent = new Intent(this, WelcomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
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
				return new ProfileFragment();
			default:
				return new HomeFragment();
		}
	}

	public void restartMessageFragment() {
		startFragment(navigationView.getMenu().findItem(R.id.navigation_message));
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
}
