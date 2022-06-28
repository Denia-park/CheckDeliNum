package org.example;

import com.opencsv.CSVWriter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

//다운로드 받은 poi 파일 주소 : https://archive.apache.org/dist/poi/release/bin/
//참고한 블로그 글 : https://yangsosolife.tistory.com/7  , https://yangsosolife.tistory.com/8 , https://junghn.tistory.com/entry/JAVA-%EC%9E%90%EB%B0%94-POI-%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%EC%97%91%EC%85%80-%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C-%EC%97%91%EC%85%80-%EC%9D%BD%EA%B8%B0-3?category=870199

public class Main {

    static final String PROGRAM_VERSION = "Version : 1.1 , UpdateDate : 22년 6월 29일";
    static boolean FILE_START_FLAG = true;
    static int completeDeliProduct = 0;
    static String fileNameCSV = getStringOfNowLocalDateTime();
    static final int DELIVERY_NUMBER_CELL_INDEX = 6; //G [0부터 시작임.]
    static final int ORDER_NUMBER_CELL_INDEX = 9; //J [0부터 시작임.]

    public static void main(String[] args) {
        System.out.println("송장 번호 등록 프로그램을 시작합니다. [ " + PROGRAM_VERSION + " ]");

        HashMap<String, String> hashMap = new HashMap<>(1500);//초기 용량(capacity)지정
        List<String> savedDeliNumList = new ArrayList<>(500);//초기 용량(capacity)지정
        Scanner sc = new Scanner(System.in); // 사용자로부터 데이터를 받기 위한 Scanner

        String path = System.getProperty("user.dir") + "\\"; //현재 작업 경로
        String fileName = "parcelExcel.xlsx"; //파일명 설정

        XSSFSheet sheetDataFromExcel = readExcel(path, fileName); //엑셀 파일 Read
        if (sheetDataFromExcel == null) { //파일을 못 읽어오면 종료.
            System.out.println("파일을 찾지 못했으므로 프로그램을 종료 합니다.");

            System.out.println("Enter 를 치면 정상 종료됩니다.");
            sc.nextLine(); //프로그램 종료 전 Holding
            return; //프로그램 종료
        }

        saveReadDataToHashMap(sheetDataFromExcel,hashMap); //읽은 엑셀 파일 HashMap으로 저장

        System.out.print("Excel 파일을 읽었습니다. \n\n" +
                "이제 바코드를 입력해주시면 됩니다!!! \n" +
                "===========================================================================\n\n");

        Toolkit toolkit = Toolkit.getDefaultToolkit(); // 비프음 내기 위해서 생성해야할 인스턴스

        while(true){
            //바코드 입력을 무한으로 Read , "qq" 입력이 들어오면 프로그램 종료
            String barcodeNumber = getBarcodeNumber(sc).replace("\n", ""); // \n을 없애고 저장
//            System.out.println(barcodeNumber);
            if (barcodeNumber.equals("qq") || barcodeNumber.equals("QQ")) {
                break;
            }

            //이미 등록된 송장 번호 인지 확인하기.
            if(savedDeliNumList.contains(barcodeNumber)){
                System.out.println("이미 등록된 송장입니다.");
                System.out.println();
                continue;
            }

            //바코드 입력이 들어오면 HashMap에서 주문번호 확인
            ActualData actualData = getActualData(hashMap, barcodeNumber, toolkit);
            if(!actualData.isValidBarcodeNumInHashMap){
                continue;
            }
            //주문번호 확인이 되면 CSV 파일에 내용을 추가 (추가할때 "상품별주문번호"도 추가해줘야함)
            writeDataToCSV(path,actualData,savedDeliNumList);
        }

        System.out.println("오늘 처리한 송장 개수 : "+ completeDeliProduct +" 개 , 사용해주셔서 감사합니다.");
        System.out.println("Enter 를 치면 정상 종료됩니다.");
        sc.nextLine(); //프로그램 종료 전 Holding
    }

    private static void writeDataToCSV(String path, ActualData actualData, List<String> savedDeliNumList) {
        File file = new File(path, fileNameCSV);
        try (
                FileOutputStream fos = new FileOutputStream(file,true);
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                CSVWriter writer = new CSVWriter(osw);
        ) {
            if(FILE_START_FLAG){
                String[] title = {
                        "주문번호",
                        "상품별주문번호",
                        "운송장번호",
                        "택배업체코드"
                };
                writer.writeNext(title,false);
                FILE_START_FLAG = false;
            }

            String[] writeData = {
                    actualData.getOrderNumber(), //주문번호
                    actualData.getOrderNumber() + "_1", //상품별 주문번호
                    actualData.getDeliveryNumber(), //운송장번호
                    null //택배업체코드 인데 안쓰므로 공백
            };
            writer.writeNext(writeData,false);
            System.out.println("등록되었습니다!");

            savedDeliNumList.add(actualData.getDeliveryNumber());
            completeDeliProduct = savedDeliNumList.size();
            System.out.println("오늘 처리한 송장 개수 : " + completeDeliProduct + " 개");

            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getStringOfNowLocalDateTime() {
        // 현재 날짜/시간
        LocalDateTime now = LocalDateTime.now();

//        // 현재 날짜/시간 출력
//        System.out.println(now); // 2021-06-17T06:43:21.419878100

        // 포맷팅
        String formatedNow = now.format(DateTimeFormatter.ofPattern("yyMMdd_HH_mm_ss"));

//        // 포맷팅 현재 날짜/시간 출력
//        System.out.println(formatedNow);  // 2021년 06월 17일 06시 43분 21초

        return "CSV_" + formatedNow + ".csv"; //Ex) CSV_220628_02_38_02

    }

    private static ActualData getActualData(HashMap<String, String> hashMap, String barcodeNumber, Toolkit toolkit) {
        String orderNumber = hashMap.get(barcodeNumber);
        boolean isValid;
        if (orderNumber == null) {
            System.out.print("입력하신 송장번호 와 매칭되는 주문번호가 없습니다. \n"
                    + "다시 확인해주세요! \n\n");
            toolkit.beep();

            isValid = false;
        }else{
            //System.out.println(orderNumber);
            isValid = true;
        }

        return new ActualData(barcodeNumber, orderNumber, isValid);
    }

    private static String getBarcodeNumber(Scanner sc) {
        String buffer, BarcodeNumber;
        //        buffer = sc.nextLine();

        System.out.println("바코드 번호를 입력하세요");

        BarcodeNumber = sc.nextLine();

        return BarcodeNumber;

    }

    public static XSSFSheet readExcel(String path, String fileName){
        try {
            FileInputStream file = new FileInputStream(path + fileName);
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