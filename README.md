# ShoppingCart
### Descrição do Projeto

Este é um projeto de carrinho de compras desenvolvido em Java 17, utilizando JDBC para integração com o banco de dados e tentando seguir um padrão arquitetural MVC.


## Pré-requisitos
Certifique-se de ter os seguintes itens instalados na sua máquina:

Java 17 (JDK)

1. Eclipse IDE ou outra IDE de sua preferência
2. MySQL Server e MySQL Workbench
3. Conector JDBC para MySQL (arquivo mysql-connector-java-x.x.x.jar)

## Configuração do Banco de Dados
#### Criação do Banco de Dados

1. Abra o MySQL Workbench e conecte-se ao servidor MySQL.

2. Execute o seguinte comando para criar o banco de dados: 
*CREATE DATABASE challenge;*

## Criação das Tabelas

CREATE TABLE stock (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL
);

CREATE TABLE cart (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    total_value DECIMAL(10, 2) AS (quantity * price) STORED
);

## Produtos para o Banco de Dados

INSERT INTO stock (name, category, price, quantity) VALUES
('Laptop', 'Electronics', 4500.00, 10),
('Headphones', 'Electronics', 50.00, 15),
('Smartphone', 'Electronics', 1200.00, 8),
('Tablet', 'Electronics', 800.00, 10),
('Printer', 'Electronics', 900.00, 10),
('Keyboard', 'Electronics', 50.00, 4),
('T-shirt', 'Clothing', 20.00, 9),
('Jeans', 'Clothing', 40.00, 5),
('Sweater', 'Clothing', 60.00, 8),
('Jacket', 'Clothing', 100.00, 10),
('Shoes', 'Clothing', 80.00, 12),
('Shirt', 'Clothing', 25.00, 22),
('The Da Vinci Code', 'Books', 100.00, 6),
('how make friends and influence others', 'Books', 150.00, 5),
('PS5', 'Games', 5500.00, 5),
('GTA-6', 'Games', 997.00, 1),
('Farcry-7', 'Games', 350.00, 3),
('Skin Valorant', 'Games', 50.00,10);

#### Configuração do Projeto
1. Clonando o Repositório
Clone o repositório do projeto para sua máquina:

git clone https://github.com/ElderLuiz/ShoppingCart

2. Configuração do Banco de Dados no Projeto
private static final String URL = "jdbc:mysql://localhost:3306/challenge";
private static final String USER = "root";
private static final String PASSWORD = "1234567";

### Executando o Projeto
1. Abra o projeto na sua Ide.
2. Compile e execute a classe (CartView) para Gerenciar um carrinho.
3. Compile e execute a classe (StockControllerView) para Gerenciar um estoque.
