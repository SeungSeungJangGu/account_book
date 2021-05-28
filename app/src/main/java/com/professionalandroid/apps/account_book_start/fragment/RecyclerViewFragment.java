package com.professionalandroid.apps.account_book_start.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.professionalandroid.apps.account_book_start.DataBase.DTO;
import com.professionalandroid.apps.account_book_start.R;
import com.professionalandroid.apps.account_book_start.TestRecyclerViewAdapter;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
*   각 요소의 각들을 출력할 Recycler 뷰 Fragment
*/
public class RecyclerViewFragment extends Fragment {

    private static final boolean GRID_LAYOUT = false;       // GridLayout 사용 여부
    private ArrayList<DTO> data_list;                       // Item 리스트


    // butterknife 라이브러리를 이용하여
    // 위젯객체와 View를 Bind
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    /*
    *   @Params ArrayList<DTO> data_list 어댑터에 넘겨줄 데이터 List 객체
    */
    public RecyclerViewFragment(ArrayList<DTO> data_list){
        this.data_list = data_list;
    }

    // RecyclerViewFragment 를 생성후 반환
    public static RecyclerViewFragment newInstance(ArrayList<DTO> data_list) {
        return new RecyclerViewFragment(data_list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // layout 뷰를 연결해줌
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);



        // materialviewpager 셋업
        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(new TestRecyclerViewAdapter(data_list));
    }
}
