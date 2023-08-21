package org.lee;

import org.junit.jupiter.api.Test;

import java.util.*;

public class Solution {

    @Test
    void test_result() {
//        List<List<Integer>> lists = kSmallestPairs(new int[]{1, 7, 11}, new int[]{2, 4, 6}, 3);
//        lists.forEach(i -> {
//            i.forEach(System.out::print);
//            System.out.println();
//        });
    }

    public int findRepeatNumber(int[] nums) {
        Set<Integer> result = new HashSet<>(nums.length / 2);
        for (int i = 0; i < nums.length; i++) {
            if (result.contains(nums[i])) {
                return nums[i];
            } else {
                result.add(nums[i]);
            }
        }

        return nums[0];
    }


    @Test
    void test_findNumberIn2DArray() {
        boolean numberIn2DArray = findNumberIn2DArray(
                new int[][]{{-1, 3}}
//                new int[][]{
////                        {5},
////                        {6}
//                        {4, 5},
//                        {4, 6},
//                        {9, 14},
//                        {10, 15}
//                }

//                new int[][]{
//                        {1, 4, 7, 11, 15},
//                        {2, 5, 8, 12, 19},
//                        {3, 6, 9, 16, 22},
//                        {10, 13, 14, 17, 24},
//                        {18, 21, 23, 26, 30}
//                }

//        new int[][]{
//                {1, 3, 5, 7, 9},
//                {2, 4, 6, 8, 10},
//                {11, 13, 15, 17, 19},
//                {12, 14, 16, 18, 20},
//                {21, 22, 23, 24, 25}
//        }
                , 3);
        System.out.println(numberIn2DArray);
    }

    /**
     * 在一个 n * m 的二维数组中，每一行都按照从左到右 非递减 的顺序排序，每一列都按照从上到下 非递减 的顺序排序。请完成一个高效的函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
     * <p>
     * 2,5
     * 2,8
     * 7,9
     * 7,11
     * 9,11
     * 1,4,7,11,15
     * 2,5,8,12,19
     * 3,6,9,16,22
     * 10,13,14,17,24
     * 18,21,23,26,30
     *
     * @param matrix
     * @param target
     * @return
     */
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }


        int x = 0;
        int y = matrix.length - 1;
        while (x < matrix[0].length && y >= 0) {
            if (matrix[y][x] == target) {
                return true;
            } else if (matrix[y][x] < target) {
                x++;
            } else {
                y--;
            }
        }
        return false;
