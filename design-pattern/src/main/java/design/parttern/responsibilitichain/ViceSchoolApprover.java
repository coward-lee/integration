package design.parttern.responsibilitichain;

public class ViceSchoolApprover extends Approver{
    public ViceSchoolApprover(String name) {
        super(name);
    }

    @Override
    public void processRequest(PureRequest pureRequest) {
        if (pureRequest.getPrice() > 10000 && pureRequest.getPrice() < 20000){
            System.out.println("请求编号：id="+pureRequest.getId() + "被【"+this.name+"】处理了");
        }else{
            getApprover().processRequest(pureRequest);
        }
    }
}
