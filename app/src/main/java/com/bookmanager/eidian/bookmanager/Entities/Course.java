package com.bookmanager.eidian.bookmanager.Entities;

import android.util.Log;

/**
 * Created by xiang on 2017/1/19.
 */

class Course {
    private String courseName, courseType, courseTime, courseTeacer, courseLoc;
    private int x, y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Course(String courseString, int x, int y) {
        this.x = x;
        this.y = y;
        courseName = courseType = courseTime = courseTeacer = courseLoc = "";
        if (courseString.length()>1) {
            courseName = courseString.substring(0, courseString.indexOf(' '));
        }
        if (courseString.length()>1) {
            courseString = courseString.substring(courseString.indexOf(' ') + 1);
            courseType = courseString.substring(0, courseString.indexOf(' '));
        }
        if (courseString.length()>1) {
            courseString = courseString.substring(courseString.indexOf(' ') + 1);
            courseTime = courseString.substring(0, courseString.indexOf(' '));
            Log.d("courseTime", courseTime);
        }
        if (courseString.length()>1) {
            courseString = courseString.substring(courseString.indexOf(' ') + 1);
            courseTeacer = courseString.substring(0, courseString.indexOf(' '));
        }
        if (courseString.length()>1) {
            courseString = courseString.substring(courseString.indexOf(' ') + 1);
            courseLoc = courseString;
        }
    }

    public String getCourseName() {
        return courseName;
    }

    public int getEndWeek() {
        int endWeek;
        int startIndex = courseTime.indexOf('-')+1;
        int endIndex = courseTime.indexOf('周');
        endWeek = Integer.parseInt(courseTime.substring(startIndex, endIndex));
        courseTime = courseTime.substring(endIndex+1);
        return  endWeek;
    }

    public int getWeekType() {
        if (courseTime.equals("|单周}")) {
            return 1;
        } else if (courseTime.equals("|双周}")) {
            return 2;
        }
        return 0;
    }

    public int getStartWeek() {
        int startWeek ;
        int startIndex = courseTime.indexOf('{')+2;
        int endIndex = courseTime.indexOf('-');
        startWeek =  Integer.parseInt(courseTime.substring(startIndex, endIndex));
        courseTime = courseTime.substring(endIndex);
        return startWeek;
    }

    public String getCourseLoc() {
        return courseLoc;
    }

    public String getCourseType() {
        return courseType;
    }

    public String getCourseTeacer() {
        return courseTeacer;
    }

}
