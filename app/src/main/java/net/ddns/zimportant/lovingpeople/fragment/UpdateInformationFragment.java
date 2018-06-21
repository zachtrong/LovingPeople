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
import android.widget.EditText;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.interfaces.OnNotifyRegisterFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_ADD_INFO;

public class UpdateInformationFragment extends Fragment {

	@BindView(R.id.et_register_full_name)
	EditText fullName;
	@BindView(R.id.et_register_introduce)
	EditText introduce;
	@BindView(R.id.et_register_birth)
	EditText birth;
	@BindView(R.id.et_register_address)
	EditText address;
	@BindView(R.id.et_register_experience)
	EditText experience;
	@BindView(R.id.fab_next)
	FloatingActionButton fabNext;

	OnNotifyRegisterFragment callBackRegister;
	Realm realm;
	UserChat user;
	Disposable checkUser;

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
		return inflater.inflate(R.layout.fragment_update_information, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);

		setUpRealm();
		setUpUser();
	}

	private void setUpRealm() {
		realm = Realm.getDefaultInstance();
	}

	private void setUpUser() {
		checkUser = realm
				.where(UserChat.class)
				.equalTo("id", SyncUser.current().getIdentity())
				.findAllAsync()
				.asFlowable()
				.filter(RealmResults::isLoaded)
				.filter(realmResults -> realmResults.size() != 0)
				.subscribe(realmResults -> {
					checkUser.dispose();
					user = realmResults.first();
					fabNext.setOnClickListener(v -> onFabClick());
				});
	}

	private void onFabClick() {
		if (isAcceptedAllEditText()) {
			updateUserInformation();
			callBackRegister.onNextFragment();
		}
	}

	private boolean isAcceptedAllEditText() {
		return isAcceptedEditText(fullName)
				&& isAcceptedEditText(introduce)
				&& isAcceptedEditText(birth)
				&& isAcceptedEditText(address)
				&& isAcceptedEditText(experience);
	}

	private boolean isAcceptedEditText(EditText editText) {
		if (editText.getText().toString().length() == 0) {
			editText.setError(ERR_ADD_INFO);
			return false;
		}
		return true;
	}

	private void updateUserInformation() {
		realm.executeTransaction(bgRealm -> {
			user.setName(fullName.getText().toString());
			user.setIntroduce(introduce.getText().toString());
			user.setBirth(birth.getText().toString());
			user.setAddress(address.getText().toString());
			user.setExperience(experience.getText().toString());
			user.setUserType(UserChat.COUNSELOR);
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		realm.close();
	}
}
