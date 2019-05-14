import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main2 {
	public static void main(String[] args) {
		
		String sql = "SELECT\r\n" + 
				"              MA.*\r\n" + 
				"            , MB.CODE_NM AS OBJECT_KND_CD_NM\r\n" + 
				"            , (CASE WHEN MA.CHRG_TY_CD_ID = 'CTW_105_002' THEN #{chrgEvlSe02} ELSE #{chrgEvlSe01} END) AS CHRG_EVL_SE\r\n" + 
				"            , SA.MAPNG_CHRG_SN, NVL(SA.CHRG_GROUP_SN, 1) AS CHRG_GROUP_SN, SA.CHRG_USER_ID\r\n" + 
				"            , SB.USER_CD, SB.USER_NM, SB.DEPT_NM\r\n" + 
				"       	FROM (\r\n" + 
				"            /** 통제 없는 프로세스 제외  */\r\n" + 
				"            SELECT\r\n" + 
				"                  DISTINCT T.OBJECT_ID, T.OBJECT_NO, T.OBJECT_NM, T.OBJECT_LEVEL, T.OBJECT_KND_CD_ID, T.UPPER_OBJECT_ID\r\n" + 
				"                , T.CHRG_TY_CD_ID, T.CNRL_CN, T.KEY_CNRL_AT, T.CNRL_RSPNBER, T.CNRL_EXCBER\r\n" + 
				"            FROM (\r\n" + 
				"                /** 위험, 통제 중복 제거 */\r\n" + 
				"                SELECT\r\n" + 
				"                     DISTINCT A.OBJECT_ID, A.OBJECT_NO, A.OBJECT_NM, A.OBJECT_LEVEL, A.OBJECT_KND_CD_ID\r\n" + 
				"                   , NVL(C.CHRG_TY_CD_ID, 'CTW_105_001') AS CHRG_TY_CD_ID\r\n" + 
				"                   , C.CNRL_CN, C.KEY_CNRL_AT, C.CNRL_RSPNBER, C.CNRL_EXCBER\r\n" + 
				"                FROM TB_CW_ELC_RCM_MAPNG A\r\n" + 
				"                LEFT OUTER JOIN TB_CW_ELC_RCM_MAPNG B\r\n" + 
				"                   ON A.UPPER_MAPNG_ID = B.MAPNG_ID\r\n" + 
				"                LEFT OUTER JOIN (\r\n" + 
				"                    /** 통제 평가자/ 테스터 */\r\n" + 
				"                    SELECT 'CTW_105_001' AS CHRG_TY_CD_ID, CNRL_ID, CNRL_CN, KEY_CNRL_AT, CNRL_RSPNBER, CNRL_EXCBER FROM TB_CW_ELC_RCM_CNRL\r\n" + 
				"                    UNION ALL\r\n" + 
				"                    SELECT 'CTW_105_002' AS CHRG_TY_CD_ID, CNRL_ID, CNRL_CN, KEY_CNRL_AT, CNRL_RSPNBER, CNRL_EXCBER FROM TB_CW_ELC_RCM_CNRL\r\n" + 
				"                ) C\r\n" + 
				"                    ON A.OBJECT_ID = C.CNRL_ID\r\n" + 
				"                WHERE A.OBJECT_KND_CD_ID != 'CTW_001_004'\r\n" + 
				"           ) T\r\n" + 
				"        ) MA\r\n" + 
				"        LEFT OUTER JOIN TB_CO_CODE MB\r\n" + 
				"            ON MB.GROUP_ID = 'CTW_001'\r\n" + 
				"            AND CASE WHEN MA.OBJECT_KND_CD_ID = 'CTW_001_002' AND #{elcProcsStep} = 2\r\n" + 
				"                		THEN 'CTW_001_003'\r\n" + 
				"                		ELSE MA.OBJECT_KND_CD_ID END = MB.CODE_ID\r\n" + 
				"        LEFT OUTER JOIN TB_CW_ELC_RCM_MAPNG_USER SA\r\n" + 
				"            ON MA.OBJECT_ID = SA.OBJECT_ID\r\n" + 
				"            AND MA.CHRG_TY_CD_ID = SA.CHRG_TY_CD_ID\r\n" + 
				"        LEFT OUTER JOIN TB_CO_USER_HIST SB\r\n" + 
				"            ON SA.CHRG_USER_ID = SB.USER_ID\r\n" + 
				"            AND SB.HIST_ENDDE  = '99991231'     /** 최신정보 */\r\n" + 
				"            AND SB.STTUS_CD_ID = 'COM_003_001'  /** 재직자 */\r\n" + 
				" 		ORDER BY MA.OBJECT_ID ASC, NVL(SA.MAPNG_CHRG_SN, 1) ASC, SA.CHRG_GROUP_SN ASC, MA.CHRG_TY_CD_ID ASC";
		
		
		
		
		try {
			Pattern p = Pattern.compile(regex());
			sql = sql.replace("\n", "");
			sql = sql.replace("\r", "");
			Matcher matcher = p.matcher(sql);
			
			//sql = sql.replace("\t", "");
			
			// System.out.println(matcher.find());
			if ( matcher.find() ) {
				System.out.println(matcher.groupCount());
				for ( int i = 1 ; i <= matcher.groupCount() ; i ++ ) {
					String text = matcher.group(i);
					System.out.println( "[" + i + "]\t" + text );
				}
			}else {
				System.out.println("not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	private static String regex() {
		// String regex = "(WITH *[a-zA-Z]* *AS \\(.*\\) *)*(, *[a-zA-Z]* *AS \\(.*\\) *)SELECT *([ a-zA-Z,\\(\\)*]*) *FROM(.*)(GROUP BY)? *(.*)";
		String regex = ""
				+ "(WITH *[a-zA-Z0-9]* *AS *\\(.*\\) *)* *"
				+ "SELECT *([ a-zA-Z\\(\\)*_.,]*) *"
				+ "FROM *([a-zA-Z0-9_]*) *[a-zA-Z0-9]*"
				+ "(INNER *JOIN *[ a-zA-Z0-9]*)* *"
				+ "(LEFT *OUTER *JOIN *[a-zA-Z0-9_]*)* *"
				+ "( *GROUP *BY *( *[a-zA-Z0-9._]* *)+ *( *, *[a-zA-Z0-9._]* *)* *)* *"
				+ "( *ORDER *BY *( *[a-zA-Z0-9._]* *)+ *( *, *[a-zA-Z0-9._]* *)* *)* *"
				+ ""
				+ ""
				+ ""
				+ "";
		
		return regex;
	}
}
