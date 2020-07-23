#JobRepository
* Job의 메타 정보를 조회하거나 상태를 Storage에 저장하는데 사용되는 모듈
* 일반적으로 SimpleJobRepository
* 변수는 4가지, JobInstanceDao, JobExecutionDao, StepExecutionDao, ExecutionContextDao
  * JobInstanceDao : JDBC 기준, BATCH_JOB_INSTANCE 테이블 관련 Dao
  * JobExecutionDao : JDBC 기준, BATCH_JOB_EXECUTION, BATCH_JOB_EXECUTION_PARAMS 테이블 관련 Dao
  * StepExecutionDao : JDBC 기준, BATCH_STEP_EXECUTION 테이블 관련 Dao
  * ExecutionContextDao : JDBC 기준, BATCH_JOB_EXECUTION_CONTEXT, BATCH_STEP_EXECUTION_CONTEXT 테이블 관련 Dao
* 4가지의 Dao는 생성자에서 주입
  * Jdbc~~~와 Map~~~ 이 존재하며 커스터마이징으로 다른 DBMS나 다른 storage도 사용 가능합니다.
