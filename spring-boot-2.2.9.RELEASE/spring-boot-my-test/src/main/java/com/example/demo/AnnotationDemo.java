package com.example.demo;

import com.example.demo.annotation.Annotation1;
import com.example.demo.annotation.Annotation2;

@Annotation1
@Annotation2
public class AnnotationDemo {
	public static void main(String[] args) throws Exception {
		Class<?> annotationDemo = Class.forName(AnnotationDemo.class.getName());
		System.out.println(annotationDemo.getAnnotation(Annotation1.class));
	}
}
