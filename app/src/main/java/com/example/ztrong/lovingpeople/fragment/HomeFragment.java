package com.example.ztrong.lovingpeople.fragment;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.example.ztrong.lovingpeople.R;
import com.example.ztrong.lovingpeople.adapter.HomePostsListAdapter;
import com.example.ztrong.lovingpeople.service.common.model.HomeItem;
import com.example.ztrong.lovingpeople.service.persistence.HomePostsData;
import com.example.ztrong.lovingpeople.service.utils.AppUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.SyncConfiguration;

public class HomeFragment extends BaseFragment {

	@BindView(R.id.tb_home)
	Toolbar toolbar;
	@BindView(R.id.rv_home)
	RecyclerView recyclerView;

	RecyclerView.LayoutManager layoutManager;

	Realm realm;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);
		setUpToolbar(toolbar);
		setUpRecyclerView();
	}

	private void setUpRecyclerView() {
		RealmResults<HomeItem> items = setUpRealm();
		HomePostsListAdapter homePostsListAdapter = new HomePostsListAdapter(items);

		layoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(homePostsListAdapter);

		/*
		realm.executeTransaction(realm -> {
			ArrayList<String> data = HomePostsData.getHomePosts();
			for (int i = 0; i < 20; ++i)
				realm.insert(new HomeItem(String.valueOf(i), data.get(i)));
		});
		*/
	}

	private RealmResults<HomeItem> setUpRealm() {
		Realm.setDefaultConfiguration(SyncConfiguration.automatic());
		realm = Realm.getDefaultInstance();

		return realm
				.where(HomeItem.class)
				.sort("timestamp", Sort.DESCENDING)
				.findAllAsync();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		realm.close();
	}
}
