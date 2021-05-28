package com.professionalandroid.accountbook.DataBase;
/*
*   Record에 관련된 정보를 가지고 있는 객체
*   RecyclerView의 Item 객체로 활용하기위해 부가적인
*   total, id, week , month, year등의 인스턴트 변수를 추갛였다.
 */
public class DTO {
    private String content;
    private String income   = "0";
    private String expends  = "0";
    private String date;




    private int total = 0;
    private int id = 0;

    private int week = -1;
    private int month = -1;
    private int year = -1;


    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getWeek() {
        return week;
    }

    public int getTotal() {
        return total;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getExpends() {
        return expends;
    }

    public String getIncome() {
        return income;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setExpends(String expends) {
        this.expends = expends;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public DTO clone(){
        DTO dto = new DTO();
        dto.setId(this.getId());
        dto.setContent(this.getContent());
        dto.setIncome(this.getIncome());
        dto.setExpends(this.getExpends());
        dto.setDate(this.getDate());
        dto.setTotal(this.getTotal());
        dto.setYear(this.getYear());
        dto.setMonth(this.getMonth());
        dto.setWeek(this.getWeek());
        return dto;
    }
}
