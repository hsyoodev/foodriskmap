package com.hangaramit.foodriskmap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import org.hibernate.annotations.Comment;

@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_seq")
    @SequenceGenerator(name = "restaurant_seq", sequenceName = "restaurant_seq", allocationSize = 1)
    Integer id;

    @Column
    @Comment("업소명")
    String prcscitypointBsshnm;

    @Column
    @Comment("업종")
    String indutyCdNm;

    @Column
    @Comment("인허가번호")
    String lcnsNo;

    @Column
    @Comment("처분확정일자")
    String dspsDcsndt;

    @Column
    @Comment("처분시작일(영업정지의경우)")
    String dspsBgndt;

    @Column
    @Comment("처분종료일(영업정지의경우)")
    String dspsEnddt;

    @Column
    @Comment("처분유형")
    String dspsTypecdNm;

    @Column
    @Comment("위반일자및위반내용")
    String viltcn;

    @Column
    @Comment("주소")
    String addr;

    @Column
    @Comment("전화번호")
    String telNo;

    @Column
    @Comment("대표자명")
    String prsdntNm;

    @Column
    @Comment("처분내용")
    String dspscn;

    @Column
    @Comment("위반법령")
    String lawordCdNm;

    @Column
    @Comment("공개기한")
    String publicDt;

    @Column
    @Comment("최종수정일")
    String lastUpdtDtm;

    @Column
    @Comment("처분기관명")
    String dspsInsttcdNm;

    @Column
    @Comment("행정처분전산키")
    String dspsDtlsSeq;
}
