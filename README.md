# 📷 ObjectDetection

TensorFlow Lite 오픈소스 머신러닝 API를 이용해 카메라에 비치는 사물을 감지하고 영어 단어로 변환해 단어장을 만드는 서비스입니다. 단어장에서 단어를 검색하거나 카메라로 검색된 단어를 저장할 수 있으며, 토글 스위치로 단어의 뜻을 가렸다 보였다 하며 학습할 수 있습니다. 하루별 북마크 저장 개수에 따라 캘린더 색상이 변경됩니다.

Kotlin + Clean Architecture + MVVM 기반이며, CameraX와 TFLite를 연동해 실시간 객체 인식을 하고, Retrofit으로 사전·스프레드시트 API를 호출하며, Room으로 단어 목록을 로컬에 캐시하고, Firebase Auth·Firestore로 로그인·북마크를 관리합니다.
