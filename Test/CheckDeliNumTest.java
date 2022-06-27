package Test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CheckDeliNumTest {

    // 1. 중복되는 경우 - 택배사 Excel - 중복 경우 나올 수 X
    // 2. 중복되는 경우 - CSV File - 중복 되면 처음에 나온 얘가 우선권을 가짐
    // 3. 해당 데이터가 없는 경우 - 처리함
    // 4. 숫자를 잘못 입력 -> 한글 , 영어 , 자리수 12자리 아님 - 인식 못함
    // 5. 종료하는 방법 - qq 추가
    // 6. 기존에 파일이 존재하면 처리는 어떻게 ? - 추가됨
    // 7. 증긴에 그냥 강제로 끄면 어떻게 될까 ?? - 괜찮음

    @Nested
    @DisplayName("중복되는 경우 테스트")
    class duplication{
        @Test
        @DisplayName("송장 번호 In 택배사 Excel")
        void duplicationDeliveryNumInParcelExcel(){
            System.out.println("송장 번호 In 택배사 Excel");
        }
        @Test
        @DisplayName("주문 번호 In CSV File")
        void duplicationOrderNumInCSVFile(){
            System.out.println("주문 번호 In CSV File");
        }
    }
}