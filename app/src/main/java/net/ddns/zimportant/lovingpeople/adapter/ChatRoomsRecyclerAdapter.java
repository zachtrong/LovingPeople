package net.ddns.zimportant.lovingpeople.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.ChatRoom;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.utils.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_BUSY;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_OFFLINE;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_ONLINE;

public class ChatRoomsRecyclerAdapter
		extends RealmRecyclerViewAdapter<ChatRoom, ChatRoomsRecyclerAdapter.ChatRoomViewHolder> {

	public ChatRoomsRecyclerAdapter(@Nullable OrderedRealmCollection<ChatRoom> data) {
		super(data, true);
	}

	@NonNull
	@Override
	public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_chatroom, parent, false);
		return new ChatRoomViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
		holder.setItem(getItem(position));
	}

	static class ChatRoomViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.dialogAvatar)
		CircleImageView imageView;
		@BindView(R.id.onlineIndicator)
		ImageView onlineIndicatorView;
		@BindView(R.id.dialogName)
		TextView chatRoomName;
		@BindView(R.id.dialogLastMessage)
		TextView chatRoomLastMessage;
		@BindView(R.id.dialogDate)
		TextView chatRoomLastDate;

		ChatRoom chatRoom;
		UserChat user;

		ChatRoomViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		void setItem(ChatRoom chatRoom) {
			this.chatRoom = chatRoom;
			updateLayout();
		}

		private void updateLayout() {
			updateUser();
			updateChatRoomInfo();
		}

		private void updateUser() {
			getUserFromChatRoom();
			updateImageChatRoom();
			updateOnlineIndicator();
			updateChatRoomName();
		}

		private void getUserFromChatRoom() {
			user = chatRoom
					.getRealm()
					.where(UserChat.class)
					.findFirst();

			if (user == null) {
				throw new Error("HA");
			}
		}

		private void updateImageChatRoom() {
			Picasso.get()
					.load(user.getAvatarUrl())
					.into(imageView);
		}

		private void updateOnlineIndicator() {
			switch (user.getStatus()) {
				case USER_ONLINE:
					onlineIndicatorView.setImageResource(R.drawable.shape_bubble_online);
					break;
				case USER_OFFLINE:
					onlineIndicatorView.setImageResource(R.drawable.shape_bubble_offline);
					break;
				case USER_BUSY:
					onlineIndicatorView.setImageResource(R.drawable.shape_bubble_busy);
					break;
			}
		}

		private void updateChatRoomName() {
			chatRoomName.setText(user.getName());
		}

		private void updateChatRoomInfo() {
			updateChatRoomLastMessage();
			updateChatRoomLastDate();
		}

		private void updateChatRoomLastMessage() {
			chatRoomLastMessage.setText(
					chatRoom.getMessages().last().getBody()
			);
		}

		private void updateChatRoomLastDate() {
			chatRoomLastDate.setText(getFormatLastDate());
		}

		private String getFormatLastDate() {
			return FormatUtils.getDurationString(
					chatRoom.getMessages().first().getCreatedAt()
			);
		}
	}
}
