package com.pettalk.location.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class LocationDTO {

    @Getter
    @Setter
    public static class Post{
        private String lat;
        private String lon;
    }

    @Getter
    @Setter
    public static class Get{
        private String lat;
        private String lon;
    }


    @Getter
    @Setter
    public static class PostResponse {
        private float lat;
        private float lon;
    }

    @Getter
    @Setter
    public static class GetResponse{
        private float lat;
        private float lon;
    }
}


