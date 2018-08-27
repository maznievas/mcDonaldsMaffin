package com.example.andrey.mcdonaldsmaffin.main_activity;

import android.content.Intent;
import android.os.Bundle;

import com.arellomobile.mvp.MvpActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.andrey.mcdonaldsmaffin.R;
import com.example.andrey.mcdonaldsmaffin.edit_activity.EditActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends MvpActivity implements MainView {
    @InjectPresenter
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.enterManuallyButton)
    public void onEnterManuallyClicked(){
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }
}
