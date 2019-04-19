package controller;

import dto.TotalOrderDto;
import model.*;
import utils.MySQLConnection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MainController {

    private final String PATH_FILE = System.getProperty("user.dir") + "/src/main/java/report/";

    private final String INSERT_CUSTOMER = "INSERT INTO Customers (customer_id, company_name, firstname, lastname, billing_address, city, state_of_province, zip_code, email, company_website, phone_number, fax_number, ship_address, ship_city, ship_state_or_province, ship_zip_code, ship_phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String INSERT_EMPLOYEES = "INSERT INTO Employees (employee_id, firstname, lastname, title, work_phone) VALUES (?, ?, ?, ?, ?)";
    private final String INSERT_SHIPPING_METHODS = "INSERT INTO Shipping_Methods (shipping_method_id, shipping_method) VALUES (?, ?)";
    private final String INSERT_PRODUCTS = "INSERT INTO Products (product_id, product_name, unit_price, in_stock) VALUES (?, ?, ?, ?)";
    private final String INSERT_ORDERS = "INSERT INTO Orders (order_id, customer_id, employee_id, order_date, purchase_order_number, ship_date, shipping_method_id, freight_charge, taxes, payment_received, comment) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String INSERT_ORDER_DETAILS = "INSERT INTO ORDER_DETAILS (order_detail_id, order_id, product_id, quantity, unit_price, discount) VALUES (?, ?, ?, ?, ?, ?)";

    private final String FIND_CUSTOMER_BY_CITY = "SELECT firstname, lastname FROM Customers WHERE city = (?)";
    private final String FIND_CUSTOMER_BY_EMPLOYEE = "SELECT c.firstname, c.lastname FROM Customers c INNER JOIN Orders o on c.customer_id = o.customer_id INNER JOIN Employees e on o.employee_id = e.employee_id where CONCAT(e.firstname, ' ', e.lastname) like ? GROUP BY c.firstname, c.lastname";
    private final String FIND_PRODUCT_BY_CUSTOMER = "SELECT p.product_name FROM Products p INNER JOIN Order_Details od on p.product_id = od.product_id INNER JOIN Orders o on od.order_id = o.order_id INNER JOIN Customers c on o.customer_id = c.customer_id WHERE c.company_name = ?";
    private final String FIND_ORDER_BY_SHIPPING_METHOD = "SELECT c.firstname, c.lastname, e.firstname, e.lastname, o.order_date, o.purchase_order_number, o.ship_date, sm.shipping_method, o.freight_charge, o.taxes, o.payment_received, o.comment FROM Orders o INNER JOIN Customers c on o.customer_id = c.customer_id INNER JOIN Employees e on o.employee_id = e.employee_id INNER JOIN Shipping_Methods sm on o.shipping_method_id = sm.shipping_method_id WHERE sm.shipping_method = ?";
    private final String TOTAL_ORDERS = "SELECT o.order_id, o.order_date, o.taxes, o.freight_charge, ((SELECT SUM(quantity * unit_price) FROM Order_Details WHERE order_id = o.order_id) + o.taxes + o.freight_charge) AS extended_price FROM Orders o INNER JOIN Order_Details od on o.order_id = od.order_id GROUP BY o.order_id order by o.order_date DESC";

    public static MainController getInstance() {
        return SoalBHolder.INSTANCE;
    }

    private static class SoalBHolder {

        private static final MainController INSTANCE = new MainController();
    }

    private java.sql.Date convertDate(String value) {
        try {
            if (!value.isEmpty()) {
                return new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(value).getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Customers> findCustomerByCity(String param) {
        List<Customers> customers = new ArrayList<>();

        try {

            PreparedStatement statement = MySQLConnection
                    .getInstance()
                    .getConnection()
                    .prepareStatement(FIND_CUSTOMER_BY_CITY);

            statement.setString(1, param);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                customers.add(
                        Customers.builder()
                                .firstname(result.getString(1))
                                .lastname(result.getString(2)).build()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;

    }

    public List<Customers> findCustomerByEmployee(String param) {
        List<Customers> customers = new ArrayList<>();

        try {

            PreparedStatement statement = MySQLConnection
                    .getInstance()
                    .getConnection()
                    .prepareStatement(FIND_CUSTOMER_BY_EMPLOYEE);

            statement.setString(1, "%" + param + "%");

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                customers.add(
                        Customers.builder()
                                .firstname(result.getString(1))
                                .lastname(result.getString(2)).build()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;

    }

    public List<Products> findProductByCustomer(String param) {
        List<Products> products = new ArrayList<>();

        try {

            PreparedStatement statement = MySQLConnection
                    .getInstance()
                    .getConnection()
                    .prepareStatement(FIND_PRODUCT_BY_CUSTOMER);

            statement.setString(1, param);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                products.add(Products.builder()
                        .productName(result.getString("product_name"))
                        .build()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;

    }

    public List<Orders> findOrderByShippingMethod(String param) {
        List<Orders> orders = new ArrayList<>();

        try {

            PreparedStatement statement = MySQLConnection
                    .getInstance()
                    .getConnection()
                    .prepareStatement(FIND_ORDER_BY_SHIPPING_METHOD);

            statement.setString(1, param);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                orders.add(
                        Orders.builder()
                                .customer(Customers.builder()
                                        .firstname(result.getString(1))
                                        .lastname(result.getString(2))
                                        .build())
                                .employee(Employees.builder()
                                        .firstname(result.getString(3))
                                        .lastname(result.getString(4))
                                        .build())
                                .orderDate(result.getDate(5))
                                .puchaseOrderNumber(result.getString(6))
                                .shipDate(result.getDate(7))
                                .shippingMethod(ShippingMethods.builder()
                                        .shippingMethod(result.getString(8))
                                        .build())
                                .freightCharge(result.getInt(9))
                                .taxes(result.getInt(10))
                                .paymentRecevied(result.getString(11).charAt(0))
                                .comment(result.getString(12))
                                .build()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;

    }

    public List<TotalOrderDto> totalOrders() {
        List<TotalOrderDto> listTotalOrder = new ArrayList<>();

        try {

            PreparedStatement statement = MySQLConnection
                    .getInstance()
                    .getConnection()
                    .prepareStatement(TOTAL_ORDERS);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                listTotalOrder.add(TotalOrderDto.builder()
                        .orderId(result.getInt(1))
                        .orderDate(result.getDate(2))
                        .taxes(result.getInt(3))
                        .freightCharge(result.getInt(4))
                        .extendedCharge(result.getDouble(5))
                        .build()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listTotalOrder;

    }

    private void insertCustomer(Stream<String> stream) {
        try {
            PreparedStatement statement = MySQLConnection
                    .getInstance()
                    .getConnection()
                    .prepareStatement(INSERT_CUSTOMER);

            stream.forEach(s -> {
                String values[] = s.split(";");
                try {
                    statement.setInt(1, Integer.valueOf(values[0]));
                    statement.setString(2, values[1]);
                    statement.setString(3, values[2]);
                    statement.setString(4, values[3]);
                    statement.setString(5, values[4]);
                    statement.setString(6, values[5]);
                    statement.setString(7, values[6]);
                    statement.setString(8, values[7]);
                    statement.setString(9, values[8]);
                    statement.setString(10, values[9]);
                    statement.setString(11, values[10]);
                    statement.setString(12, values[11]);
                    statement.setString(13, values[12]);
                    statement.setString(14, values[13]);
                    statement.setString(15, values[14]);
                    statement.setString(16, values[15]);
                    statement.setString(17, values[16]);

                    statement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertEmployees(Stream<String> stream) {
        try {
            PreparedStatement statement = MySQLConnection
                    .getInstance()
                    .getConnection()
                    .prepareStatement(INSERT_EMPLOYEES);

            stream.forEach(s -> {
                String values[] = s.split(";");
                try {
                    statement.setInt(1, Integer.valueOf(values[0]));
                    statement.setString(2, values[1]);
                    statement.setString(3, values[2]);
                    statement.setString(4, values[3]);
                    statement.setString(5, values[4]);

                    statement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertShippingMethods(Stream<String> stream) {
        try {
            PreparedStatement statement = MySQLConnection
                    .getInstance()
                    .getConnection()
                    .prepareStatement(INSERT_SHIPPING_METHODS);

            stream.forEach(s -> {
                String values[] = s.split(";");
                try {
                    statement.setInt(1, Integer.valueOf(values[0]));
                    statement.setString(2, values[1]);

                    statement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void insertProducts(Stream<String> stream) {
        try {
            PreparedStatement statement = MySQLConnection
                    .getInstance()
                    .getConnection()
                    .prepareStatement(INSERT_PRODUCTS);

            stream.forEach(s -> {
                String values[] = s.split(";");
                try {
                    statement.setInt(1, Integer.valueOf(values[0]));
                    statement.setString(2, values[1]);
                    statement.setDouble(3, Double.valueOf(values[2].replace(",", ".")));
                    statement.setString(4, values[3]);

                    statement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void insertOrders(Stream<String> stream) {
        try {
            PreparedStatement statement = MySQLConnection
                    .getInstance()
                    .getConnection()
                    .prepareStatement(INSERT_ORDERS);

            stream.forEach(s -> {
                String values[] = s.split(";");
                try {
                    statement.setInt(1, Integer.valueOf(values[0]));
                    statement.setInt(2, Integer.valueOf(values[1]));
                    statement.setInt(3, Integer.valueOf(values[2]));
                    statement.setDate(4, convertDate(values[3]));
                    statement.setString(5, values[4]);
                    statement.setDate(6, convertDate(values[5]));
                    statement.setInt(7, Integer.valueOf(values[6]));
                    statement.setInt(8, (values[7].isEmpty()) ? 0 : Integer.valueOf(values[7]));
                    statement.setInt(9, Integer.valueOf(values[8]));
                    statement.setString(10, values[9]);
                    statement.setString(11, "");

                    statement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void insertOrderDetails(Stream<String> stream) {
        try {
            PreparedStatement statement = MySQLConnection
                    .getInstance()
                    .getConnection()
                    .prepareStatement(INSERT_ORDER_DETAILS);

            stream.forEach(s -> {
                String values[] = s.split(";");
                try {
                    statement.setInt(1, Integer.valueOf(values[0]));
                    statement.setInt(2, Integer.valueOf(values[1]));
                    statement.setInt(3, Integer.valueOf(values[2]));
                    statement.setInt(4, Integer.valueOf(values[3]));
                    statement.setDouble(5, Double.valueOf(values[4].replace(",", ".")));
                    statement.setInt(6, Integer.valueOf(values[5].replace("%", "")));

                    statement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void exportToDB(String fileName) {
        try (Stream<String> stream = Files.lines(Paths.get(PATH_FILE + fileName)).filter(lines -> Character.isDigit(lines.charAt(0)))) {
            switch (fileName) {
                case "Customers.csv":
                    insertCustomer(stream);
                    System.out.println("Customers berhasil ditambahkan!!");
                    break;
                case "Employees.csv":
                    insertEmployees(stream);
                    System.out.println("Employees berhasil ditambahkan!!");
                    break;
                case "ShippingMethods.csv":
                    insertShippingMethods(stream);
                    System.out.println("Shipping Methods berhasil ditambahkan!!");
                    break;
                case "Products.csv":
                    insertProducts(stream);
                    System.out.println("Products berhasil ditambahkan!!");
                    break;
                case "Orders.csv":
                    insertOrders(stream);
                    System.out.println("Orders berhasil ditambahkan!!");
                    break;
                case "OrderDetails.csv":
                    insertOrderDetails(stream);
                    System.out.println("Order Details berhasil ditambahkan!!");
                    break;
            }
        } catch (IOException io) {
            System.out.println("Error : " + io.getMessage());
        }
    }

}
