# Spring-Batch-Study
 * 먼저 readme를 작성하여 시나리오를 작성 후 조금씩 코드 작성 예정
 * 추가로 레퍼런스를 살펴보며 각 기능들 사용해볼 예정
   * https://docs.spring.io/spring-batch/docs/4.2.x/reference/html/index.html

# 목표
  * SpringBatch에 제공하는 기능들을 파악
  * SpringBatch에서 제공하는 기능들의 내부 동작 파악
  * SpringBatch 활용해서 심화 기능들을 활용
    * SpringBatch의 ETL은 기본적으로 한 스레드로 동작하지만, 내부적으로 어떻게 병렬처리를 활용할지
      * Partitioner가 아니라 ChunkedTasklet 내부에서 어떤 방식으로
        * E-T-L 에서 T의 처리가 CPU 작업
        * E-T-L 에서 T의 처리가 blocking 작업(http call, 트랜잭션 상관없는 db 작업 등)
        

# 샘플 Job
  * TaskletSimpleJob
    * aa
  * ChunkedSimpleJob
    * aa
  * PartitionChunkedSimpleJob
    * aa
  * PartitionChunkedParallelJob
    * aa

# 선후처리 체크

