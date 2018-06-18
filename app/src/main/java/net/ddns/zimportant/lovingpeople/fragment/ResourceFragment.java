package net.ddns.zimportant.lovingpeople.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.zimportant.lovingpeople.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResourceFragment extends BaseFragment {

	@BindView(R.id.tb_resource)
	Toolbar toolbar;
	@BindView(R.id.rv_resource)
	RecyclerView recyclerView;

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

	}
}
