package vn.com.example.democontactapp.model;

public class Contact {
    private String mName;
    private String mPhoneNumber;

    public Contact(String name, String phoneNumber) {
        this.mName = name;
        this.mPhoneNumber = phoneNumber;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String name) {
        this.mName = name;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }
}
