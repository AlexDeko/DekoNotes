package com.app.dekonotes.data.note;

public class TestData {

    public static Note TEST_NOTE = new Note(
            1L,
            "testTitle",
            "testText",
            true,
            Long.MAX_VALUE,
            Long.MIN_VALUE,
            0
    );
}
