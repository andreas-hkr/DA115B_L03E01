package se.hkr;

import java.util.List;
import java.util.function.DoubleConsumer;
import java.util.function.Predicate;

import static java.lang.StringTemplate.STR;
import static java.util.FormatProcessor.FMT;

public class Main {
    public static void main(String[] args) {
        var students = List.of(
                new Student("Anna", 20, "4"),
                new Student("Kalle", 29, "3"),
                new Student("Max", 22, "U"),
                new Student("Eva", 19, "U"),
                new Student("Olle", 24, "4"),
                new Student("Jenny", 33, "5"),
                new Student("Per", 21, "3"),
                new Student("Tomas", 18, "4")
        );


        // --- Base case to compare with ---


        // Find all students with grade 4 and print their names using a regular for-loop
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).grade().equals("4")) {
                System.out.println(students.get(i).name());
            }
        }

        // Find all students with grade 4 and print their names using a enhanced for-loop
        for (var student : students) {
            if (student.grade().equals("4")) {
                System.out.println(student.name());
            }
        }


        // --- Step by step, filter and map ---


        // Filter out all students with grade 4
        var withGrade4 = students.stream()                              // Look at all students...
                                 .filter(s -> s.grade().equals("4"))    // ... filter out those with grade 4
                                 .toList();                             // ... and save them in a list

        // Print all students with grade 4 (just to check)
        withGrade4.stream()                                             // Can skip stream() if not using
                  .forEach(s -> System.out.println(s));                 // intermediate operations

        // Get the names of all students with grade 4
        var names = withGrade4.stream()                                 // Look at all students with grade 4...
                              .map(s -> s.name())                       // ... take their names     (alt. Student::name)
                              .toList();                                // ... and save them in a list

        // Print the names of all students with grade 4
        names.forEach(System.out::println);


        // --- Combine them into one stream ---


        // All in one go, get the names of all students with grade 4 and print them
        students.stream()                                   // Look at all students...
                .filter(s -> s.grade().equals("4"))         // ... filter out those with grade 4
                .map(s -> s.name())                         // ... take their names
                .forEach(System.out::println);              // ... and print them

        // A predicate that checks if a student has grade 4
        Predicate<Student> hasGrade4 = s -> s.grade().equals("4");

        // All in one go, get the names of all students with grade 4 and print them (using the predicate)
        students.stream()                                   // Look at all students...
                .filter(hasGrade4)                          // ... filter out those with grade 4
                .map(Student::name)                         // ... take their names
                .forEach(System.out::println);              // ... and print them


        // --- Another example - Get the average age of those with grade 3 ---


        // Without the use of streams
        var years = 0;
        var count = 0;
        for (var student : students) {              // Responsible for iteration (in this case not a problem)
            if (student.grade().equals("3")) {      // What if no students with grade 3 are found?
                years += student.age();             // We are responsible for calculation
                count++;
            }
        }
        System.out.println(STR."The average age is \{years / count} years.");


        // With streams
        var totalYears = students.stream()                                   // Look at all students...
                                 .filter(s -> s.grade().equals("3"))         // ... filter out those with grade 3
                                 .map(s -> s.age())                          // ... take their age
                                 .reduce(0, (a, b) -> a + b);                // ... add them together

        var countGrade3 = students.stream()                                   // Look at all students...
                                  .filter(s -> s.grade().equals("3"))        // ... filter out those with grade 3
                                  .count();                                  // ... count them

        // Calculate and print the average age
        var averageAge = totalYears / countGrade3;
        System.out.println(STR."The average age is \{averageAge} years.");

        // All in one go, get the average age of those with grade 3 and print it (using functional interfaces)
        Predicate<Student> hasGrade3 = s -> s.grade().equals("3");
        DoubleConsumer printAverage = average -> System.out.println(FMT."The average age is %.0f\{average} years.");
        Runnable noStudentsFound = () -> System.out.println("No students with grade 3 found.");

        students.stream()                                   // Look at all students...
                .filter(hasGrade3)                          // ... filter out those with grade 3
                .mapToInt(Student::age)                     // ... take their age           (Note: mapToInt instead of map)
                .average()                                  // ... calculate the average    (Note: average instead of reduce)
                .ifPresentOrElse(
                        printAverage,                       // ... if there is an average, print it
                        noStudentsFound);                   // ... otherwise, print that no students were found
    }
}