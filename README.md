세종대학교 모바일 프로그래밍 프로젝트 기술정리
==============================================

프로젝트를 진행하면서 겪었던 기술들에 대한 정리를 기록할 예정입니다.
--------------------------------------------------------------------

### 기술관련

*	안태현
	*	액션바 없애기
	```
	ActionBar actionBar = getSupportActionBar();
	actionBar.hide();
	```

	-	imageView 에서 이미지 꽉 차게 하는법
	```
	src로 경로를 지정하는 대신 background 속성을 이용하면 됨
	```

	-	colorCode - webCode
	```
	Color.parseColor("color WEBCODE")
	ex) Color.parseColor("#FFFFFF")
	단, minSDK 21 이상
	```

	- Android StatusBar Color (MinSDK 21)
	```
	getWindow().setStatusBarColor(Color.parseColor("E37FA8"));
	```

	-	[TableLayout](http://recipes4dev.tistory.com/138)
	```
	TableLayout - TableLow 구조를 가짐
	TableLow(행) 태그 안에 추가되는 요소들의 갯수(열) 구조
	```

	-	안드로이드 이미지 관련 이슈
	```
	각 TableLayout 안에 있는 ImageView에 이미지를 설정하니맨 위의 이미지 뷰가 아래쪽의 이미지뷰를 전부 침범하는 문제가 발생
	이방법 저방법 다 써보다가 결국 layout_height=150dp로 설정
	그래도 상위 TableRow의 비율이 설정되어 있어서인지 각 핸드폰마다의 비율은 일정하게 설정됨
	```

	-	안드로이드 View 그림자 효과
	```
	android:elevation="dp"
	android:translationZ="1dp"
	```

	-	[엘리베이션과 그림자](http://davidhyk.github.io/google-design-ko/what-is-material/elevation-shadows.html#)

	-	슬라이드 애니메이션 (페이지)

	```
	fromXDelta 는 출발지, toXDelta는 목적지 0, 100의 값으로 넣어주면 된다
	(오른쪽에서 나오게 할 수도 있음)

	res/anim/trans_left.xmln
	<?xml version="1.0" encoding="utf-8"?>
	<set xmlns:android="http://schemas.android.com/apk/res/android"
	    android:interpolator="@android:anim/accelerate_decelerate_interpolator">
	    <translate
	        android:fromXDelta="-100%p"
	        android:toXDelta="0%p"
	        android:duration="500"
	        android:repeatCount="0"
	        android:fillAfter="true"
	        />
	</set>

	JAVA  
	Animation 객체 2개  
	이벤트 동작시킬 Button 1개  
	슬라이드 애니메이션으로 띄울 LinearLayout 1개

	// 버튼 이벤트
	BUTTON.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                if (isPageOpen) {
	                    mainLayout.startAnimation(translateRightAnim);
	                } else {
	                    mainLayout.setVisibility(View.VISIBLE);
	                    mainLayout.startAnimation(translateLeftAnim);
	                }
	            }
	        });

	// 애니메이션 객체 로딩
	translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_left);
	translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_right);

	SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
	translateLeftAnim.setAnimationListener(animListener);
	translateRightAnim.setAnimationListener(animListener);

	// 슬라이딩 페이지 애니메이션 리스너 선언
	private class SlidingPageAnimationListener implements Animation.AnimationListener {
	        public void onAnimationEnd(Animation animation) {
	            if (isPageOpen) {
	                mainLayout.setVisibility(View.INVISIBLE);
	                isPageOpen = false;
	            } else {
	                isPageOpen = true;
	            }
	        }

	        public void onAnimationRepeat(Animation animation) {}
	        public void onAnimationStart(Animation animation) {}
	    }

	XML  
	루트 Layout은 FrameLayout으로 선언  
	슬라이드로 띄울 Layout은 아래와같이 선언

	<LinearLayout
	    android:id="@+id/layout_myPage"
	    android:layout_width="200dp"
	    android:layout_height="match_parent"
	    android:orientation="vertical"
	    android:layout_gravity="left"
	    android:background="#FFFFFFFF"
	    android:visibility="gone">
	</LinearLayout>
	```
	* [Unable to merge dex](https://stackoverflow.com/questions/46053902/dex-error-on-android-studio-3-0-beta4)
	```
	Build-Clean Project
	Build-Rebuild Project
	```

	* Layout 투명하게 하기
	```
	onCreate에서 LinearLayout 선언 후
	LinearLayout.setAlpha(0.4f);
	```

	* 라운드 처리
	```
	rounded.xml
	<?xml version="1.0" encoding="UTF-8"?>
	<shape xmlns:android="http://schemas.android.com/apk/res/android">
	    <solid android:color="#FFFFFF"/>
	    <stroke android:width="3dip" android:color="#FFFFFF" />
	    <corners android:radius="10dip"/>
	    <padding android:left="0dip" android:top="0dip" android:right="0dip" android:bottom="0dip" />
	</shape>

	라운드처리할 객체에 background로 지정
	```
