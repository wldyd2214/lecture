insert into TB_LECTURE(TL_KEY, TL_TITLE, TL_DESC, TL_REG_DATE)
values (1, '허재 코치님의 특강', '무신사 29cm 전시 시스템 개발자 허재 코치님의 특강', '2024-06-25'),
       (2, '하헌우 코치님의 특강', '무신사 29cm 검색 시스템 개발자 하헌우 코치님의 특강', '2024-06-25'),
       (3, '이석범 코치님의 특강', '버킷플레이스 커머스 개발자 이석범 코치님의 특강', '2024-06-25'),
       (4, '김종협 코치님의 특강', 'SIR.LOIN 테크팀 리드 김종협 코치님의 특강', '2024-06-25');

insert into TB_LECTURE_SCHEDULE(TLS_KEY, TL_KEY, TLS_DATE, TLS_MAX_COUNT, TLS_REG_DATE, TLS_CURRENT_COUNT)
values (1, 4, '2024-07-01', 30, '2024-06-25', 0),
       (2, 4, '2024-07-02', 25, '2024-06-25', 0),
       (3, 4, '2024-07-03', 20, '2024-06-25', 0),
       (4, 4, '2024-07-04', 15, '2024-06-25', 0);

insert into TB_APPLICATION(TA_KEY, TLS_KEY, TA_USER_ID, TA_REG_DATE)
values (9999, 1, 1, '2024-06-25');