package com.professionalandroid.accountbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.professionalandroid.accountbook.DataBase.DTO;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class TestRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Item 의 리스트
    ArrayList<DTO> contents;
    int totoal = 0;

    // butterknife 라이브러리를 이용하여
    // 위젯과 뷰를 바인딩
    TextView tv_date;
    TextView tv_no;
    TextView tv_income;
    TextView tv_expend;
    TextView tv_total;
    TextView tv_content;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    // TestRecyclerViewAdapter 생성자
    public TestRecyclerViewAdapter(ArrayList<DTO> contents) {
        this.contents = contents;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;


        switch (viewType) {
            // 뷰타입이 헤더인 경우,
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_big, parent, false);
                ButterKnife.bind(view);
                // 각 위젯 들을 뷰와 바인딩해줌
                tv_date     = view.findViewById(R.id.tv_small_card_date);
                tv_no       = view.findViewById(R.id.tv_small_card_no);
                tv_income   = view.findViewById(R.id.tv_small_card_income);
                tv_expend   = view.findViewById(R.id.tv_small_card_expend);
                tv_total    = view.findViewById(R.id.tv_small_card_total);
                tv_content  = view.findViewById(R.id.tv_small_card_contents);

                return new RecyclerView.ViewHolder(view) {
                };


            }
            // 뷰타입이 셀 인 경우,
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_small, parent, false);
                ButterKnife.bind(view);

                // 각 위젯들을 뷰와 바인딩 해줌
                tv_date     = view.findViewById(R.id.tv_small_card_date);
                tv_no       = view.findViewById(R.id.tv_small_card_no);
                tv_income   = view.findViewById(R.id.tv_small_card_income);
                tv_expend   = view.findViewById(R.id.tv_small_card_expend);
                tv_total    = view.findViewById(R.id.tv_small_card_total);
                tv_content  = view.findViewById(R.id.tv_small_card_contents);

                return new RecyclerView.ViewHolder(view) {
                };
            }
        }


        return null;
    }


    // 각 item 들에 위젯의 값을 입력
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        System.out.println(position);
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                tv_content.setText(contents.get(position).getContent());
                tv_date.setText(contents.get(position).getDate());
                tv_no.setText(String.valueOf(contents.get(position).getId()));
                tv_income.setText(contents.get(position).getIncome() + "원");
                tv_expend.setText(contents.get(position).getExpends() + "원");
                tv_total.setText(String.valueOf(contents.get(position).getTotal()));

                break;
            case TYPE_CELL:
                tv_content.setText(contents.get(position).getContent());
                tv_date.setText(contents.get(position).getDate());
                tv_no.setText(String.valueOf(contents.get(position).getId()));
                tv_income.setText(contents.get(position).getIncome() + "원");
                tv_expend.setText(contents.get(position).getExpends() + "원");
                tv_total.setText(String.valueOf(contents.get(position).getTotal()));
                break;
        }
    }
}