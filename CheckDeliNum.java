
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;

public class CheckDeliNum {
    public static void main(String[] args) {
        String path = System.getProperty("user.dir"); //현재 작업 경로
        String fileName = "parcelExcel.xlsx"; //파일명 설정
        readExcel(path,fileName);

        //다운로드 받은 poi 파일 주소 : https://archive.apache.org/dist/poi/release/bin/
        //참고한 블로그 글 : https://yangsosolife.tistory.com/7  , https://yangsosolife.tistory.com/8

    }

    public static void readExcel(String path, String fileName){
        try {
            FileInputStream file = new FileInputStream(path+"\\"+fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            XSSFSheet sheet = workbook.getSheetAt(0); // 첫번째 시트만 사용

            //행 갯수
            int rows = sheet.getPhysicalNumberOfRows();

            //행 표시
            for(int r = 0 ; r < rows ; r++) {
                XSSFRow row = sheet.getRow(r);

                final int DELIVERY_NUMBER = 6; //G [0부터 시작임.]
                final int ORDER_NUMBER = 9; //J [0부터 시작임.]

                int cells = row.getPhysicalNumberOfCells();

                System.out.print("|	" + r + "	|");

                showCells(row, DELIVERY_NUMBER);
                showCells(row, ORDER_NUMBER);

                System.out.println();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void showCells(XSSFRow row, int cellIndex) {
        //열 표시
            XSSFCell cell = row.getCell(cellIndex);

            String value = "";
            if(cell!=null) {
                value = cell.getStringCellValue(); // 무조건 스트링값만 사용
            }
            System.out.print("		"+value+"		|");
    }
}