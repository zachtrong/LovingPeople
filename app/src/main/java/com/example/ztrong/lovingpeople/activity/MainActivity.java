package com.example.ztrong.lovingpeople.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.ztrong.lovingpeople.R;
import com.example.ztrong.lovingpeople.R2;
import com.example.ztrong.lovingpeople.fragment.FriendFragment;
import com.example.ztrong.lovingpeople.fragment.HomeFragment;
import com.example.ztrong.lovingpeople.fragment.ListenFragment;
import com.example.ztrong.lovingpeople.fragment.ShareFragment;
import com.example.ztrong.lovingpeople.service.messenger.Constant;
import com.example.ztrong.lovingpeople.service.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class MainActivity extends BaseActivity {

    private final static String TAG = "Main";

    @BindView(R.id.nav_main)
    BottomNavigationView navigation;

    @BindView(R.id.fl_main)
    FrameLayout frameLayout;

    @BindView(R.id.tb_main)
    Toolbar toolbar;

    Realm realm;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        Fragment mFragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                mFragment = HomeFragment.getInstance();
                break;
            case R.id.navigation_share:
                mFragment = ShareFragment.getInstance();
                break;
            case R.id.navigation_listen:
                mFragment = ListenFragment.getInstance();
                break;
            case R.id.navigation_friends:
                mFragment = FriendFragment.getInstance();
                break;
        }

        if (mFragment == null) {
            return false;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_main, mFragment)
                .commit();

        return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            SyncUser syncUser = SyncUser.current();
            if (syncUser != null) {
                syncUser.logOut();
                Intent intent = new Intent(this, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
