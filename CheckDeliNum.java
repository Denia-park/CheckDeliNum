import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

//다운로드 받은 poi 파일 주소 : https://archive.apache.org/dist/poi/release/bin/
//참고한 블로그 글 : https://yangsosolife.tistory.com/7  , https://yangsosolife.tistory.com/8 , https://junghn.tistory.com/entry/JAVA-%EC%9E%90%EB%B0%94-POI-%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%EC%97%91%EC%85%80-%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C-%EC%97%91%EC%85%80-%EC%9D%BD%EA%B8%B0-3?category=870199

//※버전 1.0 - 22년 6월 28일

public class CheckDeliNum {
    static final String PROGRAM_VERSION = "Version : 1.0 , UpdateDate : 22년 6월 28일";
    static final int DELIVERY_NUMBER_CELL_INDEX = 6; //G [0부터 시작임.]
    static final int ORDER_NUMBER_CELL_INDEX = 9; //J [0부터 시작임.]

    public static void main(String[] args) {
        System.out.println("송장 번호 등록 프로그램을 시작합니다. [ " + PROGRAM_VERSION + " ]");

        String path = System.getProperty("user.dir"); //현재 작업 경로
        String fileName = "parcelExcel.xlsx"; //파일명 설정

        XSSFSheet readSheet = readExcel(path, fileName); //엑셀 파일 Read
        if (readSheet == null) {
            return; //프로그램 종료
        }

//        saveReadDataToHashMap(readSheet); //읽은 엑셀 파일 HashMap으로 저장

        System.out.print("Excel 파일을 읽었습니다. \n" +
                "이제 바코드를 입력해주시면 됩니다!!! \n" +
                "===========================================================================");
        
        //바코드 입력을 무한으로 Read , "qq" 입력이 들어오면 프로그램 종료
        
        //바코드 입력이 들어오면 HashMap에서 주문번호 확인
        
        //주문번호 확인이 되면 CSV 파일에 내용을 추가 (추가할때 "상품별주문번호"도 추가해줘야함)
    }

    public static XSSFSheet readExcel(String path, String fileName){
        try {
            FileInputStream file = new FileInputStream(path + "\\" + fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            return workbook.getSheetAt(0); // 첫번째 시트만 사용
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveReadDataToHashMap(XSSFSheet sheet){
        if (sheet == null) {
            return;
        }
        //행 갯수
        int rows = sheet.getPhysicalNumberOfRows();

        //행 표시
        for(int rowIndex = 0 ; rowIndex < rows ; rowIndex++) {
            XSSFRow row = sheet.getRow(rowIndex);



            showRowsAndCells(rowIndex, row);
        }
    }

    private static void showRowsAndCells(int rowIndex, XSSFRow row) {
        showRow(rowIndex);
        showCells(row, DELIVERY_NUMBER);
        showCells(row, ORDER_NUMBER);
        System.out.println();
    }

    private static void showRow(int rowIndex) {
        //행 표시
        System.out.print("|	" + rowIndex + "	|");
    }

    private static void showCell(XSSFRow row, int cellIndex) {
        //열 표시
            XSSFCell cell = row.getCell(cellIndex);

            String value = "";
            if(cell!=null) {
                value = cell.getStringCellValue(); // 무조건 스트링값만 사용
            }
            System.out.print("		"+value+"		|");
    }

    private static String getCell(XSSFRow row, int cellIndex) {
        //열 표시
            XSSFCell cell = row.getCell(cellIndex);

        if (cell != null) {
            return cell.getStringCellValue(); // 무조건 스트링값만 사용된다
        } else {
            return null;
        }
    }
}