package jmh;


import org.openjdk.jmh.annotations.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations =3)
@Measurement(iterations = 5)
public class TestJMH {
	public static void main(String[] args) {
	}

	@Benchmark
	public static void t1() throws ExecutionException, InterruptedException {
		FutureTask<Integer> t1 = new FutureTask<>(new ToDo());
		FutureTask<Integer> t2 = new FutureTask<>(new ToDo());
		FutureTask<Integer> t3 = new FutureTask<>(new ToDo());
		FutureTask<Integer> t4 = new FutureTask<>(new ToDo());
		new Thread(t1).start();
		new Thread(t2).start();
		new Thread(t3).start();
		new Thread(t4).start();
		System.out.println(t1.get()+t2.get()+t3.get()+t4.get());
	}
	@Benchmark
	public static void t2() throws ExecutionException, InterruptedException {
		System.out.println(test()+test()+test()+test());
	}
	public static int test(){
		long sum = 0L;
		for (int i = 0;i<10000000;i++){
			sum+=i;
		}
		return 10;
	}

	static class ToDo implements Callable<Integer> {


		public Integer call() throws Exception {
			return test();
		}
	}
}

