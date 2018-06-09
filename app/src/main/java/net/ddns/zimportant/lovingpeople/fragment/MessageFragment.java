package net.ddns.zimportant.lovingpeople.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatCheckedTextView;
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
import net.ddns.zimportant.lovingpeople.activity.MainActivity;
import net.ddns.zimportant.lovingpeople.adapter.ChatRoomsRecyclerAdapter;
import net.ddns.zimportant.lovingpeople.service.common.model.ChatRoom;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.ObjectChangeSet;
import io.realm.Realm;
import io.realm.RealmModel;
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
	UserChat currentUser;
	RecyclerView.LayoutManager layoutManager;
	String buttonText;
	String chatRoomRoleId;
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
		setUpUser();
	}

	private void setUpRealm() {
		realm = Realm.getDefaultInstance();
	}

	private void setUpUser() {
		realm
				.where(UserChat.class)
				.findAllAsync();

		realm
				.where(UserChat.class)
				.equalTo("id", SyncUser.current().getIdentity())
				.findFirstAsync()
				.addChangeListener((UserChat userChat) -> {
					currentUser = userChat;
					if (!(userChat.getId()).equals(SyncUser.current().getIdentity())) {
						((MainActivity) getContext()).logOutRealm();
						return;
					}
					setUpInformation();
					setUpSwitchButton();
					setUpRecyclerView();
					setUpFab();
					//currentUser.removeAllChangeListeners();
				});
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
				isShowFab = true;
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
			((MainActivity) getContext()).restartMessageFragment();
		});
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
			fab.setVisibility(View.INVISIBLE);
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
	public void onDestroy() {
		super.onDestroy();
		realm.close();
	}
}
