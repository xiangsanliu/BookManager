package com.bookmanager.eidian.bookmanager.Entities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiang on 2017/1/19.
 */

public class CourseLoc {
    private List<Course> courses = new ArrayList<>();
    private int x, y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setCourses(String courseString) {
        int blankNumber = 0;
        for (int i=0; i<courseString.length(); i++) {
            char a = courseString.charAt(i);
            if (a == ' ')   blankNumber++;
            if (blankNumber == 5) {
                courses.add(new Course(courseString.substring(0,i), x, y));
                courseString = courseString.substring(i+1, courseString.length());
                blankNumber = 0;
                i = 0;
            }
        }
        if (courseString.length()>1) {
            courses.add(new Course(courseString, x, y));
        }
    }

    public CourseLoc(String courseString, int x, int y) {
        this.x = x;
        this.y = y;
        setCourses(courseString);
    }

    private int getCourseNum() {
        return courses.size();
    }

    public void addToDatabase(SQLiteDatabase database) {
        for (Course course: courses) {
            ContentValues values = new ContentValues();
            values.put("courseName", course.getCourseName());
            values.put("courseType", course.getCourseType());
            values.put("courseTeacher", course.getCourseTeacer());
            values.put("startWeek", course.getStartWeek());
            values.put("endWeek", course.getEndWeek());
            values.put("weekType", course.getWeekType());
            values.put("locX", course.getX());
            values.put("locY", course.getY());
            database.insert("course", null, values);
            values.clear();
        }
    }
}
