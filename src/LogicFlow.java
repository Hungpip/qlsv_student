import java.util.*;
import java.util.regex.Pattern;

enum Option {
    New_SinhVien("1", "Insert new SinhVien", new InsertSinhVienAction()),
    Print_SinhVien_List("2", "SinhVien list", new PrintSinhVienListAction()),
    Delete_SinhVien("3", "Delete SinhVien", new DeleteSinhVienAction()),
    Print_SinhVien_List_ByGrade("4", "SinhVien list (by grade from high to low)", new PrintSinhVienListAction(
            (Comparator<SinhVien>) (o1, o2) -> Float.compare(o2.getGrade(), o1.getGrade())
    )),
    Find_SinhVien("5", "Find SinhViens", new FindSinhVienByName()),
    Find_SinhVien_ByGrade("6", "Find SinhViens (by grade)", new FindSinhVienByGrade()),
    Exit("0", "Exit", s -> {
        System.out.println("Exiting...");
        s.close();
        System.exit(0);
    });

    final String key;
    final String text;
    final Action action;

    private Option(String key, String text, Action action) {
        this.key = key;
        this.text = text;
        this.action = action;
    }
}

interface Action {
    void execute(Scanner scanner);
}

public class LogicFlow {
    public static final List<SinhVien> SinhVienList = new ArrayList<>();

    public LogicFlow() {
        startLogicFlow();
    }

    private void startLogicFlow() {
        var options = Option.values();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n\nSelect an option:");
            for (Option option : options) {
                System.out.println(option.key + ": " + option.text);
            }
            System.out.print("\nYour choice: ");
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                var opt = Arrays.stream(options).filter(it -> it.key.equals(input)).findFirst();
                if (opt.isPresent()) {
                    opt.get().action.execute(scanner);
                } else
                    System.out.format("Invalid option: %s\n", input);
            }
        }
    }
}

class InsertSinhVienAction implements Action {

    @Override
    public void execute(Scanner scanner) {
        var emailPattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        var phonePattern = Pattern.compile("^0\\d{9}$");
        System.out.println("===Insert new SinhVien===");
        String id = null;
        do {
            System.out.print("ID: ");
            var tmp = scanner.nextLine();
            if (tmp.isBlank()) {
                System.out.println("Invalid ID.");
                continue;
            }
            id = tmp;
        } while (id == null);
        String name = null;
        do {
            System.out.print("Name: ");
            var tmp = scanner.nextLine();
            if (tmp.isBlank()) {
                System.out.println("Invalid name.");
                continue;
            }
            name = tmp;
        } while (name == null);
        int age = -1;
        do {
            try {
                System.out.print("Age: ");
                var str = scanner.nextLine().trim();
                age = Integer.parseInt(str);
                if (age < 0)
                    System.out.println("Invalid age.");
            } catch (Exception ignore) {
                System.out.println("Invalid age.");
            }
        } while (age < 0);
        String email = null;
        do {
            System.out.print("Email: ");
            var tmp = scanner.nextLine();
            if (tmp.isBlank() || !emailPattern.matcher(tmp).matches()) {
                System.out.println("Invalid email.");
                continue;
            }
            email = tmp;
        } while (email == null);
        String phone = null;
        do {
            System.out.print("Phone: ");
            var tmp = scanner.nextLine();
            if (tmp.isBlank() || !phonePattern.matcher(tmp).matches()) {
                System.out.println("Invalid phone.");
                continue;
            }
            phone = tmp;
        } while (phone == null);
        String gender = "";
        do {
            System.out.print("Gender (Male, Female): ");
            gender = scanner.nextLine();
        } while (gender.isBlank());/* */
        float grade = -1;
        do {
            try {
                System.out.print("Grade: ");
                var tmp = Float.parseFloat(scanner.nextLine());
                grade = tmp;
                if (grade < 0)
                    System.out.println("Invalid grade.");
            } catch (Exception ignore) {
                System.out.println("Invalid grade.");
            }
        } while (grade < 0);
        var sv = new SinhVien();
        sv.setId(id);
        sv.setName(name);
        sv.setAge(age);
        sv.setEmail(email);
        sv.setGender(gender);
        sv.setGrade(grade);
        LogicFlow.SinhVienList.add(sv);
    }
}

class PrintSinhVienListAction implements Action {
    private final Comparator<SinhVien> comparator;

    public PrintSinhVienListAction() {
        this((o1, o2) -> 0);
    }

    public PrintSinhVienListAction(Comparator<SinhVien> comparator) {
        this.comparator = comparator;
    }


    @Override
    public void execute(Scanner scanner) {
        System.out.println("===SinhVien list===");
        var list = LogicFlow.SinhVienList;
        try {
        } catch (Exception e) {
            System.err.println("Cannot get SinhVien list: " + e.getMessage());
        }
        list.sort(comparator);
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("#%d: %s%n", i + 1, list.get(i));
        }
        System.out.print("\n\nPress enter to continue...");
        scanner.nextLine();
    }
}

class DeleteSinhVienAction implements Action {
    @Override
    public void execute(Scanner scanner) {
        System.out.println("===Delete SinhVien===");
        String id = null;
        do {
            System.out.print("ID (exit to cancel): ");
            var tmp = scanner.nextLine();
            if (tmp.isBlank()) {
                System.out.println("Invalid ID.");
                continue;
            }
            if (tmp.equals("exit")) {
                return;
            }
            if (checkId(tmp))
                id = tmp;
            else
                System.out.println("SinhVien ID not found.");
        } while (id == null);
    }

    private boolean checkId(String id) {
        return false;
    }
}

class FindSinhVienByName implements Action {
    @Override
    public void execute(Scanner scanner) {
        System.out.println("===SinhVien finder===");
        String filter;
        do {
            System.out.print("Filter (ID or Name): ");
            filter = scanner.nextLine();
        } while (filter == null);
        var list = new ArrayList<SinhVien>();
        String finalFilter = filter;
        var filtered = list.stream().filter(it -> it.getName().startsWith(finalFilter) || it.getId().startsWith(finalFilter)).toList();
        for (int i = 0; i < filtered.size(); i++) {
            System.out.printf("#%d: %s%n", i + 1, filtered.get(i));
        }
        System.out.print("\n\nPress enter to continue...");
        scanner.nextLine();
    }
}

class FindSinhVienByGrade implements Action {
    @Override
    public void execute(Scanner scanner) {
        System.out.println("===SinhVien finder===");
        float filter = -1f;
        do {
            System.out.print("SinhVien have grade >= ");
            try {
                filter = Float.parseFloat(scanner.nextLine());
            } catch (Exception ignore) {
                System.out.println("Invalid grade.");
            }
        } while (filter < 0);
        var list = new ArrayList<SinhVien>();
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("#%d: %s%n", i + 1, list.get(i));
        }
        System.out.print("\n\nPress enter to continue...");
        scanner.nextLine();
    }
}