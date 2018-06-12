package net.ddns.zimportant.lovingpeople.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.interfaces.OnNotifyRegisterFragment;
import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyPoliciesFragment extends Fragment {

	@BindView(R.id.fab_privacy_policies)
	FloatingActionButton floatingActionButton;
	@BindView(R.id.cb_privacy_policies)
	CheckBox checkBox;
	boolean isChecked = false;

	OnNotifyRegisterFragment callBackRegister;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		try {
			callBackRegister = (OnNotifyRegisterFragment) context;
		} catch (Exception e) {
			throw new ClassCastException(context.toString() +
					"must implement OnNotifyRegisterFragment");
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_agreement, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);

		checkBox.setText(getResources().getString(R.string.accept_privacy));
		checkBox.setOnClickListener(v -> {
			isChecked = !isChecked;
		});

		floatingActionButton.setOnClickListener(v -> {
			if (isChecked) {
				callBackRegister.onNextFragment();
			} else {
				AppUtils.showToast(
						getContext(),
						"You must agree with our Privacy Policies to continue",
						true);
			}
		});
	}
}
