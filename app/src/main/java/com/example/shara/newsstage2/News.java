package com.example.shara.newsstage2;




import java.util.Date;

    public class News {
        private String sectionName;
        private String publicationDate;
        private String webTitle;
        private String webUrl;
        private String authorName;


        public News(String name, String dte, String title, String url, String aname) {
            sectionName = name;
            publicationDate = dte;
            webTitle = title;
            webUrl = url;
            authorName = aname;

        }

        public String getAuthorName() {
            return authorName;
        }

        public String getPublicationDate() {
            return publicationDate;
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


