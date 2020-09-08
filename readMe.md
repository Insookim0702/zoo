# 동물원 예약 시스템 웹 애플리케이션
웹 앱을 개발하고 싶었던 저는 회사를 퇴사하고 웹 앱을 개발하기 시작했습니다. 코로나로 인해 문화 생활을 즐기기 어려운 지금, 조금이나마 설레는 마음으로 인터넷으로나마 귀여운 동물들을 보러 가기 위한 예약을 할 수 있는 사이트를 만들어보았습니다.

## 사용한 기술

- SpringBoot
- JUnit5
- Spring Security
- Java Persistence Api
- PostgresSQl
- Thymeleaf
- IDE : IntelliJ
- macOS

## Working with StudyMoim in your IDE
### preparation

- Java11



### Steps:

1) IntelliJ로 GitClone하기<br>
`File -> New -> Project from Version Control...`

2) git Url과 pc에 프로젝트를 내려받을 경로 지정

3) npm 내려 받기<br>
터미널에서 `studymoim홈경로/resources/static` 경로로 이동 후, 아래 명령어를 순서대로 입력합니다.
    - npm init
    - npm install jquery
    - npm install bootstrap
    - npm install jdenticon
    - npm install font-awesome
    - npm install tagify
    - npm install summernote
4) 초기 Application.properties

    인메모리 데이터베이인 [h2](https://www.h2database.com/html/main.html
)를 사용했습니다.

4) 애플리케이션 실행

    Visit [http://localhost:8080](http://localhost:8080) in your browser.






