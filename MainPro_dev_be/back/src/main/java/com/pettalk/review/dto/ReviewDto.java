package com.pettalk.review.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

public class ReviewDto {
    @AllArgsConstructor
    @Getter
    public static class Post {
//        private Long reviewId;
        private String content;
        private String memberNickName;
        private Float star;
//        private Long petSitterId;
    }

    @AllArgsConstructor
    @Getter
    public static class Patch {
        private Long reviewId;
        private String content;
        private String memberNickName;
        private Float star;
//        private Long petSitterId;
    }

    @AllArgsConstructor
    @Getter
    public static class Response {
        private Long reviewId;
        private String content;
        private String memberNickName;
        private Float star;
        private Long petSitterId;
    }


    @AllArgsConstructor
    @Getter
    public static class petSitterReviews{
        private String petSitterName;
        private Long contents;
        private Long reviews;
        private Float star;
    }



}
