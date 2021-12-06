public class person {

    // attributes
    String type;
    String name;
    String project;
    String telephoneNum;
    String emailAddress;
    String postAddress;

    //  Constructor method
    public person(String type, String name, String project, String telephoneNum, String emailAddress, String postAddress){
        this.type = type;
        this.name = name;
        this.project = project;
        this.telephoneNum = telephoneNum;
        this.emailAddress = emailAddress;
        this.postAddress = postAddress;
    }

    // toString
    public String toString(){
        String output = " ";
        output += "Type : " + type;
        output += " \nName : " + name;
        output += " \n Project : " + project;
        output += " \nTelephone Number : " + telephoneNum;
        output += " \nEmail Address : " + emailAddress;
        output += " \nPostal Address : " + postAddress;
        return output;
    }

}
