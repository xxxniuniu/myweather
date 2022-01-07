package com.example.finalwork.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.finalwork.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecommendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecommendFragment extends Fragment {
    private View view;
    private EditText ed_url;
    private ImageView img_url;
    private ImageView img_day_pic;
    private Button btn_url;
    private Button btn_day_pic;
    private String urlPic="https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=5&mkt=zh-CN";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecommendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecommendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecommendFragment newInstance(String param1, String param2) {
        RecommendFragment fragment = new RecommendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recommend, container, false);
        ed_url = view.findViewById(R.id.ed_url);
        ed_url.setText("www.hznu.edu.cn");
        btn_url = view.findViewById(R.id.btn_url);
        btn_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ed_url.getText().toString();
                createQRcodeImage(url);
            }
        });

        img_day_pic = view.findViewById(R.id.img_day_pic);
        btn_day_pic = view.findViewById(R.id.btn_day_pic);
        btn_day_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendHttpRequest(urlPic,cbPic);
            }
        });
        return view;
    }

    public void createQRcodeImage(String url)
    {
        img_url = view.findViewById(R.id.img_url);
        int w = img_url.getWidth();
        int h = img_url.getHeight();
        try
        {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1)
            {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, w, h, hints);
            int[] pixels = new int[w * h];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < h; y++)
            {
                for (int x = 0; x < w; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * w + x] = 0xff000000;
                    }
                    else
                    {
                        pixels[y * w + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            //显示到我们的ImageView上面
            img_url.setImageBitmap(bitmap);
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
    }
    private void sendHttpRequest(String strUrl,okhttp3.Callback cb)
    {
        try {
            //Log.i("weather","exx");
            OkHttpClient client = new OkHttpClient();
            Request req = new Request.Builder().url(strUrl).build();
            client.newCall(req).enqueue(cb);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private okhttp3.Callback cbPic=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.i("picture",e.toString());
        }


        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                String urlPicture = response.body().string();
                JSONObject jsonObject = new JSONObject(urlPicture);
                //Log.d("testF","array:"+jsonArray);
                String images = jsonObject.optString("images",null);
                JSONArray jsonArray = new JSONArray(images);
                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                String url = jsonObject1.optString("url",null);
                Log.d("picture",url);
                String allurl = "http://www.bing.com" + url;
                Bitmap bmp = getURLimage(allurl);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                System.out.println("000");
                handle.sendMessage(msg);
                //showResponse("p");
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    //在消息队列中实现对控件的更改
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");
                    Bitmap bmp=(Bitmap)msg.obj;
                    img_day_pic.setImageBitmap(bmp);
                    break;
            }
        };
    };
    //加载图片
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);//读取图像数据
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}