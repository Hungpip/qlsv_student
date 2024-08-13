public class SinhVien {
    private String id;
    private String name;
    private int age;
    private String email;
    private String phone;
    private String gender;
    private float grade;

    public SinhVien() {
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format(
            "SinhVien(id=%s, name=%s, age=%d, gender=%s, email=%s, phone=%s, grade=%.2f)",
            id, name, age, gender, email,phone, grade
        );
    }
}
