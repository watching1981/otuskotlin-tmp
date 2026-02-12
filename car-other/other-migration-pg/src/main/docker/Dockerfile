FROM liquibase/liquibase:4.31.1

COPY ./* /liquibase/


 CMD ["liquibase", "--changelog-file=main/liquibase/changelog/changelog-v0.0.1.sql", "update"]
 #  CMD ["liquibase", "--changelog-file=changelog-v0.0.1.sql", "update"]
# CMD ["liquibase", "--changelog-file=/liquibase/main/liquibase/changelog/changelog-v0.0.1.sql", "update"]
