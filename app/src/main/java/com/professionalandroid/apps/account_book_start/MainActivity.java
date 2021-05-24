package com.professionalandroid.apps.account_book_start;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;

    @BindView(R.id.fab_add)
    FloatingActionButton btn_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        ButterKnife.bind(this);     // butterknife 라이브러리를 이용하여 해당 context에 바인딩
    }
}