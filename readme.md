# SimpleSplit

SimpleSplit is a Java application for managing shared expenses among friends or groups.

## Features

- Add users to the system
- Record transactions and split expenses
- View transaction history
- Check individual user balances

## Prerequisites

- Java 11 or higher
- Maven
- MySQL

## Setup

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/simplesplit.git
   ```

2. Navigate to the project directory:
   ```
   cd simplesplit
   ```

3. Set up the MySQL database:
   - Create a new database named `simplesplit`
   - Update the database connection details in `src/main/resources/database.properties`

4. Build the project:
   ```
   mvn clean install
   ```

5. Run the application:
   ```
   java -jar target/simplesplit-1.0-SNAPSHOT.jar
   ```

## Usage

Follow the on-screen prompts to:
- Add users
- Add transactions and split expenses
- View all transactions
- Check user balances

## Running Tests

Execute the following command to run the tests:
```
mvn test
```

## Contributing

Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the LICENSE.md file for details.
