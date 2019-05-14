import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GetFiles {
	
	private File root = null ; 
	private List<File> list = null ; 
	private List<String> extension = null;
	
	public List<File> getList(){
		return list;
	}
	
	public GetFiles(String path) {
		this.root = new File(path);
	}
	
	public GetFiles(File file) {
		this.root =file;
	}
	
	public void getFiles() {
		list = new ArrayList<File>();
		getFilesLoop(root);
	}
	
	private void getFilesLoop(File file) {
		if(file != null && file.exists()) {
			if( file.isDirectory() ) {
				getFilesLoop(file);
			}else {
				if(extension != null && extension.size() > 0) {
					for(String ext : extension ) {
						if(file.getName().endsWith(ext)) {
							list.add(file);
						}
					}
				}else {
					list.add(file);
				}
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
}
