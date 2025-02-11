package org.lee;

/**
 * 有效的数独
 */
public class isValidSudoku {

    /**
     * 只要再 一行、一列、或者一个方格里面出现了重复就可以判定数独无效
     * 这个有一个注意点需要使用一个三维数组简化迭代
     */
    public boolean isValidSudoku(char[][] board) {
        int[][] rows = new int[9][9];
        int[][] columns = new int[9][9];
        int[][][] subboxes = new int[3][3][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                char c = board[i][j];
                if (c != '.') {
                    int index = c - '0' - 1;
                    rows[i][index]++;
                    columns[j][index]++;
                    subboxes[i / 3][j / 3][index]++;
                    if (rows[i][index] > 1 || columns[j][index] > 1 || subboxes[i / 3][j / 3][index] > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isValidSudoku1(char[][] board) {
        // row check
        for (int i = 0; i < 9; i++) {
            int[] arr = new int[9];
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    int index = board[i][j] - '0' - 1;
                    arr[index]++;
                    if (arr[index] > 1) {
                        return false;
                    }
                }
            }
        }

        // column check
        for (int i = 0; i < 9; i++) {
            int[] arr = new int[9];
            for (int j = 0; j < 9; j++) {
                if (board[j][i] != '.') {
                    int index = board[j][i] - '0' - 1;
                    arr[index]++;
                    if (arr[index] > 1) {
                        return false;
                    }
                }
            }
        }
        int[][][] subboxes = new int[3][3][9];

        // square check
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {

                    int index = board[i][j] - '0' - 1;
                    subboxes[i / 3][j / 3][index]++;
                    if (subboxes[i / 3][j / 3][index] > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {

        char[][] chars = new char[9][];
//        chars[0] = new char[]{'8', '3', '.', '.', '7', '.', '.', '.', '.'};
//        chars[1] = new char[]{'6', '.', '.', '1', '9', '5', '.', '.', '.'};
//        chars[2] = new char[]{'.', '9', '8', '.', '.', '.', '.', '6', '.'};
//        chars[3] = new char[]{'8', '.', '.', '.', '6', '.', '.', '.', '3'};
//        chars[4] = new char[]{'4', '.', '.', '8', '.', '3', '.', '.', '1'};
//        chars[5] = new char[]{'7', '.', '.', '.', '2', '.', '.', '.', '6'};
//        chars[6] = new char[]{'.', '6', '.', '.', '.', '.', '2', '8', '.'};
//        chars[7] = new char[]{'.', '.', '.', '4', '1', '9', '.', '.', '5'};
//        chars[8] = new char[]{'.', '.', '.', '.', '8', '.', '.', '7', '9'};
        
        chars[0] = new char[]{'5','3','.','.','7','.','.','.','.'};
        chars[1] = new char[]{'6','.','.','1','9','5','.','.','.'};
        chars[2] = new char[]{'.','9','8','.','.','.','.','6','.'};
        chars[3] = new char[]{'8','.','.','.','6','.','.','.','3'};
        chars[4] = new char[]{'4','.','.','8','.','3','.','.','1'};
        chars[5] = new char[]{'7','.','.','.','2','.','.','.','6'};
        chars[6] = new char[]{'.','6','.','.','.','.','2','8','.'};
        chars[7] = new char[]{'.','.','.','4','1','9','.','.','5'};
        chars[8] = new char[]{'.','.','.','.','8','.','.','7','9'};
        System.out.println(new isValidSudoku().isValidSudoku1(chars));
    }
}
/*
[['8','3','.','.','7','.','.','.','.']
,['6','.','.','1','9','5','.','.','.']
,['.','9','8','.','.','.','.','6','.']
,['8','.','.','.','6','.','.','.','3']
,['4','.','.','8','.','3','.','.','1']
,['7','.','.','.','2','.','.','.','6']
,['.','6','.','.','.','.','2','8','.']
,['.','.','.','4','1','9','.','.','5']
,['.','.','.','.','8','.','.','7','9']]


[{'5','3','.','.','7','.','.','.','.'}
,{'6','.','.','1','9','5','.','.','.'}
,{'.','9','8','.','.','.','.','6','.'}
,{'8','.','.','.','6','.','.','.','3'}
,{'4','.','.','8','.','3','.','.','1'}
,{'7','.','.','.','2','.','.','.','6'}
,{'.','6','.','.','.','.','2','8','.'}
,{'.','.','.','4','1','9','.','.','5'}
,{'.','.','.','.','8','.','.','7','9'}]
 */