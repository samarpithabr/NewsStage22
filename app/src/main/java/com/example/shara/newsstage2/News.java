package com.example.shara.newsstage2;


public class News {
        private String sectionName;
        private String webTitle;
        private String webUrl;
        private String authorName;
        private  String newsDate;
        private String newsTime;

        public News(String name, String date,String time,String title, String url, String aname) {
            sectionName = name;
            newsDate=date;
            newsTime=time;
            webTitle = title;
            webUrl = url;
            authorName = aname;

        }

        public String getAuthorName() {
            return authorName;
        }

        public String getNewsDate() {
            return newsDate;
        }

        public String getNewsTime() {
            return newsTime;
        }

        public String getSectionName() {
            return sectionName;
        }

        public String getWebTitle() {
            return webTitle;
        }

        public String getWebUrl() {
            return webUrl;
        }
    }


