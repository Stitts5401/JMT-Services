package com.jmt.webservice.literal;

import java.util.List;

public class NationalityData {
    public static List<String> getCommonNationalities() {
        return List.of(
            "Chinese", "Indian", "American", "Indonesian", "Pakistani",
            "Brazilian", "Nigerian", "Bangladeshi", "Russian", "Mexican",
            "Japanese", "Ethiopian", "Filipino", "Egyptian", "Vietnamese",
            "Congolese", "Turkish", "Iranian", "German", "Thai"
        );
    }
}
