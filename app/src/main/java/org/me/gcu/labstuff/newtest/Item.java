package org.me.gcu.labstuff.newtest;

//Karolina Lelito S1821063
import java.util.Date;


public class Item
{
    private String title, desc, link, author, georss, pubDate, comments, dat1, dat2;

    private Date startDate, endDate;

    public Boolean inc;


    public String getTitle() {
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }
    public void setLink(String link){
        this.link = link;
    }

    public String getGeorss() {
        return georss;
    }
    public void setGeorss(String georss){
        this.georss = georss;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author){
        this.author = author;
    }

    public String getComments() {
        return comments;
    }
    public void setComements(String comments){
        this.comments = comments;
    }

    public String getPubDate() {
        return pubDate;
    }
    public void setPubDate(String pubDate){
        this.pubDate = pubDate;
    }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date sdate){ this.startDate = sdate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date edate){ this.endDate = edate; }

    public String getDat1() { return dat1; }
    public void setDat1(String p){ this.dat1 = p; }

    public String getDat2() { return dat2; }
    public void setDat2(String p2){ this.dat2 = p2; }



    public Boolean getIncs() {return inc;}
    public void setInc(Boolean inc){this.inc = inc;}





    @Override
    public String toString() {
        return title + desc +georss+ pubDate;
    }



}
