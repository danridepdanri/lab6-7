package pack;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Параметри підключення до бази даних
        String url = "jdbc:mysql://localhost:3306/housing";
        String username = "root";
        String password = "";

        try {
            // Підключення до бази даних
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database");

            // Створення об'єкту Scanner для отримання введення користувача
            Scanner scanner = new Scanner(System.in);

            // Головне меню
            int choice = 0;
            while (choice != 5) {
                System.out.println("\nМеню:");
                System.out.println("1. Введення даних");
                System.out.println("2. Побудова звітів");
                System.out.println("3. Пошук за критерієм");
                System.out.println("4. Показати всі дані у таблицях");
                System.out.println("5. Вийти");
                System.out.print("Виберіть опцію: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        // Введення даних
                        System.out.println("\nВведення даних:");
                        System.out.println("1. Buildings");
                        System.out.println("2. Residents");
                        System.out.println("3. Services");
                        System.out.print("Виберіть таблицю для введення даних: ");
                        int tableChoice = scanner.nextInt();
                        scanner.nextLine(); // Очищення буфера

                        switch (tableChoice) {
                            case 1:
                                // Введення даних у таблицю Buildings
                                System.out.print("Введіть ID будівлі: ");
                                int buildingID = scanner.nextInt();
                                scanner.nextLine(); // Очищення буфера
                                System.out.print("Введіть адресу будівлі: ");
                                String address = scanner.nextLine();
                                System.out.print("Введіть кількість поверхів: ");
                                int numOfFloors = scanner.nextInt();
                                System.out.print("Введіть загальну площу: ");
                                float totalArea = scanner.nextFloat();

                                String insertBuildingQuery = "INSERT INTO Buildings (ID, Address, NumberOfFloors, TotalArea) VALUES (?, ?, ?, ?)";
                                PreparedStatement insertBuildingStatement = connection.prepareStatement(insertBuildingQuery);
                                insertBuildingStatement.setInt(1, buildingID);
                                insertBuildingStatement.setString(2, address);
                                insertBuildingStatement.setInt(3, numOfFloors);
                                insertBuildingStatement.setFloat(4, totalArea);
                                insertBuildingStatement.executeUpdate();
                                System.out.println("Дані успішно введені");
                                break;

                            case 2:
                                // Введення даних у таблицю Residents
                                System.out.print("Введіть ID мешканця: ");
                                int residentID = scanner.nextInt();
                                scanner.nextLine(); // Очищення буфера
                                System.out.print("Введіть прізвище мешканця: ");
                                String lastName = scanner.nextLine();
                                System.out.print("Введіть ім'я мешканця: ");
                                String firstName = scanner.nextLine();
                                System.out.print("Введіть контактний номер: ");
                                String contactNumber = scanner.nextLine();

                                String insertResidentQuery = "INSERT INTO Residents (ID, LastName, FirstName, ContactNumber) VALUES (?, ?, ?, ?)";
                                PreparedStatement insertResidentStatement = connection.prepareStatement(insertResidentQuery);
                                insertResidentStatement.setInt(1, residentID);
                                insertResidentStatement.setString(2, lastName);
                                insertResidentStatement.setString(3, firstName);
                                insertResidentStatement.setString(4, contactNumber);
                                insertResidentStatement.executeUpdate();
                                System.out.println("Дані успішно введені");
                                break;

                            case 3:
                                // Введення даних у таблицю Services
                                System.out.print("Введіть ID послуги: ");
                                int serviceID = scanner.nextInt();
                                scanner.nextLine(); // Очищення буфера
                                System.out.print("Введіть ID мешканця: ");
                                int residentIDService = scanner.nextInt();
                                scanner.nextLine(); // Очищення буфера
                                System.out.print("Введіть назву послуги: ");
                                String serviceName = scanner.nextLine();
                                System.out.print("Введіть дату послуги (у форматі РРРР-ММ-ДД): ");
                                String serviceDateStr = scanner.nextLine();
                                Date serviceDate = Date.valueOf(serviceDateStr);

                                String insertServiceQuery = "INSERT INTO Services (ID, ResidentID, ServiceName, ServiceDate) VALUES (?, ?, ?, ?)";
                                PreparedStatement insertServiceStatement = connection.prepareStatement(insertServiceQuery);
                                insertServiceStatement.setInt(1, serviceID);
                                insertServiceStatement.setInt(2, residentIDService);
                                insertServiceStatement.setString(3, serviceName);
                                insertServiceStatement.setDate(4, serviceDate);
                                insertServiceStatement.executeUpdate();
                                System.out.println("Дані успішно введені");
                                break;

                            default:
                                System.out.println("Невірний вибір");
                                break;
                        }
                        break;

                    case 2:
                        // Меню для побудови звітів
                        int reportChoice = 0;
                        while (reportChoice != 3) {
                            System.out.println("\nПобудова звітів");
                            System.out.println("1. Загальна площа будівель за адресами");
                            System.out.println("2. Перелік послуг, наданих мешканцям за певний період часу");
                            System.out.println("3. Повернутися до попереднього меню");
                            System.out.print("Виберіть опцію: ");
                            reportChoice = scanner.nextInt();

                            switch (reportChoice) {
                                case 1:
                                    // Звіт: Загальна площа будівель за адресами
                                    String reportQuery1 = "SELECT Address, SUM(TotalArea) AS TotalArea FROM Buildings GROUP BY Address";
                                    Statement reportStatement1 = connection.createStatement();
                                    ResultSet reportResult1 = reportStatement1.executeQuery(reportQuery1);
                                    while (reportResult1.next()) {
                                        String buildingAddress = reportResult1.getString("Address");
                                        float buildingTotalArea = reportResult1.getFloat("TotalArea");
                                        System.out.println("Address: " + buildingAddress + ", Total Area: " + buildingTotalArea);
                                    }
                                    break;

                                case 2:
                                    // Звіт: Перелік послуг, наданих мешканцям за певний період часу
                                    System.out.print("Введіть початкову дату (у форматі yyyy-mm-dd): ");
                                    String startDate = scanner.next();
                                    System.out.print("Введіть кінцеву дату (у форматі yyyy-mm-dd): ");
                                    String endDate = scanner.next();

                                    String reportQuery2 = "SELECT Residents.LastName, Residents.FirstName, Services.ServiceName, Services.ServiceDate " +
                                            "FROM Residents " +
                                            "INNER JOIN Services ON Residents.ID = Services.ResidentID " +
                                            "WHERE Services.ServiceDate >= ? AND Services.ServiceDate <= ?";
                                    PreparedStatement reportStatement2 = connection.prepareStatement(reportQuery2);
                                    reportStatement2.setString(1, startDate);
                                    reportStatement2.setString(2, endDate);
                                    ResultSet reportResult2 = reportStatement2.executeQuery();
                                    while (reportResult2.next()) {
                                        String lastName = reportResult2.getString("LastName");
                                        String firstName = reportResult2.getString("FirstName");
                                        String serviceName = reportResult2.getString("ServiceName");
                                        Date serviceDate = reportResult2.getDate("ServiceDate");
                                        System.out.println("Last Name: " + lastName + ", First Name: " + firstName + ", Service: " + serviceName + ", Date: " + serviceDate);
                                    }
                                    break;

                                case 3:
                                    break;

                                default:
                                    System.out.println("Невірний вибір");
                                    break;
                            }
                        }
                        break;

                    case 3:
                        // Пошук у базі даних за критерієм
                        System.out.print("Введіть критерій пошуку (адреса): ");
                        String searchCriteria = scanner.next();

                        String searchQuery = "SELECT * FROM Buildings WHERE Address LIKE '%" + searchCriteria + "%'";
                        Statement searchStatement = connection.createStatement();
                        ResultSet searchResult = searchStatement.executeQuery(searchQuery);
                        while (searchResult.next()) {
                            int id = searchResult.getInt("ID");
                            String buildingAddress = searchResult.getString("Address");
                            int buildingNumOfFloors = searchResult.getInt("NumberOfFloors");
                            float buildingTotalArea = searchResult.getFloat("TotalArea");
                            System.out.println("ID: " + id + ", Address: " + buildingAddress + ", Number of Floors: " + buildingNumOfFloors + ", Total Area: " + buildingTotalArea);
                        }
                        break;

                    case 4:
                        // Показати всі дані у таблицях
                        String selectQuery = "SELECT * FROM Buildings";
                        Statement selectStatement = connection.createStatement();
                        ResultSet selectResult = selectStatement.executeQuery(selectQuery);
                        System.out.println("\nТаблиця Buildings:");
                        while (selectResult.next()) {
                            int id = selectResult.getInt("ID");
                            String buildingAddress = selectResult.getString("Address");
                            int buildingNumOfFloors = selectResult.getInt("NumberOfFloors");
                            float buildingTotalArea = selectResult.getFloat("TotalArea");
                            System.out.println("ID: " + id + ", Address: " + buildingAddress + ", Number of Floors: " + buildingNumOfFloors + ", Total Area: " + buildingTotalArea);
                        }

                        String selectQuery2 = "SELECT * FROM Residents";
                        Statement selectStatement2 = connection.createStatement();
                        ResultSet selectResult2 = selectStatement2.executeQuery(selectQuery2);
                        System.out.println("\nТаблиця Residents:");
                        while (selectResult2.next()) {
                            int id = selectResult2.getInt("ID");
                            String lastName = selectResult2.getString("LastName");
                            String firstName = selectResult2.getString("FirstName");
                            String contactNumber = selectResult2.getString("ContactNumber");
                            System.out.println("ID: " + id + ", Last Name: " + lastName + ", First Name: " + firstName + ", Contact Number: " + contactNumber);
                        }

                        String selectQuery3 = "SELECT * FROM Services";
                        Statement selectStatement3 = connection.createStatement();
                        ResultSet selectResult3 = selectStatement3.executeQuery(selectQuery3);
                        System.out.println("\nТаблиця Services:");
                        while (selectResult3.next()) {
                            int id = selectResult3.getInt("ID");
                            int residentID = selectResult3.getInt("ResidentID");
                            String serviceName = selectResult3.getString("ServiceName");
                            Date serviceDate = selectResult3.getDate("ServiceDate");
                            System.out.println("ID: " + id + ", Resident ID: " + residentID + ", Service: " + serviceName + ", Date: " + serviceDate);
                        }
                        break;

                    case 5:
                        break;

                    default:
                        System.out.println("Невірний вибір");
                        break;
                }
            }

            // Закриття підключення до бази даних
            connection.close();
            System.out.println("Disconnected from the database");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
