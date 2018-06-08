package net.ddns.zimportant.lovingpeople.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import net.ddns.zimportant.lovingpeople.R;

public class SearchCounselorActivity extends AppCompatActivity
		implements SearchView.OnQueryTextListener {

	@BindView(R.id.tb_search)
	Toolbar toolbar;
	@BindView(R.id.rv_search_counselor)
	RecyclerView recyclerView;

	Realm realm;

	public static void open(Context context) {
		context.startActivity(new Intent(context, SearchCounselorActivity.class));
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpView();
		setUpRealm();
	}

	private void setUpView() {
		setContentView(R.layout.activity_search);
		ButterKnife.bind(this);
		setUpToolbar();
	}

	private void setUpToolbar() {
		setSupportActionBar(toolbar);
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
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}
}
