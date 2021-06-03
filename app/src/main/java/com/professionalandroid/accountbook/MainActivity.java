package com.professionalandroid.accountbook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.professionalandroid.accountbook.DataBase.DTO;
import com.professionalandroid.accountbook.DataBase.DbOpenHelper;
import com.professionalandroid.accountbook.fragment.RecyclerViewFragment;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    // DbOpenHelper 객체 선언
    private DbOpenHelper mDbOpenHelper;

    // Drawer 레이아웃 선언
    private DrawerLayout mDrawer;

    // ActionBar 토글버튼 위젯 선언
    private ActionBarDrawerToggle mDrawerToggle;

    // 각       항복별,
    //          일간별,
    //          주간별,
    //          월별,
    // Item 요소를 담을 List 선언
    private ArrayList<DTO> data_list;
    private ArrayList<DTO> day_list;
    private ArrayList<DTO> week_list;
    private ArrayList<DTO> month_list;


    // butterknife 라이브러리를 이용하여
    // 위젯과 뷰를 바인딩
    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;

    @BindView(R.id.fab_add)
    FloatingActionButton btn_fab;

    @BindView(R.id.tV_drawer_contact)
    TextView tv_drawer_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        ButterKnife.bind(this);     // butterknife 라이브러리를 이용하여 해당 context에 바인딩


        // list 객체 생성
        data_list   = new ArrayList<DTO>();
        day_list    = new ArrayList<DTO>();
        week_list   = new ArrayList<DTO>();
        month_list  = new ArrayList<DTO>();

        // DB Create & Search
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        int total = 0;

        // 날짜 별로 정렬된 record 값들을 가지고있는 Cursor 를 가져옴
        Cursor iCursor = mDbOpenHelper.sortColumn("date desc");
        while(iCursor.moveToNext()){        // Cursor 순회
            // Cursor를 통해 각 필드의 값을 가져옴
            String content = iCursor.getString(iCursor.getColumnIndex("content"));
            String income = iCursor.getString(iCursor.getColumnIndex("income"));
            String expend = iCursor.getString(iCursor.getColumnIndex("expend"));
            String date = iCursor.getString(iCursor.getColumnIndex("date"));
            int id = iCursor.getInt(iCursor.getColumnIndex("_id"));

            String Result = content + "," + income + "," +expend + "," + date;
            System.out.println(Result);

            // DB에서 가져온 Date의 포멧을 변경
            String date_str ="";
            if(date != null){
                String[] tmpStr = date.split("-");
                date_str = tmpStr[0] +"년" +tmpStr[1] + "월" + tmpStr[2] + "일";
                System.out.println(date_str);
            }
            int tmptotal = 0;
            tmptotal += Integer.parseInt(income);
            tmptotal -= Integer.parseInt(expend);


            // HOME 리스트 추가
            DTO dto = new DTO();
            dto.setId(id);
            dto.setContent(content);
            dto.setExpends(expend);
            dto.setIncome(income);
            dto.setDate(date_str);
            dto.setTotal(total + tmptotal);
            dto.setWeek(getWeekOfYear(date));
            dto.setMonth(getMonthOfYear(date));
            dto.setYear(getYear(date));

            data_list.add(dto);
            total += tmptotal;

            DTO dto_day = dto.clone();
            // 일간 리스트 추가
            if(day_list.size() == 0){   // day_list 사이즈가 0 이면,
                dto_day.setContent("");
                dto_day.setId(0);
                dto_day.setTotal(tmptotal);
                day_list.add(dto_day);
            }else {                     // day_list 사이즈가 0 이 아니면,
                int index = -1;

                // 추가하려는 일자의 데이터가 존재하는지 순회
                for(int i = 0; i< day_list.size(); i++){
                    DTO tmpDTO = day_list.get(i);
                    if(tmpDTO.getDate().equals(dto_day.getDate()))
                        index = i;
                }

                if(index != -1){        // 추가하려는 데이터와 일치하는 일자가 없을 경우,
                    DTO tmpDTO = day_list.get(index);
                    dto_day.setContent("");
                    dto_day.setId(index);
                    dto_day.setIncome(String.valueOf(Integer.parseInt(dto_day.getIncome()) + Integer.parseInt(tmpDTO.getIncome())));
                    dto_day.setExpends(String.valueOf(Integer.parseInt(dto_day.getExpends()) + Integer.parseInt(tmpDTO.getExpends())));
                    dto_day.setTotal(tmptotal + tmpDTO.getTotal());
                    day_list.set(index,dto_day);
                }else{                  // 추가하려는 데이터와 일치하는 일자가 있을 경우,
                    dto_day.setContent("");
                    dto_day.setId(day_list.size());
                    dto_day.setTotal(tmptotal);
                    day_list.add(dto_day);
                }
            }


            DTO dto_week = dto.clone();
            // 주간
            if(week_list.size() == 0){
                dto_week.setDate(String.valueOf(dto_week.getYear())+"년"+String.valueOf(dto_week.getMonth())+"월-"+String.valueOf(dto_week.getWeek())+"주차");
                dto_week.setContent("");
                dto_week.setId(0);
                dto_week.setTotal(tmptotal);
                week_list.add(dto_week);
            }else {
                int index = -1;
                for(int i = 0; i < week_list.size(); i++){
                    DTO tmpDTO = week_list.get(i);
                    if(tmpDTO.getWeek() == dto_week.getWeek() && tmpDTO.getMonth() == dto_week.getMonth()){
                        index = i;
                    }
                }

                if(index != -1){
                    DTO tmpDTO = week_list.get(index);
                    dto_week.setDate(String.valueOf(dto_week.getYear())+"년"+String.valueOf(dto_week.getMonth())+"월-"+String.valueOf(dto_week.getWeek())+"주차");
                    dto_week.setContent("");
                    dto_week.setId(index);
                    dto_week.setIncome(String.valueOf(Integer.parseInt(dto_week.getIncome()) + Integer.parseInt(tmpDTO.getIncome())));
                    dto_week.setExpends(String.valueOf(Integer.parseInt(dto_week.getExpends()) + Integer.parseInt(tmpDTO.getExpends())));
                    dto_week.setTotal(tmptotal + tmpDTO.getTotal());
                    week_list.set(index,dto_week);
                }else {
                    dto_week.setDate(String.valueOf(dto_week.getYear())+"년"+String.valueOf(dto_week.getMonth())+"월-"+String.valueOf(dto_week.getWeek())+"주차");
                    dto_week.setContent("");
                    dto_week.setId(0);
                    dto_week.setTotal(tmptotal);
                    week_list.add(dto_week);
                }
            }

            DTO dto_month = dto.clone();
            // 월간
            if(month_list.size() == 0){
                dto_month.setDate(String.valueOf(dto_month.getYear())+"년"+String.valueOf(dto_month.getMonth())+"월");
                dto_month.setContent("");
                dto_month.setId(0);
                dto_month.setTotal(tmptotal);
                month_list.add(dto_month);
            }else {
                int index = -1;
                for(int i = 0; i < month_list.size(); i++){
                    DTO tmpDTO = month_list.get(i);
                    if(tmpDTO.getMonth() == dto_month.getMonth()){
                        index = i;
                    }
                }

                if(index != -1){
                    DTO tmpDTO = month_list.get(index);
                    dto_month.setDate(String.valueOf(dto_month.getYear())+"년"+String.valueOf(dto_month.getMonth())+"월");
                    dto_month.setContent("");
                    dto_month.setId(index);
                    dto_month.setIncome(String.valueOf(Integer.parseInt(dto_month.getIncome()) + Integer.parseInt(tmpDTO.getIncome())));
                    dto_month.setExpends(String.valueOf(Integer.parseInt(dto_month.getExpends()) + Integer.parseInt(tmpDTO.getExpends())));
                    dto_month.setTotal(tmptotal + tmpDTO.getTotal());
                    month_list.set(index,dto_month);
                }else {
                    dto_month.setDate(String.valueOf(dto_month.getYear())+"년"+String.valueOf(dto_month.getMonth())+"월");
                    dto_month.setContent("");
                    dto_month.setId(0);
                    dto_month.setTotal(tmptotal);
                    month_list.add(dto_month);
                }
            }
        }


        // 헤더 추가
        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
        Date time = new Date();
        String tmpDate      = format1.format(time);
        String tmpContent   = "결산";

        int tmpIncome   = 0;
        int tmpExpends  = 0;
        int tmpTotal    = 0;
        int tmpId       = 0;


        for(int i = 0; i < data_list.size(); i++){
            tmpIncome += Integer.parseInt(data_list.get(i).getIncome());
            tmpExpends += Integer.parseInt(data_list.get(i).getExpends());
        }
        tmpTotal = tmpIncome - tmpExpends;

        DTO header_dto = new DTO();
        header_dto.setContent(tmpContent);
        header_dto.setDate(tmpDate);
        header_dto.setIncome(String.valueOf(tmpIncome));
        header_dto.setExpends(String.valueOf(tmpExpends));
        header_dto.setTotal(tmpTotal);
        header_dto.setId(tmpId);

        data_list.add(0,header_dto);
        day_list.add(0,header_dto);
        week_list.add(0,header_dto);
        month_list.add(0,header_dto);

        /////////////////////////////////////////////////////////////
        //              일간, 주간 , 월간 total 재배열
        /////////////////////////////////////////////////////////////

        tmpTotal    = 0;

        for(int i = data_list.size()-1; i >  0; i--){
            tmpTotal += Integer.parseInt(data_list.get(i).getIncome());
            tmpTotal -= Integer.parseInt(data_list.get(i).getExpends());
            data_list.get(i).setTotal(tmpTotal);
        }

        tmpTotal    = 0;

        for(int i = day_list.size()-1; i >  0; i--){
            tmpTotal += Integer.parseInt(day_list.get(i).getIncome());
            tmpTotal -= Integer.parseInt(day_list.get(i).getExpends());
            day_list.get(i).setTotal(tmpTotal);
        }

        tmpTotal    = 0;

        for(int i = week_list.size()-1; i >  0; i--){
            tmpTotal += Integer.parseInt(week_list.get(i).getIncome());
            tmpTotal -= Integer.parseInt(week_list.get(i).getExpends());
            week_list.get(i).setTotal(tmpTotal);
        }

        tmpTotal    = 0;

        for(int i = month_list.size()-1; i >  0; i--){
            tmpTotal += Integer.parseInt(month_list.get(i).getIncome());
            tmpTotal -= Integer.parseInt(month_list.get(i).getExpends());
            month_list.get(i).setTotal(tmpTotal);
        }

        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),InsertDataView.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

        dialog.setTitle("Contact E-mail")
                .setMessage("victory9c@gmail.com")
                .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        tv_drawer_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dialog.create().show();
            }
        });



        final Toolbar toolbar = mViewPager.getToolbar();
        setSupportActionBar(toolbar);


        // 어댑터란 = 뷰 와 연결된 리스트
        // 뷰페이저 각 요소별 플래그먼트 선언
        // 플래그먼트 : Activity 안에 들어가는 하나의 화면,
        // 뷰페이저 == 플래그먼트의 묶음

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 4) {
                    case 0:
                        Toast.makeText(getApplicationContext(),"0",Toast.LENGTH_SHORT).show();
                        return RecyclerViewFragment.newInstance(data_list);
                    case 1:
                        Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
                        return RecyclerViewFragment.newInstance(day_list);
                    case 2:
                        Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
                        return RecyclerViewFragment.newInstance(week_list);
                    case 3:
                        Toast.makeText(getApplicationContext(),"3",Toast.LENGTH_SHORT).show();
                        return RecyclerViewFragment.newInstance(month_list);
                    default:
                        return RecyclerViewFragment.newInstance(data_list);
                }
            }

            @Override
            public int getCount() {
                return 4;
            }


            // Sequence  == 나열
            // IntegerSequence   Int형 배열을 객체로 구성한것.
            // CharSequence      Char형 배열을 객체로 구성한것.
            // 뷰페이저의 각 요소 이름
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 4) {
                    case 0:
                        return "HOME";
                    case 1:
                        return "일간";
                    case 2:
                        return "주간";
                    case 3:
                        return "월별";
                }
                return "";
            }
        });


        // 뷰페이저의 로드 이미지
        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "http://phandroid.s3.amazonaws.com/wp-content/uploads/2014/06/android_google_moutain_google_now_1920x1080_wallpaper_Wallpaper-HD_2560x1600_www.paperhi.com_-640x400.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://www.hdiphonewallpapers.us/phone-wallpapers/540x960-1/540x960-mobile-wallpapers-hd-2218x5ox3.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }
                return null;
            }
        });


        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());


    }

    @Override
    protected void onStart() {
        super.onStart();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);
        ButterKnife.bind(mDrawer);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }
    
    // yyyy-mm-dd에서 주를 산출함
    private int getWeekOfYear(String date) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();

        String[] dates = date.split("-");
        int year = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]);
        int day = Integer.parseInt(dates[2]);
        calendar.set(year, month - 1, day);

        calendar1.set(year, month - 1, 1);
        return calendar.get(Calendar.WEEK_OF_YEAR) - calendar1.get(Calendar.WEEK_OF_YEAR);
    }

    // yyyy-mm-dd에서 달을 가져옴
    private int getMonthOfYear(String date) {
        String[] dates = date.split("-");
        int month = Integer.parseInt(dates[1]);
        return month;
    }

    // yyyy-mm-dd에서 년을 가져옴
    private int getYear(String date) {
        String[] dates = date.split("-");
        int year = Integer.parseInt(dates[0]);
        return year;
    }
}