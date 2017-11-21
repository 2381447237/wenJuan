package com.youli.huangpuwenjuantest2017_11_15;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.youli.huangpuwenjuantest2017_11_15.bean.NaireListInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 作者: zhengbin on 2017/11/15.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 */

public class NaireDetailActivity extends Activity implements View.OnClickListener{

    private boolean isTablet=true;//判断是平板，还是手机

    private Context mContext=this;
    private LinearLayout bigll;
    private Button btnStart,btnNext,btnLast,btnAll,btnRestart,btnSubmit;

    private List<NaireListInfo.DetilsBean> questionDetailsList;//问卷的信息

    private List<NaireListInfo.DetilsBean> juanInfos = new ArrayList<>();//问卷的信息

    private List<NaireListInfo.DetilsBean> questionTitleList = new ArrayList<>();//问题
    private List<NaireListInfo.DetilsBean> answerInfo = new ArrayList<>();//选项

    private int tempQuestionIndex = 0;// 用于标识上一题的题号

    private int index = 0;//问题在集合中的索引


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naire_detail);

        isTablet=isTabletDevice(this);

        questionDetailsList=((NaireListInfo)getIntent().getSerializableExtra("wenjuan")).getDetils();

        initViews();

        addTitle();
    }

    private void addTitle(){

        String number=((NaireListInfo) (getIntent().getSerializableExtra("wenjuan"))).getNO();
        String date=((NaireListInfo) (getIntent().getSerializableExtra("wenjuan"))).getCREATE_TIME().split("T")[0];
        String title=((NaireListInfo) (getIntent().getSerializableExtra("wenjuan"))).getTITLE();
        //String content=((NaireListInfo) (getIntent().getSerializableExtra("wenjuan"))).getQUESTION_TEST();
        View titleView= LayoutInflater.from(this).inflate(R.layout.question_title,null);

        TextView numberTv= (TextView) titleView.findViewById(R.id.title_tv_number);//试卷编号
        TextView numberFtv= (TextView) titleView.findViewById(R.id.title_tv_number_fianl);
        TextView nameTv= (TextView) titleView.findViewById(R.id.title_tv_name);//姓名
        TextView nameFtv= (TextView) titleView.findViewById(R.id.title_tv_name_fianl);
        TextView dateTv= (TextView) titleView.findViewById(R.id.title_tv_date);//访问日期
        TextView dateFtv= (TextView) titleView.findViewById(R.id.title_tv_date_fianl);
        TextView titleTv= (TextView) titleView.findViewById(R.id.title_tv_title);//问卷标题
        TextView contentTv= (TextView) titleView.findViewById(R.id.title_tv_content);//问卷介绍

        if(isTablet){
            numberTv.setTextSize(18);
            numberFtv.setTextSize(18);
            nameTv.setTextSize(18);
            nameFtv.setTextSize(18);
            dateTv.setTextSize(18);
            dateFtv.setTextSize(18);
            titleTv.setTextSize(19);
        }else{
            numberTv.setTextSize(14);
            numberFtv.setTextSize(14);
            nameTv.setTextSize(14);
            nameFtv.setTextSize(14);
            dateTv.setTextSize(14);
            dateFtv.setTextSize(14);
            titleTv.setTextSize(15);
        }

        numberTv.setText(number);
        nameTv.setText("张三丰");
        dateTv.setText(date);
        titleTv.setText(title);
        titleTv.setTextColor(Color.parseColor("#000000"));
        //contentTv.setText(Html.fromHtml(content));

        contentTv.setText(TitleStr.titleStr);

        bigll.addView(titleView);

    }

    private void initViews(){
        bigll = (LinearLayout) findViewById(R.id.ll);//所有问题的父布局
        btnStart= (Button) findViewById(R.id.btn_start);//开始答题
        btnStart.setOnClickListener(this);
        btnNext= (Button) findViewById(R.id.btn_next);//下一题
        btnNext.setOnClickListener(this);
        btnLast= (Button) findViewById(R.id.btn_last);//上一题
        btnLast.setOnClickListener(this);
        btnAll= (Button) findViewById(R.id.btn_all);//查看全部
        btnAll.setOnClickListener(this);
        btnRestart= (Button) findViewById(R.id.btn_restart);//重新答题
        btnRestart.setOnClickListener(this);
        btnSubmit= (Button) findViewById(R.id.btn_submit);//提交
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_start://开始答题

                startQuestion();
                btnStart.setVisibility(View.GONE);
                if(questionTitleList.size()>0) {
                    btnLast.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(mContext,"已经是最后一题了",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_last://上一题

                btnAll.setVisibility(View.GONE);

                lastQuestion();
                break;

            case R.id.btn_next://下一题

                nextQuestion();
                break;

            case R.id.btn_all://查看全部

                btnAll.setVisibility(View.GONE);
                btnLast.setVisibility(View.GONE);
                btnNext.setVisibility(View.GONE);
                btnRestart.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);

                seeAllQuestion();
                break;

            case R.id.btn_restart://重新答题

                btnLast.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.GONE);
                btnRestart.setVisibility(View.GONE);
                restartQuestion();
                break;

            case R.id.btn_submit://提交

                Toast.makeText(mContext,"提交",Toast.LENGTH_SHORT).show();
                submitAnswerInfo();
                break;
        }

    }

    private void startQuestion(){
        //开始答题
        index=0;
        if(questionDetailsList.size()>0) {
            juanInfos.addAll(questionDetailsList);
        }
        questionTitleList = getQuestionByParent();

        if(questionTitleList.size()>0){
            fretchTree(bigll, questionTitleList.get(index), "");
        }

    }

    private void lastQuestion(){
        //上一题

        if (index == 0) {
            Toast.makeText(this, "已经是第一题了", Toast.LENGTH_SHORT).show();
            return;
        }

        index--;
        if(questionTitleList.size()>0){
            fretchTree(bigll, questionTitleList.get(index), "");
        }
    }

    private void nextQuestion(){
        //下一题

        if (index >= questionTitleList.size() - 1) {
            Toast.makeText(this, "已经是最后一题了", Toast.LENGTH_SHORT).show();
            btnAll.setVisibility(View.VISIBLE);
            return;
        }

        index++;
        if(questionTitleList.size()>0){
            fretchTree(bigll, questionTitleList.get(index), "");
        }
    }

    private void submitAnswerInfo(){}//提交

    private void seeAllQuestion(){
        //查看全部
        questionTitleList = getQuestionByParent();

        bigll.removeAllViews();
        addTitle();
        for(NaireListInfo.DetilsBean info:questionTitleList){
            fretchTree(bigll,info,"all");
        }



    }

    private void restartQuestion(){
          //重新答题
        index=0;
        if(questionTitleList.size()>0){
            fretchTree(bigll, questionTitleList.get(index), "");
        }
    }

    //问题的布局
    private void fretchTree(LinearLayout layout, NaireListInfo.DetilsBean info, String isAll){

        if("".equals(isAll)){
            bigll.removeAllViews();
        }

        LinearLayout alllinearLayout=new LinearLayout(this);//整体布局（包括问题和选项）
        alllinearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams allparam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        alllinearLayout.setLayoutParams(allparam);
        LinearLayout qll=new LinearLayout(this);//问题的布局

        if(info.getTITLE_L().length()>=20) {//左边的文字长度大于等于20就换行

            qll.setOrientation(LinearLayout.VERTICAL);
        }else{

            qll.setOrientation(LinearLayout.HORIZONTAL);
        }
        TextView tvLeft = new TextView(this);//问题左边的部分

        tvLeft.setText(info.getTITLE_L());
        tvLeft.setPadding(10,0,0,0);//向右移动10xp
        tvLeft.setTextColor(Color.parseColor("#000000"));
        tvLeft.setTextSize(18);
        qll.addView(tvLeft,allparam);

        LinearLayout qRightll=new LinearLayout(this);//问题的右边布局(包括一个EditText和一个TextView)
        qRightll.setOrientation(LinearLayout.HORIZONTAL);
        if(info.isINPUT()){
            qRightll.setVisibility(View.VISIBLE);
            EditText et=new EditText(this);//问题的输入框
            et.setTextSize(15);
            if(isAll.equals("all")){
                et.setEnabled(false);
            }
            et.setPadding(0,0,0,10);
            et.setId(info.getID());
            if(!info.getTITLE_L().contains("出生")&&!info.getTITLE_L().contains("就业时间")&&!info.getTITLE_L().contains("就业的时间")&&!info.getTITLE_L().contains("什么时候")){
                if(TextUtils.equals(info.getINPUT_TYPE(),"int")){
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
                qRightll.addView(et,allparam);
            }else{

                final TextView tv=new TextView(this);//问题里面选择日期的
                if(isAll.equals("all")){
                    tv.setEnabled(false);
                }
                tv.setTextSize(15);

                tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
                tv.setTextColor(0xff538ee5);
                tv.setPadding(10,0,0,0);
                Drawable drawable= getResources().getDrawable(R.drawable.rili);
                /// 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv.setCompoundDrawables(null,null,drawable,null);

                tv.setText("请点击选择");
                tv.setId(info.getID());
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Calendar c=Calendar.getInstance();

                        new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                tv.setText(year+"年"+(month+1)+"月"+dayOfMonth+"日");

                            }
                        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();

                    }
                });


                qRightll.addView(tv,allparam);
            }


        }else{
            qRightll.setVisibility(View.GONE);
        }

        TextView tvRight = new TextView(this);//问题右边的部分
        tvRight.setText(info.getTITLE_R());
        tvRight.setTextSize(18);
        tvRight.setTextColor(Color.parseColor("#000000"));
        qRightll.addView(tvRight,allparam);

        qll.addView(qRightll,allparam);
        alllinearLayout.addView(qll,allparam);



        LinearLayout optionlinearLayout=new LinearLayout(this);//选项的布局
        optionlinearLayout.setOrientation(LinearLayout.HORIZONTAL);



        answerInfo = getAnswerByParentId(info);


        LinearLayout xuanxiangll=new LinearLayout(this);
        xuanxiangll.setOrientation(LinearLayout.VERTICAL);

        RadioGroup rg=new RadioGroup(this);
        rg.setLayoutParams(allparam);

        if(info.getTITLE_L().contains("多选")||info.getTITLE_L().contains("可以选择多个答案")){
            //多选题的布局

            List<CheckBox> checkBoxGroup=new ArrayList<CheckBox>();

            for(NaireListInfo.DetilsBean list:answerInfo){

                fretchTreeByQuestionMultiSelect(checkBoxGroup, rg,
                        list, xuanxiangll, isAll,
                        getMaxxuangxiang(info.getTITLE_L()));

            }

        }else{
            //单选题的布局

            for(NaireListInfo.DetilsBean list:answerInfo){

                fretchTreeByQuestion(rg,xuanxiangll,list,isAll);

            }

        }


        xuanxiangll.setLayoutParams(allparam);
        optionlinearLayout.addView(rg,allparam);

         optionlinearLayout.addView(xuanxiangll,allparam);
      //  optionlinearLayout.setBackgroundResource(R.drawable.gridebg);
         alllinearLayout.addView(optionlinearLayout,allparam);

        layout.addView(alllinearLayout,allparam);
    }

    private int getMaxxuangxiang(String a){

        if(a.contains("最多选")&&a.contains("项")){

            return Integer.valueOf(a.substring(a.indexOf("最多选")+3,a.indexOf("项")));

        }

        if(a.contains("不要超过")&&a.contains("个)")){

            return Integer.valueOf(a.substring(a.indexOf("不要超过")+4,a.indexOf("个)")));


        }

        if(a.contains("不要超过")&&a.contains("个）")){

            return Integer.valueOf(a.substring(a.indexOf("不要超过")+4,a.indexOf("个）")));


        }

        return 30;
    }

    //单选题的布局
    private void fretchTreeByQuestion(RadioGroup rg,LinearLayout xuanxiangll, NaireListInfo.DetilsBean info, String isAll) {

        LinearLayout linearLayout=new LinearLayout(this);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 70);
        linearLayout.setLayoutParams(llParam);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        RadioButton rb=new RadioButton(this);
        LinearLayout.LayoutParams rbParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 70);
        rb.setLayoutParams(rbParam);
        rb.setId(info.getID());

        if(isAll.equals("all")){
            rb.setEnabled(false);
        }

        rg.addView(rb);

        LinearLayout.LayoutParams textparams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 70);

        TextView tv=new TextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setText(info.getTITLE_L());
      //  tv.setTextColor(Color.parseColor("#000000"));
        linearLayout.addView(tv,textparams);

        if(info.isINPUT()) {
            EditText et = new EditText(this);
            if(isAll.equals("all")){
                et.setEnabled(false);
            }
            if(isTablet) {
                et.setPadding(0, 0, 0, 10);
            }else{
                et.setPadding(0, -20, 0, 0);
            }
            et.setText("");
            et.setId(info.getID());
            linearLayout.addView(et, textparams);
            if (TextUtils.equals(info.getINPUT_TYPE(), "int")) {
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        }

            TextView tvRight = new TextView(this);
       //     tvRight.setTextColor(Color.parseColor("#000000"));
            tvRight.setText(info.getTITLE_R());
            LinearLayout.LayoutParams tvRightParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 70);
            linearLayout.addView(tvRight, tvRightParams);


        xuanxiangll.addView(linearLayout,llParam);
    }


    //多选题选项的布局
    private void fretchTreeByQuestionMultiSelect(List<CheckBox> CheckBoxGroup,
     RadioGroup group, NaireListInfo.DetilsBean info,
      LinearLayout xuanxiangll, String isAll, int MultiSelect){

        LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 70);
        ll.setLayoutParams(llParam);

        CheckBox cb=new CheckBox(this);
        cb.setOnCheckedChangeListener(new MyOnCheckedChangeListener(
                CheckBoxGroup,MultiSelect));
        cb.setId(info.getID());
        if(isAll.equals("all")){
            cb.setEnabled(false);
        }
        group.addView(cb,llParam);

        //多选题选项的文字和输入框
        TextView tvLeft=new TextView(this);//左边的文字
        tvLeft.setText(info.getTITLE_L());
        tvLeft.setGravity(Gravity.CENTER);
        //tvLeft.setTextColor(Color.parseColor("#000000"));
        tvLeft.setLayoutParams(llParam);
        ll.addView(tvLeft,llParam);

        if(info.isINPUT()) {
            EditText et = new EditText(this);
            if(isAll.equals("all")){
                et.setEnabled(false);
            }
            if(isTablet) {
                et.setPadding(0, 0, 0, 10);
            }else{
                et.setPadding(0, -20, 0, 0);
            }
            et.setText("");
            et.setId(info.getID());
            ll.addView(et, llParam);
            if (TextUtils.equals(info.getINPUT_TYPE(), "int")) {
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            TextView tvRight = new TextView(this);
            tvRight.setGravity(Gravity.CENTER);
            //tvRight.setTextColor(Color.parseColor("#000000"));
            tvRight.setText(info.getTITLE_R());
            ll.addView(tvRight,llParam);
        }
        xuanxiangll.addView(ll,llParam);
    }

    private List<NaireListInfo.DetilsBean> getQuestionByParent(){
        //从问卷信息中找出问题的信息
        List<NaireListInfo.DetilsBean> questionInfos = new ArrayList<>();

        for(NaireListInfo.DetilsBean detailsList : juanInfos){
            if(detailsList.getPARENT_ID()==0){
                questionInfos.add(detailsList);
            }
        }
        return questionInfos;
    }

    private List<NaireListInfo.DetilsBean> getAnswerByParentId(NaireListInfo.DetilsBean info){
        //用问题的信息得到选项的信息
        List<NaireListInfo.DetilsBean> aInfos = new ArrayList<>();

        for(NaireListInfo.DetilsBean list:juanInfos){

            if(list.getPARENT_ID()==info.getID()){
                aInfos.add(list);
            }

        }

        return aInfos;
    }


    //复选框的监听
    public class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        int _MultiSelect;
        List<CheckBox> _group;

        public MyOnCheckedChangeListener(List<CheckBox> _group, int _MultiSelect) {
            this._group = _group;
            this._MultiSelect = _MultiSelect;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(isChecked){

                if(_group.size()>_MultiSelect-1){
                    buttonView.setChecked(false);
                    Toast.makeText(mContext, "最多选" + _MultiSelect + "项",
                            Toast.LENGTH_SHORT).show();
                }else{
                    _group.add((CheckBox)buttonView);
                }

            }else{

              CheckBox _check_box=(CheckBox) buttonView;

                if(_group.contains(_check_box)){
                    _group.remove(_check_box);
                };


            }

        }
    }


    /**
     * 判断是否平板设备
     * @param context
     * @return true:平板,false:手机
     */
    private boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
