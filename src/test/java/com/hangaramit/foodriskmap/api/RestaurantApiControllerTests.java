package com.hangaramit.foodriskmap.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hangaramit.foodriskmap.entity.Restaurant;
import com.hangaramit.foodriskmap.repository.RestaurantRepository;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@SpringBootTest
public class RestaurantApiControllerTests {

    private final String URL = "https://openapi.foodsafetykorea.go.kr/api";
    private final String keyId = "5206ab77eeb148fd9ba3";
    private final String serviceId = "I2630";
    private final String dataType = "json";
    private final String startIdx = "1";
    private final String endIdx = "1000";
    private ResponseEntity<Object> responseEntity;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RestaurantRepository restaurantRepository;


    @DisplayName("Open API 행정처분결과(식품접객업) GET 요청 결과 확인")
    @BeforeEach
    void get() {
        // 데이터요청은 한번에 최대 1000건을 넘을 수 없습니다.
        responseEntity = restTemplate.getForEntity(
                URL + "/" + keyId + "/" + serviceId + "/" + dataType + "/" + startIdx + "/" + endIdx,
                Object.class);
        HttpStatus httpStatus = (HttpStatus) responseEntity.getStatusCode();
        Object body = responseEntity.getBody();

        assertEquals(HttpStatus.OK, httpStatus);
        assertThat(body).isNotNull();
    }

    @DisplayName("Open API Data -> Insert Oracle DB")
    @Test
    @Transactional
    void insert() {
        Object body = responseEntity.getBody();

        assertThat(body).isInstanceOf(Map.class);
        Map<String, Object> data = (Map<String, Object>) body;

        assertThat(data.get("I2630")).isInstanceOf(Map.class);
        Map<String, Object> i2630 = (Map<String, Object>) data.get("I2630");

        assertThat(i2630.get("row")).isInstanceOf(List.class);
        List<Map<String, String>> row = (List<Map<String, String>>) i2630.get("row");

        for (Map<String, String> map : row) {
            String prcscitypointBsshnm = map.get("PRCSCITYPOINT_BSSHNM");
            String indutyCdNm = map.get("INDUTY_CD_NM");
            String lcnsNo = map.get("LCNS_NO");
            String dspsDcsndt = map.get("DSPS_DCSNDT");
            String dspsBgndt = map.get("DSPS_BGNDT");
            String dspsEnddt = map.get("DSPS_ENDDT");
            String dspsTypecdNm = map.get("DSPS_TYPECD_NM");
            String viltcn = map.get("VILTCN");
            String addr = map.get("ADDR");
            String telNo = map.get("TEL_NO");
            String prsdntNm = map.get("PRSDNT_NM");
            String dspscn = map.get("DSPSCN");
            String lawordCdNm = map.get("LAWORD_CD_NM");
            String publicDt = map.get("PUBLIC_DT");
            String lastUpdtDtm = map.get("LAST_UPDT_DTM");
            String dspsInsttcdNm = map.get("DSPS_INSTTCD_NM");
            String dspsDtlsSeq = map.get("DSPSDTLS_SEQ");

            Restaurant restaurant = Restaurant.builder()
                    .prcscitypointBsshnm(prcscitypointBsshnm)
                    .indutyCdNm(indutyCdNm)
                    .lcnsNo(lcnsNo)
                    .dspsDcsndt(dspsDcsndt)
                    .dspsBgndt(dspsBgndt)
                    .dspsEnddt(dspsEnddt)
                    .dspsTypecdNm(dspsTypecdNm)
                    .viltcn(viltcn)
                    .addr(addr)
                    .telNo(telNo)
                    .prsdntNm(prsdntNm)
                    .dspscn(dspscn)
                    .lawordCdNm(lawordCdNm)
                    .publicDt(publicDt)
                    .lastUpdtDtm(lastUpdtDtm)
                    .dspsInsttcdNm(dspsInsttcdNm)
                    .dspsDtlsSeq(dspsDtlsSeq)
                    .build();

            restaurantRepository.save(restaurant);
        }
    }
}