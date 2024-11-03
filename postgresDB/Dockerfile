#PostgreSQL 15 버전을 베이스 이미지로 사용
FROM postgres:15

#init.sql
COPY ./init/init.sql /docker-entrypoint-initdb.d/

#postgresql.conf파일을 /etc/postgresql/postgresql.conf 로 복사, 기본 설정 파일을 덮어쓰기하여 새로운 설정 적용
COPY ./config/postgresql.conf /etc/postgresql/custom.conf

EXPOSE 5432

CMD ["postgres", "-c", "config_file=/etc/postgresql/custom.conf"]
