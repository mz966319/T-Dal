package com.example.t_dal;

public class JobPost {
//    userMap.put("coursename",coursename);
//                            userMap.put("description",description);
//                            userMap.put("jobtitle",jobtitle);
//                            userMap.put("date",date);
//                            userMap.put("userid",currUserID);
//                            userMap.put("fullname",fullname);

    public String coursename,description,jobtitle,date,userid,fullname;

    public JobPost(){}

    public JobPost(String coursename, String description, String jobtitle, String date, String userid, String fullname) {
        this.coursename = coursename;
        this.description = description;
        this.jobtitle = jobtitle;
        this.date = date;
        this.userid = userid;
        this.fullname = fullname;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
