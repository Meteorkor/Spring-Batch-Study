#DispatcherServlet

Spring MVC, Spring Web에서
진입점이 되는 서블릿 클래스


- ApplicationFilterChain을 통해 servlet.service 호출
  - 여기서 servlet이 DispatcherServlet
  - method에 따라 doGet, doPost, 등이 불림
    - doGet, doPost 등의 구현체는 FrameworkServlet에 있음
    - 그런데 결국 processRequest가 불리도록 되어있음, 결국 DispatcherServlet에 doService 호출
  - DispatcherServlet.doService()
    - request attribute에 여러가지 세팅(webApplicationContext라거나, resolver들)
    - doDispatch(req,res)
      - getHandler
        - ![image](https://user-images.githubusercontent.com/5335333/88764611-34e39200-d1b0-11ea-857c-c2d5f183e2eb.png)
        - 여러가지 Mapping을 하나씩 돌려보며 대응되는 Handler를 찾는다.
          - 일반적으로 Controller에 Mapping을 설정한경우 RequestMappingHandlerMapping에서 핸들러를 찾게된다.
      - 전반적으로 대부분 처리는 Request에 attribute에 값들을 세팅하며 체크하며 진행
      - getHandlerAdapter()
        - ![image](https://user-images.githubusercontent.com/5335333/88781318-37051b00-d1c7-11ea-8e85-cff401e3d208.png)
        - supports()를 통해 지원한는 Adapter인지 체크해서 adapter를 선정(보통은 RequestMappingHandlerAdapter)
        - 선정된 adapter를 통해 핸들러에 param 매핑 로직 처리
        - 그후 invokeHandlerMethod를 호출하여 해당 Controller 호출
      - 처리 완료 이후 processDispatchResult()
        - 에러 발생했을 경우, 내부에 getExceptionHandlerMethod를 구해서 ControllerAdvice 호출
