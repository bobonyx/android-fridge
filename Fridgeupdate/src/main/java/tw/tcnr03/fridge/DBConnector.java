package tw.tcnr03.fridge;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DBConnector {
    public static int httpstate = 0;
    //--------------------------------------------------------
    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static OkHttpClient client = new OkHttpClient();
    //---------------------------------------------------------
//-------000webhost 柏昇-------
//static String connect_ip = "https://tcnr2021a03.000webhostapp.com/android_mysql_connect/android_test.php";
    //-----hostinger 第一組-----
    static String connect_ip = "https://jim.tcnrcloud110a.com/android_mysql_connect/android_test.php";

    //----------------------------------------------------------------------------------------
    public static String executeQuery_fri(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","query")
                .add("query_string", query_0)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        // ===========================================
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        // ===========================================
        try (Response response = client.newCall(request).execute()) {
            httpstate = response.code();
            // ===========================================
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeInsert_fri(ArrayList<String> query_string) {
//        //--*****檢查Mysql 使否僅剩下一筆-----
//        int record_only = 0;  //0 > 1
//        String sql1 = "SELECT * FROM member ORDER BY id ASC";
////        ArrayList<String> ValuePairs = new ArrayList<>();
////        ValuePairs.add(sql1);
//        try {
//            ArrayList<String> ValuePairs = new ArrayList<>();
//            ValuePairs.add(sql1);
////            executeQuery(ArrayList<ValuePairs);
//            JSONArray jsonArray = new JSONArray(result);
//            // -------------------------------------------------------
//            if (jsonArray.length() < 2) { // MySQL 連結成功有資料
//                record_only =1;
//            }else{
//                record_only =0;
//            }
//        }catch (Exception e){
//
//        }
///  -- 這裡加　判斷　record_only　＝０　才刪除
        //        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","insert")
                .add("name", query_0)
                .add("dates", query_1)
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

    public static String executeDelet_fri(ArrayList<String> query_string) {
        //        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","delete")
                .add("id", query_0)
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

    //---更新資料--------------------------------------------------------------
    public static String executeUpdate_fri(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","update")
                .add("id", query_0)
                .add("name", query_1)
                .add("dates", query_2)
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
//==========================//==========================
}