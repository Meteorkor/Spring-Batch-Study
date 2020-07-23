[![Build Status](https://travis-ci.org/Meteorkor/Spring-Batch-Study.svg?branch=master)](https://travis-ci.org/Meteorkor/Spring-Batch-Study)
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
* AlphabetTaskletCountingJob
  * tasklet을 통해 char counting
* AlphabetChunkCountingJob
  * chunk(etl)를 통해 char counting
* AlphabetChunkParallelCountingJob
  * chunk(etl)를 통해 char counting을 하는데 병렬로 처리(CPU 작업이 아니라 blocking 작업일 경우 유용할듯)
    * Partitioner의 경우 처음부터 데이터를 나눠야 함
    * 하나의 데이터로 CPU 작업이 큰경우, 그리고 데이터를 나눌수 있는 경우 내부에서 Stream.parallel를 활용 좋을듯함
  * T에서 ThreadPool을 활용하여 Future를 Writer에 반환(T 작업을 병렬처리)
  

# 정리
  * [JobLauncher](docs/JobLauncher.md)

