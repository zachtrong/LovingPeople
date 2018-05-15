package com.example.ztrong.lovingpeople.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ztrong.lovingpeople.R;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.media.audiofx.AudioEffect.CONTENT_TYPE_VOICE;

public class ListenFragment extends BaseFragment {
    // TODO: ListenFragment
    private static ListenFragment listenFragment = new ListenFragment();
    public static ListenFragment getInstance() {
        return listenFragment;
    }

    @BindView(R.id.ml_message)
    MessagesList messagesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }
}
