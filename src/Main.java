import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {
	private static String path = "D:\\grcSolution_kbsec\\workspace\\comSolution_v3.6_MS_2019_03_19\\src\\main\\resources\\sqlmap\\mappers\\oracle\\cwsol\\";
	private static String extension = ".xml"; 
	private static List<File> list = null; 
	private static String convertPath = "C:\\Users\\cas\\Desktop\\sol\\MoveOraToMs\\Convert\\";
	public static StringBuilder sb = new StringBuilder();

	// test
	public static void print(String str) {
		System.out.println(str);
		sb.append(str);
		sb.append("\n");

	}
	
	public static void main(String[] args) {
		System.out.println("start");
		// convertProcess();
		
		String sql = "SELECT 'GCI_' + TO_CHAR(LPAD(NVL(MAX(TO_NUMBER(SUBSTR(STD_WORDS_CD_ID, 5))),0)+1, 3, '0')) AS STD_WORDS_CD_ID\r\n" + 
				"			FROM TB_CO_STD_WORDS_CODE \r\n" + 
				"			WHERE STD_WORDS_CD_ID LIKE 'GCI_%'";
		sql = Change.all(sql,"hello");
		System.out.println(sql);
	}
	
	private static void convertProcess() {
		getFilesLoop(new File(path));
		
		File root = new File(convertPath);
		if(!root.exists())
			root.mkdirs();
			
		for(File f : list) {
			String temp = f.getAbsolutePath();
			for(int i = 0 ; i < 4 ; i ++) {
				temp = temp.substring(0,temp.lastIndexOf("\\"));
			}
			String newPath = f.getAbsolutePath().substring(temp.length(),f.getAbsolutePath().length());
			String sql = getSql(f.getAbsolutePath());
			String convertSql = Change.all(sql,f.getName());
			write(convertSql,convertPath+"\\" + newPath);
			
			// rowNumber(convertPath+"\\" + newPath);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static void write(String sql ,String path) {
		OutputStream os = null;
		
		File dir = new File(path.substring(0, path.lastIndexOf("\\")));
		if(dir != null && !dir.exists()) {
			dir.mkdirs();
		}
		
		try {
			os = new FileOutputStream(path);
			os.write(sql.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(os != null)
					os.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		
	}
	
	public static String getSql (String path) {
		File file = new File(path) ;
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			String line = "";
			
			while ( (line = read.readLine()) != null ) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	private static void getFilesLoop(File file) {
		if(list == null )
			list = new ArrayList<File>();
		
		
		if( file != null ) {
			File[] files = file.listFiles();
			
			for( File f : files ) {
				if(f.isDirectory()) {
					//System.out.println("Directory >> " + f.getName());
					getFilesLoop(f);
				}
				else {
					//System.out.println("File >> " + f.getName());
					if(extension != null && !extension.equals("")) {
						if(f.getName().endsWith(extension)) {
							list.add(f);
						}
					}else {
						list.add(f);
					}
				}
			}
		}
	}
}
