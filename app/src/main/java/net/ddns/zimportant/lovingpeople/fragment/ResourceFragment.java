package net.ddns.zimportant.lovingpeople.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.adapter.ResourceTypeRecyclerAdapter;
import net.ddns.zimportant.lovingpeople.service.common.model.ResourceType;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ResourceFragment extends BaseFragment {

	@BindView(R.id.tb_resource)
	Toolbar toolbar;
	@BindView(R.id.rv_resource)
	RecyclerView recyclerView;

	ResourceTypeRecyclerAdapter adapter;
	RecyclerView.LayoutManager layoutManager;
	Realm realm;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_resource, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);
		setUpToolbar(toolbar);

		setUpRecyclerView();
	}

	private void setUpRecyclerView() {
		RealmResults<ResourceType> items = setUpRealm();
		adapter = new ResourceTypeRecyclerAdapter(items);
		layoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapter);
	}

	private RealmResults<ResourceType> setUpRealm() {
		realm = getMainActivity().getRealm();
		return realm
				.where(ResourceType.class)
				.sort("title", Sort.ASCENDING)
				.findAllAsync();
	}
}
