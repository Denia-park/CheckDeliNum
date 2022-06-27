import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

//다운로드 받은 poi 파일 주소 : https://archive.apache.org/dist/poi/release/bin/
//참고한 블로그 글 : https://yangsosolife.tistory.com/7  , https://yangsosolife.tistory.com/8 , https://junghn.tistory.com/entry/JAVA-%EC%9E%90%EB%B0%94-POI-%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%EC%97%91%EC%85%80-%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C-%EC%97%91%EC%85%80-%EC%9D%BD%EA%B8%B0-3?category=870199

//※버전 1.0 - 22년 6월 28일

public class CheckDeliNum {
    static final String PROGRAM_VERSION = "Version : 1.0 , UpdateDate : 22년 6월 28일";
    static final int DELIVERY_NUMBER_CELL_INDEX = 6; //G [0부터 시작임.]
    static final int ORDER_NUMBER_CELL_INDEX = 9; //J [0부터 시작임.]

    public static void main(String[] args) {
        System.out.println("송장 번호 등록 프로그램을 시작합니다. [ " + PROGRAM_VERSION + " ]");

        HashMap<String, String> hashMap = new HashMap<>(3000);//초기 용량(capacity)지정
        Toolkit toolkit = Toolkit.getDefaultToolkit(); // 비프음 내기 위해서 생성해야할 인스턴스

        String path = System.getProperty("user.dir"); //현재 작업 경로
        String fileName = "parcelExcel.xlsx"; //파일명 설정

        XSSFSheet readSheet = readExcel(path, fileName); //엑셀 파일 Read
        if (readSheet == null) {
            return; //프로그램 종료
        }

        saveReadDataToHashMap(readSheet,hashMap); //읽은 엑셀 파일 HashMap으로 저장

        System.out.print("Excel 파일을 읽었습니다. \n" +
                "이제 바코드를 입력해주시면 됩니다!!! \n" +
                "===========================================================================\n");

        Scanner sc = new Scanner(System.in);

        while(true){
            //바코드 입력을 무한으로 Read , "qq" 입력이 들어오면 프로그램 종료
            String barcodeNumber = getBarcodeNumber(sc).replace("\n", ""); // \n을 없애고 저장
//            System.out.println(barcodeNumber);
            if (barcodeNumber.equals("qq") || barcodeNumber.equals("QQ")) {
                break;
            }

            //바코드 입력이 들어오면 HashMap에서 주문번호 확인
            String orderNumber = hashMap.get(barcodeNumber);
            if (orderNumber == null) {
                System.out.print("입력하신 송장번호 와 매칭되는 주문번호가 없습니다. \n"
                + "다시 확인해주세요! \n\n");
                toolkit.beep();

                continue;
            }else {
                System.out.println(orderNumber);
            }
            //주문번호 확인이 되면 CSV 파일에 내용을 추가 (추가할때 "상품별주문번호"도 추가해줘야함)

        }

        System.out.println("사용해주셔서 감사합니다.");
    }

    private static String getBarcodeNumber(Scanner sc) {
        String buffer,BarcodeNumber;
        //        buffer = sc.nextLine();

        System.out.println("바코드 번호를 입력하세요");

        BarcodeNumber = sc.nextLine();

        return BarcodeNumber;

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

    public static void saveReadDataToHashMap(XSSFSheet sheet , HashMap<String,String> hashMap){
        //행 갯수 가져오기
        int rows = sheet.getPhysicalNumberOfRows();

        //반드시 "행(row)"을 읽고 "열(cell)"을 읽어야함 ..
        for(int rowIndex = 1 ; rowIndex < rows ; rowIndex++) {
            XSSFRow row = sheet.getRow(rowIndex);
            String deliveryNumber = getCell(row, DELIVERY_NUMBER_CELL_INDEX);
            String orderNumber = getCell(row, ORDER_NUMBER_CELL_INDEX);

            if(deliveryNumber == null){
                throw new RuntimeException("송장 번호 가 Null 입니다. ");
            } else if (orderNumber == null) {
                throw new RuntimeException("주문 번호 가 Null 입니다. ");
            }

            //showRowsAndCells(rowIndex, row); // 저장되는 데이터를 표시

            hashMap.put(deliveryNumber, orderNumber);
        }
    }

    private static void showRowsAndCells(int rowIndex, XSSFRow row) {
        showRow(rowIndex);
        showCell(row, DELIVERY_NUMBER_CELL_INDEX);
        showCell(row, ORDER_NUMBER_CELL_INDEX);
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