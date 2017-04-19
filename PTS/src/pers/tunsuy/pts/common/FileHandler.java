package pers.tunsuy.pts.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import pers.tunsuy.pts.util.Generator;
import pers.tunsuy.pts.model.JsonFileOpr;

public class FileHandler {

	/**
	 * 从Json文件读取数据 
	 * @param file File类型的文件对象
	 * @return String 返回文件数据字符串
	 */
	private static String getFileData(File file) {
		if (null == file) {
			return null;
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		String line = null;

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while( (line = br.readLine()) != null ){
				stringBuffer.append(line);
			} 
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringBuffer.toString();
	}

	/**
	 * 生成器函数，每次返回一个文件
	 * @param folder File类型的文件夹对象
	 * @param generator 生成器对象
	 * @return void
	 */
	private static void genFileFromFolder(final File folder, Generator<File> generator) {
		if (null == generator || null == folder) {
			return;
		}
		
	    for (File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	//以递归方式遍历文件
	        	genFileFromFolder(fileEntry, generator);
	        } else {
	        	try {
	        		generator.yield(fileEntry);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
	    }  
	}


	/**
	 * 以迭代器的模式处理每个文件
	 * @param folder 文件类型的文件夹对象
	 * @param jsonFileOpr json文件操作类对象
	 * @return void
	 */
	public static void iterDealFileFromFolder(final File folder, JsonFileOpr jsonFileOpr) {
		if (null == folder || null == jsonFileOpr) {
			return;
		}
		
		//以匿名内部类方式返回一个文件生成器
		Generator<File> fileGenerator = new Generator<File>() {
			@Override
			public void run() throws InterruptedException {
				genFileFromFolder(folder, this);
			}
		};

		for (File file : fileGenerator) {
			System.out.println("file: " + file.getName());
			
        	String jsonFileData = getFileData(file);
        	jsonFileOpr.testDataFromJsonFile(jsonFileData);
        }
	}

}
