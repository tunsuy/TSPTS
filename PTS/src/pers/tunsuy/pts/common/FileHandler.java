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
	 * ��Json�ļ���ȡ���� 
	 * @param file File���͵��ļ�����
	 * @return String �����ļ������ַ���
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
	 * ������������ÿ�η���һ���ļ�
	 * @param folder File���͵��ļ��ж���
	 * @param generator ����������
	 * @return void
	 */
	private static void genFileFromFolder(final File folder, Generator<File> generator) {
		if (null == generator || null == folder) {
			return;
		}
		
	    for (File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	//�Եݹ鷽ʽ�����ļ�
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
	 * �Ե�������ģʽ����ÿ���ļ�
	 * @param folder �ļ����͵��ļ��ж���
	 * @param jsonFileOpr json�ļ����������
	 * @return void
	 */
	public static void iterDealFileFromFolder(final File folder, JsonFileOpr jsonFileOpr) {
		if (null == folder || null == jsonFileOpr) {
			return;
		}
		
		//�������ڲ��෽ʽ����һ���ļ�������
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
