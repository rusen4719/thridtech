# Rocketalk
전남대학교 전기·전자통신·컴퓨터공학부(여수) 졸업작품

***

### 프로젝트명
Realtime sOKCET.io TALK
- 실시간으로 Socket.io를 통한 채팅 애플리케이션 

***

### 프로젝트 선정
기술적 자질을 향상시키면서 대중적인 서비스를 구현하고 싶었습니다.<br>
여러 서비스를 살펴보는중 자주 사용하던 카카오톡, 디스코드와 같은 메신저 서비스가 눈에 띄었고, <br>
그로 인해 채팅 서비스를 주제로 선택하게 되었습니다. <br><br>
여러 채팅 어플들을 참고하여 메커니즘을 이해할려고 노력했고 공부했던 기술들을 사용하게 되었습니다.<br>
그러나 일반 채팅어플들과는 다른 차이점을 두고 싶어서 채팅을 하는 도중에 영상을 공유할 수 있게 했습니다.<br>
그리고 이미지 URL을 전송했을경우 주소 값이 아닌 이미지를 그대로 보여주도록 했습니다.<br>

***

### 역할
- 이재룡 
    - 프론트 엔드
    - 전반적인 앱 개발 및 기능 개발
    - 서버 통신

- 김항래
    - 백엔드 
    - 서버와 DB 구축

***

### 개발 환경 및 기술
백엔드
  - HEROKU
  - node.js
  - javaScript
  - mongoose
  - mongoDB

프론트엔드
  - Android Studio
  - Kotlin
  - Retrofit : Http Connection
  - Yotube API
  - Glide
  - Socket.io

***

### 시연

#### 로그인 및 회원가입, 아이디 찾기

<img src = "https://user-images.githubusercontent.com/53455328/143876179-cc0804cd-ea8a-488d-aca7-d9a9f4253f72.png" width="200" height="350">   <img src = "https://user-images.githubusercontent.com/53455328/143876692-dc4b9ed5-aef4-4d19-964a-8afa1f05c21c.png" width="200" height="350">
<img src = "https://user-images.githubusercontent.com/53455328/143876728-3b0b5738-ba0c-4dcd-a05e-9c57352d2d10.png" width="200" height="350"> <br>

#### 친구 목록 및 프로필 변경, 친구 추가

<img src = "https://user-images.githubusercontent.com/53455328/143877154-a50ecdac-1a07-4316-9b12-72ed1ba9ac35.png" width="200" height="350">   <img src = "https://user-images.githubusercontent.com/53455328/143877217-d7d893cc-a044-4c76-be27-26fa88c6f0cf.png" width="200" height="350"> <img src = "https://user-images.githubusercontent.com/53455328/143877270-6c196086-2630-4bd0-9d4b-40e4bf384cb5.png" width="200" height="350"> <img src = "https://user-images.githubusercontent.com/53455328/143877431-4c0a39b6-f38b-4fd6-b8a7-dbcc4bab047b.png" width="200" height="350"> <br>

#### 채팅방 목록과 채팅방 생성

<img src = "https://user-images.githubusercontent.com/53455328/143877488-c9f9c332-7bdf-4bfe-b327-9c9f9cbaf6ea.png" width="200" height="350"> <img src = "https://user-images.githubusercontent.com/53455328/143877558-cd78cc35-ec5f-42af-87f5-8e0cdbccfb84.png" width="200" height="350"> <br>

#### 채팅 화면 및 채팅 시연

<img src = "https://user-images.githubusercontent.com/53455328/143877865-e34f82ea-1314-4489-ab9f-7331f9c7c22b.png" width="200" height="350"> <img src = "https://user-images.githubusercontent.com/53455328/143878659-8fbc2019-6748-4948-bded-1b6cdd1f8f86.gif" width="200" height="350"> 

***

### 느낀점
처음 해본 협업 프로젝트입니다. 평소에는 Android Studio 로 간단한 프로젝트를 혼자 만들어 보고 해서 큰 불편함이 없었는데, <br>
협업을 통해서 해보니 마주하는 문제점들을 느꼈습니다. 간단하게만 정리하면 <br>
  - 이름 짓기 (변수, 함수, 클래스이름 ... )
  - Rest Api 의 중요성 
  - 여러가지의 외부 라이브러리
  - Code 구조 짜기
  - 동기, 비동기화 (coroutine) 등등

개발자 유머로 변수 이름짓기가 가장 힘들다고 하던데 그걸 이번에 느껴버렸습니다.... 비슷한 기능을 하는 여러가지 변수들을 선언할때 <br>
길게 쓰고 싶지 않지만 조금씩 길어지고 어떤 명사를 써야할지도 잘 모르겠고 그러면서도 다른사람에게 어떤 변수인지 알게 해줘야하는게 힘들다는걸 깨달았습니다<br>

Rest Api가 중요하다는건 알고 있었지만 아직도 어떤 개념인지 확실하지는 모르겠습니다. 그리고 Retrofit을 사용해서 서버와 HTTP 통신 하는데 <br>
GET 방식인지 POST인지 주고받는 데이터의 타입이 서버측과 클라이언트측이 동일해야 통신이 되는 부분에서 많은 애를 먹었습니다. 이부분을 이해하려고 서버측 <br>
코드까지 공부하게 되었습니다. 하지만 이로 인해 node.js 코드는 어느정도 볼 수 있게 됬네요!

이번 프로젝트를 진행하면서 가장 크게 느낀게 Code의 구조를 미리 짜놓고 코딩하는게 중요하다는걸 느꼇습니다 <br>
저는 일단 코딩을 한뒤 추가적인 기능을 코딩할때마다 이럴거면 어떤 기능이 들어갈지 정해두고 그에 맞는 구조를 짠뒤 코딩하는게 좋겠다라는 생각했습니다.<br>
이미 다 짜여진 코드를 수정하고 추가하는게 여간 힘든게 아니더군요... <br>

여러 버그들이 터졌고 버그 고치느라 밤을 새보기도 했지만 버그를 고치고 원하는 결과가 나왔을 때, 코드가 생각한 대로 돌아 갈때, <br>
새로운 라이브러리를 사용하고 새로운 기능을 사용할때 큰 기쁨을 느꼈고 다른 프로젝트를 또 해보고 싶다라는 생각이 들었습니다.

***

사용한 유튜브 영상 출처 <br>
  - [유튜버 haha ha](https://www.youtube.com/c/hahahaYouTube)

