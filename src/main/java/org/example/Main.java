
package org.example;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
class Book {
    private String name;
    private String author;
    private int publishingYear;
    private String isbn;
    private String publisher;

    public String getName() {
        return name;
    }

    public int getPublishingYear() {
        return publishingYear;
    }

    public String getAuthor() {
        return author;
    }
}

@Data
@AllArgsConstructor
class Visitor {
    private String name;
    private String surname;
    private String phone;
    private boolean subscribed;
    private List<Book> favoriteBooks;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public List<Book> getFavoriteBooks() {
        return favoriteBooks;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public String getPhone() {
        return phone;
    }
}

@Data
@AllArgsConstructor
class Sms {
    private String phone;
    private String message;


    public String getPhone() {
        return phone;
    }

    public String getMessage() {
        return message;
    }
}

public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("books.json")) {
            Type visitorListType = new TypeToken<List<Visitor>>() {
            }.getType();
            List<Visitor> visitors = gson.fromJson(reader, visitorListType);


            // Задание 1
            System.out.println("Задание 1");
            System.out.println("Список посетителей:");
            visitors.forEach(visitor ->
                    System.out.println(visitor.getName() + " " + visitor.getSurname()));
            System.out.println("Количество посетителей: " + visitors.size());

            // Задание 2
            System.out.println("Задание 2");
            List<Book> uniqueBooks = visitors.stream()
                    .flatMap(visitor -> visitor.getFavoriteBooks().stream())
                    .distinct()
                    .collect(Collectors.toList());
            System.out.println("Список уникальных книг в избранном:");
            uniqueBooks.forEach(book -> System.out.println(book.getName()));
            System.out.println("Количество уникальных книг: " + uniqueBooks.size());

            // Задание 3
            System.out.println("Задание 3");
            List<Book> sortedBooks = visitors.stream()
                    .flatMap(visitor -> visitor.getFavoriteBooks().stream())
                    .sorted((b1, b2) -> Integer.compare(b1.getPublishingYear(), b2.getPublishingYear()))
                    .collect(Collectors.toList());
            System.out.println("Список книг, отсортированных по году издания:");
            sortedBooks.forEach(book ->
                    System.out.println(book.getName() + " (" + book.getPublishingYear() + ")"));

            // Задание 4
            System.out.println("Задание 4");
            boolean hasJaneAustenBook = visitors.stream()
                    .flatMap(visitor -> visitor.getFavoriteBooks().stream())
                    .anyMatch(book -> "Jane Austen".equals(book.getAuthor()));
            System.out.println("Есть ли книга автора Jane Austen в избранном: " + hasJaneAustenBook);

            // Задание 5
            System.out.println("Задание 5");
            int maxFavoriteBooks = visitors.stream()
                    .mapToInt(visitor -> visitor.getFavoriteBooks().size())
                    .max()
                    .orElse(0);
            System.out.println("Максимальное число добавленных в избранное книг: " + maxFavoriteBooks);

            // Задание 6
            System.out.println("Задание 6");
            double averageBooksCount = visitors.stream()
                    .mapToInt(visitor -> visitor.getFavoriteBooks().size())
                    .average()
                    .orElse(0);

            List<Sms> smsList = visitors.stream()
                    .filter(Visitor::isSubscribed)
                    .map(visitor -> {
                        int count = visitor.getFavoriteBooks().size();
                        String message;
                        if (count > averageBooksCount) {
                            message = "you are a bookworm";
                        } else if (count < averageBooksCount) {
                            message = "read more";
                        } else {
                            message = "fine";
                        }
                        return new Sms(visitor.getPhone(), message);
                    }).collect(Collectors.toList());

            // Вывод SMS
            System.out.println("SMS-сообщения:");

            smsList.forEach(sms ->
                    System.out.println("Телефон: " + sms.getPhone() + ", Сообщение: " + sms.getMessage()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
