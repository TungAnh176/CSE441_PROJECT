package com.example.driverslicense.model.question;

public class Option {
    private String a;
    private String b;
    private String c;
    private String d;
    private String image;
    private String description;

    public Option(String a, String b, String c, String d, String image, String description) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.image = image;
        this.description = description;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
