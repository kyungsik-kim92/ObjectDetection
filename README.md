# 📷 ObjectDetection
TensorFlow Lite 오픈소스 머신러닝 API를 이용해 카메라에 비치는 사물을 감지하고 영어 단어로 변환해 단어장을 만드는 Android 앱입니다. 단어장에서 단어를 검색하거나 카메라로 검색된 단어를 저장할 수 있으며, 토글 스위치로 단어의 뜻을 가렸다 보였다 하며 학습할 수 있습니다. 하루별 북마크 저장 개수에 따라 캘린더 색상이 변경됩니다.Kotlin + Clean Architecture + MVVM 기반으로, TFLite와 CameraX로 실시간 객체 인식을 하고, Room으로 단어 목록을 로컬에 캐시하며, Firebase Auth·Firestore로 로그인과 북마크를 관리합니다.


## 주요 기능

- **객체 인식 단어 검색** — TensorFlow Lite(Object Detection)과 CameraX 연동으로 카메라 이미지 해석·분류 후, 선택한 항목으로 단어 뜻 조회(권한 → 카메라 → 객체 선택).
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

## 기술적 도전과 해결

### 1. 북마크 화면과 단어 상세 간 상태 동기화
북마크 탭에서 단어 상세로 진입 후 해제 시 목록에 반영되지 않는 문제

- **문제:** 북마크 화면에서 단어를 눌러 WordDetail로 진입한 뒤, 상세에서 북마크 해제를 해도 북마크 목록이 갱신되지 않음.
- **원인:** WordDetailFragment에서 북마크 삭제 시 메인(북마크 탭)의 목록을 갱신하는 구조가 아니었음.
- **해결:** WordDetailFragment에 HomeViewModel을 주입하고, 북마크 해제 시 해당 ViewModel을 통해 목록 갱신. observeEvents로 일회성 이벤트를 전달해 UI 반영.

### 2. 일회성 이벤트와 상태 분리 (ViewEvent)
재구독 시 토스트·화면 전환이 다시 실행되는 문제

- **문제:** 화면 상태와 토스트·화면 전환을 같은 Flow로 다루어, 재구독 시 이벤트가 다시 소비되어 토스트나 전환이 중복 실행됨.
- **원인:** “상태”와 “한 번만 실행해야 할 이벤트”를 구분하지 않음.
- **해결:** 토스트 등 일회성 이벤트를 ViewEvent에서 한 곳에서 관리하도록 분리해, 소비 후 다시 쓰이지 않도록 구성.

### 3. API 키 노출 방지
앱 코드에 API 키가 포함되어 노출 위험

- **문제:** API 키가 소스에 포함되면 빌드 산출물에서 노출될 수 있음.
- **원인:** API 키를 코드 또는 리소스에 직접 작성.
- **해결:** API 키를 local.properties로 분리하고 BuildConfig 등으로만 주입해 사용. .gitignore로 local.properties 제외해 저장소에 키가 올라가지 않도록 처리.

### 4. 리스트 갱신 최적화 (ListAdapter + DiffUtil)
리스트 갱신 시 전체 갱신·깜빡임

- **문제:** RecyclerView 어댑터로 리스트를 갱신할 때 변경 구간만 반영되지 않고 전체가 다시 그려지거나 UX가 나쁨.
- **원인:** 기존 RecyclerViewAdapter 사용으로 아이템 단위 비교·갱신이 이뤄지지 않음.
- **해결:** RecyclerViewAdapter를 ListAdapter + DiffUtil로 전환해 아이템 비교 기반 갱신. BookmarkAdapter 등에도 동일 방식 적용해 갱신 시 깜빡임 완화.

### 5. ML Kit 텍스트 인식률 개선
텍스트 스캔 시 인식률이 낮거나 불안정한 문제

- **문제:** TextScanFragment에서 ML Kit 텍스트 인식 결과가 불안정하거나 인식률이 낮음.
- **원인:** 입력 이미지 해상도 부족 및 전체 프레임 사용으로 노이즈·배경 영향.
- **해결:** 텍스트 인식에 사용하는 이미지 해상도 향상. 사각형 인식 영역을 두어 관심 영역만 크롭해 인식하도록 변경해 인식률 개선.

### 6. KSP와 DataBinding 충돌
presentation 모듈에서 KSP 적용 시 빌드 에러

- **문제:** 프로젝트 전반에 KSP를 적용한 뒤 presentation 모듈에서 DataBinding 등과 충돌해 빌드 실패.
- **원인:** KSP와 DataBinding이 같은 모듈에서 함께 쓰일 때의 호환성 이슈.
- **해결:** presentation 모듈만 kapt 유지, data/domain 등 나머지 모듈은 KSP 사용. Room·Hilt는 KSP로 통일해 빌드 속도와 호환성 모두 확보.
