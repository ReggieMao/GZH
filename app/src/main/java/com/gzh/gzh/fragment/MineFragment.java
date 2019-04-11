package com.gzh.gzh.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gzh.gzh.R;
import com.gzh.gzh.activity.AboutUsActivity;
import com.gzh.gzh.activity.Base1Activity;
import com.gzh.gzh.activity.HelpCenterActivity;
import com.gzh.gzh.activity.MessageActivity;
import com.gzh.gzh.activity.SettingActivity;
import com.gzh.gzh.view.CircleImageView;
import com.gzh.library.api.GZHApi;
import com.gzh.library.constant.ConstantCode;
import com.gzh.library.constant.Event;
import com.gzh.library.pojo.Market;
import com.gzh.library.pojo.UserInfo;
import com.gzh.library.util.CheckUtil;
import com.gzh.library.util.DialogUtil;
import com.gzh.library.util.FileUtil;
import com.gzh.library.util.ImageLoadUtil;
import com.gzh.library.util.ToastUtil;
import com.gzh.library.util.UserPreference;
import com.gzh.library.util.Util;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;

/**
 * Created by MaoLJ on 2018/7/23.
 * 我的页面
 */

public class MineFragment extends Base1Fragment {

    private static final String TAG = "MineFragment";
    private Base1Activity mBaseActivity;
    private int mPosition = -1;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_uid)
    TextView mTvUid;
    @Bind(R.id.tv_unread)
    TextView mTvUnread;
    @Bind(R.id.tv_state)
    TextView mTvState;
    @Bind(R.id.et_name)
    EditText mEtName;
    @Bind(R.id.img_avatar)
    CircleImageView mImgAvatar;
    @Bind(R.id.pb)
    ProgressBar mPb;
    private int mAllUnread;
    private boolean mFinishDone = true;
    private String mNetPath;
    private Bitmap mNewBitmap;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView() {

    }

    @Override
    public void onChangeFragment(int position) {
        super.onChangeFragment(position);
        if (mPosition == -1) {
            mPosition = position;
            initViewDelayedLoading();
        }
    }

    private void initViewDelayedLoading() {
        mBaseActivity = (Base1Activity) getActivity();
        CheckUtil.setNickname(mEtName);
//        mEtName.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                int index = mEtName.getSelectionStart() - 1;
//                if (index > 0) {
//                    if (Util.isEmojiCharacter(s.charAt(index))) {
//                        Editable edit = mEtName.getText();
//                        edit.delete(s.length() - 2, s.length());
//                        ToastUtil.toast(getContext(), "不支持输入表情符号");
//                    }
//                }
//
//            }
//        });

        getUserInfo();
        mEtName.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final Rect rect = new Rect();
                getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                final int screenHeight = getActivity().getWindow().getDecorView().getRootView().getHeight();
                final int heightDifference = screenHeight - rect.bottom;
                boolean visible = heightDifference > screenHeight / 3;
                if (!visible) {
                    mEtName.setVisibility(View.GONE);
                    mTvName.setVisibility(View.VISIBLE);
                    mTvState.setText(getString(R.string.update));
                    mFinishDone = true;
                } else {
                    mEtName.setVisibility(View.VISIBLE);
                    mTvName.setVisibility(View.GONE);
                    mTvState.setText(getString(R.string.done));
                    mFinishDone = false;
                }
            }
        });
        initDataDelayedLoading();
    }

    private void initDataDelayedLoading() {
        mBaseActivity.rxManage.on(Event.NOTICE_UNREAD, new Action1() {
            @Override
            public void call(Object o) {
                mTvUnread.setVisibility(View.VISIBLE);
                mTvUnread.setText((mAllUnread + 1) + "");
            }
        });

        mBaseActivity.rxManage.on(Event.FIND_NEWS_LIST2, new Action1<Market>() {
            @Override
            public void call(Market o) {
                mAllUnread = o.getUnreadNum();
                if (mAllUnread == 0)
                    mTvUnread.setVisibility(View.GONE);
                else {
                    mTvUnread.setVisibility(View.VISIBLE);
                    mTvUnread.setText(mAllUnread + "");
                }
            }
        });

        mBaseActivity.rxManage.on(Event.GET_USER_INFO, new Action1<UserInfo>() {
            @Override
            public void call(UserInfo o) {
                mTvName.setText(o.getNickName());
                mTvUid.setText("UID: " + o.getUserId());
                mNetPath = Util.isEmpty(o.getPortraitImagUrl()) ? "no image" : o.getPortraitImagUrl();
                if (!Util.isEmpty(o.getPortraitImagUrl())) {
                    if (!o.getPortraitImagUrl().equals("no image")) {
                        if (FileUtil.fileIsExists(getActivity())) {
                            FileInputStream fs = null;
                            try {
                                fs = new FileInputStream(FileUtil.getUploadPath(getActivity()) + "avatar.jpg");
                                Bitmap bitmap = BitmapFactory.decodeStream(fs);
                                mImgAvatar.setImageBitmap(bitmap);
                                ImageLoadUtil.loadServiceRoundImg(mImgAvatar, o.getPortraitImagUrl(), 0);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                ImageLoadUtil.loadServiceRoundImg(mImgAvatar, o.getPortraitImagUrl(), 0);
                            }
                        } else {
                            ImageLoadUtil.loadServiceRoundImg(mImgAvatar, o.getPortraitImagUrl(), 0);
                        }
                    } else {
                        mImgAvatar.setImageResource(R.mipmap.avatar);
                    }
                } else {
                    mImgAvatar.setImageResource(R.mipmap.avatar);
                }
            }
        });

        mBaseActivity.rxManage.on(Event.UPDATE_NAME, new Action1() {
            @Override
            public void call(Object o) {
                ToastUtil.toast(getActivity(), getString(R.string.toast_update_name1));
                InputMethodManager imm = (InputMethodManager) mEtName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(mEtName.getWindowToken(),0);
                }
                mTvName.setVisibility(View.VISIBLE);
                mTvName.setText(mEtName.getText().toString());
                mEtName.setVisibility(View.GONE);
                mTvState.setText(getString(R.string.update));
                mFinishDone = true;
            }
        });

        mBaseActivity.rxManage.on(Event.UPDATE_AVATAR, new Action1() {
            @Override
            public void call(Object o) {
                ImageLoadUtil.loadServiceImg(mImgAvatar, mNetPath, R.mipmap.avatar);
                mImgAvatar.setImageBitmap(mNewBitmap);
                mPb.setVisibility(View.GONE);
                ToastUtil.toast(getActivity(), getString(R.string.toast_upload_avatar));
            }
        });

        mBaseActivity.rxManage.on(Event.UPLOAD_AVATAR, new Action1<String>() {
            @Override
            public void call(String o) {
                mNetPath = o;
                String sign = UserPreference.getString(UserPreference.SECRET, "") + "imageUrl=" + mNetPath + "&nickName=" + mTvName.getText().toString() + "&submitTime=" +
                        Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
                sign = Util.encrypt(sign);
                GZHApi.getInstance().resetNickNameAndImg(1, Util.getNowTime(), sign, UserPreference.getString(UserPreference.SESSION_ID, ""), mTvName.getText().toString(), mNetPath);
            }
        });

        mBaseActivity.rxManage.on(Event.UPDATE_MESSAGE, new Action1() {
            @Override
            public void call(Object o) {
                getUserInfo();
                mEtName.setVisibility(View.GONE);
                mTvName.setVisibility(View.VISIBLE);
                mTvState.setText(getString(R.string.update));
                mFinishDone = true;
            }
        });
    }

    private void getUserInfo() {
        String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
        sign = Util.encrypt(sign);
        GZHApi.getInstance().getUserInfo(Util.getNowTime(), sign, UserPreference.getString(UserPreference.SESSION_ID, ""));
        String sign1 = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
        sign1 = Util.encrypt(sign1);
        GZHApi.getInstance().findNewList(2, Util.getNowTime(), sign1, UserPreference.getString(UserPreference.SESSION_ID, ""));
    }

    @OnClick({R.id.img_setting, R.id.rl_about, R.id.rl_help, R.id.rl_message, R.id.ll_update, R.id.rl_share, R.id.rl_feedback, R.id.img_avatar})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.rl_about:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
            case R.id.rl_help:
                startActivity(new Intent(getActivity(), HelpCenterActivity.class));
                break;
            case R.id.rl_message:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.ll_update:
                if (mFinishDone) {
                    mEtName.setVisibility(View.VISIBLE);
                    mEtName.setText(mTvName.getText().toString());
                    mEtName.setSelection(mEtName.getText().toString().length());
                    mTvName.setVisibility(View.GONE);
                    mTvState.setText(getString(R.string.done));
                    mFinishDone = false;
                    InputMethodManager imm = (InputMethodManager) mEtName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        mEtName.requestFocus();
                        imm.showSoftInput(mEtName, 0);
                    }
                } else {
                    String sign = UserPreference.getString(UserPreference.SECRET, "") + "imageUrl=" + mNetPath + "&nickName=" + mEtName.getText().toString() + "&submitTime=" +
                            Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
                    sign = Util.encrypt(sign);
                    GZHApi.getInstance().resetNickNameAndImg(0, Util.getNowTime(), sign, UserPreference.getString(UserPreference.SESSION_ID, ""), mEtName.getText().toString(), mNetPath);
                }
                break;
            case R.id.rl_share:

                break;
            case R.id.rl_feedback:
                Uri uri = Uri.parse("mailto:support@gzh.com");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, ""); // 主题
                intent.putExtra(Intent.EXTRA_TEXT, ""); // 正文
                startActivity(Intent.createChooser(intent, getString(R.string.select_email)));
                UserPreference.putInt(UserPreference.SHOULD_PWD, 1);
                break;
            case R.id.img_avatar:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 先判断是否有权限
                    if (AndPermission.hasPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // 有权限
                        DialogUtil.avatarDialog(getActivity(), new DialogUtil.OnResultListener4() {
                            @Override
                            public void select1() {
                                PictureSelector.create(MineFragment.this).openCamera(PictureMimeType.ofImage()).maxSelectNum(1).imageSpanCount(3)
                                        .selectionMode(PictureConfig.SINGLE).previewImage(true).previewVideo(false).enablePreviewAudio(false).compressGrade(Luban.THIRD_GEAR)
                                        .isCamera(false).enableCrop(true).compress(true).compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)
                                        .withAspectRatio(1, 1).compressWH(1, 1).freeStyleCropEnabled(true).forResult(PictureConfig.CHOOSE_REQUEST);
                            }

                            @Override
                            public void select2() {
                                PictureSelector.create(MineFragment.this).openGallery(PictureMimeType.ofImage()).maxSelectNum(1).imageSpanCount(3)
                                        .selectionMode(PictureConfig.SINGLE).previewImage(true).previewVideo(false).enablePreviewAudio(false).compressGrade(Luban.THIRD_GEAR)
                                        .isCamera(false).enableCrop(true).compress(true).compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)
                                        .withAspectRatio(1, 1).compressWH(1, 1).freeStyleCropEnabled(true).forResult(PictureConfig.CHOOSE_REQUEST);
                            }
                        });
                    } else {
                        // 申请权限
                        AndPermission.with(MineFragment.this).requestCode(ConstantCode.REQUEST_CODE_OF_WRITE_STORAGE).callback(MineFragment.this)
                                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).start();
                    }
                } else {
                    DialogUtil.avatarDialog(getActivity(), new DialogUtil.OnResultListener4() {
                        @Override
                        public void select1() {
                            PictureSelector.create(MineFragment.this).openCamera(PictureMimeType.ofImage()).maxSelectNum(1).imageSpanCount(3)
                                    .selectionMode(PictureConfig.SINGLE).previewImage(true).previewVideo(false).enablePreviewAudio(false).compressGrade(Luban.THIRD_GEAR)
                                    .isCamera(false).enableCrop(true).compress(true).compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)
                                    .withAspectRatio(1, 1).compressWH(1, 1).freeStyleCropEnabled(true).forResult(PictureConfig.CHOOSE_REQUEST);
                        }

                        @Override
                        public void select2() {
                            PictureSelector.create(MineFragment.this).openGallery(PictureMimeType.ofImage()).maxSelectNum(1).imageSpanCount(3)
                                    .selectionMode(PictureConfig.SINGLE).previewImage(true).previewVideo(false).enablePreviewAudio(false).compressGrade(Luban.THIRD_GEAR)
                                    .isCamera(false).enableCrop(true).compress(true).compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)
                                    .withAspectRatio(1, 1).compressWH(1, 1).freeStyleCropEnabled(true).forResult(PictureConfig.CHOOSE_REQUEST);
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST://上传头像
                    String mPath;
                    if (PictureSelector.obtainMultipleResult(data).get(0).isCompressed())
                        mPath = PictureSelector.obtainMultipleResult(data).get(0).getCompressPath();
                    else
                        mPath = PictureSelector.obtainMultipleResult(data).get(0).getPath();
                    mPb.setVisibility(View.VISIBLE);
                    File file = ImageLoadUtil.getImage(getActivity(), mPath);

                    Uri uri = Uri.fromFile(file);
                    try {
                        mNewBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    GZHApi.getInstance().uploadAvatar(file, UserPreference.getString(UserPreference.SESSION_ID, ""));
                    break;
            }
        }
    }

    @PermissionYes(ConstantCode.REQUEST_CODE_OF_WRITE_STORAGE)
    private void getStorageYes(List<String> grantedPermissions) {
        DialogUtil.avatarDialog(getActivity(), new DialogUtil.OnResultListener4() {
            @Override
            public void select1() {
                PictureSelector.create(MineFragment.this).openCamera(PictureMimeType.ofImage()).maxSelectNum(1).imageSpanCount(3)
                        .selectionMode(PictureConfig.SINGLE).previewImage(true).previewVideo(false).enablePreviewAudio(false).compressGrade(Luban.THIRD_GEAR)
                        .isCamera(false).enableCrop(true).compress(true).compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)
                        .withAspectRatio(1, 1).compressWH(1, 1).freeStyleCropEnabled(true).forResult(PictureConfig.CHOOSE_REQUEST);
            }

            @Override
            public void select2() {
                PictureSelector.create(MineFragment.this).openGallery(PictureMimeType.ofImage()).maxSelectNum(1).imageSpanCount(3)
                        .selectionMode(PictureConfig.SINGLE).previewImage(true).previewVideo(false).enablePreviewAudio(false).compressGrade(Luban.THIRD_GEAR)
                        .isCamera(false).enableCrop(true).compress(true).compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)
                        .withAspectRatio(1, 1).compressWH(1, 1).freeStyleCropEnabled(true).forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });
    }

    @PermissionNo(ConstantCode.REQUEST_CODE_OF_WRITE_STORAGE)
    private void getStorageNo(List<String> deniedPermissions) {
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            AndPermission.defaultSettingDialog(this, ConstantCode.REQUEST_CODE_OF_WRITE_STORAGE).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getUserInfo();
        mEtName.setVisibility(View.GONE);
        mTvName.setVisibility(View.VISIBLE);
        mTvState.setText(getString(R.string.update));
        mFinishDone = true;
    }

}