//        for (int i = 0; i < matrix.length; i++) {
//            int left = 0;
//            int right = matrix[0].length;
//            int[] arr = matrix[i];
//            if (arr[0] > target || target > arr[right - 1]) {
//                continue;
//            }
//            while (left <= right) {
//                int mid = (left + right) / 2;
//                if (arr[mid] == target) {
//                    return true;
//                } else if (arr[mid] > target) {
//                    right = mid - 1;
//                } else {
//                    left = mid + 1;
//                }
//            }
//        }
//        return false;
    }

    @Test
    void test_buildTreE() {
        TreeNode treeNode = buildTree(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7});
        print(treeNode);
        System.out.println();
    }

    void print(TreeNode treeNode) {
        if (treeNode == null) {
            System.out.print("null, ");
            return;
        }
        System.out.print(treeNode.val + ", ");
        print(treeNode.left);
        print(treeNode.right);
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder.length == 0) {
            return null;
        }
        return build(preorder, inorder);
    }

    private TreeNode build(int[] preorder, int[] inorder) {
//        if (preorder.length == 0)
        if (preorder.length == 0) {
            return null;
        }
        TreeNode root = new TreeNode(preorder[0]);
        if (preorder.length == 1) {
            return root;
        }
        int rootIndex = indexOf(inorder, 0, inorder.length - 1, preorder[0]);

        root.left = build(
                Arrays.copyOfRange(preorder, 1, 1 + rootIndex),
                Arrays.copyOfRange(inorder, 0, rootIndex)
        );
        root.right = build(
                Arrays.copyOfRange(preorder, 1 + rootIndex, preorder.length),
                Arrays.copyOfRange(inorder, rootIndex + 1, inorder.length)
        );
        return root;
    }

    int indexOf(int[] arr, int start, int end, int value) {
        for (int i = start; i <= end; i++) {
            if (arr[i] == value)
                return i;
        }
        return -1;
    }


    @Test
    void test_minArray() {
        int i = minArray(new int[]{4, 5, 6, 7, 0, 1, 2});
        System.out.println(i);
    }

    /**
     * 把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。
     * <p>
     * 给你一个可能存在 重复 元素值的数组 nums ，它原来是一个升序排列的数组，并按上述情形进行了一次旋转。请返回旋转数组的最小元素。例如，数组 [3,4,5,1,2] 为 [1,2,3,4,5] 的一次旋转，该数组的最小值为 1。
     * <p>
     * 注意，数组 [a[0], a[1], a[2], ..., a[n-1]] 旋转一次 的结果为数组 [a[n-1], a[0], a[1], a[2], ..., a[n-2]] 。
     * <p>
     * 示例 1：
     * <p>
     * 输入：nums = [3,4,5,1,2]
     * 输出：1
     * 示例 2：
     * <p>
     * 输入：nums = [5,1,2,3,4]
     * 输出：1
     * 示例 2：
     * <p>
     * 输入：nums = [2,2,2,0,1]
     * 输出：0
     *
     * @param nums
     * @return
     */

    public int minArray(int[] nums) {
        int left = 0, right = nums.length - 1;
        if (nums.length == 1) {
            return nums[0];
        }
        if (nums[left] < nums[right]) {
            return nums[left];
        }
//        if (nums[left] > nums[left + 1]) {
//            return nums[right];
//        }
        while (left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] > nums[left]) {
                if (nums[left] < nums[right]) {
                    return nums[left];
                }
                left = mid + 1;
            } else if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else if (nums[mid] < nums[right]) {
                right = mid;
            } else {
                right--;
            }
        }
        return nums[right];
    }

    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>(nums.length);
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (map.containsKey(num)) {
                return new int[]{i, map.get(num)};
            }
            map.putIfAbsent(target - num, i);
        }
        return new int[0];
    }


    @Test
    void test_map() {
        char[][] chars = {
//                {'A', 'B', 'C', 'E'},
//                {'S', 'F', 'C', 'S'},
//                {'A', 'D', 'E', 'E'}

//                {'A', 'B', 'C', 'E'},
//                {'S', 'F', 'E', 'S'},
//                {'A', 'D', 'E', 'E'},
//                {'A','B'}
                {'A', 'A'}

        };
        System.out.println(exist(chars, "AA"));
    }

    public boolean exist(char[][] board, String word) {
        if (board.length == 1 && board[0].length == 1 && word.length() == 1) {
            return board[0][0] == word.toCharArray()[0];
        }
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (search(i, j, board, word, 0, new byte[board.length][board[0].length])) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean search(int x, int y, char[][] board, String word, int currentIndex, byte[][] visited) {
        if (currentIndex >= word.length()) {
            return true;
        }
        if (board[y][x] != word.substring(currentIndex, currentIndex + 1).toCharArray()[0]) {
            return false;
        }

        visited[y][x] = 1;
        currentIndex++;
        if (currentIndex >= word.length()) {
            return true;
        }


        ArrayList<int[]> list = new ArrayList<>();
        if (x + 1 < board[0].length && visited[y][x + 1] == 0) {
            list.add(new int[]{x + 1, y});
        }
        if (y + 1 < board.length && visited[y + 1][x] == 0) {
            list.add(new int[]{x, y + 1});
        }
        if (x - 1 >= 0 && visited[y][x - 1] == 0) {
            list.add(new int[]{x - 1, y});
        }
        if (y - 1 >= 0 && visited[y - 1][x] == 0) {
            list.add(new int[]{x, y - 1});
        }
        int resultC = 0;
        for (int[] ints : list) {
            if (search(ints[0], ints[1], board, word, currentIndex, visited)) {
            } else {
                resultC++;
            }
        }
        boolean result = list.size() != resultC;
        if (!result) visited[y][x] = 0;
        return result;
    }

    public boolean search(int x1, int y1, char[][] board, String word) {
        char[] arr = word.toCharArray();
        if (board[y1][x1] != arr[0]) {
            return false;
        }
        byte[][] visited = new byte[board.length][board[0].length];
        Stack<int[]> stack = new Stack<>();
        stack.add(new int[]{x1, y1});
        int wordIndex = 0;
        LinkedList<Pair> intCount = new LinkedList<>();
        for (; wordIndex < arr.length && !stack.isEmpty(); ) {
            int[] pop = stack.peek();
            int x = pop[0];
            int y = pop[1];
            if (arr[wordIndex] != board[pop[1]][pop[0]]) {
                stack.pop();
                int last = intCount.size() - 1;
                Pair lastLevelCount = intCount.get(last);
                lastLevelCount.count--;
                if (lastLevelCount.count == 0) {
                    intCount.remove(last);
                    visited[lastLevelCount.y][lastLevelCount.x] = 0;
                    wordIndex--;
                } else {
                    intCount.set(last, lastLevelCount);
                }
                continue;
            }

            // instack
            ArrayList<int[]> list = new ArrayList<>();
            if (x + 1 < board[0].length && visited[y][x + 1] == 0) {
                list.add(new int[]{x + 1, y});
            }
            if (y + 1 < board.length && visited[y + 1][x] == 0) {
                list.add(new int[]{x, y + 1});
            }
            if (x - 1 >= 0 && visited[y][x - 1] == 0) {
                list.add(new int[]{x - 1, y});
            }
            if (y - 1 >= 0 && visited[y - 1][x] == 0) {
                list.add(new int[]{x, y - 1});
            }
            if (!list.isEmpty()) {
                intCount.add(new Pair(list.size(), x, y));
            }
            for (int[] ints : list) {
                stack.push(ints);
            }
            wordIndex++;
            if (wordIndex >= arr.length) {
                return true;
            }
            visited[y][x] = 1;
        }
        return false;
    }

    class Pair {
        int count;
        int x;
        int y;

        public Pair(int count, int x, int y) {
            this.count = count;
            this.x = x;
            this.y = y;
        }
    }


}
