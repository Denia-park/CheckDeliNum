
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;

public class CheckDeliNum {
    public static void main(String[] args) {
        String path = "C:\\"; //파일 경로
        String fileName = "parcelExcel.xlsx"; //파일명 설정
        readExcel(path,fileName);

        //이진 검색 진행
        //기존에 있는 데이터를 삭제 ? 굳이 ?

        //다운로드 받은 poi 파일 주소 : https://archive.apache.org/dist/poi/release/bin/
        //참고한 블로그 글 : https://yangsosolife.tistory.com/7  , https://yangsosolife.tistory.com/8

    }

    public static void readExcel(String path, String fileName){
        try {
            FileInputStream file = new FileInputStream(path+fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            NumberFormat f = NumberFormat.getInstance();
            f.setGroupingUsed(false);	//지수로 안나오게 설정

            //시트 갯수
            int sheetNum = workbook.getNumberOfSheets();

            for(int s = 0; s < sheetNum; s++) {
                XSSFSheet sheet = workbook.getSheetAt(s);
                //행 갯수
                int rows = sheet.getPhysicalNumberOfRows();

                //행 표시
                for(int r = 0 ; r < rows ; r++) {
                    XSSFRow row = sheet.getRow(r);

                    int cells = row.getPhysicalNumberOfCells();

                    System.out.print("|	" + r + "	|");
                    //열 표시
                    for(int c = 0 ; c < cells; c++) {
                        XSSFCell cell = row.getCell(c);

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
                    System.out.println();
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

    }
}