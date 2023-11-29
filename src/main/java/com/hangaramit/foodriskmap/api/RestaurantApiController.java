package com.hangaramit.foodriskmap.api;

import com.hangaramit.foodriskmap.entity.Restaurant;
import com.hangaramit.foodriskmap.repository.RestaurantRepository;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
public class RestaurantApiController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RestaurantRepository restaurantRepository;

    //    @GetMapping("/restaurant/insert")
    public String insert() {
        final int requestMaxRange = 1000;
        int startIdx = 1;
        int endIdx = requestMaxRange;

        // ex) 1 ~ 1000
        Map<String, Object> data = getData(startIdx, endIdx);
        Map<String, Object> i2630 = (Map<String, Object>) data.get("I2630");
        save(data);

        // ex) 4084
        int totalCount = Integer.parseInt(i2630.get("total_count").toString());

        // ex) 1001 ~ 4000
        startIdx += requestMaxRange;
        endIdx += requestMaxRange;
        while (startIdx <= totalCount && endIdx <= totalCount) {
            data = getData(startIdx, endIdx);
            save(data);
            startIdx += requestMaxRange;
            endIdx += requestMaxRange;
        }

        // ex) 4001 ~ 4084
        endIdx = startIdx + (totalCount - startIdx);
        data = getData(startIdx, endIdx);
        save(data);

        return "redirect:/";
    }

    private Map<String, Object> getData(int startIdx, int endIdx) {
        final String URL = "https://openapi.foodsafetykorea.go.kr/api";
        final String keyId = "5206ab77eeb148fd9ba3";
        final String serviceId = "I2630";
        final String dataType = "json";
        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(
                URL + "/" + keyId + "/" + serviceId + "/" + dataType + "/" + startIdx + "/" + endIdx,
                Object.class);
        Map<String, Object> data = (Map<String, Object>) responseEntity.getBody();
        return data;
    }

    private void save(Map<String, Object> data) {
        Map<String, Object> i2630 = (Map<String, Object>) data.get("I2630");
        List<Map<String, String>> row = (List<Map<String, String>>) i2630.get("row");
        for (Map<String, String> map : row) {
            Restaurant restaurant = Restaurant.builder()
                    .prcscitypointBsshnm(map.get("PRCSCITYPOINT_BSSHNM"))
                    .indutyCdNm(map.get("INDUTY_CD_NM"))
                    .lcnsNo(map.get("LCNS_NO"))
                    .dspsDcsndt(map.get("DSPS_DCSNDT"))
                    .dspsBgndt(map.get("DSPS_BGNDT"))
                    .dspsEnddt(map.get("DSPS_ENDDT"))
                    .dspsTypecdNm(map.get("DSPS_TYPECD_NM"))
                    .viltcn(map.get("VILTCN"))
                    .addr(map.get("ADDR"))
                    .telNo(map.get("TEL_NO"))
                    .prsdntNm(map.get("PRSDNT_NM"))
                    .dspscn(map.get("DSPSCN"))
                    .lawordCdNm(map.get("LAWORD_CD_NM"))
                    .publicDt(map.get("PUBLIC_DT"))
                    .lastUpdtDtm(map.get("LAST_UPDT_DTM"))
                    .dspsInsttcdNm(map.get("DSPS_INSTTCD_NM"))
                    .dspsDtlsSeq(map.get("DSPSDTLS_SEQ"))
                    .build();

            restaurantRepository.save(restaurant);
        }
    }
}


