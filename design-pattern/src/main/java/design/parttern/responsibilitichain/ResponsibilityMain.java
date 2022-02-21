package design.parttern.responsibilitichain;

public class ResponsibilityMain {
    public static void main(String[] args) {
        PureRequest pureRequest = new PureRequest(1, 310000, 1);
        // 创建审批人
        var departmentApprover = new DepartmentApprover("张主任");
        var collegeApprover = new CollegeApprover("王院长");
        var viceSchoolApprover = new ViceSchoolApprover("李副校");
        var masterSchoolApprover = new MasterSchoolApprover("李书记");

        departmentApprover.setApprover(collegeApprover);
        collegeApprover.setApprover(viceSchoolApprover);
        viceSchoolApprover.setApprover(masterSchoolApprover);
        departmentApprover.processRequest(pureRequest);
    }
}
