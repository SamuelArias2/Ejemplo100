package org.admin.entities;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user")
public class user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Integer userid;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "roleid", nullable = false)
    private role role;

    @ManyToOne
    @JoinColumn(name = "areaid", nullable = false)
    private area area;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<deviceassignment> deviceassignments;


    //getters and setters

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public role getRole() {
        return role;
    }

    public void setRole(role role) {
        this.role = role;
    }

    public area getArea() {
        return area;
    }

    public void setArea(area area) {
        this.area = area;
    }

    public Set<deviceassignment> getDeviceassignments() {
        return deviceassignments;
    }

    public void setDeviceassignments(Set<deviceassignment> deviceassignments) {
        this.deviceassignments = deviceassignments;
    }
}
