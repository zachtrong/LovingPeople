package net.ddns.zimportant.lovingpeople.adapter.holder;

import android.view.View;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.Dialog;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static net.ddns.zimportant.lovingpeople.service.messenger.Constant.USER_BUSY;
import static net.ddns.zimportant.lovingpeople.service.messenger.Constant.USER_OFFLINE;
import static net.ddns.zimportant.lovingpeople.service.messenger.Constant.USER_ONLINE;

public class MessageDialogViewHolder extends DialogsListAdapter.DialogViewHolder<Dialog> {

	@BindView(R.id.onlineIndicator)
	View onlineIndicator;

	public MessageDialogViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}

	@Override
	public void onBind(Dialog dialog) {
		super.onBind(dialog);

		String status = dialog.getUsers().get(0).getStatus();
		onlineIndicator.setVisibility(View.VISIBLE);
		switch (status) {
			case USER_ONLINE:
				onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_online);
				break;
			case USER_BUSY:
				onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_busy);
				break;
			case USER_OFFLINE:
				onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_offline);
				break;
		}
	}
}
