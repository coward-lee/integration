package design.parttern.visitor;


public class VisitorMain {
    public static void main(String[] args) {
        ObjectStructure os = new ObjectStructure();

        os.attach(new Man());
        os.attach(new Woman());

        Success success = new Success();
        os.display(success);
        Fail fail = new Fail();
        os.display(fail);
        Wait wait = new Wait();
        os.display(wait);
    }
}
