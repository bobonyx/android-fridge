//package tw.tcnr01.m1416;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class DBConnector {
//    //--------------------------------------------------------
//    private static String postUrl;
//    private static String myResponse;
//    static String result = null;
//    private static OkHttpClient client = new OkHttpClient();
////---------------------------------------------------------
////-------000webhost oldpa-------
////    static String connect_ip="https://olda88.000webhotapp.com/android_mysql_connect/android_connect_db.php";
////-------------聖寰---------------------
//    static String connect_ip ="https://tcnr2021a01.000webhostapp.com/android_mysql_connect/android_connect_db.php";
//
//
//    //----------------------------------------------------------------------------------------
//    public static String executeQuery(ArrayList<String> query_string) {//mysql查詢語法
////        OkHttpClient client = new OkHttpClient();
//        postUrl=connect_ip ;
//        //--------------
//        String query_0 = query_string.get(0);
////        String query_0 = "SELECT * FROM XXXX where name=test and";語法可直接寫
//        FormBody body = new FormBody.Builder()
//                .add("selefunc_string","query")
//                .add("query_string", query_0)
//                .build();
////--------------
//        Request request = new Request.Builder()
//                .url(postUrl)
//                .post(body)
//                .build();
//        try (Response response = client.newCall(request).execute()) {
//            return response.body().string();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
////==========================
//}

package tw.tcnr01.mytrashcar;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DBConnectorgars01 {
    public static int httpstate=0;
    //--------------------------------------------------------
    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static OkHttpClient client = new OkHttpClient();

//---------------------------------------------------------

    //-------000webhost oldpa-------
//    static String connect_ip = "https://tcnr2021a01.000webhostapp.com/android_mysql_connect/android_connect_db.php";
//    static  String connect_ip="https://tcnr2021a01.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";


    //-------000webhost oldpa-------
//    static String connect_ip = "https://olda88.000webhotapp.com/android_mysql_connect/android_connect_db.php%22;
//班長-------000webhost 維尼-------
//    static String connect_ip = "https://tcnr2021a06.000webhostapp.com/android_mysql_connect/android_insert_db.php";
//副班長-------000webhost ++-------
    static String connect_ip = "https://tcnr2021a01.000webhostapp.com/android_mysql_connect/android_connect_db_car.php";

//學藝
//    static String connect_ip = "https://tcnr2021a13.000webhostapp.com/android_mysql_connect/android_insert_db.php";
//        static String connect_ip = "https://tcnr2021a13.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";
//02 晨霖
//    static String connect_ip = "https://tcnr2021a02.000webhostapp.com/android_mysql_connect/android_insert_db.php";
//05  育誠
//    static String connect_ip = "https://tcnr2021a05.000webhostapp.com/android_mysql_connect/android_insert_db.php";


    //----------------------------------------------------------------------------------------
    public static String executeQuery(ArrayList<String> query_string) { //MYSQL查詢語法
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip;
        //--------------
        String query_0 = query_string.get(0);
        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "query")
                .add("query_string", query_0)
                .build();
        //--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
//--------------
//        Request request = new Request.Builder()
//                .url(postUrl)
//                .post(body)
//                .build();
//        try (Response response = client.newCall(request).execute()) {
//            return response.body().string();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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

    public static String executeInsert(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","insert")
                .add("name", query_0)
                .add("grp", query_1)
                .add("address", query_2)
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
        public static String executeUpdate(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
            postUrl=connect_ip ;
            //--------------
            String query_0 = query_string.get(0);
            String query_1 = query_string.get(1);
            String query_2 = query_string.get(2);
            String query_3 = query_string.get(3);

            FormBody body = new FormBody.Builder()
                    .add("selefunc_string","update")
                    .add("id", query_0)
                    .add("name", query_1)
                    .add("grp", query_2)
                    .add("address", query_3)
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

    public static String executeDelet(ArrayList<String> query_string) {
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
}
//==========================