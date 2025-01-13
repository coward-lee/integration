package design.parttern.responsibilitichain;

public class CollegeApprover extends Approver{
    public CollegeApprover(String name) {
        super(name);
    }

    @Override
    public void processRequest(PureRequest pureRequest) {
        if (pureRequest.getPrice() > 5000 && pureRequest.getPrice() < 10000){
            System.out.println("请求编号：id="+pureRequest.getId() + "被【"+this.name+"】处理了");
        }else{
            getApprover().processRequest(pureRequest);
        }
    }
}
