package net.ddns.zimportant.lovingpeople.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.activity.ListCounselorActivity;
import net.ddns.zimportant.lovingpeople.adapter.ChatRoomsRecyclerAdapter;
import net.ddns.zimportant.lovingpeople.service.common.model.ChatRoom;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.COUNSELOR;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.STORYTELLER;

public class MessageFragment extends BaseFragment {

	@BindView(R.id.tb_message)
	Toolbar toolbar;
	@BindView(R.id.rv_message)
	RecyclerView chatRoomRecyclerView;
	@BindView(R.id.fab_message)
	FloatingActionButton fab;
	@BindView(R.id.bt_message)
	Button switchCurrentUserButton;

	Realm realm;
	RecyclerView.LayoutManager layoutManager;
	String buttonText;
	String chatRoomRoleId;
	RealmResults<UserChat> userChats;
	UserChat currentUser;
	boolean isShowFab;

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
		super.setUpToolbar(toolbar);
		setUpRealm();
		setUpCurrentUser();
		setUpViewDelayed();
	}

	private void setUpRealm() {
		realm = getMainActivity().getRealm();

		userChats = realm
				.where(UserChat.class)
				.findAllAsync();
	}

	private void setUpViewDelayed() {
		userChats.addChangeListener((userChats, changeSet) -> {
			if (userChats.isLoaded()) {
				userChats.removeAllChangeListeners();
				setUpCurrentUser();
				setUpView();
			}
		});
	}

	@SuppressLint("CheckResult")
	private void setUpCurrentUser() {
		currentUser = userChats
				.where()
				.equalTo("id", SyncUser.current().getIdentity())
				.findFirst();
	}

	@SuppressLint("CheckResult")
	private void setUpView() {
		if (currentUser == null || !currentUser.isValid()) {
			getMainActivity().logOutRealm();
			return;
		}
		setUpInformation();
		setUpSwitchButton();
		setUpRecyclerView();
		setUpFab();
	}

	private void setUpInformation() {
		switch (currentUser.getCurrentUserType()) {
			case STORYTELLER:
				buttonText = "Switch to Counselor";
				chatRoomRoleId = "storytellerId";
				isShowFab = true;
				break;
			case COUNSELOR:
				buttonText = "Switch to Storyteller";
				chatRoomRoleId = "counselorId";
				isShowFab = false;
				break;
		}
	}

	protected void setUpSwitchButton() {
		switchCurrentUserButton.setText(buttonText);
		switchCurrentUserButton.setOnClickListener(v -> {
			if (currentUser.getUserType().equals(STORYTELLER)) {
				createAlertDialog();
				return;
			}

			if (currentUser.getCurrentUserType().equals(STORYTELLER)) {
				switchCurrentUser(COUNSELOR);
			} else {
				switchCurrentUser(STORYTELLER);
			}
		});
	}

	private void createAlertDialog() {
		new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert)
				.setTitle("Notification")
				.setMessage("You are not Counselor yet. Please go to Profile to register")
				.setPositiveButton("OK", null)
				.show();
	}

	private void switchCurrentUser(String userRole) {
		realm.executeTransaction(bgRealm -> {
			currentUser.setCurrentUserType(userRole);
		});
		getMainActivity().restartMessageFragment();
	}

	private void setUpRecyclerView() {
		RealmResults<ChatRoom> items = getRealmResults();
		ChatRoomsRecyclerAdapter chatRoomsRecyclerAdapter = new ChatRoomsRecyclerAdapter(items);

		layoutManager = new LinearLayoutManager(getContext());
		chatRoomRecyclerView.setLayoutManager(layoutManager);
		chatRoomRecyclerView.setAdapter(chatRoomsRecyclerAdapter);
	}

	private RealmResults<ChatRoom> getRealmResults() {
		return realm
				.where(ChatRoom.class)
				.equalTo(chatRoomRoleId, currentUser.getId())
				.findAllAsync();
	}

	private void setUpFab() {
		if (isShowFab) {
			fab.setVisibility(View.VISIBLE);
			fab.setOnClickListener(view -> {
				ListCounselorActivity.open(getContext());
			});
		} else {
			fab.setVisibility(View.GONE);
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_message, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		realm.removeAllChangeListeners();
	}
}
