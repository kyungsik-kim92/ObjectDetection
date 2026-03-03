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

### 1. 인식률 개선 (ML Kit 텍스트 인식)
텍스트 스캔 시 인식률이 낮거나 불안정한 문제

- **문제:** 전체 프레임을 입력으로 쓰면 배경·노이즈 영향으로 인식률 저하.
- **원인:** 관심 영역 없이 전체 이미지를 사용해 불필요한 정보가 함께 입력됨.
- **해결:** 사각형 인식 영역을 두어 관심 영역만 크롭해 인식하도록 변경해 인식률 개선.

### 2. 단어 목록 로컬 캐시
단어 목록 조회 시마다 API 호출로 인한 지연·오프라인 미지원

- **문제:** 단어 검색·필터 시마다 Sheet API를 호출하면 응답 지연이 있고, 오프라인에서 목록을 쓸 수 없음.
- **원인:** 단어 목록의 소스가 원격 API뿐인 구조.
- **해결:** 앱 시작 시(스플래시) Sheet API로 단어 목록을 받아 로컬 DB(Room)에 캐시하고, 검색·필터는 이 DB만 사용해 응답 속도와 오프라인 대응을 확보함.

### 3. bundleOf → Safe Args 마이그레이션
화면 간 인자 전달 시 타입·키 오타로 인한 런타임 오류 가능성

- **문제:** bundleOf로 데이터를 넘기면 키 오타·타입 불일치 시 런타임에서만 드러남.
- **원인:** 수동 Bundle 구성에 의존하는 구조.
- **해결:** Navigation Safe Args로 전환해 화면 간 인자를 타입 안전하게 전달하고 유지보수성을 높임.

### 4. 북마크 화면과 단어 상세 간 상태 동기화
북마크 탭에서 단어 상세로 진입 후 해제 시 목록에 반영되지 않는 문제

- **문제:** 북마크 화면에서 단어를 눌러 상세로 들어간 뒤, 상세에서 북마크 해제를 해도 북마크 목록이 갱신되지 않음.
- **원인:** 단어 상세에서 북마크 삭제 시 북마크 탭의 목록을 갱신하는 흐름이 없었음.
- **해결:** WordDetailFragment에 HomeViewModel을 주입하고, 북마크 해제 시 해당 ViewModel을 통해 삭제 후 일회성 이벤트(observeEvents)로 북마크 탭에 알려 목록이 갱신되도록 함.

### 5. 리스트 갱신 최적화 (ListAdapter + DiffUtil)
리스트 갱신 시 전체 갱신·깜빡임

- **문제:** RecyclerView 어댑터로 리스트를 갱신할 때 변경 구간만 반영되지 않고 전체가 다시 그려지거나 UX가 나쁨.
- **원인:** 기존 RecyclerViewAdapter 사용으로 아이템 단위 비교·갱신이 이뤄지지 않음.
- **해결:** RecyclerViewAdapter를 ListAdapter + DiffUtil로 전환해 아이템 비교 기반 갱신. BookmarkAdapter 등에도 동일 방식 적용해 갱신 시 깜빡임 완화.
