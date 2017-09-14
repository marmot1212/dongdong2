package com.example.administrator.vegetarians824.video;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.DownloadFileListAdapter;
import com.example.administrator.vegetarians824.util.SDHelper;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.base.Status;
import org.wlf.filedownloader.listener.OnDeleteDownloadFileListener;
import org.wlf.filedownloader.listener.OnDeleteDownloadFilesListener;
import org.wlf.filedownloader.listener.OnMoveDownloadFileListener;
import org.wlf.filedownloader.listener.OnMoveDownloadFilesListener;
import org.wlf.filedownloader.listener.OnRenameDownloadFileListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoDownload extends BaseActivity implements DownloadFileListAdapter.OnItemSelectListener{
    private LinearLayout fanhui;
    private ListView listView;
    private DownloadFileListAdapter mDownloadFileListAdapter;

    private LinearLayout mLnlyOperation;
    private Button mBtnDelete;
    private Button mBtnMove;
    private Button mBtnRename;
    private Toast mToast;
    private FrameLayout doing;
    private TextView downloadtask;
    private SharedPreferences pre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_download);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        pre=getSharedPreferences("shared", Context.MODE_PRIVATE);
        initView();
        if(SDHelper.isHasSdcard()){
           SDHelper.getDir();
        }else {
            Toast.makeText(getBaseContext(),"找不到SD卡",Toast.LENGTH_SHORT);
        }

        mDownloadFileListAdapter = new DownloadFileListAdapter(this,0);
        listView.setAdapter(mDownloadFileListAdapter);
        mDownloadFileListAdapter.setOnItemSelectListener(this);
        FileDownloader.registerDownloadStatusListener(mDownloadFileListAdapter);


    }
    public void initView(){
        fanhui=(LinearLayout)findViewById(R.id.video_download_fanhui);
        doing=(FrameLayout) findViewById(R.id.video_download_doing);
        downloadtask=(TextView)findViewById(R.id.downloadtask);
        listView=(ListView)findViewById(R.id.video_download_list);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        doing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),VideoDownloading.class));
            }
        });

        mLnlyOperation = (LinearLayout) findViewById(R.id.lnlyOperation);
        mBtnDelete = (Button) findViewById(R.id.btnDelete);
        mBtnMove = (Button) findViewById(R.id.btnMove);
        mBtnRename = (Button) findViewById(R.id.btnRename);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDownloadFileListAdapter != null) {
            mDownloadFileListAdapter.updateShow();
        }

        List<DownloadFileInfo> filelist= FileDownloader.getDownloadFiles();
        int pause=0;
        int downloading=0;
        for(int i=0;i<filelist.size();i++){
            if(filelist.get(i).getStatus()== Status.DOWNLOAD_STATUS_PAUSED){
                pause++;
            }
            if(filelist.get(i).getStatus()== Status.DOWNLOAD_STATUS_DOWNLOADING){
                downloading++;
            }
        }
        if((pause+downloading)>0){
            doing.setVisibility(View.VISIBLE);
            downloadtask.setText(downloading+"/"+(downloading+pause));
        }else {
            doing.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // pause all downloads
       // FileDownloader.pauseAll();
        // unregisterDownloadStatusListener
        FileDownloader.unregisterDownloadStatusListener(mDownloadFileListAdapter);
    }
    private void showToast(CharSequence text) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    private void updateAdapter() {
        if (mDownloadFileListAdapter == null) {
            return;
        }
        mDownloadFileListAdapter.updateShow();


    }
    @Override
    public void onSelected(final List<DownloadFileInfo> selectDownloadFileInfos) {

        mLnlyOperation.setVisibility(View.VISIBLE);

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(VideoDownload.this);
                builder.setTitle(getString(R.string.main__confirm_whether_delete_save_file));
                builder.setNegativeButton(getString(R.string.main__confirm_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDownloadFiles(false, selectDownloadFileInfos);
                    }
                });
                builder.setPositiveButton(getString(R.string.main__confirm_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDownloadFiles(true, selectDownloadFileInfos);
                    }
                });
                builder.show();
            }

            private void deleteDownloadFiles(boolean deleteDownloadedFile, List<DownloadFileInfo>
                    selectDownloadFileInfos) {

                List<String> urls = new ArrayList<String>();

                for (DownloadFileInfo downloadFileInfo : selectDownloadFileInfos) {
                    if (downloadFileInfo == null) {
                        continue;
                    }
                    urls.add(downloadFileInfo.getUrl());
                }

                // single delete
                if (urls.size() == 1) {
                    FileDownloader.delete(urls.get(0), deleteDownloadedFile, new OnDeleteDownloadFileListener() {
                        @Override
                        public void onDeleteDownloadFileSuccess(DownloadFileInfo downloadFileDeleted) {
                            showToast(getString(R.string.main__delete_succeed));
                            updateAdapter();
                            Log.e("wlf", "onDeleteDownloadFileSuccess 成功回调，单个删除" + downloadFileDeleted.getFileName()
                                    + "成功");
                            SharedPreferences.Editor editor = pre.edit();
                            editor.remove(downloadFileDeleted.getUrl());
                            editor.commit();
                        }

                        @Override
                        public void onDeleteDownloadFilePrepared(DownloadFileInfo downloadFileNeedDelete) {
                            if (downloadFileNeedDelete != null) {
                                showToast(getString(R.string.main__deleting) + downloadFileNeedDelete.getFileName());
                            }
                        }

                        @Override
                        public void onDeleteDownloadFileFailed(DownloadFileInfo downloadFileInfo,
                                                               DeleteDownloadFileFailReason failReason) {
                            showToast(getString(R.string.main__delete) + downloadFileInfo.getFileName() + getString(R
                                    .string.main__failed));

                            Log.e("wlf", "onDeleteDownloadFileFailed 出错回调，单个删除" + downloadFileInfo.getFileName() +
                                    "失败");
                        }
                    });
                }
                // multi delete
                else {
                    Log.e("wlf_deletes", "点击开始批量删除");
                    FileDownloader.delete(urls, deleteDownloadedFile, new OnDeleteDownloadFilesListener() {

                        @Override
                        public void onDeletingDownloadFiles(List<DownloadFileInfo> downloadFilesNeedDelete,
                                                            List<DownloadFileInfo> downloadFilesDeleted,
                                                            List<DownloadFileInfo> downloadFilesSkip,
                                                            DownloadFileInfo downloadFileDeleting) {
                            if (downloadFileDeleting != null) {
                                showToast(getString(R.string.main__deleting) + downloadFileDeleting.getFileName() +
                                        getString(R.string.main__progress) + (downloadFilesDeleted.size() +
                                        downloadFilesSkip.size()) + getString(R.string.main__failed2) +
                                        downloadFilesSkip.size() + getString(R.string
                                        .main__skip_and_total_delete_division) +
                                        downloadFilesNeedDelete.size());
                            }
                            updateAdapter();
                        }

                        @Override
                        public void onDeleteDownloadFilesPrepared(List<DownloadFileInfo> downloadFilesNeedDelete) {
                            showToast(getString(R.string.main__need_delete) + downloadFilesNeedDelete.size());
                        }

                        @Override
                        public void onDeleteDownloadFilesCompleted(List<DownloadFileInfo> downloadFilesNeedDelete,
                                                                   List<DownloadFileInfo> downloadFilesDeleted) {

                            String text = getString(R.string.main__delete_finish) + downloadFilesDeleted.size() +
                                    getString(R.string.main__failed3) + (downloadFilesNeedDelete.size() -
                                    downloadFilesDeleted.size());

                            showToast(text);

                            updateAdapter();

                            for(int i=0;i<downloadFilesDeleted.size();i++){
                                SharedPreferences.Editor editor = pre.edit();
                                editor.remove(downloadFilesDeleted.get(i).getUrl());
                                editor.commit();
                            }

                            Log.e("wlf", "onDeleteDownloadFilesCompleted 完成回调，" + text);
                        }
                    });
                }
            }
        });

        mBtnMove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String oldDirPath = FileDownloader.getDownloadDir();

                final EditText etFileDir = new EditText(VideoDownload.this);
                etFileDir.setText(oldDirPath);
                etFileDir.setFocusable(true);

                LinearLayout linearLayout = new LinearLayout(VideoDownload.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                        .MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayout.addView(etFileDir, params);

                AlertDialog.Builder builder = new AlertDialog.Builder(VideoDownload.this);
                builder.setTitle(getString(R.string.main__confirm_the_dir_path_move_to)).setView(linearLayout)
                        .setNegativeButton(getString(R.string.main__dialog_btn_cancel), null);
                builder.setPositiveButton(getString(R.string.main__dialog_btn_confirm), new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // move to file dir
                        String newDirPath = etFileDir.getText().toString().trim();

                        List<String> urls = new ArrayList<String>();

                        for (DownloadFileInfo downloadFileInfo : selectDownloadFileInfos) {
                            if (downloadFileInfo == null) {
                                continue;
                            }
                            urls.add(downloadFileInfo.getUrl());
                        }

                        // single move
                        if (urls.size() == 1) {
                            FileDownloader.move(urls.get(0), newDirPath, new OnMoveDownloadFileListener() {

                                @Override
                                public void onMoveDownloadFileSuccess(DownloadFileInfo downloadFileMoved) {
                                    showToast(getString(R.string.main__move_succeed) + downloadFileMoved.getFilePath());
                                    updateAdapter();
                                }

                                @Override
                                public void onMoveDownloadFilePrepared(DownloadFileInfo downloadFileNeedToMove) {
                                    if (downloadFileNeedToMove != null) {
                                        showToast(getString(R.string.main__moving) + downloadFileNeedToMove
                                                .getFileName());
                                    }
                                }

                                @Override
                                public void onMoveDownloadFileFailed(DownloadFileInfo downloadFileInfo,
                                                                     MoveDownloadFileFailReason failReason) {
                                    showToast(getString(R.string.main__move) + downloadFileInfo.getFileName() +
                                            getString(R.string.main__failed));
                                    Log.e("wlf", "出错回调，移动" + downloadFileInfo.getFileName() + "失败");
                                }
                            });
                        }
                        // multi move
                        else {
                            FileDownloader.move(urls, newDirPath, new OnMoveDownloadFilesListener() {

                                @Override
                                public void onMoveDownloadFilesPrepared(List<DownloadFileInfo> downloadFilesNeedMove) {
                                    showToast(getString(R.string.main__need_move) + downloadFilesNeedMove.size());
                                }

                                @Override
                                public void onMovingDownloadFiles(List<DownloadFileInfo> downloadFilesNeedMove,
                                                                  List<DownloadFileInfo> downloadFilesMoved,
                                                                  List<DownloadFileInfo> downloadFilesSkip,
                                                                  DownloadFileInfo downloadFileMoving) {
                                    if (downloadFileMoving != null) {
                                        showToast(getString(R.string.main__moving) + downloadFileMoving.getFileName() +
                                                getString(R.string.main__progress) + (downloadFilesMoved.size() +
                                                downloadFilesSkip.size()) + getString(R.string.main__failed2) +

                                                downloadFilesSkip.size() + getString(R.string
                                                .main__skip_and_total_delete_division) + downloadFilesNeedMove.size());
                                    }
                                    updateAdapter();

                                }

                                @Override
                                public void onMoveDownloadFilesCompleted(List<DownloadFileInfo> downloadFilesNeedMove,
                                                                         List<DownloadFileInfo> downloadFilesMoved) {
                                    showToast(getString(R.string.main__move_finish) + downloadFilesMoved.size() +
                                            getString(R.string.main__failed3) + (downloadFilesNeedMove.size() -
                                            downloadFilesMoved.size()));
                                }
                            });
                        }

                    }
                });
                builder.show();
            }
        });

        mBtnRename.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final List<String> urls = new ArrayList<String>();

                for (DownloadFileInfo downloadFileInfo : selectDownloadFileInfos) {
                    if (downloadFileInfo == null) {
                        continue;
                    }
                    if (TextUtils.isEmpty(downloadFileInfo.getUrl())) {
                        return;
                    }
                    urls.add(downloadFileInfo.getUrl());
                }

                if (urls.size() == 1) {

                    DownloadFileInfo downloadFileInfoNeedToRename = null;

                    for (DownloadFileInfo downloadFileInfo : selectDownloadFileInfos) {
                        if (downloadFileInfo == null) {
                            continue;
                        }
                        if (urls.get(0).equals(downloadFileInfo.getUrl())) {
                            downloadFileInfoNeedToRename = downloadFileInfo;
                            break;
                        }
                    }

                    if (downloadFileInfoNeedToRename == null) {
                        showToast(getString(R.string.main__can_not_rename));
                        return;
                    }

                    String oldName = downloadFileInfoNeedToRename.getFileName();

                    final EditText etFileName = new EditText(VideoDownload.this);
                    etFileName.setText(oldName);
                    etFileName.setFocusable(true);

                    final CheckBox cbIncludedSuffix = new CheckBox(VideoDownload.this);
                    cbIncludedSuffix.setChecked(true);
                    cbIncludedSuffix.setText(getString(R.string.main__rename_included_suffix));

                    LinearLayout linearLayout = new LinearLayout(VideoDownload.this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                            .MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    linearLayout.addView(etFileName, params);
                    params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams
                            .WRAP_CONTENT);
                    linearLayout.addView(cbIncludedSuffix, params);

                    AlertDialog.Builder builder = new AlertDialog.Builder(VideoDownload.this);
                    builder.setTitle(getString(R.string.main__confirm_rename_info)).setView(linearLayout)
                            .setNegativeButton(getString(R.string.main__dialog_btn_cancel), null);
                    builder.setPositiveButton(getString(R.string.main__dialog_btn_confirm), new DialogInterface
                            .OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String newName = etFileName.getText().toString();

                            FileDownloader.rename(urls.get(0), newName, cbIncludedSuffix.isChecked(), new
                                    OnRenameDownloadFileListener() {

                                        @Override
                                        public void onRenameDownloadFilePrepared(DownloadFileInfo downloadFileNeedRename) {

                                        }

                                        @Override
                                        public void onRenameDownloadFileSuccess(DownloadFileInfo downloadFileRenamed) {
                                            showToast(getString(R.string.main__rename_succeed));
                                            updateAdapter();
                                        }

                                        @Override
                                        public void onRenameDownloadFileFailed(DownloadFileInfo downloadFileInfo,
                                                                               RenameDownloadFileFailReason failReason) {
                                            showToast(getString(R.string.main__rename_failed));
                                            Log.e("wlf", "出错回调，重命名失败");
                                        }
                                    });
                        }
                    });
                    builder.show();
                } else {
                    showToast(getString(R.string.main__rename_failed_note));
                }
            }
        });

    }

    @Override
    public void onNoneSelect() {
        mLnlyOperation.setVisibility(View.GONE);
    }

    @Override
    public void updataView() {
        List<DownloadFileInfo> filelist= FileDownloader.getDownloadFiles();
        int pause=0;
        int downloading=0;
        for(int i=0;i<filelist.size();i++){
            if(filelist.get(i).getStatus()== Status.DOWNLOAD_STATUS_PAUSED){
                pause++;
            }
            if(filelist.get(i).getStatus()== Status.DOWNLOAD_STATUS_DOWNLOADING){
                downloading++;
            }
        }
        if((pause+downloading)>0){
            doing.setVisibility(View.VISIBLE);
            downloadtask.setText(downloading+"/"+(downloading+pause));
        }else {
            doing.setVisibility(View.GONE);
        }
    }

}
