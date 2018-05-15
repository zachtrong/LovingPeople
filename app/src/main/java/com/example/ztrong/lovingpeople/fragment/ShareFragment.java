package com.example.ztrong.lovingpeople.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ztrong.lovingpeople.R;
import com.example.ztrong.lovingpeople.R2;
import com.example.ztrong.lovingpeople.service.model.Message;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareFragment extends BaseFragment {
    private static ShareFragment shareFragment = new ShareFragment();

    public static ShareFragment getInstance() {
        return shareFragment;
    }


    // TODO: ShareFragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
