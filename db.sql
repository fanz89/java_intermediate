create table Customers
(
	customer_id int(11) auto_increment,
	company_name varchar(50) null,
	firstname varchar(30) null,
	lastname varchar(50) null,
	billing_address varchar(255) null,
	city varchar(50) null,
	state_of_province varchar(20) null,
	zip_code varchar(20) null,
	email varchar(75) null,
	company_website varchar(200) null,
	phone_number varchar(30) null,
	fax_number varchar(30) null,
	ship_address varchar(255) null,
	ship_city varchar(50) null,
	ship_state_or_province varchar(50) null,
	ship_zip_code varchar(20) null,
	ship_phone_number varchar(30) null,

	constraint Customers_pk primary key (customer_id)

);


create table Employees
(
	employee_id int(11) auto_increment,
	firstname varchar(50) null,
	lastname varchar(50) null,
	title varchar(50) null,
	work_phone varchar(30) null,

	constraint Employees_pk primary key (employee_id)
);

create table Shipping_Methods
(
	shipping_method_id int(11) auto_increment,
	shipping_method varchar(20) null,

	constraint Shipping_Methods_pk primary key (shipping_method_id)

);

create table Products
(
	product_id int auto_increment,
	product_name varchar(50) null,
	unit_price double(3,1) null,
	in_stock char null,

	constraint Products_pk primary key (product_id)

);

create table Orders
(
	order_id int(11) auto_increment,
	customer_id int(11) null,
	employee_id int(11) null,
	order_date date null,
	purchase_order_number varchar(30) null,
	ship_date date null,
	shipping_method_id int(11) null,
	freight_charge int(11) null,
	taxes int(11) null,
	payment_received char null,
	comment varchar(150) null,

	constraint Orders_pk primary key (order_id)

);

create table Order_Details
(
	order_detail_id int(11) auto_increment,
	order_id int(11) null,
	product_id int(11) null,
	quantity int(4) null,
	unit_price double(3,1) null,
	discont int(3) null,

	constraint Order_Details_pk primary key (order_detail_id)

);

alter table Orders
	add constraint Orders_Customers_customer_id_fk
		foreign key (customer_id) references Customers (customer_id);

alter table Orders
	add constraint Orders_Employees_employee_id_fk
		foreign key (employee_id) references Employees (employee_id);

alter table Orders
	add constraint Orders_Shipping_Methods_shipping_method_id_fk
		foreign key (shipping_method_id) references Shipping_Methods (shipping_method_id);

alter table Order_Details
	add constraint Order_Details_Orders_order_id_fk
		foreign key (order_id) references Orders (order_id);

alter table Order_Details
	add constraint Order_Details_Products_product_id_fk
		foreign key (product_id) references Products (product_id);









