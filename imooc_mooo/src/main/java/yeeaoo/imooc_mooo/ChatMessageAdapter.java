package yeeaoo.imooc_mooo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import bean.ChatMessage;

/**
 * Created by yo on 2016/5/9.
 */
public class ChatMessageAdapter extends BaseAdapter {
    private Context mContext;
    private List<ChatMessage> mDatas;
    public ChatMessageAdapter(Context context, List<ChatMessage> datas){
        this.mContext = context;
        this.mDatas = datas;
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = mDatas.get(position);
        if (chatMessage.getType() == ChatMessage.Type.INCOMING){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = mDatas.get(position);
        ViewHolder holder = null;
        if (convertView == null){
            if (getItemViewType(position) == 0){
                convertView = View.inflate(mContext, R.layout.item_from_msg, null);
                holder = new ViewHolder();
                holder.mDate = (TextView) convertView.findViewById(R.id.id_form_msg_date);
                holder.mMsg = (TextView) convertView.findViewById(R.id.id_form_msg_info);
            }else {
                convertView = View.inflate(mContext, R.layout.item_to_msg, null);
                holder = new ViewHolder();
                holder.mDate = (TextView) convertView.findViewById(R.id.id_to_msg_date);
                holder.mMsg = (TextView) convertView.findViewById(R.id.id_to_msg_info);
            }
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.mDate.setText(df.format(chatMessage.getDate()));
        holder.mMsg.setText(chatMessage.getMsg());
        return convertView;
    }
    private final class ViewHolder{
        TextView mDate;
        TextView mMsg;
    }
}
