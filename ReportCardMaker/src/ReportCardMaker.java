// Updated by Claude on 2026-10-30

import java.util.Scanner;

class ReportCardMaker {
    static Scanner sc = new Scanner(System.in);

    // Minimum width of the "Subject" column, so short names still line up
    static final int MIN_SUBJECT_COLUMN_WIDTH = 20;

    // ===================== Input helpers =====================

    // Reads one line of input. Exits cleanly if the input ends unexpectedly.
    static String readLine() {
        if (!sc.hasNextLine()) {
            System.out.println("\nNo more input. Exiting.");
            System.exit(1);
        }
        return sc.nextLine().trim();
    }

    // Keeps asking until the user types a non-empty line
    static String readNonEmptyLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLine();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("  This cannot be empty. Please try again.");
        }
    }

    // Keeps asking until the user types a whole number of at least `min`
    static int readInt(String prompt, int min) {
        while (true) {
            System.out.print(prompt);
            String line = readLine();
            try {
                int value = Integer.parseInt(line);
                if (value >= min) {
                    return value;
                }
                System.out.println("  Please enter a whole number of at least " + min + ".");
            } catch (NumberFormatException e) {
                System.out.println("  '" + line + "' is not a whole number. Please try again.");
            }
        }
    }

    // Keeps asking until the user types a number between 0 and maxMark
    static double readMark(String prompt, int maxMark) {
        while (true) {
            System.out.print(prompt);
            String line = readLine();
            try {
                double value = Double.parseDouble(line);
                if (value >= 0 && value <= maxMark) {
                    return value;
                }
                System.out.println("  The mark must be between 0 and " + maxMark + ".");
            } catch (NumberFormatException e) {
                System.out.println("  '" + line + "' is not a number. Please try again.");
            }
        }
    }

    // ===================== Reading the data =====================

    static String[] readNames(String heading, String label, int count) {
        String[] names = new String[count];
        System.out.println("--- " + heading + " ---");
        for (int i = 0; i < count; i++) {
            names[i] = readNonEmptyLine("Enter name for " + label + " #" + (i + 1) + ": ");
        }
        System.out.println();
        return names;
    }

    static double[][] readMarks(String[] students, String[] subjects, int maxMark) {
        double[][] marks = new double[students.length][subjects.length];
        System.out.println("--- Enter Marks for Each Student (0 to " + maxMark + ") ---");
        for (int i = 0; i < students.length; i++) {
            System.out.println("-> For student: " + students[i]);
            for (int j = 0; j < subjects.length; j++) {
                marks[i][j] = readMark("Enter mark for " + students[i] + " in " + subjects[j] + ": ", maxMark);
            }
            System.out.println();
        }
        return marks;
    }

    // ===================== Calculations =====================

    static double calculateTotal(double[] studentMarks) {
        double total = 0;
        for (double mark : studentMarks) {
            total += mark;
        }
        return total;
    }

    static double calculatePercentage(double marks, double maximum) {
        return (marks / maximum) * 100;
    }

    // ===================== Output =====================

    static void printReportCards(String[] students, String[] subjects, double[][] marks, int maxMark) {
        // Widen the subject column if any subject name is longer than the default
        int subjectWidth = MIN_SUBJECT_COLUMN_WIDTH;
        for (String subject : subjects) {
            subjectWidth = Math.max(subjectWidth, subject.length());
        }
        String rowFormat = "%-" + subjectWidth + "s | %-10s | %-10s | %-12s%n";
        String divider = "-".repeat(subjectWidth + 41);

        double maximumTotalMarks = (double) subjects.length * maxMark;

        System.out.println("\n--- GENERATED REPORT CARDS ---");
        for (int i = 0; i < students.length; i++) {
            System.out.println(divider);
            System.out.println("Student: " + students[i]);
            System.out.println(divider);
            System.out.printf(rowFormat, "Subject", "Mark", "Max Mark", "Percentage");
            System.out.println(divider);

            for (int j = 0; j < subjects.length; j++) {
                System.out.printf(rowFormat,
                        subjects[j],
                        String.format("%.2f", marks[i][j]),
                        maxMark,
                        String.format("%.2f%%", calculatePercentage(marks[i][j], maxMark)));
            }

            double totalMarks = calculateTotal(marks[i]);
            System.out.println(divider);
            System.out.printf("Total Marks: %.2f / %.2f%n", totalMarks, maximumTotalMarks);
            System.out.printf("Overall Percentage: %.2f%%%n", calculatePercentage(totalMarks, maximumTotalMarks));
            System.out.printf("Average Mark: %.2f%n", totalMarks / subjects.length);
            System.out.println(divider + "\n");
        }
    }

    public static void main(String[] args) {
        int numStudents = readInt("Enter no of students: ", 1);
        int numSubjects = readInt("Enter no of subjects: ", 1);
        int maxMark = readInt("Enter maximum mark: ", 1);
        System.out.println();

        String[] students = readNames("Enter Student Names", "student", numStudents);
        String[] subjects = readNames("Enter Subject Names", "subject", numSubjects);
        double[][] marks = readMarks(students, subjects, maxMark);

        printReportCards(students, subjects, marks, maxMark);
    }
}
/*can calculate percentage
calculate average
calculate total
calculate any no of students and any no of subjects
rejects invalid input (non-numbers, out-of-range marks, empty names) and asks again*/
