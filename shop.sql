-- Xóa CSDL nếu tồn tại
IF EXISTS (SELECT name FROM sys.databases WHERE name = N'webshop')
BEGIN
    DROP DATABASE webshop;
END
GO

-- Tạo CSDL
CREATE DATABASE webshop;
GO

-- Sử dụng CSDL
USE webshop;
GO

-- ROLES
CREATE TABLE roles (
  id INT PRIMARY KEY IDENTITY(1,1),
  name VARCHAR(50) UNIQUE NOT NULL,
  description VARCHAR(255),
  created_at DATETIME DEFAULT GETDATE(),
  status BIT DEFAULT 0
);

-- USERS
CREATE TABLE users (
  id INT PRIMARY KEY IDENTITY(1,1),
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  username VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255),
  birth_of_date DATE,
  phone_number VARCHAR(20),
  role_id INT,
  created_at DATETIME DEFAULT GETDATE(),
  [avatar] VARCHAR(MAX) DEFAULT 'images/default-avatar.png',
  status BIT DEFAULT 0,
  FOREIGN KEY (role_id) REFERENCES roles(id)
);
;
-- ADDRESSES
CREATE TABLE addresses (
  id INT PRIMARY KEY IDENTITY(1,1),
  user_id INT,
  address_line VARCHAR(255),
  city VARCHAR(100),
  phone_number VARCHAR(20),
  created_at DATETIME DEFAULT GETDATE(),
  status BIT DEFAULT 0,
  FOREIGN KEY (user_id) REFERENCES users(id),
  postal_code VARCHAR(20),
   is_default BIT
);
-- BẢNG SẢN PHẨM CHÍNH
CREATE TABLE products (
  id INT PRIMARY KEY IDENTITY(1,1),
  name NVARCHAR(255),
  description TEXT,
  summary NVARCHAR(255),
  cover NVARCHAR(255), -- ảnh đại diện chính
  created_at DATETIME DEFAULT GETDATE(),
  updated_at DATETIME DEFAULT GETDATE(),
  status BIT DEFAULT 1  -- 1: active, 0: inactive
);

-- BẢNG ẢNH SẢN PHẨM (nhiều ảnh cho 1 sản phẩm)
CREATE TABLE product_images (
  id INT PRIMARY KEY IDENTITY(1,1),
  product_id INT,
  image_url NVARCHAR(255),
  is_primary BIT DEFAULT 0, -- có thể xác định ảnh chính
  created_at DATETIME DEFAULT GETDATE(),
  FOREIGN KEY (product_id) REFERENCES products(id)
);

-- BẢNG THUỘC TÍNH (size,...)
CREATE TABLE product_attributes (
  id INT PRIMARY KEY IDENTITY(1,1),
  type VARCHAR(50) ,
  value NVARCHAR(100),
  created_at DATETIME DEFAULT GETDATE(),
  status BIT DEFAULT 1
);

-- BẢNG SKU (từng biến thể sản phẩm: size + giá + số lượng)
CREATE TABLE products_skus (
  id INT PRIMARY KEY IDENTITY(1,1),
  product_id INT,
  size_attribute_id INT,
  sku NVARCHAR(100), --mã định danh sp
  price DECIMAL(18,2),
  quantity INT,
  created_at DATETIME DEFAULT GETDATE(),
  status BIT DEFAULT 1,
  FOREIGN KEY (product_id) REFERENCES products(id),
  FOREIGN KEY (size_attribute_id) REFERENCES product_attributes(id),
);

