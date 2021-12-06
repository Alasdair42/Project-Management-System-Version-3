import java.math.BigInteger;

public class project {

    // attributes
    int projectNum;
    String projectName;
    String buildingType;
    String projectAddress;
    BigInteger erfNum;
    int totalFee;
    int paidToDate;
    int amountDue;
    String dueDate;
    String architect;
    String contractor;
    String customer;

    //  Constructor method
    public project(int projectNum, String projectName, String buildingType, String projectAddress, BigInteger erfNum, int totalFee, int paidToDate, int amountDue, String dueDate, String architect, String contractor, String customer){
        this.projectNum = projectNum;
        this.projectName = projectName;
        this.buildingType = buildingType;
        this.projectAddress = projectAddress;
        this.erfNum = erfNum;
        this.totalFee = totalFee;
        this.paidToDate = paidToDate;
        this.amountDue = amountDue;
        this.dueDate = dueDate;
        this.architect = architect;
        this.contractor = contractor;
        this.customer = customer;
    }

    // Getter for paidToDate
    public int getPaidToDate() {

        return paidToDate;
    }

    // Getter for dueDate
    public String getDueDate() {

        return dueDate;
    }
    // Setter for dueDate
    public void setDueDate(String dueDate) {

        this.dueDate = dueDate;
    }

    // setter for paidToDate
    public void setPaidToDate(int paidToDate) {

        this.paidToDate = paidToDate;
    }

    // toString
    public String toString(){
        String output = " ";
        output += " \nProject Number : " + projectNum;
        output += " \nProject Name : " + projectName;
        output += " \nBuilding Type : " + buildingType;
        output += " \nProject Address : " + projectAddress;
        output += " \nERF Number : " + erfNum;
        output += " \nTotal Fee : " + totalFee;
        output += " \nPaid to Date : " + paidToDate;
        output += " \nAmount Due : " + amountDue;
        output += " \nDue Date : " + dueDate;
        output += " \nArchitect : " + architect;
        output += " \nContractor : " + contractor;
        output += " \nCustomer : " + customer;
        return output;
    }

}
