package se.hkr;

public record Student(String name, int age, String grade) {

    @Override
    public String toString() {
        return String.format("(%d) %-10s Grade: %s", age, name, grade);
    }
}
