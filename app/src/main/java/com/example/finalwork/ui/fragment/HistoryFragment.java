package com.example.finalwork.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.finalwork.R;
import com.example.finalwork.common.db.UserDatabase;
import com.example.finalwork.common.db.history.HistoryLog;
import com.example.finalwork.common.shared.SharedViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private UserDatabase userDatabase;
    private String username;
    private List<HistoryLog> historyLogs;
    // 历史记录城市名列表
    private List<String> all_city_in_history;
    private List<String> week_tem;
    private List<String> week_date;
    private LineChartView lineChart;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private Spinner spinner;
    private TextView tv_spinner;
    private ArrayAdapter<String> adapter;
    private String[] cityname;
    private SharedViewModel viewModelProvider_receive;

 /*   private [] tt;*/
    private int citynum;
    private String getnowcity;
    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        view = inflater.inflate(R.layout.fragment_history, container, false);
        spinner = view.findViewById(R.id.spinner);
        tv_spinner = view.findViewById(R.id.tv_spinner);


        lineChart = view.findViewById(R.id.line_chart);
        userDatabase = UserDatabase.getInstance(getActivity());
        username = userDatabase.getUserDao().getUsernameByLogin();
        citynum = userDatabase.getUserDao().getCitynumByUsername(username);
        all_city_in_history = userDatabase.getHistoryDao().getAllCityInHistoryByUsernameLimitCitynum(username,citynum);
        viewModelProvider_receive = new ViewModelProvider(Objects.requireNonNull(getActivity()),new ViewModelProvider.NewInstanceFactory()).get(SharedViewModel.class);
        viewModelProvider_receive.getWhichCity().observe(getViewLifecycleOwner(), new Observer<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(String s) {
                    String [] newcityname;
                    all_city_in_history = userDatabase.getHistoryDao().getAllCityInHistoryByUsernameLimitCitynum(username,citynum);
                    cityname = all_city_in_history.toArray(new String[all_city_in_history.size()]);
                    //将可选内容与ArrayAdapter连接起来
                    adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,cityname);

                    //设置下拉列表的风格
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    //将adapter 添加到spinner中
                    spinner.setAdapter(adapter);
                    //添加事件Spinner事件监听
                    spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

                    //设置默认值
                    spinner.setVisibility(View.VISIBLE);

            }
        });
        viewModelProvider_receive.getNowFocusCity().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                String [] newcityname;
                all_city_in_history = userDatabase.getHistoryDao().getAllCityInHistoryByUsernameLimitCitynum(username,citynum);
                cityname = all_city_in_history.toArray(new String[all_city_in_history.size()]);
                //将可选内容与ArrayAdapter连接起来
                adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,cityname);

                //设置下拉列表的风格
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //将adapter 添加到spinner中
                spinner.setAdapter(adapter);
                //添加事件Spinner事件监听
                spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

                //设置默认值
                spinner.setVisibility(View.VISIBLE);
            }
        });
        if (all_city_in_history.size() > 0){
            cityname = all_city_in_history.toArray(new String[all_city_in_history.size()]);
            //将可选内容与ArrayAdapter连接起来
            adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,cityname);

            //设置下拉列表的风格
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //将adapter 添加到spinner中
            spinner.setAdapter(adapter);
            //添加事件Spinner事件监听
            spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

            //设置默认值
            spinner.setVisibility(View.VISIBLE);
        }
        return view;
    }

    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables(){
        for (int i = 0; i < week_date.size(); i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(week_date.get(i).substring(0,week_date.get(i).length()-5)));
        };
    }    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        for (int i = 0; i < week_tem.size(); i++) {
            String tem = week_tem.get(i).substring(0,week_tem.get(i).length()-1);
            int t = convertToInt(tem,10);
//            int t = convertToInt(tem,0);
//            tt[i] = t;
            mPointValues.add(new PointValue(i, t));
        }

    }
    private void initLineChart () {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("未来七天气温走势");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
//        mAxisXValues.clear();
//        mPointValues.clear();
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        axisY.setTextColor(Color.BLACK);
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);
    }

    //把String转化为double
    public static double convertToDouble(String number, double defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(number);
        } catch (Exception e) {
            return defaultValue;
        }

    }

    //把String转化为int
    public static int convertToInt(String number, int defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(number);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            tv_spinner.setText("您要浏览的城市是："+cityname[arg2]);
            getnowcity = cityname[arg2];
//            week_date.clear();
//            week_tem.clear();
            mAxisXValues.clear();
            mPointValues.clear();
            week_tem = userDatabase.getHistoryDao().getTemByUsernameAndCity(username,getnowcity);
            week_date = userDatabase.getHistoryDao().getDateByUsernameAndCity(username,getnowcity);
            // 绘图
            getAxisXLables();//获取x轴的标注
            getAxisPoints();//获取坐标点
            initLineChart();//初始化

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

}