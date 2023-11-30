package com.hangaramit.foodriskmap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_seq")
    @SequenceGenerator(name = "restaurant_seq", sequenceName = "restaurant_seq", allocationSize = 1)
    private Integer id;

    @Column
    @Comment("업소명")
    private String prcscitypointBsshnm;

    @Column
    @Comment("업종")
    private String indutyCdNm;

    @Column
    @Comment("인허가번호")
    private String lcnsNo;

    @Column
    @Comment("처분확정일자")
    private String dspsDcsndt;

    @Column
    @Comment("처분시작일(영업정지의경우)")
    private String dspsBgndt;

    @Column
    @Comment("처분종료일(영업정지의경우)")
    private String dspsEnddt;

    @Column
    @Comment("처분유형")
    private String dspsTypecdNm;

    @Column(length = 500)
    @Comment("위반일자및위반내용")
    private String viltcn;

    @Column
    @Comment("주소")
    private String addr;

    @Column
    @Comment("위도")
    private String lat;

    @Column
    @Comment("경도")
    private String lng;

    @Column
    @Comment("전화번호")
    private String telNo;

    @Column
    @Comment("대표자명")
    private String prsdntNm;

    @Column
    @Comment("처분내용")
    private String dspscn;

    @Column
    @Comment("위반법령")
    private String lawordCdNm;

    @Column
    @Comment("공개기한")
    private String publicDt;

    @Column
    @Comment("최종수정일")
    private String lastUpdtDtm;

    @Column
    @Comment("처분기관명")
    private String dspsInsttcdNm;

    @Column
    @Comment("행정처분전산키")
    private String dspsDtlsSeq;
}
