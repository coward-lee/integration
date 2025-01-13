package design.parttern.responsibilitichain;

public class DepartmentApprover extends Approver{
    public DepartmentApprover(String name) {
        super(name);
    }

    @Override
    public void processRequest(PureRequest pureRequest) {
        if (pureRequest.getPrice() <= 5000){
            System.out.println("请求编号：id="+pureRequest.getId() + "被【"+this.name+"】处理了");
        }else{
            getApprover().processRequest(pureRequest);
        }
    }
}
