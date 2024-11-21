import java.math.BigDecimal;
import java.math.RoundingMode;

public class Student implements Comparable<Student> {
    private final String id;
    private final String name;
    private final double score;
    private final Rank rank;

    public Student(String id, String name, double score) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.rank = Rank.getRank(score);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public Rank getRank() {
        return rank;
    }

    public static Student create(String id, String name, Number score) {
        double r_score = BigDecimal.valueOf(score.doubleValue())
                .setScale(2, RoundingMode.HALF_EVEN)
                .doubleValue();
        return new Student(id, name, r_score);
    }

    @Override
    public int compareTo(Student other) {
        return id.compareTo(other.id);
    }

    @Override
    public String toString() {
        return String.format("Student(id = %s, name = %s, score = %.2f, rank = %s)", id, name, score, rank);
    }
}

enum Rank {
    FAIL("Fail"),
    MEDIUM("Medium"),
    GOOD("Good"),
    VERY_GOOD("Very Good"),
    EXCELLENT("Excellent");

    private final String label;

    Rank(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static Rank getRank(double score) {
        if (score < 0.0 || score > 10.0) {
            throw new IllegalArgumentException("Score must be between 0 and 10 but was " + score);
        }
        if (score < 5.0) {
            return FAIL;
        }
        if (score < 6.5) {
            return MEDIUM;
        }
        if (score < 7.5) {
            return GOOD;
        }
        if (score < 8.5) {
            return VERY_GOOD;
        }
        return EXCELLENT;
    }
}