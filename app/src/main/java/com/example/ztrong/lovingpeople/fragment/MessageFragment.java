package com.example.ztrong.lovingpeople.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ztrong.lovingpeople.R;
import com.example.ztrong.lovingpeople.service.common.model.Dialog;
import com.example.ztrong.lovingpeople.service.persistence.DialogData;
import com.example.ztrong.lovingpeople.service.utils.AppUtils;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageFragment extends BaseFragment
		implements DialogsListAdapter.OnDialogClickListener<Dialog>,
		DialogsListAdapter.OnDialogLongClickListener<Dialog> {

	// TODO: MessageFragment
	@BindView(R.id.tb_message)
	Toolbar toolbar;
	@BindView(R.id.dl_message)
	DialogsList dialogsList;

	private ImageLoader imageLoader;
	private DialogsListAdapter<Dialog> dialogsAdapter;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_message, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);
		setUpToolbar(toolbar);
		setUpImageLoader();
		initAdapter();
	}

	private void setUpImageLoader() {
		imageLoader = (imageView, url) -> Picasso.get().load(url).into(imageView);
	}

	private void initAdapter() {
		dialogsAdapter = new DialogsListAdapter<>(imageLoader);
		dialogsAdapter.setItems(DialogData.getDialogs());

		dialogsAdapter.setOnDialogClickListener(this);
		dialogsAdapter.setOnDialogLongClickListener(this);

		dialogsList.setAdapter(dialogsAdapter);
	}

	@Override
	public void onDialogClick(Dialog dialog) {
		AppUtils.showToast(this.getContext(), "onClick", false);
	}

	@Override
	public void onDialogLongClick(Dialog dialog) {
		AppUtils.showToast(this.getContext(), "onLongClick", false);
	}
}
