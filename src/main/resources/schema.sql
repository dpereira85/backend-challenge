CREATE TABLE IF NOT EXISTS STORE (
	id UUID NOT NULL,
	name VARCHAR(255),
	address VARCHAR(255),
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS "ORDER" (
	id UUID NOT NULL,
	store_id UUID NOT NULL,
	address VARCHAR(255),
	confirmation_date TIMESTAMP,
	status VARCHAR(15),	
	PRIMARY KEY(id),
	FOREIGN KEY(store_id) REFERENCES STORE(id)
);

CREATE TABLE IF NOT EXISTS ORDER_ITEM (
	id UUID NOT NULL,
	order_id UUID NOT NULL,
	description VARCHAR(255),
	unit_price DECIMAL(12,2),
	quantity INT,
	PRIMARY KEY(id),
	FOREIGN KEY(order_id) REFERENCES "ORDER"(id)
);

CREATE TABLE IF NOT EXISTS PAYMENT (
	id UUID NOT NULL,
	order_id UUID NOT NULL,
	credit_card BIGINT,
	payment_date TIMESTAMP,
	status VARCHAR(9),
	PRIMARY KEY(id),
	FOREIGN KEY(order_id) REFERENCES "ORDER"(id)
);

CREATE TABLE IF NOT EXISTS REFUND (
	id UUID NOT NULL,
	order_id UUID,
	order_item_id UUID,
	type VARCHAR(10),
	PRIMARY KEY(id),
	FOREIGN KEY(order_id) REFERENCES "ORDER"(id),
	FOREIGN KEY(order_item_id) REFERENCES ORDER_ITEM(id),
);