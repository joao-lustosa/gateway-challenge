package io.github.joaolustosa.gatewaychallenge;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Menu:");
                System.out.println("1. Buscar por CEP");
                System.out.println("2. Exibir endereços salvos");
                System.out.println("3. Sair");
                System.out.print("Insira a opção desejada: ");

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
                        System.out.println("Opção inválida. Por favor, escolha 1, 2, ou 3.");
                }
            }
        } catch (Exception e) {
            System.err.println("Ocorreu um erro: " + e.getMessage());
        }
    }

    private static void getAllAddresses() throws SQLException {
        List<Address> addressList = DatabaseService.getAllAddress();

        if (addressList.isEmpty()) {
            System.out.println("O banco de dados está vazio.");
        } else {
            for (Address address : addressList) {
                System.out.println("------------------------------");
                System.out.println("CEP: " + address.getCep());
                System.out.println("Estado: " + address.getState());
                System.out.println("Cidade: " + address.getCity());
                System.out.println("Vizinhança: " + address.getNeighborhood());
                System.out.println("Rua: " + address.getStreet());
                System.out.println("------------------------------");
            }
        }
    }

    private static void searchForCep(Scanner scanner) throws SQLException {
        System.out.print("Digite um CEP válido: ");

        String cep = scanner.nextLine();

        //Validate CEP

        Address addressInfo = DatabaseService.getAddressByCep(cep);

        if (addressInfo == null) {
            addressInfo = AddressService.fetchCepInfo(cep);
        }

        if (addressInfo != null) {
            System.out.println("CEP: " + addressInfo.getCep());
            System.out.println("Estado: " + addressInfo.getState());
            System.out.println("Cidade: " + addressInfo.getCity());
            System.out.println("Vizinhança: " + addressInfo.getNeighborhood());
            System.out.println("Rua: " + addressInfo.getStreet());
        } else {
            System.out.println("Não foi possível buscar o CEP desejado.");
        }
    }
}
