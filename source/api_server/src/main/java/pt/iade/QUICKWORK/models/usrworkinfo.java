package pt.iade.QUICKWORK.models;

public class usrworkinfo {
    private boolean haswork;
    private boolean isowner;

    public usrworkinfo(){}
    public usrworkinfo(boolean haswork, boolean isowner) {
        this.haswork = haswork;
        this.isowner = isowner;
    }
    public usrworkinfo(boolean haswork) {
        this.haswork = haswork;
    }
    public boolean isHaswork() {
        return haswork;
    }
    public boolean isIsowner() {
        return isowner;
    }
    public void setHaswork(boolean haswork) {
        this.haswork = haswork;
    }
    public void setIsowner(boolean isowner) {
        this.isowner = isowner;
    }
    

        
    
}
