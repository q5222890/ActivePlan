package com.xz.activeplan.ui.recommend.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.utils.DensityUtil;
import com.xz.activeplan.utils.Utiles;

import java.io.File;

/**
 * 添加赞助
 */
public class AddSupportActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_CODE_PICK_IMAGE = 2016;
    private TextView title;
    private ImageView iv_uploadimg;
    private LinearLayout line_imageholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_add_support);

        initView();
    }

    private void initView() {

        findViewById(R.id.id_ImageHeadTitleBlack).setOnClickListener(this);
        title = (TextView) findViewById(R.id.id_TextViewHeadTitle);
        title.setText("添加赞助");
        iv_uploadimg = (ImageView) findViewById(R.id.iv_uploadimage);
        line_imageholder = (LinearLayout) findViewById(R.id.line_imageholder);
        iv_uploadimg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_ImageHeadTitleBlack:
                finish();
                break;
            case R.id.iv_uploadimage:
                Intent intent = new Intent(AddSupportActivity.this, PhotoPickerActivity.class);
                intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_COUNT, 5);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE) {

            if (data != null) {
                String[] photoPaths = data.getStringArrayExtra(PhotoPickerActivity.INTENT_PHOTO_PATHS);

                for (String photoPath : photoPaths) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            DensityUtil.dip2px(this, 0), ViewGroup.LayoutParams.WRAP_CONTENT);
                    View view = LayoutInflater.from(this).inflate(R.layout.inflate_support, null);
                    RelativeLayout relative_support = (RelativeLayout) view.findViewById(R.id.relative_support);
                    lp.weight = 1;
                    relative_support.setLayoutParams(lp);
                    final ImageView imgThumb = (ImageView) view.findViewById(R.id.iv_support_thumbnail);
                    final ImageView imageDelete = (ImageView) view.findViewById(R.id.iv_delete);
                    imageDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            line_imageholder.removeView(line_imageholder.getChildAt(0));
                        }
                    });
                    Utiles.log("图片地址：" + photoPath);
                    File file = new File(photoPath);
                    Picasso.with(this).load(file)
                            .placeholder(R.drawable.thumb)
                            .centerCrop().fit().into(imgThumb);
                    line_imageholder.addView(view, lp);

                }
            }
        }
    }
}
