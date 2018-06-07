package net.ddns.zimportant.lovingpeople.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.activity.ConversationActivity;
import net.ddns.zimportant.lovingpeople.service.common.model.ChatRoom;
import net.ddns.zimportant.lovingpeople.service.persistence.DialogData;
import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MessageFragment extends BaseFragment {

	@BindView(R.id.tb_message)
	Toolbar toolbar;
	@BindView(R.id.dl_message)
	DialogsList dialogsList;

	Realm realm;

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
		setupRecyclerView();
	}

	private void setupRecyclerView() {
		RealmResults<ChatRoom> items = setupRealm();
	}

	private RealmResults setupRealm() {
		realm = Realm.getDefaultInstance();

		return realm
				.where(ChatRoom.class)
				.findAllAsync();
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
}
