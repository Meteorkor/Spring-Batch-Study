# JobLauncher
* 기본적으로 SimpleJobLauncher를 사용
* 내부적으로 JobRepository와 taskExecutor를 변수로 가지고 있음
  * JobRepository는 메타정보를 로딩하고 상태를 기록할때 사용
  * taskExecutor는 Job을 어떤 방식으로 처리할지(taskExecutor를 지정하지 않을 경우 SyncTaskExecutor를 사용하며 현재 스레드에서 Job을 실행)

* 흐름(run)
  * 마지막 JobExecution을 조회
    * 과거 JobExecution이 존재하고 재시작 불가 flag가 설정되어있으면 JobRestartException throw
    * step의 상태가 실행중이거나, stop 호출로 인해 멈추는중이라면 JobExecutionAlreadyRunningException throw
    * 혹은 상태가 UNKNOWN이라도 throw
  * JobParameter의 valid 체크
  * JobExecution 생성
    * 과거 실행한 내역이 있는경우(JobInstance가 있는 경우)
      * ExecutionContext 복원해서 제공
    * 과거 실행한 내역이 없는경우(JobInstance가 없는 경우)
      * JobInstance 생성
  * Job 실행(job.execute())
  * return JobExecution;
