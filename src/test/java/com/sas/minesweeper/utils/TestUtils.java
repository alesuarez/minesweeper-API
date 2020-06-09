package com.sas.minesweeper.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {
    public static void assertEqual(Integer[][] expectedMask, Integer[][] maskedBoard, int row) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                assertEquals(expectedMask[i][j], maskedBoard[i][j]);
            }
        }
    }
}
