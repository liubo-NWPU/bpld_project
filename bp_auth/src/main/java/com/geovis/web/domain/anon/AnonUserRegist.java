package com.geovis.web.domain.anon;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2018/12/4.
 */
@Table(name = "anon_user_regist")
public class AnonUserRegist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private String position;

    @Column(name = "enter_date")
    private Date enterDate;

    @Column(name = "sex")
    private String sex;

    @Column(name = "telphone")
    private String telephone;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "company")
    private String company;

    public String getId() {
        if (id == null) {
            return "";
        } else {
            return id;
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        if (username == null) {
            return "";
        } else {
            return username;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        if (password == null) {
            return "";
        } else {
            return password;
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        if (salt == null) {
            return "";
        } else {
            return salt;
        }
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        if (name == null) {
            return name;
        } else {
            return name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        if (position == null) {
            return "";
        } else {
            return position;
        }
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getEnterDate() {
        if (enterDate == null) {
            return new Date(0000, 00, 00);
        } else {
            return enterDate;
        }
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

    public String getSex() {
        if (sex == null) {
            return "";
        } else {
            return sex;
        }
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTelephone() {
        if (telephone == null) {
            return "";
        } else {
            return telephone;
        }
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        if (mobile == null) {
            return "";
        } else {
            return mobile;
        }
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        if (email == null) {
            return "";
        } else {
            return email;
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        if (company == null) {
            return "";
        } else {
            return company;
        }
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "AnonUserRegist{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", enterDate=" + enterDate +
                ", sex='" + sex + '\'' +
                ", telephone='" + telephone + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
