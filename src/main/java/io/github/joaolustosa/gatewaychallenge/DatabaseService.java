package io.github.joaolustosa.gatewaychallenge;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseService {
    public static final String DB_URL;
    public static final String DB_USER;
    public static final String DB_PASSWORD;

    static {
        Properties properties = new Properties();

        try {
            properties.load(AddressService.class.getClassLoader().getResourceAsStream("config.properties"));

            DB_URL = properties.getProperty("dbUrl");
            DB_USER = properties.getProperty("dbUser");
            DB_PASSWORD = properties.getProperty("dbPassword");
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar as propriedades.", e);
        }
    }

    public static void addAddress(Address addressInfo) throws SQLException {
        String query = "INSERT INTO addresses (cep, state, city, neighborhood, street) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, addressInfo.getCep());
            statement.setString(2, addressInfo.getState());
            statement.setString(3, addressInfo.getCity());
            statement.setString(4, addressInfo.getNeighborhood());
            statement.setString(5, addressInfo.getStreet());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("A operação de inserção falhou.");
            }
        } catch (SQLException e) {
            System.err.println("Ocorreu uma falha ao tentar inserir dados do CEP: " + e.getMessage());

            throw e;
        }
    }

    public static Address getAddressByCep(String cep) throws SQLException {
        String sql = "SELECT * FROM addresses WHERE cep = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cep);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Address retrievedAddress = new Address();

                    retrievedAddress.setCep(resultSet.getString("cep"));
                    retrievedAddress.setState(resultSet.getString("state"));
                    retrievedAddress.setCity(resultSet.getString("city"));
                    retrievedAddress.setNeighborhood(resultSet.getString("neighborhood"));
                    retrievedAddress.setStreet(resultSet.getString("street"));

                    return retrievedAddress;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Ocorreu um erro ao buscar dados do CEP: " + e.getMessage());

            throw e;
        }
    }

    public static List<Address> getAllAddress() throws SQLException {
        String sql = "SELECT * FROM addresses";

        List<Address> addressList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Address address = new Address();

                address.setCep(resultSet.getString("cep"));
                address.setState(resultSet.getString("state"));
                address.setCity(resultSet.getString("city"));
                address.setNeighborhood(resultSet.getString("neighborhood"));
                address.setStreet(resultSet.getString("street"));

                addressList.add(address);
            }
        } catch (SQLException e) {
            System.err.println("Ocorreu um erro de SQL: " + e.getMessage());

            throw e;
        }

        return addressList;
    }
}
