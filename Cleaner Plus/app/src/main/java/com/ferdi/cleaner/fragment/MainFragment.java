package com.ferdi.cleaner.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ferdi.cleaner.base.BaseFragment;
import com.ferdi.cleaner.model.SDCardInfo;
import com.ferdi.cleaner.ui.AutoStartManageActivity;
import com.ferdi.cleaner.ui.MemoryCleanActivity;
import com.ferdi.cleaner.ui.RubbishCleanActivity;
import com.ferdi.cleaner.ui.SoftwareManageActivity;
import com.ferdi.cleaner.utils.AppUtil;
import com.ferdi.cleaner.utils.StorageUtil;
import com.ferdi.cleaner.widget.circleprogress.ArcProgress;
import com.ferdi.cleanerr.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainFragment extends BaseFragment implements View.OnClickListener{

    @Bind(R.id.arc_store)
    ArcProgress arcStore;

    @Bind(R.id.arc_process)
    ArcProgress arcProcess;

    @Bind(R.id.capacity)
    TextView capacity;

    Context mContext;

    private Timer timer;
    private Timer timer2;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = inflater.inflate(R.layout.fragment_main1, container, false);
        ButterKnife.bind(this, view);
        mContext = getActivity();

        LinearLayout phoneboost = (LinearLayout) view.findViewById(R.id.phone_boost);
        phoneboost.setOnClickListener(this);



        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        fillData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    private void fillData() {
        // TODO Auto-generated method stub
        timer = null;
        timer2 = null;
        timer = new Timer();
        timer2 = new Timer();


        long l = AppUtil.getAvailMemory(mContext);
        long y = AppUtil.getTotalMemory(mContext);
        final double x = (((y - l) / (double) y) * 100);
        //   arcProcess.setProgress((int) x);

        arcProcess.setProgress(0);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (arcProcess.getProgress() >= (int) x) {
                            timer.cancel();
                        } else {
                            arcProcess.setProgress(arcProcess.getProgress() + 1);
                        }

                    }
                });
            }
        }, 50, 20);

        SDCardInfo mSDCardInfo = StorageUtil.getSDCardInfo();
        SDCardInfo mSystemInfo = StorageUtil.getSystemSpaceInfo(mContext);

        long nAvailaBlock;
        long TotalBlocks;
        if (mSDCardInfo != null) {
            nAvailaBlock = mSDCardInfo.free + mSystemInfo.free;
            TotalBlocks = mSDCardInfo.total + mSystemInfo.total;
        } else {
            nAvailaBlock = mSystemInfo.free;
            TotalBlocks = mSystemInfo.total;
        }

        final double percentStore = (((TotalBlocks - nAvailaBlock) / (double) TotalBlocks) * 100);

        capacity.setText(StorageUtil.convertStorage(TotalBlocks - nAvailaBlock) + "/" + StorageUtil.convertStorage(TotalBlocks));
        arcStore.setProgress(0);

        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (arcStore.getProgress() >= (int) percentStore) {
                            timer2.cancel();
                        } else {
                            arcStore.setProgress(arcStore.getProgress() + 1);
                        }

                    }
                });
            }
        }, 50, 20);


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.phone_boost ){
            startActivity(MemoryCleanActivity.class);
        }
    }




    @OnClick(R.id.junk_clean)
    void rubbishClean() {
        startActivity(RubbishCleanActivity.class);
    }


    @OnClick(R.id.app_manager)
    void AutoStartManage() {
        startActivity(SoftwareManageActivity.class);
    }

    @OnClick(R.id.background_app)
    void SoftwareManage() {
        startActivity(AutoStartManageActivity.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onDestroy() {
        timer.cancel();
        timer2.cancel();
        super.onDestroy();
    }
}
