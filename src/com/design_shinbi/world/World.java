package com.design_shinbi.world;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class World {
	private static final int ACTION_EXIT         = 0;
	private static final int ACTION_COUNTRIES    = 1;
	private static final int ACTION_CITIES       = 2;
	private static final int ACTION_LANGUAGES    = 3;
	
	
	private static Connection connectDb() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		String dbUrl = "jdbc:mysql://localhost/world";
		String userName = "root";
		String password = "";
		
		Connection connection = DriverManager.getConnection(
			dbUrl, userName, password
		);
		
		return connection;
	}
	
	private static void showCountries(Connection connection) throws SQLException {
		System.out.println(
			String.format(
				"%3s   %50s   %15s   %10s   %10s",
				"Code", "Name", "Continent", "Population", "Surface Area"
			)
		);
		
		String sql = "SELECT * FROM country ORDER BY population DESC";
		
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
			
		while(resultSet.next()) {
			String code = resultSet.getString("code");
			String name = resultSet.getString("name");
			String continent = resultSet.getString("continent");
			int population = resultSet.getInt("population");
			double surfaceArea = resultSet.getDouble("surfacearea");
			
			String line = String.format(
				"%3s   %50s   %15s   %10d   %10.1f",
				code, name, continent, population, surfaceArea
			);
			System.out.println(line);
		}
		
		resultSet.close();
		statement.close();
	}
	
	
	private static void showCities(Connection connection, String code) throws SQLException {
		System.out.println(
			String.format(
				"%30s   %30s   %9s",
				"Name", "District", "Population"
			)
		);
		
		String sql = "SELECT * FROM city WHERE countrycode = ? ORDER BY population DESC";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, code);
		
		ResultSet resultSet = statement.executeQuery();
		while(resultSet.next()) {
			String name = resultSet.getString("name");
			String district = resultSet.getString("district");
			int population = resultSet.getInt("population");
			
			String line = String.format(
				"%30s   %30s   %9d",
				name, district, population
			);
			System.out.println(line);
		}
		
		resultSet.close();
		statement.close();
	}
	
	
	private static int selectAction(Scanner scanner) {
		int action = -1;
		while(action != ACTION_COUNTRIES && action != ACTION_CITIES
				&& action != ACTION_LANGUAGES && action != ACTION_EXIT) {
			System.out.println(
				String.format(
					"コマンドを入力してください。[%d] 国一覧, [%d] 都市一覧, [%d] 言語一覧, [%d] 終了",
					ACTION_COUNTRIES, ACTION_CITIES, ACTION_LANGUAGES, ACTION_EXIT
				)
			);
			
			String line = scanner.nextLine();
			try {
				action = Integer.parseInt(line);
			}
			catch(Exception e) {
			}
		}
		
		return action;
			
	}
	
	
	private static void showLanguages(Connection connection, String code) throws SQLException {

		
		// 実装してみよう


	}

	
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		Connection connection =  connectDb();
		
		showCountries(connection);
		
		boolean loop = true;
		
		while(loop) {
			int action = selectAction(scanner);
			if(action == ACTION_EXIT) {
				loop = false;
			}
			else if(action == ACTION_COUNTRIES) {
				showCountries(connection);
			}
			else if(action == ACTION_CITIES) {
				System.out.println("国コードを入力してください。");
				String code = scanner.nextLine();
			
				showCities(connection, code);
			}
			else if(action == ACTION_LANGUAGES) {
				System.out.println("国コードを入力してください。");
				String code = scanner.nextLine();
			
				showLanguages(connection, code);				
			}
		}

		scanner.close();
		connection.close();
	}
}
