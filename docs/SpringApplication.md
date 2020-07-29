# SpringApplication


 - run 
   - prepareContext
     - applyInitializers()
       - ApplicationContextInitializer의 initialize 호출
     - SpringApplicationRunListener의 contextPrepared 호출
     - arg를 Bean으로 등록
       - beanFactory.registerSingleton("springApplicationArguments", applicationArguments);
     - banner를 Bean으로 등록
       - beanFactory.registerSingleton("springBootBanner", printedBanner);
     - getAllSources()
       - primarySources는 mainClass
       - 결국에 SpringApplication.run의 args로 들어오는 Class들
     - load
       - getAllSources() 값을 넘김
       - BeanDefinitionLoader 생성
       - Sources들을 load 처리
         - groovy면 여기서 로드를 하는데, 아니면 cnt만 올리는것으로 보임
         - 그런데, 이 return 값을 사용하지 않음..;;;;??
       - 그리고 만든 BeanDefinitionLoader 은 버려짐..;
      - 결국 하는일은 arg와 bannerfmf bean으로 등록하고, groovy의 경우 빈등록, init 처리들 호출
   - refreshContext
     - refresh(context)
       - 결국 context.refresh()
       - 대부분은 오버라이드 없이 org.springframework.context.support.AbstractApplicationContext.refresh() 처리
         - prepareRefresh
         - obtainFreshBeanFactory
         - prepareBeanFactory
         - postProcessBeanFactory
         - invokeBeanFactoryPostProcessors
         - registerBeanPostProcessors
         - initMessageSource
         - initApplicationEventMulticaster
         - onRefresh
         - registerListeners
         - finishBeanFactoryInitialization
         - finishRefresh
         - resetCommonCaches
           - (ReflectionUtils, AnnotationUtils, ResolvableType).clearCache
         
         
     - context.registerShutdownHook();
       - JVM 내려갈때 불리도록 shutdown hook 등록
         - registerShutdownHook의 구현체는 결국 org.springframework.context.support.AbstractApplicationContext에 존재
           - context에 doClose() 호출하도록
             - 모니터링 관련 정리
             - ContextClosedEvent publish
             - LifecycleProcessor close
             - destryBeans()
             - closeBeanFactory
             - onclose()
   - afterRefresh
     - 딱히 수행하는 내용 없음
   - callRunners
     - ApplicationRunner 와 CommandLineRunner 수행




- ApplicationContext
  - DEFAULT_CONTEXT_CLASS
    - AnnotaionConfigApplicationContext
  - DEFAULT_SERVLET_WEB_CONTEXT_CLASS
    - AnnotationConfigServletWebServerApplicationContext
  - DEFAULT_REACTIVE_WEB_CONTEXT_CLASS
    - AnnotationConfigReactiveWebServerApplicationContext
  - 기본적으로 AnnotatedBean
