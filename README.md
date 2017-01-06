# methodstream
안드로이드 메서드 관리 라이브러리
안드로이드 MVP 패턴을 좀 더 추상화 하기 위해 만들어진 라이브러리로
함수형 프로그래밍에서 영감을 얻어 메서드를 관리할 수 있도록 도와줌

장점으로 onAcitivityResult를 사용하지 않아도됨
그리고 여러 방면으로 사용가능

#사용예제

https://github.com/roka88/methodStreamExample


#Gradle

<pre>
repositories {
    maven { url "https://jitpack.io" }
}
</pre>

<pre>
dependencies {
    compile 'com.github.roka88:methodstream:0.0.3'
}
</pre>

#Version
<pre>
0.0.4 메서드 실행 오류시, 로그 메세지 출력 변경
0.0.3 메서드 실행 오류시, 로그 안뜨는 문제 해결
0.0.2 관련 클래스 캡슐화 및 run() 연속 실행 시 생기는 오류 해결
0.0.1 초기버전
</pre>

#사용법 1
<pre>

private RokaMethodStream.Procedure mShowToast = () -> {
    Toast.makeText(this, "테스트입니다", Toast.LENGTH_SHORT).show();
};

// 매서드 스트림에 메서드를 추가
RokaMethodStream.init().attach(mShowToast, "showToast");

// 메서드 스트림 실행
RokaMethodStream.init().run(null, "showToast");

</pre>

#사용법 2
<pre>
private RokaMethodStream.Func mInputJsonData = (Object obj) -> {
    if (obj instanceof JSONObject) {
        JSONObject jsonObj = (JSONObject)obj;
        mView.setText(jsonObj.getString("name");
    }
};

// 매서드 스트림에 메서드를 추가
RokaMethodStream.init().attach(mInputJsonData, "input");

// 메서드 스트림 실행
RokaMethodStream.init().run(jsonObject, "input");


// 메서드 삭제
RokaMethodStream.init().detach("input");

</pre>

#사용법 3
<pre>

// 메서드를 넣고 바로 실행가능.
RokaMethodStream.init().attach(mInputJsonData, "input").run(jsonObject, "input");


// 연달아 실행 가능
RokaMethodStream.init().attach(mInputJsonData, "input").syncRun(jsonObject, "input").syncRun(jsonObject, "input");
RokaMethodStream.init().attach(mInputJsonData, "input").run(jsonObject, "input").syncRun(jsonObject, "input");

// 메서드 삭제
RokaMethodStream.init().detach("input");

</pre>



#주의사항
<pre>
1. 메서드를 attach 후 호출할 key가 필요함.
2. 메서드를 실행시키는 방법은 두가지가 있음
 1) run(obj, key) 비동기
 2) syncRun(obj, key) 동기
3. run 내부에는 thread를 실행시키므로, 같은 메서드를 동시에 호출시 메서드 내부에 syncronized를 넣어 해결해야함
4. syncRun은 동기 실행으로 호출 순서에 따라 메서드가 실행됨
5. run 내부에 핸들러가 있어서 View를 변경가능
6. RokaMethodStream.Func와 RokaMethodStream.Procedure의 차이는 파라미터로 데이터가 있나 없나의 차이
7. RokaMethodStream.Procedure를 attach후 실행시에는 run(null, "key") 또는 syncRun(null, "key") 호
</pre>


#Interface
<pre>
public interface Func {
    void func(Object data) throws Exception;
}

public interface Procedure {
    void proc() throws Exception;
}
</pre>


#License
<pre>
Copyright 2016 Roka

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
