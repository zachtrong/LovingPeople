package com.example.ztrong.lovingpeople.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ztrong.lovingpeople.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends BaseFragment {
	// TODO: HomeFragment

	@BindView(R.id.tb_home)
	Toolbar toolbar;

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
	}
}
