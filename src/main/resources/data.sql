insert into TB_LECTURE(TL_KEY, TL_TITLE, TL_DESC, TL_START_DATE, TL_MAX_COUNT)
values (1, '허재 코치님의 특강', '무신사 29cm 전시 시스템 개발자 허재 코치님의 특강', now(), 30),
       (2, '하헌우 코치님의 특강', '무신사 29cm 검색 시스템 개발자 하헌우 코치님의 특강', now(), 30),
       (3, '이석범 코치님의 특강', '버킷플레이스 커머스 개발자 이석범 코치님의 특강', now(), 30),
       (4, '김종협 코치님의 특강', 'SIR.LOIN 테크팀 리드 김종협 코치님의 특강', now(), 30);

insert into TB_USER(TU_KEY, TU_NAME)
values (1, '박지용'),
       (2, '위정현'),
       (3, '백현명'),
       (4, '이강주');