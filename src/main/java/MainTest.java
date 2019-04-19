import controller.MainController;
import dto.TotalOrderDto;
import model.Customers;
import model.Orders;
import model.Products;

import java.util.Arrays;
import java.util.List;

public class MainTest {

    private void run() {

        // Buat program Java Non-GUI untuk mentransfer data-data dari file-file CSV ke tabel-tabel yang bersesuaian.

        String [] fileNames = {"Customers.csv", "Employees.csv", "ShippingMethods.csv", "Products.csv", "Orders.csv", "OrderDetails.csv"};
        Arrays.stream(fileNames).forEach(file -> MainController.getInstance().exportToDB(file));
        System.out.println();

        // Tulis perintah SQL untuk menampilkan data, sebagai berikut:

        // Daftar pelanggan yang beralamat di kota Irvine
        // SELECT firstname, lastname FROM Customers WHERE city = 'Irvine'

        List<Customers> customerByCity = MainController.getInstance().findCustomerByCity("Irvine");
        System.out.println("Daftar pelanggan yang beralamat di kota Irvine : ");
        customerByCity.forEach(customer -> System.out.println(customer.getFirstname() + " " + customer.getLastname()));
        System.out.println();

        // Daftar semua pelanggan yang pesanannya ditangani karyawan bernama Adam Barr
        // SELECT c.firstname, c.lastname
        // FROM Customers c
        // INNER JOIN Orders o on c.customer_id = o.customer_id
        // INNER JOIN Employees e on o.employee_id = e.employee_id
        // WHERE CONCAT(e.firstname, ' ', e.lastname) like '%Adam Barr%'
        // GROUP BY c.firstname, c.lastname

        List<Customers> customerByEmployee = MainController.getInstance().findCustomerByEmployee("Adam Barr");
        System.out.println("Daftar semua pelanggan yang pesanannya ditangani karyawan bernama Adam Barr : ");
        customerByEmployee.forEach(customer -> System.out.println(customer.getFirstname() + " " + customer.getLastname()));
        System.out.println();

        // Daftar produk yang dipesan oleh pelanggan Contoso, Ltd
        // SELECT p.product_name
        // FROM Products p
        // INNER JOIN Order_Details od on p.product_id = od.product_id
        // INNER JOIN Orders o on od.order_id = o.order_id
        // INNER JOIN Customers c on o.customer_id = c.customer_id
        // WHERE c.company_name = 'Contoso, Ltd'

        List<Products> productByCustomer = MainController.getInstance().findProductByCustomer("Contoso, Ltd");
        System.out.println("Daftar produk yang dipesan oleh pelanggan Contoso, Ltd : ");
        productByCustomer.forEach(product -> System.out.println(product.getProductName()));
        System.out.println();

        // Daftar transaksi pemesanan yang dikirimkan melalui UPS Ground
        // SELECT c.firstname, c.lastname, e.firstname, e.lastname, o.order_date, o.purchase_order_number, o.ship_date, sm.shipping_method, o.freight_charge, o.taxes, o.payment_received, o.comment
        // FROM Orders o
        // INNER JOIN Customers c on o.customer_id = c.customer_id
        // INNER JOIN Employees e on o.employee_id = e.employee_id
        // INNER JOIN Shipping_Methods sm on o.shipping_method_id = sm.shipping_method_id
        // WHERE sm.shipping_method = 'UPS Ground'

        List<Orders> orderByShippingMethod = MainController.getInstance().findOrderByShippingMethod("UPS Ground");
        System.out.println("Daftar transaksi pemesanan yang dikirimkan melalui UPS Ground : ");
        orderByShippingMethod.forEach(order -> System.out.println(
                order.getCustomer().getFirstname() + " " + order.getCustomer().getLastname() + ", " +
                order.getEmployee().getFirstname() + " " + order.getEmployee().getLastname() + ", " +
                order.getOrderDate() + ", " +
                order.getPuchaseOrderNumber() + ", " +
                order.getShipDate() + ", " +
                order.getShippingMethod().getShippingMethod() + ", " +
                order.getFreightCharge() + ", " +
                order.getTaxes() + ", " +
                order.getPaymentRecevied() + ", " +
                order.getComment()
        ));
        System.out.println();

        // Daftar biaya total pemesanan (termasuk pajak dan biaya pengiriman) setiap
        // transaksi diurut berdasarkan tanggal transaksi
        // SELECT o.order_id, o.order_date, o.taxes, o.freight_charge,
        // ((SELECT SUM(quantity * unit_price) FROM Order_Details WHERE order_id = o.order_id) + o.taxes + o.freight_charge) AS extended_price
        // FROM Orders o
        // INNER JOIN Order_Details od on o.order_id = od.order_id
        // GROUP BY o.order_id
        // order by o.order_date DESC
        List<TotalOrderDto> totalOrders = MainController.getInstance().totalOrders();
        System.out.println("Daftar produk yang dipesan oleh pelanggan Contoso, Ltd : ");
        totalOrders.forEach(System.out::println);
        System.out.println();


    }

    public static void main(String[] args) {
        new MainTest().run();
    }

}
