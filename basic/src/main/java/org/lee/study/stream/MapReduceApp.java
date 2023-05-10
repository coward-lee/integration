package org.lee.study.stream;


import java.util.Objects;
import java.util.concurrent.CountedCompleter;

public class MapReduceApp {

    public static void main(String[] args) {
        // 数组中每个元素*2，再求和
        Integer result = new MapReducer<>(new Integer[]{1, 2, 3, 4}, x -> x * 2, Integer::sum).invoke();
        System.out.println(result);
    }

    interface Mapper<S, T> {

        T apply(S source);
    }

    interface Reducer<S, T> {

        T apply(S first, S second);
    }

    public static class MapReducer<T> extends CountedCompleter<T> {

        final T[] array;

        final Mapper<T, T> mapper;

        final Reducer<T, T> reducer;

        final int lo, hi;

        MapReducer<T> sibling;

        T result;

        public MapReducer(T[] array,
                          Mapper<T, T> mapper,
                          Reducer<T, T> reducer) {
            this.array = array;
            this.mapper = mapper;
            this.reducer = reducer;
            this.lo = 0;
            this.hi = array.length;
        }

        public MapReducer(CountedCompleter<?> p,
                          T[] array,
                          Mapper<T, T> mapper,
                          Reducer<T, T> reducer,
                          int lo,
                          int hi) {
            super(p);
            this.array = array;
            this.mapper = mapper;
            this.reducer = reducer;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        public void compute() {
            if (hi - lo >= 2) {
                int mid = (lo + hi) >> 1;
                MapReducer<T> left = new MapReducer<>(this, array, mapper, reducer, lo, mid);
                MapReducer<T> right = new MapReducer<>(this, array, mapper, reducer, mid, hi);
                left.sibling = right;
                right.sibling = left;
                // 创建子任务父任务的pending计数器加1
                setPendingCount(1);
                // 提交右子任务
                right.fork();
                // 在当前线程计算左子任务
                left.compute();
            } else {
                if (hi > lo) {
                    result = mapper.apply(array[lo]);
                }
                // 叶子节点完成，尝试合并其他兄弟节点的结果，会调用onCompletion方法
                tryComplete();
            }
        }

        @Override
        public T getRawResult() {
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onCompletion(CountedCompleter<?> caller) {
            if (caller != this) {
                MapReducer<T> child = (MapReducer<T>) caller;
                MapReducer<T> sib = child.sibling;
                // 合并子任务结果，只有两个子任务
                if (Objects.isNull(sib) || Objects.isNull(sib.result)) {
                    result = child.result;
                } else {
                    result = reducer.apply(child.result, sib.result);
                }
            }
        }
    }
}

