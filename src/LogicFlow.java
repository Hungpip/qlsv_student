import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

enum Option {
    New_SinhVien("1", "Insert new SinhVien", new AddSinhVienAction()),
    EditScoreAction("2", "Edit Score", new EditScoreAction()),
    Print_SinhVien_List("3", "SinhVien list", new PrintSinhVienListAction()),
    Delete_SinhVien("4", "Delete SinhVien", new DeleteSinhVienAction()),
    Find_SinhVien("5", "Find SinhViens", new FindSinhVienByName()),
    BBSort("6", "Bubble Sort SinhVien", new BBSortAction()),
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
    public static LinkedList<Student> SinhVienList = new LinkedList<>();

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
                    System.out.format("Invalid option. Please re-enter : %s\n", input);
            }
        }
    }
}

class AddSinhVienAction implements Action {

    @Override
    public void execute(Scanner scanner) {
        System.out.println("===Add new SinhVien===");
        String id = null;
        do {
            var pattern = Pattern.compile("BH[0-9]{5}");
            System.out.print("ID(e.g: BHxxxxx): ");
            var tmp = scanner.nextLine();
            if (tmp.isBlank() || !pattern.matcher(tmp).matches()) {
                System.out.println("Invalid ID.");
                continue;
            }
            id = tmp;
        } while (id == null);
        String name = null;
        do {
            var pattern = Pattern.compile("[a-zA-Z\\s]+");
            System.out.print("Name: ");
            var tmp = scanner.nextLine();
            if (tmp.isBlank() || !pattern.matcher(tmp).matches()) {
                System.out.println("Invalid name.");
                continue;
            }
            name = tmp;
        } while (name == null);
        double score = -1.0;
        do {
            try {
                System.out.print("Score: ");
                var str = scanner.nextLine().trim();
                score = Double.parseDouble(str);
                if (score < 0 || score > 10) {
                    System.out.println("Invalid score.");
                    score = -1.0;
                }
            } catch (Exception ignore) {
                System.out.println("Invalid score.");
            }
        } while (score < 0);
        var sv = Student.create(id, name, score);
        LogicFlow.SinhVienList.add(sv);
    }
}

class EditScoreAction implements Action {
    //TODO: sửa cho tôi phần này không tìm thấy điểm
    @Override
    public void execute(Scanner scanner) {
        System.out.println("===Edit SinhVien's score===");
        String id = null;
        do {
            var pattern = Pattern.compile("BH[0-9]{5}");
            System.out.print("ID(e.g: BHxxxxx): ");
            var tmp = scanner.nextLine();
            if (tmp.isBlank() || !pattern.matcher(tmp).matches()) {
                System.out.println("Invalid ID.");
                continue;
            }
            id = tmp;
        } while (id == null);

        double score = 0;
        double finalScore = score;
        Optional<Student> opt = Optional.ofNullable(LogicFlow.SinhVienList.search(s -> false));

        if (opt.isEmpty()) {
            System.out.println("SinhVien not found.");
            return;
        }

        score = -1.0;
        do {
            try {
                System.out.print("Score: ");
                var str = scanner.nextLine().trim();
                score = Double.parseDouble(str);
                if (score < 0 || score > 10) {
                    System.out.println("Invalid score.");
                    score = -1.0;
                }
            } catch (Exception ignore) {
                System.out.println("Invalid score.");
            }
        } while (score < 0);

        Student sv = opt.get();
        sv = Student.create(sv.getId(), sv.getName(), score);
        String finalId = id;
        LogicFlow.SinhVienList.remove(student -> student.getId().equals(finalId));
        LogicFlow.SinhVienList.add(sv);
    }
}

class PrintSinhVienListAction implements Action {
    private final Comparator<Student> comparator;

    public PrintSinhVienListAction() {
        this((o1, o2) -> 0);
    }

    public PrintSinhVienListAction(Comparator<Student> comparator) {
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
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("#%d: %s%n", i + 1, list.get(i).toString());
        }
        System.out.print("\n\nPress enter to continue...");
        scanner.nextLine();
    }
}

class DeleteSinhVienAction implements Action {
    @Override
    //TODO: sửa cho tôi phần này không tìm thấy sinh viên
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
            System.out.print("Filter (ID(BHxxxxx) or Name): ");
            filter = scanner.nextLine();
        } while (filter == null);
        var list = LogicFlow.SinhVienList;
        String finalFilter = filter;
        var st = list.search(it -> it.getName().startsWith(finalFilter) || it.getId().startsWith(finalFilter));
        System.out.println("Found " + st);
        System.out.print("\n\nPress enter to continue...");
        scanner.nextLine();
    }
}

class BBSortAction implements Action {
    //TODO: BBS của tôi không chạy nưã :v
    private static <T extends Comparable<T>> void bubbleSort(T[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    T temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("===Bubble Sort SinhVien by Age===");
        if (LogicFlow.SinhVienList.size == 0) {
            System.out.println("No SinhVien to sort.");
            return;
        }
        Student[] sinhVienArray = LogicFlow.SinhVienList.toArray();
        bubbleSort(sinhVienArray);
        LogicFlow.SinhVienList = new LinkedList<>();
        System.out.println("SinhVien list sorted by age.");
    }
}