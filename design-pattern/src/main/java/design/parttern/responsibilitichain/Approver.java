package design.parttern.responsibilitichain;

public abstract class Approver {
    Approver approver; // 后继节点
    String name; // 自己的名字

    public Approver(String name) {
        this.name = name;
    }

    public void setApprover(Approver approver) {
        this.approver = approver;
    }

    public Approver getApprover() {
        return approver;
    }

    // 处理请求的方法
    public abstract void processRequest(PureRequest pureRequest);
}
