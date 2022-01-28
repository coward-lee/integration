package org.lee.study.spring.bean;

public class Orders {
    private String oname;

    public Orders() {
        System.out.println("第一步：执行了无参构造");
    }

    public void setOname(String oname) {
        System.out.println("第二步：调用了set方法设置属性");
        this.oname = oname;
    }

    // 创建执行的初始化方法
    public void initMethod(){
        System.out.println("第三步：执行了自定义的初始化方法");
    }

    public void destroyMethod(){
        System.out.println("第五步：执行了自定义的初始化方法");
    }
}
