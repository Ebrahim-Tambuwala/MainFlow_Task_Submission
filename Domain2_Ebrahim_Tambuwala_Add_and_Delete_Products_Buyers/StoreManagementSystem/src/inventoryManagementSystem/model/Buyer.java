package inventoryManagementSystem.model;

public class Buyer {

    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;

    public Buyer(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    // Setter for ID (better than reflection now)
    public void setId(int id) { this.id = id; }
}