--Done 12/07/2025
-- CART
CREATE TABLE cart (
    id INT IDENTITY PRIMARY KEY,
    user_id INT NOT NULL,
    created_at DATETIME DEFAULT GETDATE()
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- CART ITEM
CREATE TABLE cart_items (
    id INT IDENTITY PRIMARY KEY,
    cart_id INT NOT NULL,
    sku_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    FOREIGN KEY (cart_id) REFERENCES cart(id),
    FOREIGN KEY (sku_id) REFERENCES products_skus(id)
);
--Order
CREATE TABLE orders (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    order_date DATETIME DEFAULT GETDATE(),
    status NVARCHAR(50) DEFAULT 'PENDING',
    total DECIMAL(10, 2) NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE order_items (
    id INT PRIMARY KEY IDENTITY(1,1),
    order_id INT NOT NULL,
    sku_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL, -- snapshot tại thời điểm mua

    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (sku_id) REFERENCES products_skus(id)
);

INSERT INTO users (first_name, last_name, username, email, password, birth_of_date, phone_number, role_id, status) VALUES
('Admin', 'System', 'admin', 'admin@gmail.com', '123456', '1990-01-01', '0901234567', 1, 1),

-- Customer users (role_id = 2)
('John', 'Smith', 'johnsmith', 'johnsmith@gmail.com', 'password123', '1995-03-15', '0901111111', 2, 1),
('Emily', 'Johnson', 'emilyjohnson', 'emilyjohnson@gmail.com', 'password123', '1992-07-22', '0902222222', 2, 1),
('Michael', 'Brown', 'michaelbrown', 'michaelbrown@gmail.com', 'password123', '1988-11-08', '0903333333', 2, 1),
('Sarah', 'Davis', 'sarahdavis', 'sarahdavis@gmail.com', 'password123', '1996-05-12', '0904444444', 2, 1),
('David', 'Wilson', 'davidwilson', 'davidwilson@gmail.com', 'password123', '1993-09-25', '0905555555', 2, 1),
('Jessica', 'Miller', 'jessicamiller', 'jessicamiller@gmail.com', 'password123', '1991-12-03', '0906666666', 2, 1),
('James', 'Garcia', 'jamesgarcia', 'jamesgarcia@gmail.com', 'password123', '1994-02-18', '0907777777', 2, 1),
('Ashley', 'Martinez', 'ashleymartinez', 'ashleymartinez@gmail.com', 'password123', '1989-06-30', '0908888888', 2, 1),
('Christopher', 'Anderson', 'christopheranderson', 'christopheranderson@gmail.com', 'password123', '1997-04-14', '0909999999', 2, 1),
('Amanda', 'Taylor', 'amandataylor', 'amandataylor@gmail.com', 'password123', '1990-08-07', '0910101010', 2, 1),
('Matthew', 'Thomas', 'matthewthomas', 'matthewthomas@gmail.com', 'password123', '1985-10-21', '0911111111', 2, 1),
('Jennifer', 'Jackson', 'jenniferjackson', 'jenniferjackson@gmail.com', 'password123', '1998-01-09', '0912121212', 2, 1),
('Andrew', 'White', 'andrewwhite', 'andrewwhite@gmail.com', 'password123', '1987-03-17', '0913131313', 2, 1),
('Nicole', 'Harris', 'nicoleharris', 'nicoleharris@gmail.com', 'password123', '1999-07-05', '0914141414', 2, 1),
('Ryan', 'Clark', 'ryanclark', 'ryanclark@gmail.com', 'password123', '1986-11-23', '0915151515', 2, 1),
('Stephanie', 'Lewis', 'stephanielewis', 'stephanielewis@gmail.com', 'password123', '1995-12-11', '0916161616', 2, 1),
('Brandon', 'Robinson', 'brandonrobinson', 'brandonrobinson@gmail.com', 'password123', '1992-04-28', '0917171717', 2, 1),
('Michelle', 'Walker', 'michellewalker', 'michellewalker@gmail.com', 'password123', '1993-08-16', '0918181818', 2, 1),
('Kevin', 'Hall', 'kevinhall', 'kevinhall@gmail.com', 'password123', '1990-02-02', '0919191919', 2, 1),
('Rachel', 'Allen', 'rachelallen', 'rachelallen@gmail.com', 'password123', '1996-06-19', '0920202020', 2, 1),

-- Sale users (role_id = 3)
('Robert', 'Young', 'robertyoung_sale', 'robertyoung.sale@gmail.com', 'sale123', '1991-05-10', '0921212121', 3, 1),
('Lisa', 'King', 'lisaking_sale', 'lisaking.sale@gmail.com', 'sale123', '1988-09-14', '0922222222', 3, 1),
('Daniel', 'Wright', 'danielwright_sale', 'danielwright.sale@gmail.com', 'sale123', '1994-01-27', '0923232323', 3, 1),
('Karen', 'Lopez', 'karenlopez_sale', 'karenlopez.sale@gmail.com', 'sale123', '1989-07-13', '0924242424', 3, 1),
('Jason', 'Hill', 'jasonhill_sale', 'jasonhill.sale@gmail.com', 'sale123', '1992-11-06', '0925252525', 3, 1),
('Nancy', 'Scott', 'nancyscott_sale', 'nancyscott.sale@gmail.com', 'sale123', '1987-03-20', '0926262626', 3, 1),
('Mark', 'Green', 'markgreen_sale', 'markgreen.sale@gmail.com', 'sale123', '1995-08-08', '0927272727', 3, 1),
('Sandra', 'Adams', 'sandraadams_sale', 'sandraadams.sale@gmail.com', 'sale123', '1993-12-24', '0928282828', 3, 1),
('Paul', 'Baker', 'paulbaker_sale', 'paulbaker.sale@gmail.com', 'sale123', '1990-04-15', '0929292929', 3, 1);




-- PRODUCTS (using local images in cover field)
INSERT INTO products (name, description, summary, cover, created_at, updated_at, status)
VALUES
(N'Parallettes Large', N'Large parallettes for advanced push-ups.', N'Parallettes Large', '/images/parallettes-max-produktfoto-1_1200x.jpg.jpeg', GETDATE(), GETDATE(), 1),
(N'Parallettes Medium', N'Medium parallettes for standard use.', N'Parallettes Medium', '/images/parallettes-active-product-photo-1-calisthenics_1200x.jpg.jpeg', GETDATE(), GETDATE(), 1),
(N'Parallettes Small', N'Small parallettes for portability.', N'Parallettes Small', '/images/product-shot-wooden-parallettes-extended-2_91945174-2e6b-43e3-b101-1cf692ed68c2_1200x.jpg.jpeg', GETDATE(), GETDATE(), 1),
(N'Dip Belt', N'Dip belt for weighted dips.', N'Dip Belt', '/images/calisthnics-dip-belt-with-rope-1_1200x.jpeg', GETDATE(), GETDATE(), 1),
(N'Dip Bars', N'Dip bars for bodyweight exercises.', N'Dip Bars', '/images/push-up-bars-gornation-1_5decedcc-70f2-4d79-a74c-dcf6e87ee405_1200x.jpg.jpeg', GETDATE(), GETDATE(), 1),
(N'Static Bar', N'Static bar for static holds.', N'Static Bar', '/images/static-bar-gornation-product.jpg.jpeg', GETDATE(), GETDATE(), 1),
(N'Workout Rings Wood', N'Wooden rings for calisthenics.', N'Workout Rings Wood', '/images/workout-rings-gornation_300x.jpeg', GETDATE(), GETDATE(), 1),
(N'Workout Rings Plastic', N'Plastic rings for beginners.', N'Workout Rings Plastic', '/images/workout-rings-gornation_300x.jpeg', GETDATE(), GETDATE(), 1),
(N'Pull Up Bars', N'Stable pull-up bars for home.', N'Pull Up Bars', '/images/massepullupbarextendit_1200x.jpeg', GETDATE(), GETDATE(), 1),
(N'Resistance Band Heavy', N'Heavy resistance band (20–40kg).', N'Resistance Band Heavy', '/images/2-heavy-resistance-band-gornation_e277d772-a4c1-444e-bef3-bba5953c478b_1200x.jpg.jpeg', GETDATE(), GETDATE(), 1),
(N'Resistance Band Medium', N'Medium resistance band (10–30kg).', N'Resistance Band Medium', '/images/1-medium-resistance-band-gornation_62a00aff-eef7-428c-b53d-04ac06360e81_1200x.jpg.jpeg', GETDATE(), GETDATE(), 1),
(N'Resistance Band Light', N'Light resistance band (5–15kg).', N'Resistance Band Light', '/images/2-light-resistance-band-gornation_f6d179e8-7aa2-47e1-a405-b909fb90850c_1200x.jpg.jpeg', GETDATE(), GETDATE(), 1),
(N'Resistance Band Ultra Light', N'Ultra light resistance band (2–7kg).', N'Resistance Band Ultra Light', '/images/Premium-Resistance-Bands-GORNATION-34973913-34973926-34973984_1200x.jpg.jpeg', GETDATE(), GETDATE(), 1),
(N'Performance Wrist Wrap', N'For strong wrist support.', N'Performance Wrist Wrap', '/images/performance-wrist-wraps-black-grey_1200x.jpeg', GETDATE(), GETDATE(), 1),
(N'Power Wrist Wrap', N'Maximum compression wrist wrap.', N'Power Wrist Wrap', '/images/1pwoerwristwrapsblackcalisthenics_1200x.jpeg', GETDATE(), GETDATE(), 1),
(N'Elbow Sleeves', N'Support for elbows during training.', N'Elbow Sleeves', '/images/arm-sleeves-black-calisthenics_1200x.jpeg', GETDATE(), GETDATE(), 1),
(N'Arm Sleeves', N'Compression arm sleeves.', N'Arm Sleeves', '/images/arm-sleeves-black-calisthenics_1200x.jpeg', GETDATE(), GETDATE(), 1),
(N'Chalk 200ml', N'Chalk for grip - 50ml.', N'Chalk 50ml', '/images/verschiedenebreiten_1200x.jpg.jpeg', GETDATE(), GETDATE(), 1),
(N'Grip Tape', N'Tape for hand grip protection.', N'Grip Tape', '/images/tape2.0_1200x.jpeg', GETDATE(), GETDATE(), 1);

-- IMAGES (using local images, 2-3 images per product)
-- Note: You'll need to update product_id values to match the actual auto-generated IDs from products table
INSERT INTO product_images (product_id, image_url, is_primary, created_at)
VALUES
-- Parallettes Large (product_id should be the actual ID from products table)
(1, '/images/parallettes-max-produktfoto-1_1200x.jpg.jpeg', 1, GETDATE()),
(1, '/images/parallettes-max-produktfoto-3-seite_1200x.jpeg', 0, GETDATE()),
(1, '/images/handstand-push-up-parallettes-max_662831c8-b5ae-4f43-9f63-64b7b71f1b2d_1200x.jpg.jpeg', 0, GETDATE()),

-- Parallettes Medium
(2, '/images/parallettes-active-product-photo-1-calisthenics_1200x.jpg.jpeg', 1, GETDATE()),
(2, '/images/parallettes-active-masse-gornation_1200x.jpg.jpeg', 0, GETDATE()),
(2, '/images/metal-parallettesmeasurements_1200x.jpg.jpeg', 0, GETDATE()),

-- Parallettes Small (Wooden)
(3, '/images/product-shot-wooden-parallettes-extended-2_91945174-2e6b-43e3-b101-1cf692ed68c2_1200x.jpg.jpeg', 1, GETDATE()),
(3, '/images/product-shot-wooden-parallettes-extended-3_c2685acc-bee1-4f89-81da-36c6601a5ee2_1200x.jpg.jpeg', 0, GETDATE()),

-- Dip Belt
(4, '/images/calisthnics-dip-belt-with-rope-1_1200x.jpeg', 1, GETDATE()),
(4, '/images/calisthnics-dip-belt-with-rope-2_1200x.jpeg', 0, GETDATE()),

-- Dip Bars
(5, '/images/push-up-bars-gornation-1_5decedcc-70f2-4d79-a74c-dcf6e87ee405_1200x.jpg.jpeg', 1, GETDATE()),
(5, '/images/push-up-bars-gornation-2_039b25af-bc89-437b-9e8f-3ff33c54f249_1200x.jpg.jpeg', 0, GETDATE()),

-- Static Bar
(6, '/images/static-bar-gornation-product.jpg.jpeg', 1, GETDATE()),
(6, '/images/2-gornation-static-bar-front.jpg.jpeg', 0, GETDATE()),
(6, '/images/frontlever-touch-static-bar.jpg.jpeg', 0, GETDATE()),

-- Workout Rings Wood
(7, '/images/workout-rings-gornation_300x.jpeg', 1, GETDATE()),

-- Workout Rings Plastic (using same image as wood rings)
(8, '/images/workout-rings-gornation_300x.jpeg', 1, GETDATE()),

-- Pull Up Bars
(9, '/images/massepullupbarextendit_1200x.jpeg', 1, GETDATE()),

-- Resistance Band Heavy
(10, '/images/2-heavy-resistance-band-gornation_e277d772-a4c1-444e-bef3-bba5953c478b_1200x.jpg.jpeg', 1, GETDATE()),
(10, '/images/Premium-Resistance-Bands-GORNATION-34973913-34973926-34973984_1200x.jpeg', 0, GETDATE()),

-- Resistance Band Medium
(11, '/images/1-medium-resistance-band-gornation_62a00aff-eef7-428c-b53d-04ac06360e81_1200x.jpg.jpeg', 1, GETDATE()),
(11, '/images/2-medium-resistance-band-gornation_7f177951-bd62-40b5-b71f-ae9a43d01ef3_1200x.jpg.jpeg', 0, GETDATE()),

-- Resistance Band Light
(12, '/images/2-light-resistance-band-gornation_f6d179e8-7aa2-47e1-a405-b909fb90850c_1200x.jpg.jpeg', 1, GETDATE()),

-- Resistance Band Ultra Light
(13, '/images/Premium-Resistance-Bands-GORNATION-34973913-34973926-34973984_1200x.jpg.jpeg', 1, GETDATE()),
(13, '/images/Premium-Resistance-Bands-GORNATION-34976284-34976217-34976232_1200x.jpg.jpeg', 0, GETDATE()),

-- Performance Wrist Wrap
(14, '/images/performance-wrist-wraps-black-grey_1200x.jpeg', 1, GETDATE()),

-- Power Wrist Wrap
(15, '/images/1pwoerwristwrapsblackcalisthenics_1200x.jpeg', 1, GETDATE()),

-- Elbow Sleeves
(16, '/images/arm-sleeves-black-calisthenics_1200x.jpeg', 1, GETDATE()),

-- Arm Sleeves
(17, '/images/arm-sleeves-black-calisthenics_1200x.jpeg', 1, GETDATE()),

-- Chalk 200ml
(18, '/images/verschiedenebreiten_1200x.jpg.jpeg', 1, GETDATE()),


-- Grip Tape
(19, '/images/tape2.0_1200x.jpeg', 1, GETDATE()),
(19, '/images/2griptapecalisthenics_1200x.jpeg', 0, GETDATE());
-- ATTRIBUTES + SKU
INSERT INTO product_attributes (type, value, created_at, status) VALUES
( 'Size', 'Large', GETDATE(), 1),
( 'Size', 'Medium', GETDATE(), 1),
( 'Size', 'Small', GETDATE(), 1),
( 'Type', 'Standard', GETDATE(), 1),
( 'Type', 'Wood', GETDATE(), 1),
( 'Type', 'Plastic', GETDATE(), 1),
( 'Type', 'Heavy', GETDATE(), 1),
( 'Type', 'Medium', GETDATE(), 1),
( 'Type', 'Light', GETDATE(), 1),
( 'Type', 'Ultra Light', GETDATE(), 1),
( 'Type', 'Performance', GETDATE(), 1),
( 'Type', 'Power', GETDATE(), 1),
( 'Type', 'Elbow', GETDATE(), 1),
( 'Type', 'Arm', GETDATE(), 1),
( 'Volume', '50ml', GETDATE(), 1),
( 'Volume', '100ml', GETDATE(), 1),
( 'Type', 'Grip Tape', GETDATE(), 1);

-- SKU table
INSERT INTO products_skus (product_id, size_attribute_id, sku, price, quantity, created_at, status)
VALUES
(1, 1, 'SKU-PARALLETTES-LARGE', 29.99, 100, GETDATE(), 1),
(2, 2, 'SKU-PARALLETTES-MEDIUM', 27.99, 100, GETDATE(), 1),
(3, 3, 'SKU-PARALLETTES-SMALL', 25.99, 100, GETDATE(), 1),
(4, 4, 'SKU-DIP-BELT', 34.99, 100, GETDATE(), 1),
(5, 4, 'SKU-DIP-BARS', 45.99, 100, GETDATE(), 1),
(6, 4, 'SKU-STATIC-BAR', 39.99, 100, GETDATE(), 1),
(7, 5, 'SKU-RINGS-WOOD', 28.99, 100, GETDATE(), 1),
(8, 6, 'SKU-RINGS-PLASTIC', 24.99, 100, GETDATE(), 1),
(9, 4, 'SKU-PULLUP-BARS', 49.99, 100, GETDATE(), 1),
(10, 7, 'SKU-BAND-HEAVY', 15.99, 100, GETDATE(), 1),
(11, 8, 'SKU-BAND-MEDIUM', 13.99, 100, GETDATE(), 1),
(12, 9, 'SKU-BAND-LIGHT', 11.99, 100, GETDATE(), 1),
(13, 10, 'SKU-BAND-ULTRA', 9.99, 100, GETDATE(), 1),
(14, 11, 'SKU-WRAP-PERFORMANCE', 14.99, 100, GETDATE(), 1),
(15, 12, 'SKU-WRAP-POWER', 16.99, 100, GETDATE(), 1),
(16, 13, 'SKU-SLEEVES-ELBOW', 19.99, 100, GETDATE(), 1),
(17, 14, 'SKU-SLEEVES-ARM', 18.99, 100, GETDATE(), 1),
(18, 15, 'SKU-CHALK-200ML', 5.99, 100, GETDATE(), 1),
(19, 16, 'SKU-GRIP-TAPE', 6.99, 100, GETDATE(), 1);



