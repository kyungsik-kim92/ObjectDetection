# 📷 ObjectDetection
TensorFlow Lite 오픈소스 머신러닝 API를 이용해 카메라에 비치는 사물을 감지하고 영어 단어로 변환해 단어장을 만드는 Android 앱입니다. 단어장에서 단어를 검색하거나 카메라로 검색된 단어를 저장할 수 있으며, 토글 스위치로 단어의 뜻을 가렸다 보였다 하며 학습할 수 있습니다. 하루별 북마크 저장 개수에 따라 캘린더 색상이 변경됩니다.Kotlin + Clean Architecture + MVVM 기반으로, TFLite와 CameraX로 실시간 객체 인식을 하고, Room으로 단어 목록을 로컬에 캐시하며, Firebase Auth·Firestore로 로그인과 북마크를 관리합니다.


## 주요 기능

- **객체 인식 단어 검색** — TensorFlow Lite Object Detection(efficientdet-lite0)과 CameraX 연동으로 카메라 이미지를 해석·분류한 뒤, 선택한 항목으로 단어 뜻 조회
- **텍스트 스캔** — ML Kit 텍스트 인식으로 이미지에서 글자 추출 후 단어 검색
- **단어 검색·상세** — Google SpreadSheet를 JSON API로 받아 단어 목록 제공, Room으로 로컬 캐시. Dictionary API로 단어 뜻·발음 조회
- **즐겨찾기** — Firebase Firestore로 단어 추가·삭제, 북마크 목록 조회
- **학습 모드** — 토글 스위치로 단어 뜻 가리기/보기
- **캘린더** — 마이페이지에서 Material CalendarView로 날짜별 표시, 그날 저장한 북마크 개수에 따라 색상 변경
- **계정** — Firebase Auth 로그인, 회원가입, 회원탈퇴
