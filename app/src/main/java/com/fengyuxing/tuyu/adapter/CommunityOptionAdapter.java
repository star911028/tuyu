package com.fengyuxing.tuyu.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.fengyuxing.tuyu.R;
import com.fengyuxing.tuyu.bean.DataList;

import java.util.List;


/**
 * Created by lgj on 2018/6/2.
 */
public class CommunityOptionAdapter extends BaseAdapter {

	private List<DataList> datas;
	private Context context;


	public CommunityOptionAdapter(Context context, List<DataList> mList) {
		this.context = context;
		this.datas = mList;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int i) {
		return datas.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder holder = null;
		if (view == null) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = mInflater.inflate(R.layout.adapter_community_publish_option_grid, null);
			holder = new ViewHolder();
			holder.tvOption = view.findViewById(R.id.tv_option);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tvOption.setText(datas.get(i).getClassifyName());
		if(datas.get(i).isChecked()){
			holder.tvOption.setBackground(context.getDrawable(R.drawable.community_publish_option_selected));
			holder.tvOption.setTextColor(context.getResources().getColor(R.color.white));
		}else{
			holder.tvOption.setBackground(context.getDrawable(R.drawable.community_publish_option_normal));
			holder.tvOption.setTextColor(context.getResources().getColor(R.color.common_textcolor));
		}
		return view;
	}

	static class ViewHolder {
		TextView tvOption;
	}
}
