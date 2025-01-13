package design.parttern.proxy.cglib;

public class CglibMain {
    public static void main(String[] args) {
        TeacherDao teacherDao = new TeacherDao();
        TeacherDao proxyInstance = ((TeacherDao) new ProxyFactory(teacherDao).getProxyInstance());
        proxyInstance.tech();
    }
}
