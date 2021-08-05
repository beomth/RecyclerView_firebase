package com.example.firebaseexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능강화
        layoutManager = new LinearLayoutManager(this); // 레이아웃 매니저 설정
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // 우저 객체를 담을 어레이 리스트 (어댑터 쪽으로 날릴려고)

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("User"); //데이터베이서 네임 테이블
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 유저객체를 담았던 어레이 리스트, 기존 배열리스트가 존재하지 않게 초기화
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){ // 반복문으로 데이터 리스트를 추출해냄
                    User user = dataSnapshot.getValue(User.class); //만들어뒀던 User 객체에 데이터를 담는다
                    arrayList.add(user); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비


                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //db를 가져오던 중 에러 발생 시
                Log.e("MainACtivity", "에러발생");
            }
        });

        adapter = new CustomAdapter(arrayList, this); // 생성자로부터 값을 받음
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
    }
}