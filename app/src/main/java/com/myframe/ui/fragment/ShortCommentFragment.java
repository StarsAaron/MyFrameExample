package com.myframe.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fastbuildlibrary.adapter.recyclerview.CommonAdapter;
import com.fastbuildlibrary.adapter.recyclerview.LinearItemDecoration;
import com.fastbuildlibrary.adapter.recyclerview.base.ViewHolder;
import com.squareup.picasso.Picasso;
import com.myframe.R;
import com.myframe.model.DailyComment;
import com.myframe.net.RetrofitHelper;
import com.myframe.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 短评论
 */
public class ShortCommentFragment extends Fragment {
    @BindView(R.id.long_recycle)
    RecyclerView long_recycle;
    @BindView(R.id.tv_empty_longcomment)
    TextView empty_longcomment;

    private static final String ID = "ID";
    private int id = -1;
    private List<DailyComment.CommentInfo> commenList = new ArrayList<>();
    private CommonAdapter<DailyComment.CommentInfo> commonAdapter;

    public ShortCommentFragment() {
        // Required empty public constructor
    }

    public static ShortCommentFragment newInstance(int id) {
        ShortCommentFragment fragment = new ShortCommentFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        ButterKnife.bind(this,view);

        long_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        long_recycle.setItemAnimator(new DefaultItemAnimator());
        long_recycle.addItemDecoration(new LinearItemDecoration(getActivity(),LinearItemDecoration.VERTICAL_LIST));
        commonAdapter = new CommonAdapter<DailyComment.CommentInfo>(getActivity(),R.layout.item_comment,commenList) {
            @Override
            protected void convert(ViewHolder holder, DailyComment.CommentInfo commentInfo, int position) {
                holder.setText(R.id.user_name,commentInfo.author);
                holder.setText(R.id.user_parise_num,commentInfo.likes + "");
                holder.setText(R.id.user_content,commentInfo.content);
                holder.setText(R.id.user_time, DateUtil.getTime(commentInfo.time));
                Picasso.with(getContext())
                        .load(commentInfo.avatar)
                        .error(R.drawable.account_avatar)
                        .into((ImageView) holder.getView(R.id.user_pic));
            }
        };
        long_recycle.setAdapter(commonAdapter);
        getShortComment();
        return view;
    }

    /**
     * 获取短评论
     */
    private void getShortComment() {
        if(id != -1){
            new RetrofitHelper(getActivity()).getDailyShortCommentById(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<DailyComment>() {
                        @Override
                        public void call(DailyComment dailyComment) {
                            if(dailyComment != null){
                                List<DailyComment.CommentInfo> comments = dailyComment.comments;
                                if (comments != null && comments.size() > 0) {
                                    commenList.removeAll(null);
                                    commenList.addAll(comments);
                                    commonAdapter.notifyDataSetChanged();
                                    empty_longcomment.setVisibility(View.GONE);
                                } else {
                                    empty_longcomment.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            empty_longcomment.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }


    @Override
    public void onDestroyView() {
        Unbinder.EMPTY.unbind();
        super.onDestroyView();
    }
}
