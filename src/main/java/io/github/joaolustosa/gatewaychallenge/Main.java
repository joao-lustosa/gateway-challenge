package io.github.joaolustosa.gatewaychallenge;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Menu:");
                System.out.println("1. Search for a CEP");
                System.out.println("2. Get all CEPs");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        searchForCep(scanner);

                        break;
                    case "2":
                        getAllAddresses();

                        break;
                    case "3":
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private static void getAllAddresses() throws SQLException {
        List<Address> addressList = DatabaseService.getAllAddress();

        if (addressList.isEmpty()) {
            System.out.println("No records found in the database.");
        } else {
            for (Address address : addressList) {
                System.out.println("------------------------------");
                System.out.println("CEP: " + address.getCep());
                System.out.println("State: " + address.getState());
                System.out.println("City: " + address.getCity());
                System.out.println("Neighborhood: " + address.getNeighborhood());
                System.out.println("Street: " + address.getStreet());
                System.out.println("------------------------------");
            }
        }
    }

    private static void searchForCep(Scanner scanner) throws SQLException {
        System.out.print("Enter a valid CEP: ");

        String cep = scanner.nextLine();

        //Validate CEP

        Address addressInfo = DatabaseService.getAddressByCep(cep);

        if (addressInfo == null) {
            addressInfo = AddressService.fetchCepInfo(cep);
        }

        if (addressInfo != null) {
            System.out.println("CEP: " + addressInfo.getCep());
            System.out.println("State: " + addressInfo.getState());
            System.out.println("City: " + addressInfo.getCity());
            System.out.println("Neighborhood: " + addressInfo.getNeighborhood());
            System.out.println("Street: " + addressInfo.getStreet());
        } else {
            System.out.println("Failed to retrieve CEP information.");
        }
    }
}
