package net.ddns.zimportant.lovingpeople.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncUser;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.adapter.UserChatsListRecyclerAdapter;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;

import static android.icu.text.UnicodeSet.CASE;

public class ListCounselorActivity extends AppCompatActivity
		implements SearchView.OnQueryTextListener {

	@BindView(R.id.tb_search)
	Toolbar toolbar;
	@BindView(R.id.rv_search_counselor)
	RecyclerView recyclerView;

	Realm realm;
	RecyclerView.LayoutManager layoutManager;
	UserChatsListRecyclerAdapter userChatsListRecyclerAdapter;

	public static void open(Context context) {
		context.startActivity(new Intent(context, ListCounselorActivity.class));
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpView();
		setUpRealm();
		setUpRecyclerView();
	}

	private void setUpView() {
		setContentView(R.layout.activity_search);
		ButterKnife.bind(this);
		setUpToolbar();
	}

	private void setUpToolbar() {
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Search Counselor");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	private void setUpRealm() {
		realm = Realm.getDefaultInstance();
	}

	private void setUpRecyclerView() {

		RealmResults<UserChat> items = getDefaultRealmResults();
		userChatsListRecyclerAdapter = new UserChatsListRecyclerAdapter(items);

		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(userChatsListRecyclerAdapter);
	}

	private RealmResults<UserChat> getDefaultRealmResults() {
		return realm
				.where(UserChat.class)
				.notEqualTo("id", SyncUser.current().getIdentity())
				.and()
				.equalTo("userType", "Counselor")
				.findAllAsync();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_search, menu);

		MenuItem menuItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) menuItem.getActionView();
		searchView.setOnQueryTextListener(this);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		userChatsListRecyclerAdapter = new UserChatsListRecyclerAdapter(
				getRealmWithFilteredName(query)
		);
		recyclerView.setAdapter(userChatsListRecyclerAdapter);
		return false;
	}

	private RealmResults<UserChat> getRealmWithFilteredName(String query) {
		return realm
				.where(UserChat.class)
				.notEqualTo("id", SyncUser.current().getIdentity())
				.and()
				.equalTo("userType", "Counselor")
				.and()
				.contains("name", query, Case.INSENSITIVE)
				.findAllAsync();
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (newText.length() == 0) {
			userChatsListRecyclerAdapter = new UserChatsListRecyclerAdapter(
					getDefaultRealmResults()
			);
			recyclerView.setAdapter(userChatsListRecyclerAdapter);
		}
		return false;
	}
}
