package com.professionalandroid.accountbook;

import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.professionalandroid.accountbook.DataBase.DbOpenHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsertDataView extends AppCompatActivity {

    String date_str ="";

    // categoory_flag : 1 => 수입 모드
    // categoory_flag : 2 => 지출 모드
    int categoory_flag = 1;

    // 데이터베이스 개체 생성
    DbOpenHelper mDbOpenHelper = new DbOpenHelper(this);

    // ButterKnife 라이브러리를 이용하여
    // 위젯과 뷰를 바인딩 해줌
    @BindView(R.id.btn_insert_save)
    Button btn_save;

    @BindView(R.id.btn_insert_cancle)
    Button btn_cancle;

    @BindView(R.id.btn_insert_income)
    Button btn_income;

    @BindView(R.id.btn_insert_expend)
    Button btn_expend;

    @BindView(R.id.et_insert_price)
    EditText et_price;

    @BindView(R.id.et_insert_content)
    EditText et_content;

    @BindView(R.id.tv_insert_date)
    TextView tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data_view);
        ButterKnife.bind(this);         // ButterKnife 로 선언된 BingView annotation데 따라 위젝과 뷰를 바인딩해줌

        // 달력 다이어로그 이벤트 객체 리스너 선언
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
                String str = String.valueOf(year) + "년" + String.valueOf(monthOfYear+1) + "월" + String.valueOf(dayOfMonth) + "일";
                date_str = String.valueOf(year) + "-" + String.valueOf(monthOfYear+1) + "-" + String.valueOf(dayOfMonth);
                tv_date.setText(str);
            }
        };



        //  tv_date 를 클릭 이벤트 리스너 생성및 삽입
        tv_date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // Java 캘린더 객체 생성
                Calendar now = Calendar.getInstance();
                // DatePickerDialog를 캘린더 객체에서 가져온 년 월 일로 생성
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        onDateSetListener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                // DatePickerDialog 화면 출력
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        // 지출 버튼 클릭 이벤트 리스너 생성 및 삽입
        btn_expend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // categoory_flag 를 2로 선언
                categoory_flag = 2;
                // btn_expend 위젯의 색상을 초록색으로 선언
                btn_expend.setBackgroundColor(Color.GREEN);
                // btn_income 위젯의 색상을 하얀색으로 선언
                btn_income.setBackgroundColor(Color.WHITE);
            }
        });

        // 수입 버튼 클릭 이벤트 리스너 생성 및 삽입
        btn_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // categoory_flag 를 1로 선언
                categoory_flag = 1;
                // btn_expend 위젯의 색을하얀색으로 선언
                btn_expend.setBackgroundColor(Color.WHITE);
                // btn_income 위젯의 색상을 초록색으로 선언
                btn_income.setBackgroundColor(Color.GREEN);
            }
        });


        // 저장 버튼 클릭 이벤트 리스너 생성 및 삽입
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_price.getText().toString().equals("")){          // 금액이 입력되지 않았을 경우,
                    Toast.makeText(getApplicationContext(),"금액을 입력해 주세요.",Toast.LENGTH_LONG).show();
                }else if(et_content.getText().toString().equals("")){  // 내용이 입력되지 않았을 경우,
                    Toast.makeText(getApplicationContext(),"내용을 입력해 주세요.",Toast.LENGTH_LONG).show();
                }else if(date_str.equals("")) {             // 날짜가 입력되지 않았을 경우,
                    Toast.makeText(getApplicationContext(),"날짜를 선택해 주세요.",Toast.LENGTH_LONG).show();
                } else {
                    // 각 위젯에서
                    // 내용과 금액의 값을 가져옴
                    String content = et_content.getText().toString();
                    String price = et_price.getText().toString();

                    // DB를 열어줌
                    mDbOpenHelper.open();
                    if(categoory_flag == 1 ){           // 수입 모드 일시,
                        // Insert쿼리 실행
                        mDbOpenHelper.insertColumn(content,price,"0",date_str);
                        // Toast Message 출력
                        Toast.makeText(getApplicationContext(),"수입 저장되었습니다.",Toast.LENGTH_LONG).show();
                        // MainActivity 로 화면 전환
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {                             // 지출 모드 일 시,
                        // Insert쿼리 실행
                        mDbOpenHelper.insertColumn(content,"0",price,date_str);
                        // Toast Message 출력
                        Toast.makeText(getApplicationContext(),"지출 저장되었습니다.",Toast.LENGTH_LONG).show();
                        // MainActivity 로 화면 전환
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        });

        // dialog 를 생성하기위한 Builder 객체 생성
        AlertDialog.Builder dialog = new AlertDialog.Builder(InsertDataView.this);
        // dialog의 타이틀
        //          메시지
        //          버튼 이벤트 선언
        dialog.setTitle("가게부 기록")
                .setMessage("뒤로 가시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        // Close 버튼 이벤트 리스너 생성 및 삽입
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.create().show();
            }
        });
    }
}