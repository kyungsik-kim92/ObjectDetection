# 📷 ObjectDetection
TensorFlow Lite 오픈소스 머신러닝 API를 이용해 카메라에 비치는 사물을 감지하고 영어 단어로 변환해 단어장을 만드는 Android 앱입니다. 단어장에서 단어를 검색하거나 카메라로 검색된 단어를 저장할 수 있으며, 토글 스위치로 단어의 뜻을 가렸다 보였다 하며 학습할 수 있습니다. 하루별 북마크 저장 개수에 따라 캘린더 색상이 변경됩니다.Kotlin + Clean Architecture + MVVM 기반으로, TFLite와 CameraX로 실시간 객체 인식을 하고, Room으로 단어 목록을 로컬에 캐시하며, Firebase Auth·Firestore로 로그인과 북마크를 관리합니다.


## 주요 기능

- **객체 인식 단어 검색** — TensorFlow Lite-Object Detection과 CameraX 연동으로 카메라 이미지 해석·분류 후, 선택한 항목으로 단어 뜻 조회(권한 → 카메라 → 객체 선택).
- **텍스트 스캔** — ML Kit 텍스트 인식(Text Recognition)으로 이미지에서 글자 추출 후 단어 검색.
- **단어 검색·상세** — Google SpreadSheet 연동 API로 단어 목록 수신, Room에 캐시. Dictionary API로 단어 뜻·발음 조회.
- **즐겨찾기** — Firebase Firestore로 단어 추가·삭제, 북마크 목록 조회.
- **캘린더** — 마이페이지 Material CalendarView에서 날짜별 표시, 해당 날짜 북마크 개수에 따라 EventDecorator로 색상 구간(level_0~3) 표시.
- **계정** — Firebase Auth 로그인, 회원가입, 회원탈퇴.
- **모니터링** — Firebase Crashlytics 연동.

## 기술 스택

- **Language:** Kotlin
- **UI:** DataBinding
- **Architecture:** Clean Architecture + MVVM
- **Async:** Kotlin Coroutines + Flow
- **DI:** Hilt (KSP)
- **Jetpack:** ViewModel, Navigation Component, Lifecycle
- **Camera:** CameraX
- **ML:** TensorFlow Lite (Object Detection), ML Kit (Text Recognition)
- **Network:** Retrofit + Gson
- **Local DB:** Room (Entity, DAO)
- **Backend:** Firebase Auth, Firestore, Crashlytics
- **Image:** Lottie
- **Build:** Gradle Kotlin DSL, Version Catalog (libs.versions.toml)


## 프로젝트 구조
 ```
ObjectDetction/
├── app/ # 진입점
│ └── App.kt
│
├── presentation/ # UI 레이어
│ ├── MainActivity.kt
│ ├── ui/ # splash, search, detect, word, bookmark, mypage, login, register 등
│ ├── util/ # ObjectDetectorHelper, OverlayView, EventDecorator
│ ├── ext/
│ └── adapter/
│
├── domain/ # 비즈니스 로직
│ ├── repo/
│ └── usecase/
│
├── data/ # 데이터 레이어
│ ├── database/ # AppDatabase, ExcelDao, ExcelEntity
│ ├── source/ # local, remote
│ ├── network/ # SheetApi, DictionaryApi
│ ├── repo/
│ └── di/
│
└── model/ # WordItem, BookmarkWord, api(dto)

  ```
