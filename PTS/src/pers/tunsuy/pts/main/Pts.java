package pers.tunsuy.pts.main;

import java.io.File;
import java.io.IOException;

import pers.tunsuy.pts.model.JsonFileOpr;
import pers.tunsuy.pts.common.FileHandler;
import pers.tunsuy.pts.model.UserDataBuilder;
import pers.tunsuy.pts.model.UserData;
import pers.tunsuy.pts.net.Connector;

public class Pts {
	
	private static String jsonDataString;

	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		final File jsonFileDir = new File("./data");
		FileHandler.iterDealFileFromFolder(jsonFileDir, new JsonFileOpr() {
			
			@Override
			public void testDataFromJsonFile(String jsonDataStr) {
				// TODO Auto-generated method stub
				jsonDataString = jsonDataStr;
			}
		});

		System.out.println("jsonDataString: " + jsonDataString);

		UserData userData = UserDataBuilder.buildForUserData(jsonDataString);
		
		Connector connector = new Connector("200.200.169.165", 6800);
		try {
			connector.openSocketChannel(userData);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				connector.closeSocketChannel();
			} catch (IOException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

}
