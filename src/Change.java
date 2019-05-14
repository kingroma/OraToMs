import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Change {
	public static String all(String str,String fileName) {
		
		str = usingdual(str);
		str = append(str);
		str = lpad(str);
		str = sysdate(str);
		str = nvlToIsnull(str);
		str = toCharToDate23(str);
		str = toCharToDate23NotAs(str);
		str = toCharToDate23NotAs2(str);
		str = YYYY_MM_DD_HH24_MI_SS(str);
		str = YYYY_MM_DD_HH24_MI_SSNotAs(str);
		str = toCharYYYY_MM_DD(str);
		str = toCharYYYY_MM_DDNotAs(str);
		str = toCharYYYY(str);
		str = toCharMM(str);
		str = toCharDD(str);
		str = toCharHH24_MI_SS(str);
		str = toCharYYYYMMDDHH24MISS(str);
		str = toCharToTimeStampYYYYMMDDHH24MISSFF6(str);
		str = fromDual(str);
		str = minus(str);
		str = lenToLeng(str);
		str = toNumber(str);
		str = chrTochar(str);
		//str = rownumber(str);
		
		
		log(str, fileName);
		return str;
	}
	
	public static void log (String str,String fileName) {
		String logTitle = " || LOG || " + "[ " + fileName + " ] ";
		String text = "";
		
		
		//REGEXP_INSTR
		if(str.contains("REGEXP_INSTR")) {
			text += (" // REGEXP_INSTR  " );
			// String guide = "'[^{.1234567890}]' >> ISNUMERIC 으로 하면됨 ";
		}
		
		//DECLARE
		if(str.contains("DECLARE"))
			text += (" // DECLARE  " );
		
		//CONNECT BY 
		if(str.contains("CONNECT BY"))
			text += (" // CONNECT BY  " );
		
		//WM_CONCAT
		if(str.contains("WM_CONCAT"))
			text += (" // WM_CONCAT  " );
		
		//LISTAGG
		if(str.contains("LISTAGG"))
			text += (" // LISTAGG  " );
				
		//LISTAGG
		if(str.contains("PIVOT"))
			text += (" // PIVOT  " );
		
		//WHILE
		if(str.contains("WHILE"))
			text += (" // WHILE  " );
		
		// FOR EACH
		if(str.contains("FOR"))
			text += (" // FOR list IN  " );
		
		//999,999,999,9
		if(str.contains("999,"))
			text += (" // 999,  " );
			
		//FM9
		if(str.contains("FM9"))
			text += (" // FM9  " );
		
		
		//TO_CHAR
		if(str.contains("TO_CHAR"))
			text += (" // TO_CHAR  " );
		
		if(str.contains("ROW_NUMBER")) {
			text += (" // ROW_NUMBER  " );
		}
				
		if(!text.isEmpty()) {
			int cnt = 50 - logTitle.length(); 
			
			for ( int i = 0 ; i < cnt ; i ++ ) {
				logTitle += " ";
			}
			
			
			Main.print(logTitle + "\t" + text);
		}
			
			
	}
	
	public static String minus(String str) {
		String regex = "MINUS";
		String replacement = "EXCEPT";
		return str.replaceAll(regex, replacement);
	}
	
	public static String usingdual(String str) {
		String regex = "USING *DUAL *(T2|B)";
		String replacement = "USING (VALUES (1)) AS Source (Number) ";
		return str.replaceAll(regex, replacement);
	}
	
	public static String append(String str) {
		String regex = "\\|\\|";
		String plus = "+";
		return str.replaceAll(regex, plus);
	}
	public static String lpad(String str) {
		String regex = "LPAD *\\( *([a-zA-Z._]*) *, *([0-9]*) *, *'0'\\)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean isFind = m.find();
		int count = m.groupCount();
		
		String ret = "";
		if ( isFind && count == 2 ) {
			String str1 = m.group(1);
			String str2 = m.group(2);
			
			
			ret = " REPLICATE('0'," + str2 + " - LEN(" + str1 + ")) + CONVERT(VARCHAR, " + str1 + " )";
			
			str = m.replaceFirst(ret);
			str = lpad(str);
		}
		
		return str;
	}
	
	public static String sysdate(String str) {
		str = sysdateYYYYMMDDHH24MISS(str) ;
		str = sysdateYYYYMMDDHH24MISSFF3(str) ;
		str = sysdateYYYYMMDDHH24MISSFF6(str) ;
		str = sysdateYYYY_MM_DD(str);
		str = sysdateYYYYMMDD(str);
		str = sysdateYYYY(str);
		str = toCharYYYYMMDD_1(str);
		String regex = "SYSDATE";
		String getdate = "GETDATE()";
		
		return str.replaceAll(regex, getdate);
	}
	
	public static String toNumber(String str) {
		String regex = "TO_NUMBER *\\(";
		String replacement = "CONVERT ( NUMERIC , ";
		return str.replaceAll(regex, replacement);
	}
	
	public static String fromDual(String str) {
		String regex = "FROM DUAL";
		String replacement = "";
		return str.replaceAll(regex, replacement);
	}
	
	// TO_CHAR(SYSDATE, 'YYYYMMDD')
	public static String sysdateYYYYMMDD(String str) {
		String regex = "TO_CHAR *\\( *SYSDATE *, *'YYYYMMDD' *\\)";
		String replacement = "CONVERT ( VARCHAR , GETDATE() , 112 ) ";
		return str.replaceAll(regex, replacement); 
	}
	
	public static String sysdateYYYY_MM_DD(String str) {
		String regex = "TO_CHAR *\\( *SYSDATE *, *'YYYY-MM-DD' *\\)";
		String replacement = "CONVERT ( VARCHAR , GETDATE() , 23 ) ";
		return str.replaceAll(regex, replacement); 
	}
	
	public static String sysdateYYYYMMDDHH24MISS(String str) {
		// TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')
		// CONVERT(VARCHAR,GETDATE(),112) + REPLACE(CONVERT(VARCHAR,GETDATE(),8),':','')
		String regex = "TO_CHAR *\\( *SYSDATE *, *'YYYYMMDDHH24MISS' *\\)";
		String replacement = "CONVERT(VARCHAR,GETDATE(),112) + REPLACE(CONVERT(VARCHAR,GETDATE(),8),':','')";
		return str.replaceAll(regex, replacement);
	}
	
	public static String sysdateYYYYMMDDHH24MISSFF3(String str) { 
		// TO_CHAR(SYSTIMESTAMP,'YYYYMMDDHH24MISSFF3')
		// CONVERT(VARCHAR,GETDATE(),112) + REPLACE(CONVERT(VARCHAR,GETDATE(),14),':','') + '000'
		String regex = "TO_CHAR *\\( *SYSTIMESTAMP *, *'YYYYMMDDHH24MISSFF3' *\\)";
		String replacement = "CONVERT(VARCHAR,GETDATE(),112) + REPLACE(CONVERT(VARCHAR,GETDATE(),14),':','')";
		return str.replaceAll(regex, replacement);
	}
	
	
	public static String sysdateYYYY(String str) { 
		// TO_CHAR(SYSDATE, 'YYYY')
		// CONVERT(VARCHAR(4),GETDATE(),112)
		String regex = "TO_CHAR *\\( *SYSDATE *, *'YYYY' *\\)";
		String replacement = "CONVERT(VARCHAR(4),GETDATE(),112)";
		return str.replaceAll(regex, replacement);
	}
	
	public static String sysdateYYYYMMDDHH24MISSFF6(String str) { 
		// TO_CHAR(SYSTIMESTAMP, 'YYYYMMDDHH24MISSFF6')
		// CONVERT(VARCHAR,GETDATE(),112) + REPLACE(CONVERT(VARCHAR,GETDATE(),14),':','') + '000'
		String regex = "TO_CHAR *\\( *SYSTIMESTAMP *, *'YYYYMMDDHH24MISSFF6' *\\)";
		String replacement = "CONVERT(VARCHAR,GETDATE(),112) + REPLACE(CONVERT(VARCHAR,GETDATE(),14),':','') + '000'";
		return str.replaceAll(regex, replacement);
	}
	
	public static String nvlToIsnull(String str) {
		String regex = "NVL\\(";
		String isnull = "ISNULL(";
		return str.replaceAll(regex, isnull);
	}
	
	public static String toCharToDate23(String str) {
		String regex = "TO_CHAR *\\( * *TO_DATE *\\( *([a-zA-Z_.]*), *(.*)\\) *, *'YYYY-MM-DD' *\\) *AS *(.*)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean isFind = m.find();
		int count = m.groupCount();
		
		String ret = "";
		if ( isFind && count == 3 ) {
			String str1 = m.group(1);
			String str2 = m.group(2);
			String str3 = m.group(3);
			//String str4 = m.group(3);
			
			// CONVERT(VARCHAR,CONVERT(DATE,BGNDE,112),23) AS BGNDE
			ret = "CONVERT ( VARCHAR , CONVERT ( DATE , " + str1 + " , 112 ) , 23 ) AS " + str3;
			str = m.replaceFirst(ret);
			str = toCharToDate23(str);
		}
		
		return str;
	}
	
	public static String toCharToDate23NotAs(String str) {
		String regex = "TO_CHAR *\\( *TO_DATE *\\( *([a-zA-Z_.]*), *'YYYY-MM-DD' *\\) *, *'YYYY-MM-DD' *\\)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean isFind = m.find();
		int count = m.groupCount();
		
		String ret = "";
		if ( isFind && count == 1 ) {
			String str1 = m.group(1);
			// String str2 = m.group(2);
			// String str3 = m.group(3);
			// String str4 = m.group(3);
			
			// CONVERT(VARCHAR,CONVERT(DATE,BGNDE,112),23) AS BGNDE
			ret = "CONVERT ( VARCHAR , CONVERT ( DATE , " + str1 + " , 112 ) , 23 )";
			str = m.replaceFirst(ret);
			str = toCharToDate23NotAs(str);
		}
		
		return str;
	}
	
	public static String toCharToDate23NotAs2(String str) {
		String regex = "TO_CHAR *\\( *TO_DATE *\\( *([a-zA-Z_.]*), *'YYYYMMDD' *\\) *, *'YYYY-MM-DD' *\\)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean isFind = m.find();
		int count = m.groupCount();
		
		String ret = "";
		if ( isFind && count == 1 ) {
			String str1 = m.group(1);
			// String str2 = m.group(2);
			// String str3 = m.group(3);
			// String str4 = m.group(3);
			
			// CONVERT(VARCHAR,CONVERT(DATE,BGNDE,112),23) AS BGNDE
			ret = "CONVERT ( VARCHAR , CONVERT ( DATE , " + str1 + " , 112 ) , 23 )";
			str = m.replaceFirst(ret);
			str = toCharToDate23NotAs2(str);
		}
		
		return str;
	}
	
	//, TO_CHAR(B.UPD_DT, 'YYYY-MM-DD HH24:MI:SS')	AS AUIDT_UPD_DT
	// CONVERT(VARCHAR,DATE,23) + ' ' + CONVERT(VARCHAR,DATE,8) AS DATE 
	public static String YYYY_MM_DD_HH24_MI_SS(String str) {
		String regex = "TO_CHAR *\\( *([a-zA-Z._]*) *, *'YYYY-MM-DD HH24:MI:SS'\\).*AS *(.*)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean isFind = m.find();
		int count = m.groupCount();
		
		String ret = "";
		
		if ( isFind && count == 2 ) {
			String str1 = m.group(1);
			String str2 = m.group(2);
			
			ret = "CONVERT ( VARCHAR , " + str1 + " , 23 ) + ' ' + CONVERT ( VARCHAR , " + str1 + " , 8) AS " + str2; 
			str = m.replaceFirst(ret);
			
			str = YYYY_MM_DD_HH24_MI_SS(str);
			// str = YYYY_MM_DD_HH24_MI_SS(str);
			//m = p.matcher(str);
			//isFind = m.find();
			//count = m.groupCount();
		}
		
		return str;
	}
	
	public static String YYYY_MM_DD_HH24_MI_SSNotAs(String str) {
		String regex = "TO_CHAR *\\( *([a-zA-Z._]*) *, *'YYYY-MM-DD HH24:MI:SS'\\)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean isFind = m.find();
		int count = m.groupCount();
		
		String ret = "";
		
		if ( isFind && count == 1 ) {
			String str1 = m.group(1);
			ret = "CONVERT ( VARCHAR , " + str1 + " , 23 ) + ' ' + CONVERT ( VARCHAR , " + str1 + " , 8)"; 
			str = m.replaceFirst(ret);
			
			str = YYYY_MM_DD_HH24_MI_SSNotAs(str);
			// m = p.matcher(str);
			// isFind = m.find();
			// count = m.groupCount();
		}
		
		return str;
	}
	
	
	// , TO_CHAR(EE.UPD_DT,'YYYY-MM-DD') AS EXC_REQ_DE --미비점 개선 수행 요청일
	public static String toCharYYYY_MM_DD(String str) {
		String regex = "TO_CHAR *\\( *([a-zA-Z._]*) *, *'YYYY-MM-DD' *\\) *AS *(.*)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean isFind = m.find();
		int count = m.groupCount();
		
		String ret = "";
		if ( isFind && count == 2 ) {
			String str1 = m.group(1);
			String str2 = m.group(2);
			// String str3 = m.group(3);
			//String str4 = m.group(3);
			
			// CONVERT(VARCHAR,CONVERT(DATE,BGNDE,112),23) AS BGNDE
			ret = "CONVERT ( VARCHAR , " + str1 + " , 23 ) AS " + str2 + " ";
			str = m.replaceFirst(ret);
			str = toCharYYYY_MM_DD(str);
		}
		
		return str;
	}
	
	// , TO_CHAR(EE.UPD_DT,'YYYY-MM-DD') AS EXC_REQ_DE --미비점 개선 수행 요청일
	public static String toCharYYYY_MM_DDNotAs(String str) {
		String regex = "TO_CHAR *\\( *([a-zA-Z._]*) *, *'YYYY-MM-DD' *\\)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean isFind = m.find();
		int count = m.groupCount();
		
		String ret = "";
		if ( isFind && count == 1 ) {
			String str1 = m.group(1);
			//String str2 = m.group(2);
			// String str3 = m.group(3);
			//String str4 = m.group(3);
				
			// CONVERT(VARCHAR,CONVERT(DATE,BGNDE,112),23) AS BGNDE
			ret = "CONVERT ( VARCHAR , " + str1 + " , 23 )";
			str = m.replaceFirst(ret);
			str = toCharYYYY_MM_DDNotAs(str);
		}
		
		return str;
	}
	
	public static String toCharYYYY(String str) {
		String regex = "TO_CHAR *\\( *([a-zA-Z._]*) *, *'YYYY' *\\)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean isFind = m.find();
		int count = m.groupCount();
		
		String ret = "";
		if ( isFind && count == 1 ) {
			String str1 = m.group(1);
			//String str2 = m.group(2);
			// String str3 = m.group(3);
			//String str4 = m.group(3);
				
			// CONVERT(VARCHAR,CONVERT(DATE,BGNDE,112),23) AS BGNDE
			ret = "CONVERT ( VARCHAR(4) , " + str1 + " , 112 )";
			str = m.replaceFirst(ret);
			str = toCharYYYY(str);
		}
		
		return str;
	}
	
	
	public static String toCharMM(String str) {
		String regex = "TO_CHAR *\\( *([a-zA-Z._]*) *, *'MM' *\\)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean isFind = m.find();
		int count = m.groupCount();
		
		String ret = "";
		if ( isFind && count == 1 ) {
			String str1 = m.group(1);
			//String str2 = m.group(2);
			// String str3 = m.group(3);
			//String str4 = m.group(3);
				
			// CONVERT(VARCHAR,CONVERT(DATE,BGNDE,112),23) AS BGNDE
			ret = "CONVERT ( VARCHAR(2) , " + str1 + " , 0 )";
			str = m.replaceFirst(ret);
			str = toCharMM(str);
		}
		
		return str;
	}
	
	public static String toCharDD(String str) {
		String regex = "TO_CHAR *\\( *([a-zA-Z._]*) *, *'DD' *\\)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean isFind = m.find();
		int count = m.groupCount();
		
		String ret = "";
		if ( isFind && count == 1 ) {
			String str1 = m.group(1);
			//String str2 = m.group(2);
			// String str3 = m.group(3);
			//String str4 = m.group(3);
				
			// CONVERT(VARCHAR,CONVERT(DATE,BGNDE,112),23) AS BGNDE
			ret = "CONVERT ( VARCHAR(2) , " + str1 + " , 3 )";
			str = m.replaceFirst(ret);
			str = toCharDD(str);
		}
		
		return str;
	}
	
	public static String lenToLeng(String str) {
		String regex = "LENGTH\\(";
		String replacement = "LEN(";
		return str.replaceAll(regex, replacement);
	}
	
	public static String toCharHH24_MI_SS(String str) {
		String regex = "TO_CHAR *\\( *([a-zA-Z._]*) *, *'HH24:MI:SS' *\\)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean isFind = m.find();
		int count = m.groupCount();
		
		String ret = "";
		if ( isFind && count == 1 ) {
			String str1 = m.group(1);
			//String str2 = m.group(2);
			// String str3 = m.group(3);
			//String str4 = m.group(3);
				
			// CONVERT(VARCHAR,CONVERT(DATE,BGNDE,112),23) AS BGNDE
			ret = "CONVERT ( VARCHAR , " + str1 + " , 8 )";
			str = m.replaceFirst(ret);
			str = toCharDD(str);
		}
		
		return str;
	}
	
	// TO_CHAR(UPD_DT, 'YYYYMMDDHH24MISS')
	public static String toCharYYYYMMDDHH24MISS(String str) {
		String regex = "TO_CHAR *\\( *([a-zA-Z._]*) *, *'YYYYMMDDHH24MISS' *\\)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean isFind = m.find();
		int count = m.groupCount();
		
		String ret = "";
		if ( isFind && count == 1 ) {
			String str1 = m.group(1);
			//String str2 = m.group(2);
			// String str3 = m.group(3);
			//String str4 = m.group(3);
				
			ret = "CONVERT ( VARCHAR , " + str1 + " , 8 )";
			ret = "CONVERT(VARCHAR,"+ str1 +",112) + REPLACE(CONVERT(VARCHAR,"+ str1 +",8),':','')";
			str = m.replaceFirst(ret);
			str = toCharYYYYMMDDHH24MISS(str);
		}
		
		return str;
	}
	
	//String str = "POLICY_ENDDE 	= TO_CHAR(SYSDATE-1, 'YYYYMMDD')";  
	public static String toCharYYYYMMDD_1(String str) {
		String regex = "TO_CHAR *\\( *SYSDATE *- *1 *, *'YYYYMMDD' *\\)";
		String replacement = "CONVERT(VARCHAR,GETDATE()-1,112)";
		return str.replaceAll(regex, replacement);
	}
	
	 //, TO_CHAR(TO_TIMESTAMP(A.BGNTMSTMP, 'YYYYMMDDHH24MISSFF6'),'YYYY-MM-DD hh24:mi:ss') BGNTMSTMP_DE
	//    O_CHAR(TO_TIMESTAMP(A.BGNTMSTMP, 'YYYYMMDDHH24MISSFF6'),'YYYY-MM-DD hh24:mi:ss') 
	public static String toCharToTimeStampYYYYMMDDHH24MISSFF6(String str) {
		String regex = "TO_CHAR *\\( *TO_TIMESTAMP *\\( *([a-zA-Z_.]*) *, *'YYYYMMDDHH24MISSFF6' *\\) *, *'YYYY-MM-DD hh24:mi:ss' *\\)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean isFind = m.find();
		int count = m.groupCount();
			
		String ret = "";
		if ( isFind && count == 1 ) {
			String str1 = m.group(1);
			//String str2 = m.group(2);
			// String str3 = m.group(3);
			//String str4 = m.group(3);
					
			ret = "SUBSTRING(" + str1 + ",1,4) + '-' + SUBSTRING(" + str1 + ",5,2) + '-' + SUBSTRING(" + str1 + ",7,2) + ' ' + SUBSTRING(" + str1 + ",9,2) + ':' + SUBSTRING(" + str1 + ",11,2 ) + ':' + SUBSTRING(" + str1 + ",13,2)";
			str = m.replaceFirst(ret);
			str = toCharToTimeStampYYYYMMDDHH24MISSFF6(str);
		}
		
		return str;
	}
	
	
	
	//String str = "POLICY_ENDDE 	= TO_CHAR(SYSDATE-1, 'YYYYMMDD')";  
	public static String chrTochar(String str) {
		String regex = "CHR *\\(";
		String replacement = "CHAR(";
		return str.replaceAll(regex, replacement);
	}
	
	
	
	
	
	// <select id="list" resultType="EgovMap">
	// </select>
	// (.*?|\n)*?
	public static String rownumber(String str ) {
		String regex = "<select *id *= *\"list\" *resultType *= *\"EgovMap\">";
		String regex2 = "</select *>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean isFind = m.find();
		int count = m.groupCount();
		
		String ret = "";
		
		if ( isFind ) {
			int tagStart = m.start();
			int tagEnd = m.end();
			
			Pattern p2 = Pattern.compile(regex2);
			Matcher m2 = p2.matcher(str.substring(tagEnd, str.length()));
			
			if(m2.find()) {
				int closeStart = m2.start();
				int closeEnd = m2.end();
				
				String selectSql = str.substring(tagEnd,tagEnd + closeStart);
				
				int idxOrderBy = selectSql.lastIndexOf("ORDER BY");
				if(idxOrderBy > -1) {
					String orderBy = selectSql.substring(idxOrderBy,selectSql.length()).trim();
					
					String rownum = "ROW_NUMBER() OVER("+orderBy+") rownum \n";
					selectSql = selectSql.substring(0,idxOrderBy);
					selectSql += rownum;
					
					if(selectSql.contains("WITH "))
						Main.print("include WITH");
					//  WITH 문에서 걸려서 일단.. 바꾸기만 
					/*
					int idxSelect = selectSql.indexOf("SELECT");
					
					
					String temp1 = selectSql.substring(0,idxSelect + "SELECT".length());
					String temp2 = selectSql.substring("SELECT".length() + idxSelect,selectSql.length());
					
					String output = temp1 + " " + rownum + " ," + temp2 ;
					*/
					String temp1 = str.substring(0 , tagEnd);
					String temp2 = str.substring(tagEnd + closeStart , str.length());
					str = temp1 + selectSql + temp2 ;
					/*
					str = temp1 + output + temp2;
					*/
					
					
				}
				
			}
			
		}
		
		return str;
		//return str;
	}
	
}
