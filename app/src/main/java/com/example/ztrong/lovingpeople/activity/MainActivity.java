package com.example.ztrong.lovingpeople.activity;

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
import android.widget.FrameLayout;

import com.example.ztrong.lovingpeople.R;
import com.example.ztrong.lovingpeople.fragment.HomeFragment;
import com.example.ztrong.lovingpeople.fragment.MessageFragment;
import com.example.ztrong.lovingpeople.fragment.ResourceFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.SyncUser;

public class MainActivity extends BaseActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	private static final int DELAY_CLOSE_DRAWER_MS = 300;

	@BindView(R.id.fl_main)
	FrameLayout frameLayout;
	@BindView(R.id.nav_view)
	NavigationView navigationView;
	@BindView(R.id.drawer_layout)
	DrawerLayout drawerLayout;

	Realm realm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpData();
		setUpView();
	}

	private void setUpData() {
		realm = Realm.getDefaultInstance();
	}

	private void setUpView() {
		ButterKnife.bind(this);
		navigationView.setNavigationItemSelectedListener(this);
		onNavigationItemSelected(navigationView.getMenu().findItem(R.id.navigation_home));
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
			SyncUser syncUser = SyncUser.current();
			if (syncUser != null) {
				syncUser.logOut();
				Intent intent = new Intent(this, WelcomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			default:
				return new HomeFragment();
		}
	}

	public ActionBarDrawerToggle registerDrawerToggle(Toolbar toolbar) {
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this,
				drawerLayout,
				toolbar,
				R.string.navigation_drawer_close,
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
