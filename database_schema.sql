-- Database schema for Inventaris Toko Sepeda

CREATE DATABASE IF NOT EXISTS inventaris_sepeda;
USE inventaris_sepeda;

-- Users table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Merk table
CREATE TABLE merk (
    merk_id INT AUTO_INCREMENT PRIMARY KEY,
    nama_merk VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Produk table
CREATE TABLE produk (
    produk_id INT AUTO_INCREMENT PRIMARY KEY,
    nama_produk VARCHAR(100) NOT NULL,
    deskripsi TEXT,
    stok INT DEFAULT 0,
    harga DECIMAL(10,2) NOT NULL,
    merk_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (merk_id) REFERENCES merk(merk_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Penjualan table
CREATE TABLE penjualan (
    penjualan_id INT AUTO_INCREMENT PRIMARY KEY,
    produk_id INT NOT NULL,
    user_id INT NOT NULL,
    nama_pembeli VARCHAR(100),
    jumlah INT NOT NULL,
    total_harga DECIMAL(10,2) NOT NULL,
    tanggal TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (produk_id) REFERENCES produk(produk_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Tokens table (optional, for blacklisting if needed)
CREATE TABLE tokens (
    token_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    token VARCHAR(500) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Insert sample data
