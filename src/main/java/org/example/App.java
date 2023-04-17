package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public void run() {
        Scanner sc = Container.scanner; //Container class 에 정의한 Scanner 받아오기
        List<Article> articles = new ArrayList<>(); // Article에 있는 객체를 받아온다.

        int articleLastId = 0; // 횟수 확인

        Connection conn = null; // SQLyog를 안쓰고 insert 시키기
        PreparedStatement pstmt = null; // Statement는 SQL문을 실행할 때 사용하는 인터페이스이다.

        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeBehavior=convertToNull";

            conn = DriverManager.getConnection(url, "root", "");

            String sql = "INSERT INTO article";
            sql += " SET regDate = NOW()";
            sql += ", updateDate = NOW()";
            sql += ", title = 'test3'";
            sql += ", content = 'test4'";

            pstmt = conn.prepareStatement(sql);

            int affectRows = pstmt.executeUpdate();

            System.out.println("affectRows = " +affectRows);
//            System.out.println("연결 성공");
        } catch (ClassNotFoundException e) {
            System.out.println("드라이버 로딩 실패");
        }catch (SQLException e){
            System.out.println("에러 : " + e);

        }
        finally {
            try{
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

        }
        while(true){ // 질문 무한 반복문
            System.out.printf("명령어)");
            String cmd = sc.nextLine(); // 명령어 뒤에 input 받아오기

            if(cmd.equals("등록")){
                System.out.println("== 게시물 등록 ==\n");
                System.out.println("제목 : ");
                String title = sc.nextLine(); // 제목의 input
                System.out.println("내용 : ");
                String content = sc.nextLine(); // 내용의 input

                int id = ++articleLastId; // articleLastId 가 1부터 시작하기위한 정의

                Article article = new Article(id,title,content); // article 변수에 Article 생성자의 매개변수 받아오고
                articles.add(article); // list에 넣어 둔다는 문구

                System.out.printf("등록 성공 %d번\n", id);
            }else if(cmd.equals("목록")){
                if(articles.isEmpty()){ // list가 유무 확인 문구 없을시 아래 출력이 나옴
                    System.out.println("게시물이 존재하지 않습니다.");
                    continue;// 출력후 다시 실행
                }
                System.out.println("번호 / 제목 / 내용\n");

                for (Article article : articles){ //articles(list)에서 차례대로 객체를 꺼내서 article 에다가 넣겠다
                    System.out.printf("%d / %s / %s\n",article.id,article.title,article.content); // 목록에 id,title,content 확인출력문
                }
            }else if(cmd.equals("종료")){
                System.out.println("== 시스템 종료 ==\n");
                break; // 종료시 멈춤
            }else {
                System.out.println("명령어를 확인해 주세요\n"); // 다른 글자 입력시 나오는 문구
            }
        }
    }
}
