
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

        //이진 검색 진행
        //기존에 있는 데이터를 삭제 ? 굳이 ?

        //다운로드 받은 poi 파일 주소 : https://archive.apache.org/dist/poi/release/bin/
        //참고한 블로그 글 : https://yangsosolife.tistory.com/7  , https://yangsosolife.tistory.com/8

    }

    public static void readExcel(String path, String fileName){
        try {
            FileInputStream file = new FileInputStream(path+"\\"+fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            NumberFormat f = NumberFormat.getInstance();
            f.setGroupingUsed(false);	//지수로 안나오게 설정

            XSSFSheet sheet = workbook.getSheetAt(0);

            //행 갯수
            int rows = sheet.getPhysicalNumberOfRows();

            //행 표시
            for(int r = 0 ; r < rows ; r++) {
                XSSFRow row = sheet.getRow(r);

                final int DELIVERY_NUMBER = 6; //G [0부터 시작임.]
                final int ORDER_NUMBER = 9; //J [0부터 시작임.]

                int cells = row.getPhysicalNumberOfCells();

                System.out.print("|	" + r + "	|");

                showCells(f, row, DELIVERY_NUMBER);
                showCells(f, row, ORDER_NUMBER);

                System.out.println();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    private static void showCells(NumberFormat f, XSSFRow row, int cellIndex) {
        //열 표시
            XSSFCell cell = row.getCell(cellIndex);

            String value = "";
            if(cell!=null) {
                //타입 체크
                switch(cell.getCellType()) {
                    case STRING:
                        value = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        value = f.format(cell.getNumericCellValue())+"";
                        break;
                    case BLANK:
                        value = cell.getBooleanCellValue()+"";
                        break;
                    case ERROR:
                        value = cell.getErrorCellValue()+"";
                        break;
                }
            }
            System.out.print("		"+value+"		|");
    }
}