package engineTester;

import MySql.MySql;

public class mySqlTest {

	public static void main(String[] args) {
		
		MySql.setConnectionUser("Yorick");
		MySql.setConnectionPassword("EPfmS9BB");
		System.out.println(MySql.getData("Bobbie", "Username", "Password"));
		//MySql.createUser("Bobbie", "EndeRest");

	}

}
