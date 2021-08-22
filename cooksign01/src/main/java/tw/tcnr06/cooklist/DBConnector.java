package tw.tcnr06.cooklist;


import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DBConnector {

    //--------------------------------------------------------
    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static OkHttpClient client = new OkHttpClient();
//---------------------------------------------------------
// -------HOSTING-oldpa----
//static String connect_ip = "https://oldpa88.com/android_mysql_connect/android_connect_db.php";
//-------000webhost 維尼-------
//static String connect_ip = "https://tcnr2021a06.000webhostapp.com/android_mysql_connect/android_connect_db.php";

    //測試網址
//static String connect_ip = "https://tcnr2021a06.000webhostapp.com/android_mysql_connect/android_insert_db.php?selefunc_string=insert&name=測試06&grp=D&address=888.88,999.99";
//    static String connect_ip = "https://tcnr2021a06.000webhostapp.com/android_mysql_connect/android_insert_db.php";
    static String connect_ip = "https://tcnr2021a06.000webhostapp.com/android_cook/android_cook_insert_db.php";



    //----------------------------------------------------------------------------------------
    public static String executeQuery(ArrayList<String> query_string) {//Mysql查詢 標準語法
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------建立一個封包
        String query_0 = query_string.get(0);//外面帶參數
        //可寫  String query_0 = "SELECT * FROM......."
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","query")
                .add("query_string", query_0)
                .build();
//--------------丟出封包
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeInsert(ArrayList<String> query_string) {
        //        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","insert")
                .add("title", query_0)
                .add("recipe_text", query_1)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
//==========================
}//DBConnector END