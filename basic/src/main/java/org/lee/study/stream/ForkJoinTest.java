package org.lee.study.stream;


import java.util.Objects;
import java.util.concurrent.CountedCompleter;

public class ForkJoinTest {

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
        MapReducer<T> slibing;
        T result;

        public MapReducer(T[] array, Mapper<T, T> mapper, Reducer<T, T> reducer) {
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
                int mid = (lo + hi) / 2;
                MapReducer<T> left = new MapReducer<>(this, array, mapper, reducer, lo, mid);
                MapReducer<T> right = new MapReducer<>(this, array, mapper, reducer, mid, hi);
                left.slibing = right;
                right.slibing = left;
                setPendingCount(1);
                // 后面的数据 用新的线程来执行
                right.fork();
                left.compute();
            }else{
                if (hi > lo){
                    result = mapper.apply(array[lo]);
                }
                tryComplete();
            }
        }

        @Override
        public T getRawResult() {
            return result;
        }

        @Override
        public void onCompletion(CountedCompleter<?> caller) {
            if (caller != this){
                MapReducer<T> child = (MapReducer<T>) caller;
                MapReducer<T> sib = child.slibing;
                if (Objects.isNull(sib) || Objects.isNull(sib.result)){
                    result = child.result;
                }else{
                    result = reducer.apply(child.result, sib.result);
                }
            }
        }
    }
}

