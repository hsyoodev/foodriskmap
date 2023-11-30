package com.hangaramit.foodriskmap.service;

import com.hangaramit.foodriskmap.entity.Restaurant;
import com.hangaramit.foodriskmap.repository.RestaurantRepository;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Transactional
public class RestaurantService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RestaurantRepository restaurantRepository;

    public void add() {
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
        List<Map<String, String>> rows = (List<Map<String, String>>) i2630.get("row");
        for (Map<String, String> row : rows) {
            setCoordinate(row);
            Restaurant restaurant = Restaurant.builder()
                    .prcscitypointBsshnm(row.get("PRCSCITYPOINT_BSSHNM"))
                    .indutyCdNm(row.get("INDUTY_CD_NM"))
                    .lcnsNo(row.get("LCNS_NO"))
                    .dspsDcsndt(row.get("DSPS_DCSNDT"))
                    .dspsBgndt(row.get("DSPS_BGNDT"))
                    .dspsEnddt(row.get("DSPS_ENDDT"))
                    .dspsTypecdNm(row.get("DSPS_TYPECD_NM"))
                    .viltcn(row.get("VILTCN"))
                    .addr(row.get("ADDR"))
                    .lat(row.get("LAT"))
                    .lng(row.get("LNG"))
                    .telNo(row.get("TEL_NO"))
                    .prsdntNm(row.get("PRSDNT_NM"))
                    .dspscn(row.get("DSPSCN"))
                    .lawordCdNm(row.get("LAWORD_CD_NM"))
                    .publicDt(row.get("PUBLIC_DT"))
                    .lastUpdtDtm(row.get("LAST_UPDT_DTM"))
                    .dspsInsttcdNm(row.get("DSPS_INSTTCD_NM"))
                    .dspsDtlsSeq(row.get("DSPSDTLS_SEQ"))
                    .build();

            restaurantRepository.save(restaurant);
        }
    }

    private void setCoordinate(Map<String, String> row) {
        String addr = row.get("ADDR");
        String url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", "dxkas2z55y");
        headers.set("X-NCP-APIGW-API-KEY", "wcNZKPlRmcDFMKOgAM0n1dxYRYJZwBeSMfRz24O5");
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<Object> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                Object.class);
        Map<String, Object> data = (Map<String, Object>) responseEntity.getBody();
        List<Map<String, Object>> addresses = ((List<Map<String, Object>>) data.get("addresses"));
        if (!addresses.isEmpty()) {
            String lng = addresses.get(0).get("x").toString();
            String lat = addresses.get(0).get("y").toString();
            row.put("LNG", lng);
            row.put("LAT", lat);
        }
    }
}

